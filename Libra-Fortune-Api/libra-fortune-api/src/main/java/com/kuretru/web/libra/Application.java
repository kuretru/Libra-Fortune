package com.kuretru.web.libra;

import com.kuretru.microservices.oauth2.client.system.EnableOAuth2System;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableOAuth2System
@MapperScan("com.kuretru.web.libra.mapper")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
