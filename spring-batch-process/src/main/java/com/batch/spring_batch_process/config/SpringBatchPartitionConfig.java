package com.batch.spring_batch_process.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import com.batch.spring_batch_process.dao.Customer;
import com.batch.spring_batch_process.dao.CustomerWritter;
import com.batch.spring_batch_process.partition.ColoumnRangePartitioning;
import com.batch.spring_batch_process.repository.ChunkAuditRepository;

import lombok.AllArgsConstructor;

@EnableBatchProcessing
@Configuration //: Indicates that the class contains bean definitions.
//@AllArgsConstructor
public class SpringBatchPartitionConfig {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final FlatFileItemReader<Customer> csvReaderPartition;
	private final CustomerWritter csvWriter;
	private final ChunkAuditRepository chunkAuditRepository;

	// Constructor-based dependency injection
	public SpringBatchPartitionConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager,
			FlatFileItemReader<Customer> csvReaderPartition, CustomerWritter csvWriter,ChunkAuditRepository chunkAuditRepository) {
		this.jobRepository = jobRepository;
		this.transactionManager = transactionManager;
		this.csvReaderPartition = csvReaderPartition;
		this.csvWriter = csvWriter;
		this.chunkAuditRepository = chunkAuditRepository;
	}

	// Define the job
	@Bean
	public Job csvJobRunnerPartitioning(JobRepository jobRepository, Step csvStep1) {
		return new JobBuilder("importCustomerPartitioning", jobRepository)
				.start(masterStep(jobRepository, transactionManager, csvReaderPartition, csvWriter)).build();
	}

	// Define the master step
	@Bean
	public Step masterStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
			FlatFileItemReader<Customer> csvReaderPartition, CustomerWritter csvWriter) {
		System.out.println("======================Master Step==============================================");
		return new StepBuilder("masterStep", jobRepository)
				.partitioner("slaveStep", getColoumnRangePartitioning())
				.partitionHandler(getPartitionHandler())
				.build();
	}

	// Define the slave step
	@Bean
	public Step slaveStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
			FlatFileItemReader<Customer> csvReaderPartition, CustomerWritter csvWriter) {
		System.out.println("======================slaveStep==============================================");
		return new StepBuilder("slaveStep", jobRepository).<Customer, Customer>chunk(2, transactionManager) // CHUNK is used to receive notifications during the execution of a chunk,
				// which allows you to implement custom logic at specific points in the chunk processing lifecycle.
				// You can use a ChunkListener to monitor chunk execution, log information, handle exceptions, or implement custom rollback logic.
				 .reader(csvReaderPartition)
				// .processor(processor)
				 .writer(csvWriter)
				 .transactionManager(transactionManager)
				 .faultTolerant()
			     .retry(MyRetryableException.class)  // Retry on this exception
			     .retryLimit(3)  // Retry up to 3 times
			     .skip(MySkipableException.class)  // Skip records with this exception
			     .skipLimit(5)
			  //   .rollback(MySpecificException.class)  // Rollback when this exception occurs
			     .noRollback(MySpecificException.class)//  means no rollback require for this exceptions
			     .listener(new MyChunkListener(chunkAuditRepository)) // Registering the ChunkListener
	             .build();
	}

	@Bean
	public ColoumnRangePartitioning getColoumnRangePartitioning() {
		return new ColoumnRangePartitioning();
	}

	@Bean
	public PartitionHandler getPartitionHandler() {
		System.out.println("======================PartitionHandler==============================================");
		TaskExecutorPartitionHandler taskExecutorPartitionHandler = new TaskExecutorPartitionHandler();
		taskExecutorPartitionHandler.setGridSize(2);
		taskExecutorPartitionHandler.setTaskExecutor(taskExecutor());
		taskExecutorPartitionHandler.setStep(slaveStep(jobRepository, transactionManager, csvReaderPartition, csvWriter));
		return taskExecutorPartitionHandler;
	}

	@Bean
	public TaskExecutor taskExecutor() {
		System.out.println("======================TaskExecutor==============================================");
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setMaxPoolSize(4);
		threadPoolTaskExecutor.setCorePoolSize(4);
		threadPoolTaskExecutor.setQueueCapacity(4);
		return threadPoolTaskExecutor;
	}

	// Define the reader (to read data from the CSV file)
	@Bean
//	@Primary
	public FlatFileItemReader<Customer> csvReaderPartitionPartition() {
		return new FlatFileItemReaderBuilder<Customer>().name("csvReaderPartition")
				.resource(new ClassPathResource("customer.csv")).delimited()
				.delimiter(DelimitedLineTokenizer.DELIMITER_COMMA)
				.names("Customer_Index", "User Id", "First Name", "Last Name", "Sex", "Email")
				.fieldSetMapper(new BeanWrapperFieldSetMapper<>() {
					{
						setTargetType(Customer.class);
					}
				}).linesToSkip(1).build();
	}

}
