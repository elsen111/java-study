package com.realestate.dailyreportscheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DailyReportSchedulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DailyReportSchedulerApplication.class, args);
    }

}
