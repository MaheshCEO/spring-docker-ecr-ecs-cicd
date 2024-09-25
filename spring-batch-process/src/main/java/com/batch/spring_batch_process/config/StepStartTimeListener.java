package com.batch.spring_batch_process.config;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class StepStartTimeListener extends StepExecutionListenerSupport {

//    @Override
//    public void beforeStep(StepExecution stepExecution) {
//        // Set the start time in the step execution context
//      //  Timestamp startTime = new Timestamp(System.currentTimeMillis());
//    	JobParameters jobParameters = stepExecution.getJobParameters();
//        Date startTime = (Date) jobParameters.getDate("START_TIME");
//        stepExecution.getExecutionContext().put("START_TIME", startTime);
//
//        // Optional: Log the start time
//        System.out.println("Step Start Time: " + startTime);
//    }
    
    @Override
    public void beforeStep(StepExecution stepExecution) {
        // Get the execution context
        ExecutionContext executionContext = stepExecution.getExecutionContext();

        // Store the start time
        LocalDateTime startTime = LocalDateTime.now();
        executionContext.put("startTime", startTime);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        // Optional: Log or do something with the end time
        LocalDateTime endTime = LocalDateTime.now();
        System.out.println("Step ended at: " + endTime);

        return super.afterStep(stepExecution);
    }
}
