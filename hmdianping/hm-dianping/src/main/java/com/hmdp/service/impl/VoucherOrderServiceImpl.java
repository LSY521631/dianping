package com.hmdp.service.impl;

import com.hmdp.dto.Result;
import com.hmdp.entity.SeckillVoucher;
import com.hmdp.entity.VoucherOrder;
import com.hmdp.mapper.VoucherOrderMapper;
import com.hmdp.service.ISeckillVoucherService;
import com.hmdp.service.IVoucherOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.service.IVoucherService;
import com.hmdp.utils.RedisIdWorker;
import com.hmdp.utils.SimpleRedisLock;
import com.hmdp.utils.UserHolder;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

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


    /**
     * 秒杀优惠券下单
     *
     * @param voucherId
     * @return
     */
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
        SimpleRedisLock lock = new SimpleRedisLock("order:" + userId, stringRedisTemplate);

        //获取锁,为测试将锁的过期时间设置为1200秒
        boolean success = lock.tryLock(1200);

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

    }

    /**
     * 创建保存订单
     *
     * @param voucherId
     * @return
     */
    @Transactional
    public Result createVoucherOrder(Long voucherId) {
        //用户id
        Long userId = UserHolder.getUser().getId();
        //获取该用户的订单数 SELECT COUNT(*) FROM tb_voucher_order WHERE user_Id = ? AND voucher_id = ?
        int count = query().eq("user_Id", userId).eq("voucher_id", voucherId).count();

        if (count > 0) {
            return Result.fail("用户已经购买过一次！");
        }

        //扣减库存，增加乐观锁逻辑
        boolean success = iSeckillVoucherService
                .update()
                .setSql("stock = stock - 1") //set stock=stock-1
                .eq("voucher_id", voucherId)
                .gt("stock", 0)//where id = ? and stock > 0
                .update();

        //判断是否更新成功
        if (!success) {
            return Result.fail("库存不足");
        }

        //创建订单
        VoucherOrder voucherOrder = new VoucherOrder();

        //订单id
        long orderId = redisIdWorker.nextId("order");
        voucherOrder.setId(orderId);


        voucherOrder.setUserId(userId);

        //代金券id
        voucherOrder.setVoucherId(voucherId);

        //保存订单到数据库
        save(voucherOrder);

        return Result.ok(orderId);
    }
}
