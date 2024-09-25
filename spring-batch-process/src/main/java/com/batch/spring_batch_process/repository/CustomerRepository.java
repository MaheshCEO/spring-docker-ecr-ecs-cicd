package com.batch.spring_batch_process.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.batch.spring_batch_process.dao.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	

}
