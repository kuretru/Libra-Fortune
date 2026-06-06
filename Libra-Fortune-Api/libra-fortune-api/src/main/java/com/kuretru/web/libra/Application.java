package com.kuretru.web.libra;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@SpringBootApplication
@EnableTransactionManagement
@MapperScan({
        "com.kuretru.web.libra.mapper",
        "com.kuretru.web.libra.account.mapper",
        "com.kuretru.web.libra.ledger.mapper",
        "com.kuretru.web.libra.metadata.mapper"
})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
