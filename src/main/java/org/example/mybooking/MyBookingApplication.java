package org.example.mybooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.example.mybooking")
public class MyBookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyBookingApplication.class, args);
    }

}
