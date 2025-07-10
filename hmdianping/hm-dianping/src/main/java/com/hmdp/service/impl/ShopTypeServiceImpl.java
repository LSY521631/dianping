package com.hmdp.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


import java.util.ArrayList;
import java.util.List;

import static com.hmdp.utils.RedisConstants.CACHE_SHOP_TYEP_KEY;

/**
 * 服务实现类
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result queryList() {
        //从redis中查询缓存
        String key = CACHE_SHOP_TYEP_KEY;
        String shopTypeJson = stringRedisTemplate.opsForValue().get(key);

        //判断redis中是否存在
        if (StrUtil.isNotBlank(shopTypeJson)) {
            //存在，直接返回
            List<ShopType> list = JSONUtil.toList(shopTypeJson, ShopType.class);
            return Result.ok(list);
        }

        //不存在，去数据库中查询，判断是否存在
        List<ShopType> shopTypeList = query().orderByAsc("sort").list();
        if (shopTypeList == null || shopTypeList.isEmpty()) {
            return Result.fail("数据不存在");
        }

        //存在，添加到redis
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(shopTypeList));

        return Result.ok(shopTypeList);
    }
}
