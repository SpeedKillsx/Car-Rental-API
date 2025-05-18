package com.speedKillsx.Car_Rental_API.batch.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BatchDailyJob {

    @Bean
    public Job dailyJob(
            JobRepository jobRepository,
            Step carToInspectStep
    ) {
        return new JobBuilder("dailyJob", jobRepository)
                .flow(carToInspectStep)
                .end()
                .build();
    }
}
