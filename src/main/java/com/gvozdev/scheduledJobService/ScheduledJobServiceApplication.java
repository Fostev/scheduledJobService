package com.gvozdev.scheduledJobService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ScheduledJobServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScheduledJobServiceApplication.class, args);
    }

}
