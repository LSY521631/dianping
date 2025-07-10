package com.hmdp.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.hmdp.mapper.ShopMapper;
import com.hmdp.service.IShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.concurrent.TimeUnit;

import static com.hmdp.utils.RedisConstants.*;

/**
 * 服务实现类
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 根据id查询商铺信息
     *
     * @param id
     * @return
     */
    public Result queryById(Long id) {
        //缓存穿透
        //Shop shop = queryWithPassThough(id);

        //互斥锁解决缓存击穿
        Shop shop = queryWithMutex(id);
        if (shop == null) {
            return Result.fail("商铺不存在");
        }
        return Result.ok(shop);
    }

    /**
     * 解决缓存穿透
     *
     * @param id
     * @return
     */
    public Shop queryWithPassThough(Long id) {
        //从redis中查询商铺缓存
        String key = CACHE_SHOP_KEY + id;
        String shopJson = stringRedisTemplate.opsForValue().get(key);

        //判断是否存在
        if (StrUtil.isNotBlank(shopJson)) {
            //存在则直接返回
            return JSONUtil.toBean(shopJson, Shop.class);
        }

        //判断命中的是否为空值,(“”不是空值)
        if (shopJson != null) {
            return null;
        }

        //redis中不存在，根据id查询数据库
        Shop shop = getById(id);

        //判断数据库中shop是否存在
        if (shop == null) {
            //将空值写入redis(缓存穿透)
            stringRedisTemplate.opsForValue().set(key, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
            return null;
        }

        //存在shop,写入redis并返回
        stringRedisTemplate
                .opsForValue()
                .set(key, JSONUtil.toJsonStr(shop), CACHE_SHOP_TTL + RandomUtil.randomLong(1, 5), TimeUnit.MINUTES);

        return shop;
    }

    /**
     * 互斥锁解决缓存击穿
     *
     * @param id
     * @return
     */
    public Shop queryWithMutex(Long id) {
        //从redis中查询商铺缓存
        String key = CACHE_SHOP_KEY + id;
        String shopJson = stringRedisTemplate.opsForValue().get(key);

        //判断是否存在
        if (StrUtil.isNotBlank(shopJson)) {
            //存在则直接返回
            return JSONUtil.toBean(shopJson, Shop.class);
        }

        //判断命中的是否为空值,(“”不是空值)
        if (shopJson != null) {
            return null;
        }
        String lockKey = null;
        Shop shop = null;

        try {
            //实现缓存重建
            //获取互斥锁
            lockKey = LOCK_SHOP_KEY + id;
            Boolean isTrue = tryLock(lockKey);

            //判断是否成功
            if (!isTrue) {
                //失败，休眠并重试
                Thread.sleep(50);
                return queryWithMutex(id);
            }

            //TODO 获取锁成功后需要二次检验缓存是否存在

            //redis中不存在，根据id查询数据库
            shop = getById(id);

            //判断数据库中shop是否存在
            if (shop == null) {
                //将空值写入redis(缓存穿透)
                stringRedisTemplate.opsForValue().set(key, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
                return null;
            }

            //存在shop,写入redis
            stringRedisTemplate
                    .opsForValue()
                    .set(key, JSONUtil.toJsonStr(shop), CACHE_SHOP_TTL + RandomUtil.randomLong(1, 5), TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            //释放互斥锁
            unlock(lockKey);
        }

        return shop;
    }


    /**
     * 更新商铺信息
     *
     * @param shop
     * @return
     */
    @Transactional
    public Result updateShop(Shop shop) {
        //获取商户id,判断是否存在
        Long id = shop.getId();
        if (id == null) {
            return Result.fail("商户id不存在");
        }
        //更新数据库
        updateById(shop);

        //删除缓存
        stringRedisTemplate.delete(CACHE_SHOP_KEY + id);

        return Result.ok();
    }


    /**
     * 尝试获取锁
     *
     * @param key
     * @return
     */
    private Boolean tryLock(String key) {
        //尝试获取锁
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        //判断获取锁成功与否
        return BooleanUtil.isTrue(flag);
    }

    /**
     * 尝试释放锁
     *
     * @param key
     */
    private void unlock(String key) {
        stringRedisTemplate.delete(key);
    }
}
