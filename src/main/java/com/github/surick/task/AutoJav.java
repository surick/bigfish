package com.github.surick.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Jin
 * @since 2022/9/7
 */
@Component
public class AutoJav {

    @Scheduled(cron = "0 30 2 10 * ?")
    public void run() {

        AutoJavService.run();
    }
}
