package com.example.app.billing.infrastructure.adapter.in.job;

import com.example.app.billing.application.port.in.ProcessDueSubscriptionsUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Scheduled job is another inbound adapter. It reuses the same inbound port as the controller.
 */
@Component
public class ProcessDueSubscriptionsJob {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessDueSubscriptionsJob.class);

    private final ProcessDueSubscriptionsUseCase useCase;

    public ProcessDueSubscriptionsJob(ProcessDueSubscriptionsUseCase useCase) {
        this.useCase = useCase;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void runHourly() {
        LOG.info("Running scheduled billing job");
        useCase.process(LocalDate.now());
    }
}
