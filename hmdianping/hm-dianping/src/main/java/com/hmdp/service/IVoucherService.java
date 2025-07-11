package com.hmdp.service;

import com.hmdp.dto.Result;
import com.hmdp.entity.Voucher;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 优惠券服务类
 */
public interface IVoucherService extends IService<Voucher> {

    /**
     * 查询店铺的优惠券列表
     *
     * @param shopId
     * @return
     */
    Result queryVoucherOfShop(Long shopId);

    /**
     * 添加秒杀券
     *
     * @param voucher
     */
    void addSeckillVoucher(Voucher voucher);


}
