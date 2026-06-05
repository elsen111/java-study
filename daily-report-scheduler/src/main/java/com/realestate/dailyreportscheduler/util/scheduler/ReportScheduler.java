package com.realestate.dailyreportscheduler.util.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class ReportScheduler {

    @Scheduled(fixedRate = 5000)
    public void generateReport() {
        log.info("Generating report... Time {}", LocalDateTime.now());
    }

    @Scheduled(fixedDelay = 10000)
    public void cleanReports() {
        log.info("Cleaning old reports... Time {}", LocalDateTime.now());
    }

    @Scheduled(cron = "0 19 2 * * MON-SAT")
    public void startReport() {
        log.info("Daily report started... Time {}", LocalDateTime.now());
    }

}
