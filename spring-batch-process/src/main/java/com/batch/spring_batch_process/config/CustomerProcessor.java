package com.batch.spring_batch_process.config;

import org.springframework.batch.item.ItemProcessor;

import com.batch.spring_batch_process.dao.Customer;

public class CustomerProcessor implements ItemProcessor<Customer, Customer>{

	@Override
	public Customer process(Customer item) throws Exception {
		// TODO Auto-generated method stub
		return item;
	}
	

}
