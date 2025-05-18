package com.speedKillsx.Car_Rental_API.batch.sheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchScheduler {
    private final JobLauncher jobLauncher;
    private final Job dailyJob;

    @Scheduled(cron = "0 * * * * *")
    public void runJob() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            log.info("Starting job...");
            log.info("Job parameters: {}", params);
            log.info("Job name: {}", dailyJob.getName());
            jobLauncher.run(dailyJob, params);

        } catch (Exception e) {
            System.err.println("❌ Échec du job : " + e.getMessage());
        }
    }
}
