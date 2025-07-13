package com.hmdp.config;


import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RedissonConfig {


    /**
     * 创建RedissonClient对象
     *
     * @return
     */
    @Bean
    public RedissonClient redisClient() {
        //配置
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.255.128:6379");

        //创建RedisClient对象
        return Redisson.create(config);
    }
}
