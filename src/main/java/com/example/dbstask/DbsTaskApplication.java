package com.example.dbstask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

//@SpringBootApplication
//@RestController
@SpringBootApplication (
        scanBasePackages = {"com.dbs"}
)
//@EnableRetry
public class DbsTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(DbsTaskApplication.class, args);
    }
    /*@RequestMapping(value = "hello", method = RequestMethod.GET)
    public String hello() {
        return "Hello Springboot";
    }*/

}
