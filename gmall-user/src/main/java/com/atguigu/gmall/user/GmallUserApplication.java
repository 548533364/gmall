package com.atguigu.gmall.user;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan (basePackages = "com.atguigu.gmall.user.mapper") //使用通用mapper时，要用通用mapper的扫描器
public class GmallUserApplication {

    public static void main ( String[] args ) {

        SpringApplication.run ( GmallUserApplication.class,args );
    }

}
