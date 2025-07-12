package com.hmdp.utils;

import cn.hutool.core.lang.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SimpleRedisLock implements ILock {

    private String name;
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 构造函数，初始化锁的名称和RedisTemplate
     *
     * @param name
     * @param stringRedisTemplate
     */
    public SimpleRedisLock(String name, StringRedisTemplate stringRedisTemplate) {
        this.name = name;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    //锁的key的前缀
    private static final String KEY_PREFIX = "lock:";

    //生成一个UUID,true表示去掉横线
    private static final String ID_PREFIX = UUID.randomUUID().toString(true) + "_";

    //声明一个不可变的静态常量，用于执行 Redis Lua 脚本的封装对象
    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT;

    //静态代码块：在类加载时执行一次，完成 UNLOCK_SCRIPT 的初始化
    static {
        UNLOCK_SCRIPT = new DefaultRedisScript<>();//创建一个新的 DefaultRedisScript 实例，泛型指定脚本返回值类型为 Long
        UNLOCK_SCRIPT.setLocation(new ClassPathResource("unlock.lua"));
        UNLOCK_SCRIPT.setResultType(Long.class);
    }

    /**
     * 尝试获取锁
     *
     * @param timeoutSec 锁持有的超时时间，过期后自动释放
     * @return
     */
    @Override
    public boolean tryLock(long timeoutSec) {
        //获取线程标识,保证不同jvm中的线程标识不同
        String threadId = ID_PREFIX + Thread.currentThread().getId();

        //获取锁
        Boolean success = stringRedisTemplate.opsForValue().setIfAbsent(KEY_PREFIX + name, threadId, timeoutSec, TimeUnit.SECONDS);

        //判断获取锁成功与否
        return Boolean.TRUE.equals(success);
    }

    /**
     * 使用lua脚本实现释放锁
     */
    @Override
    public void unlock() {
        //调用脚本,一行代码且调用的脚本，保证了原子性
        stringRedisTemplate.execute(
                UNLOCK_SCRIPT,
                Collections.singletonList(KEY_PREFIX + name),
                ID_PREFIX + Thread.currentThread().getId()
        );

    }


 /*   @Override
    public void unlock() {
        //获取线程标识
        String threadId = ID_PREFIX + Thread.currentThread().getId();

        //获取锁中的标识
        String id = stringRedisTemplate.opsForValue().get(KEY_PREFIX + name);

        //判断是不是当前线程的锁
        if (threadId.equals(id)) {
            //删除锁
            log.info("删除锁");
            stringRedisTemplate.delete(KEY_PREFIX + name);
        }
    }*/
}
