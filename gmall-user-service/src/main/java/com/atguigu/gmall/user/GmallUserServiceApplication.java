package com.atguigu.gmall.user;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDubbo
@MapperScan(basePackages = "com.atguigu.gmall.user.mapper")
public class GmallUserServiceApplication {
    public static void main ( String[] args ) {
        SpringApplication.run ( GmallUserServiceApplication.class,args );
    }

}
