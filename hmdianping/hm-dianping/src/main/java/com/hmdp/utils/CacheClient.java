package com.hmdp.utils;


import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hmdp.entity.Shop;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.events.Event;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.hmdp.utils.RedisConstants.*;


/**
 * 缓存工具类
 */
@Slf4j
@Component
public class CacheClient {

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    //新建缓存重建线程池
    private static final ExecutorService CACHE_REBUILD_EXECUTOR =
            Executors.newFixedThreadPool(10);

    /**
     * 设置缓存，可设置TTL过期时间
     *
     * @param key
     * @param value
     * @param time
     * @param unit
     */
    public void set(String key, Object value, Long time, TimeUnit unit) {
        //写入redis
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), time, unit);
    }

    /**
     * 逻辑过期缓存
     *
     * @param key
     * @param value
     * @param time
     * @param unit
     */
    public void setWithLogicalExpire(String key, Object value, Long time, TimeUnit unit) {
        //设置逻辑过期数据
        RedisData redisData = new RedisData();
        redisData.setData(value);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(unit.toSeconds(time)));

        //写入redis
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }


    /**
     * 利用缓存空值的方式解决缓存穿透问题
     *
     * @param keyPreFix 缓存key前缀
     * @param id
     * @param type
     * @param <R>
     * @param <ID>
     * @return
     */
    public <R, ID> R queryWithPassThough(
            String keyPreFix,
            ID id,
            Class<R> type,
            Function<ID, R> dbFallBack,
            Long time,
            TimeUnit unit) {

        //从redis中查询缓存
        String key = keyPreFix + id;
        String json = stringRedisTemplate.opsForValue().get(key);

        //判断是否存在
        if (StrUtil.isNotBlank(json)) {
            //存在直接返回
            return JSONUtil.toBean(json, type);
        }

        //判断命中的是否为空值
        if (json != null) {
            //返回错误信息
            return null;
        }

        //不存在，根据id查询数据库
        R r = dbFallBack.apply(id);

        // 5.不存在，返回错误
        if (r == null) {
            // 将空值写入redis
            stringRedisTemplate.opsForValue().set(key, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
            // 返回错误信息
            return null;
        }

        //存在，写入redis
        this.set(key, r, time, unit);
        return r;
    }

    /**
     * 逻辑过期解决缓存击穿问题
     *
     * @param keyPrefix
     * @param id
     * @param type
     * @param dbFallback
     * @param time
     * @param unit
     * @param <R>
     * @param <ID>
     * @return
     */
    public <R, ID> R queryWithLogicalExpire(
            String keyPrefix,
            ID id,
            Class<R> type,
            Function<ID, R> dbFallback,
            Long time, TimeUnit unit) {
        //从redis查询商铺缓存
        String key = keyPrefix + id;
        String json = stringRedisTemplate.opsForValue().get(key);

        //判断是否存在
        if (StrUtil.isBlank(json)) {
            //？存在，直接返回
            return null;
        }

        //命中，需要先把json反序列化为对象
        RedisData redisData = JSONUtil.toBean(json, RedisData.class);
        R r = JSONUtil.toBean((JSONObject) redisData.getData(), type);
        LocalDateTime expireTime = redisData.getExpireTime();//逻辑过期时间

        //判断是否过期
        if (expireTime.isAfter(LocalDateTime.now())) {
            //未过期，直接返回店铺信息
            return r;
        }

        //已过期，需要缓存重建
        //缓存重建
        //获取互斥锁
        String lockKey = LOCK_SHOP_KEY + id;
        boolean isLock = tryLock(lockKey);
        //判断是否获取锁成功
        if (isLock) {
            //成功，开启独立线程，实现缓存重建
            CACHE_REBUILD_EXECUTOR.submit(() -> {
                try {
                    // 查询数据库
                    R newR = dbFallback.apply(id);// 重建缓存
                    this.setWithLogicalExpire(key, newR, time, unit);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    //释放锁
                    unlock(lockKey);
                }
            });
        }
        //返回过期的商铺信息
        return r;

    }

    /**
     * 互斥锁解决缓存击穿问题
     *
     * @param keyPrefix
     * @param id
     * @param type
     * @param dbFallback
     * @param time
     * @param unit
     * @param <R>
     * @param <ID>
     * @return
     */
    public <R, ID> R queryWithMutex(
            String keyPrefix,
            ID id,
            Class<R> type,
            Function<ID, R> dbFallback,
            Long time,
            TimeUnit unit) {
        //从redis查询商铺缓存
        String key = keyPrefix + id;
        String shopJson = stringRedisTemplate.opsForValue().get(key);

        //判断是否存在
        if (StrUtil.isNotBlank(shopJson)) {
            //存在，直接返回
            return JSONUtil.toBean(shopJson, type);
        }

        //判断命中的是否是空值
        if (shopJson != null) {
            //返回一个错误信息
            return null;
        }

        //实现缓存重建
        //获取互斥锁
        String lockKey = LOCK_SHOP_KEY + id;
        R r = null;
        try {
            boolean isLock = tryLock(lockKey);
            //判断是否获取成功
            if (!isLock) {
                //获取锁失败，休眠并重试
                Thread.sleep(50);
                return queryWithMutex(keyPrefix, id, type, dbFallback, time,
                        unit);
            }
            //获取锁成功，根据id查询数据库
            r = dbFallback.apply(id);

            //不存在，返回错误
            if (r == null) {
                //将空值写入redis
                stringRedisTemplate.opsForValue().set(key, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
                //返回错误信息
                return null;
            }
            //存在，写入redis
            this.set(key, r, time, unit);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            //释放锁
            unlock(lockKey);
        }
        //返回
        return r;
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
