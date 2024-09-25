package com.batch.spring_batch_process.dao;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "Customer")
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
	
	public Customer(Customer customer, Timestamp startTime) {
		// TODO Auto-generated constructor stub
	}
	@Id
	@Column(name="CustomerIndex")
	private int CustomerIndex;
	@Column(name="UserId")
	private String userid;
	@Column(name="FirstName")
	private String firstName;
	@Column(name="LastName")
	private String lastName;
	@Column(name="Sex")
	private String sex;
	@Column(name="Email")
	private String Email;
}
