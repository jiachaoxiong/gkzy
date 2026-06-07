package com.gkzy.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.gkzy")
@MapperScan("com.gkzy.**.mapper")
public class GkzyApplication {
    public static void main(String[] args) {
        SpringApplication.run(GkzyApplication.class, args);
    }
}
