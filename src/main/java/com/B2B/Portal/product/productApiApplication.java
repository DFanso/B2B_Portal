package com.B2B.Portal.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(
        exclude = { SecurityAutoConfiguration.class })
public class productApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(com.B2B.Portal.product.productApiApplication.class, args);
    }

}
