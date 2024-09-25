package com.batch.spring_batch_process.config;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.batch.spring_batch_process.dao.Customer;
import com.batch.spring_batch_process.repository.CustomerRepository;


public class CustomerWriter implements ItemWriter<Customer> {

    @Autowired
    private CustomerRepository customerRepository;

    private Timestamp startTime;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        startTime = (Timestamp) stepExecution.getExecutionContext().get("START_TIME");
    }

	@Override
	public void write(Chunk<? extends Customer> chunk) throws Exception {
		System.out.println("==========THREAD NAME ======== " +Thread.currentThread());
		for (Customer customer : chunk) {
			customerRepository.save(new Customer(customer, startTime)); 
        }
		
	}
}
