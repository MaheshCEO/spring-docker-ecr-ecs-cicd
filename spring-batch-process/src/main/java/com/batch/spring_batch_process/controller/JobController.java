package com.batch.spring_batch_process.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.batch.core.Job;
//import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
//import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//																								+-------+-----------+
//																								|       Reader      |
//																							 	+-------+-----------+
//																										/
//																									   /	
//   																								  /
//																									 /	
//     +--------------------+				+--------------------+							+--------------------+  				+-------+-----------+
//	   |	 Job Launcher   |-------------> |	    Job			 | -----------------------> |	Step 			 | -------------->  |	Processor		|
//	   +--------------------+				+--------------------+							+--------------------+ 					+-------+-----------+
//				|									|											\ 	
//				|									|												 \			
//				|									|												  \	
//				|									|												   \	
//	   +--------------------+						|											+-------+-----------+
//	   | Job Repository     |						|											|	Writer          |
//	   +--------------------+						|											+-------+-----------+
//     												|
//													|
//													|
//													|
//													|
//													|
//													|										 +--------------------+                 +--------------------+  
//         											|--------------------------------------->|  Step              |---------------->|    Tasklet         |
//																							 +--------------------+                 +--------------------+  
//
//
//
//============================================================================================================================================================================
//
//

@RestController
@RequestMapping(value = "/BatchJobApi")
public class JobController {

	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	@Qualifier("csvJobRunnerPartitioning")  
	private Job job;

	//@PostMapping(value = "/importCustomer") 
	@PostMapping(value="/importCustomerPartitioning")
	public void postCsvToDbJob() {

		// I had wasted so much time because of required tables spring batch didn't created for me 
		// so I created manually i faced many issues and one table not able to create 
		//https://stackoverflow.com/questions/38248768/spring-batch-job-metadata-persistence-issue
		//BATCH_JOB_EXECUTION_CONTEXT table data type was setting wrong 
		//got this error --> 
		//Each time a job runs spring batch stores the meta data about the job in the db. There are 2 tables: batch_step_execution_context and batch_job_execution_context that are saving the meta data in 2 columns short_context and serialized_context. Both these columns save the exact data but short_context has a limit of 2500 characters and so the data is truncated.
		//https://stackoverflow.com/questions/38248768/spring-batch-job-metadata-persistence-issue
		
		
		// Create a Timestamp for START_TIME
		//Timestamp startTime = new Timestamp(System.currentTimeMillis());

		// Set the START_TIME parameter
		JobParameters jobParams = new JobParametersBuilder().addLong("startAt", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(job, jobParams);
		} catch (Exception e) {
			// Handle exception
			e.printStackTrace();
		}
	}

}
