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
import com.hmdp.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
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

    /**
     * 秒杀优惠券下单
     *
     * @param voucherId
     * @return
     */
    @Override
    @Transactional
    public Result seckillVoucher(Long voucherId) {
        log.info("开始出售优惠券");
        //查询优惠券
        SeckillVoucher voucher = iSeckillVoucherService.getById(voucherId);

        if (voucher == null) {
            log.info("优惠券不存在");
//            return Result.fail("优惠券不存在");
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

        //用户id
        Long userId = UserHolder.getUser().getId();
        voucherOrder.setUserId(userId);

        //代金券id
        voucherOrder.setVoucherId(voucherId);

        //保存订单到数据库
        save(voucherOrder);

        return Result.ok(orderId);
    }
}
