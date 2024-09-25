package com.batch.spring_batch_process.dao;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.batch.spring_batch_process.repository.CustomerRepository;

import java.util.List;

	@Component
	public class CustomerWritter implements ItemWriter<Customer> {

		@Autowired
	    private CustomerRepository customerRepository;

//	    public CustomerWritter(CustomerRepository customerRepository) {
//	        this.customerRepository = customerRepository;
//	    }

		@Override
		public void write(Chunk<? extends Customer> chunk) throws Exception {
			System.out.println("==========THREAD NAME ======== " +Thread.currentThread());
			customerRepository.saveAll(chunk);
			// TODO Auto-generated method stub
			
		}
}
