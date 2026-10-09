package com.onlinebookingsystem.airbnbsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AirBnbSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirBnbSystemApplication.class, args);
    }

}
