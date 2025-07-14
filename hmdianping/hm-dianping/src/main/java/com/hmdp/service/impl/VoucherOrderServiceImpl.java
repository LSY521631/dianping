package com.hmdp.service.impl;

import com.hmdp.dto.Result;

import com.hmdp.entity.VoucherOrder;
import com.hmdp.mapper.VoucherOrderMapper;
import com.hmdp.service.ISeckillVoucherService;
import com.hmdp.service.IVoucherOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.hmdp.utils.RedisIdWorker;

import com.hmdp.utils.UserHolder;

import lombok.extern.slf4j.Slf4j;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import org.springframework.aop.framework.AopContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import java.util.Collections;
import java.util.concurrent.*;


/**
 * 优惠券订单管理实现类
 */
@Slf4j
@Service
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements IVoucherOrderService {


    @Resource
    private ISeckillVoucherService iSeckillVoucherService;
    @Resource
    private RedisIdWorker redisIdWorker;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedissonClient redissonClient;

    //代理对象，放在成员变量位置，以便后续线程可以拿到
    private IVoucherOrderService proxy;

    //声明一个不可变的静态常量，用于执行 Redis Lua 脚本的封装对象
    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;

    //静态代码块：在类加载时执行一次，完成 SECKILL_SCRIPT 的初始化
    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();//创建一个新的 DefaultRedisScript 实例，泛型指定脚本返回值类型为 Long
        SECKILL_SCRIPT.setLocation(new ClassPathResource("seckill.lua"));
        SECKILL_SCRIPT.setResultType(Long.class);
    }

    //阻塞队列
    private BlockingQueue<VoucherOrder> orderTasks = new ArrayBlockingQueue<VoucherOrder>(1024 * 1024);

    //创建一个单线程的异步处理线程池
    public static final ExecutorService SECKILL_ORDER_EXECUTOR = Executors.newSingleThreadExecutor();

    //创建一个线程，并执行指定的任务
    private class VoucherOrderHandler implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    //获取队列中的订单信息
                    VoucherOrder voucherOrder = orderTasks.take();

                    //创建订单
                    handleVoucherOrder(voucherOrder);
                } catch (Exception e) {
                    log.error("处理订单异常:", e);
                }

            }
        }
    }

    /**
     * 处理订单信息
     *
     * @param voucherOrder
     */
    private void handleVoucherOrder(VoucherOrder voucherOrder) {
        //获取用户
        Long userId = voucherOrder.getId();

        //创建锁对象
        RLock lock = redissonClient.getLock("lock:order" + userId);

        //获取锁
        boolean islock = lock.tryLock();

        //判断是否获得锁成功
        if (!islock) {
            log.error("不允许重复下单");
            return;
        }

        //
        try {
            proxy.createVoucherOrder(voucherOrder);
        } finally {
            //释放锁
            lock.unlock();
        }
    }


    @PostConstruct//当前类初始化后就执行
    private void init() {
        SECKILL_ORDER_EXECUTOR.submit(new VoucherOrderHandler());
    }


    /**
     * 秒杀优惠券下单
     *
     * @param voucherId
     * @return
     */
    /*
    @Override
    public Result seckillVoucher(Long voucherId) {
        log.info("开始出售优惠券");
        //查询优惠券
        SeckillVoucher voucher = iSeckillVoucherService.getById(voucherId);

        if (voucher == null) {
            log.info("优惠券不存在");
            return Result.fail("优惠券不存在");
        }

        //判断秒杀是否开始
        if (voucher.getBeginTime().isAfter(LocalDateTime.now())) {
            return Result.fail("秒杀尚未开始！");
        }
        if (voucher.getEndTime().isBefore(LocalDateTime.now())) {
            return Result.fail("秒杀活动已经结束");
        }

        //判断库存是否充足
        if (voucher.getStock() < 1) {
            return Result.fail("秒杀券库存不足");
        }

        //调整锁的颗粒度
        Long userId = UserHolder.getUser().getId();

// 修改前
//        synchronized (userId.toString().intern()) { //锁对象
//            IVoucherOrderService proxy = (IVoucherOrderService) AopContext.currentProxy();//获取代理对象,即IVoucherOrderService的代理对象
//            return proxy.createVoucherOrder(voucherId);//创建订单
//        }

        //创建锁对象
        //SimpleRedisLock lock = new SimpleRedisLock("order:" + userId, stringRedisTemplate);
        //使用redisson中的锁
        RLock lock = redissonClient.getLock("lock:order:" + userId);

        //获取锁,为测试将锁的过期时间设置为0秒,即不等待
        boolean success = lock.tryLock();

        //加锁失败
        if (!success) {
            return Result.fail("获取锁失败！不允许重复下单");
        }
        try {
            ///获取代理对象(事务)
            IVoucherOrderService proxy = (IVoucherOrderService) AopContext.currentProxy();
            return proxy.createVoucherOrder(voucherId);
        } finally {
            //释放锁
            lock.unlock();
        }

    }*/
    @Override
    public Result seckillVoucher(Long voucherId) {
        log.info("开始出售优惠券");

        //查询用户id
        Long userId = UserHolder.getUser().getId();

        //执行lua脚本
        Long result = stringRedisTemplate.execute(
                SECKILL_SCRIPT,
                Collections.emptyList(),
                voucherId.toString(),
                userId.toString()
        );

        //判断结果是否为0
        int r = result.intValue();
        if (r != 0) {
            return Result.fail(r == 1 ? "库存不足" : "不能重复下单");
        }

        //TODO 为0，有下单资格，将下单信息保存到阻塞队列
        Long orderId = redisIdWorker.nextId("order");

        //创建订单
        VoucherOrder voucherOrder = new VoucherOrder();
        voucherOrder.setId(orderId);
        voucherOrder.setUserId(userId);
        voucherOrder.setVoucherId(voucherId);

        //将订单放入阻塞队列
        orderTasks.add(voucherOrder);

        //获取代理对象
        proxy = (IVoucherOrderService) AopContext.currentProxy();

        //返回订单id
        return Result.ok(orderId);

    }


    /**
     * 创建保存订单
     *
     * @param voucherOrder
     * @return
     */
    @Transactional
    public void createVoucherOrder(VoucherOrder voucherOrder) {
        //用户id
        Long userId = voucherOrder.getUserId();
        //获取该用户的订单数 SELECT COUNT(*) FROM tb_voucher_order WHERE user_Id = ? AND voucher_id = ?
        int count = query().eq("user_Id", userId).eq("voucher_id", voucherOrder.getVoucherId()).count();

        if (count > 0) {
            log.error("用户已经购买过一次！");
            return;
        }

        //扣减库存，增加乐观锁逻辑
        boolean success = iSeckillVoucherService
                .update()
                .setSql("stock = stock - 1") //set stock=stock-1
                .eq("voucher_id", voucherOrder.getVoucherId())
                .gt("stock", 0)//where id = ? and stock > 0
                .update();

        //判断是否更新成功
        if (!success) {
            log.error("库存不足");
            return;
        }

        //保存订单到数据库
        save(voucherOrder);
    }
}
