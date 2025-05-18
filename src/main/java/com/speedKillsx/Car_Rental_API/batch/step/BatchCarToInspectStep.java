package com.speedKillsx.Car_Rental_API.batch.step;

import com.speedKillsx.Car_Rental_API.entity.Car;
import com.speedKillsx.Car_Rental_API.repository.InspectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
public class BatchCarToInspectStep {

    @Bean
    public Step carToInspectStep(JobRepository jobRepository,
                                 PlatformTransactionManager transactionManager,
                                 InspectionRepository inspectionRepository) {

        LocalDate targetDate = LocalDate.now().minusDays(7);
        log.info("Starting carToInspectStep with targetDate = {}", targetDate);

        List<Car> carsToInspect = inspectionRepository.findCarsWithFailedInspection(targetDate);
        log.info("Found {} cars with failed inspection on date {}", carsToInspect.size(), targetDate);

        return new StepBuilder("carToInspectStep", jobRepository)
                .<Car, String>chunk(10, transactionManager)
                .reader(new ListItemReader<>(carsToInspect))
                .processor(car -> {
                    String msg = "Car must be inspected (7 days overdue): " + car.getMatricule() + " (" + car.getModel() + ")";
                    log.debug(msg);
                    return msg;
                })
                .writer(items -> items.forEach(item -> {
                    log.info("Writing item: {}", item);
                    System.out.println(item);
                }))
                .build();
    }
}
