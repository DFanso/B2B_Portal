package com.B2B.Portal.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(
        exclude = { SecurityAutoConfiguration.class })
public class orderApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(orderApiApplication.class, args);
    }

}
