package com.atguigu.gmall.gmallredissontest.RedissonTest;

import com.atguigu.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

@Controller
public class TestRedisson {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    RedissonClient redissonClient;


    @RequestMapping("RedissonController")
    @ResponseBody
    public String RedissonController(){

        Jedis jedis = redisUtil.getJedis ( );
         RLock lock = redissonClient.getLock ( "lock" );//声明锁
         lock.lock ();//上锁

         try {
             String v = jedis.get ( "k" );
             if (StringUtils.isBlank ( v )) {
                 v = "1";
             }

             System.out.println ( "---->" + v );
             jedis.set ( "k",(Integer.parseInt ( v ) + 1) + "" );

         }finally {
             jedis.close ( );
             lock.unlock ();//可重入锁，解锁
         }
        return "success";
    }
}
