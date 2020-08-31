package com.atguigu.gmall.conf;

import com.atguigu.gmall.util.RedisUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//将redis的链接池创建到spring的容器中
public class RedisConfig {
    //读取配置文件中的reids的IP地址
    @Value ("${spring.redis.host:disabled}")
    private String host ;
    @Value("${spring.redis.port:0}")
    private int port ;
    @Value("${spring.redis.database:0}")
    private int database;
    @Bean
    public RedisUtil getRedisUtil(){
        if (host.equals ( "disabled" )){
            return  null;
        }
        RedisUtil redisUtil = new RedisUtil ();
        redisUtil.initPool ( host,port,database );
        return redisUtil;
    }

}
