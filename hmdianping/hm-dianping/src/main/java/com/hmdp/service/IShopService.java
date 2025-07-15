package com.hmdp.service;

import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.baomidou.mybatisplus.extension.service.IService;


public interface IShopService extends IService<Shop> {

    /**
     * 根据id查询商铺信息
     *
     * @param id
     * @return
     */
    Result queryById(Long id);


    /**
     * 更新商铺信息
     *
     * @param shop
     * @return
     */
    Result updateShop(Shop shop);


    /**
     * 根据类型分页查询商铺信息
     *
     * @param typeId
     * @param current
     * @param x
     * @param y
     * @return
     */
    Result queryShopByType(Integer typeId, Integer current, Double x, Double y);
}
