package com.example.dbstask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication (
        scanBasePackages = {"com.dbs"}
)
public class DbsTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(DbsTaskApplication.class, args);
    }

}
