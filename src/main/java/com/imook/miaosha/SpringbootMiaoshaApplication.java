package com.imook.miaosha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class SpringbootMiaoshaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootMiaoshaApplication.class, args);
    }

//    // 打成war包继承的重写的方法
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(SpringbootMiaoshaApplication.class);
//    }
}
