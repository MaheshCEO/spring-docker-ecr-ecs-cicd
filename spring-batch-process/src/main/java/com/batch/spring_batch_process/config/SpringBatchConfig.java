package com.batch.spring_batch_process.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.ListItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.batch.spring_batch_process.dao.Customer;
import com.batch.spring_batch_process.dao.CustomerWritter;
import com.batch.spring_batch_process.repository.CustomerRepository;

import lombok.AllArgsConstructor;

@EnableBatchProcessing // --> this annotation no longer required if you use spring version more than 3
@Configuration
@AllArgsConstructor
public class SpringBatchConfig {

	// private JobBuilderFactory jobBuilderFactory; no longer required if you use
	// spring version more than 3
	// private StepBuilderFactory stepBuilderFactory; no longer required if you use
	// spring version more than 3
	 private CustomerRepository customerRepository;
	 private StepStartTimeListener stepStartTimeListener; // 

	// Define the job
	@Bean
	public Job csvJobRunner(JobRepository jobRepository, Step csvStep1) {
		return new JobBuilder("importCustomer", jobRepository)
				.start(csvStep1)
				.build();
	}

	// Define the step
	@Bean
	public Step csvStep1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
			FlatFileItemReader<Customer> csvReader,
			// ListItemWriter<Customer> csvWriter) This writer keeps everything in memory, without persisting data and Useful for testing without persistence and problem is Doesn’t persist data.
			CustomerWritter csvWriter) {
			return new StepBuilder("csvStep1", jobRepository)
					  //.listener(stepStartTimeListener) //
				.<Customer, Customer>chunk(2, transactionManager) // chunk size as 2
				.reader(csvReader) // ItemReader
				//.processor(processor) // ItemProcessor
				.writer(csvWriter) //ItemWritter
				.build();
	}

	// Define the reader (to read data from the CSV file)
	@Bean
	public FlatFileItemReader<Customer> csvReader() {
		return new FlatFileItemReaderBuilder<Customer>()
				.name("csvReader")
				.resource(new ClassPathResource("customer.csv"))
				//.resource(new ClassPathResource("/src/main/resources/customer.csv"))
				.delimited()
				.delimiter(DelimitedLineTokenizer.DELIMITER_COMMA)
				.names("Customer_Index", "User Id", "First Name", "Last Name", "Sex", "Email")
				.fieldSetMapper(new BeanWrapperFieldSetMapper<>() {
					{
						setTargetType(Customer.class);
					}
				})
				.linesToSkip(1)
				.build();
	}

	// Define a writer (to write the processed data)
//	@Bean
//	public ListItemWriter<Customer> csvWriter() {
//		return new ListItemWriter<>();
//	}
//	
//	// Define a processor (to write the processed data)
//	@Bean
//	public CustomerProcessor processor1() {
//	        return new CustomerProcessor();
//	}

	
	//=========================================================================================================================================================
//	@Bean
//    public FlatFileItemReader<Customer> reader() {
//        FlatFileItemReader<Customer> itemReader = new FlatFileItemReader<>();
//        itemReader.setResource(new FileSystemResource("src/main/resources/customers.csv"));
//        itemReader.setName("csvReader");
//        itemReader.setLinesToSkip(1);
//        itemReader.setLineMapper(lineMapper());
//        return itemReader;
//    }
//    @Bean
//    public CustomerProcessor processor() {
//        return new CustomerProcessor();
//    }
//
//    @Bean
//    public RepositoryItemWriter<Customer> writer() {
//        RepositoryItemWriter<Customer> writer = new RepositoryItemWriter<>();
//        writer.setRepository(customerRepository);
//        writer.setMethodName("save");
//        return writer;
//    }
//
//    @Bean
//    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
//        return new StepBuilder("csv-step",jobRepository).
//                <Customer, Customer>chunk(10,transactionManager)
//                .reader(reader())
//                .processor(processor())
//                .writer(writer())
//                .taskExecutor(taskExecutor())
//                .build();
//    }
//
//    @Bean
//    public Job runJob(JobRepository jobRepository,PlatformTransactionManager transactionManager) {
//        return new JobBuilder("importCustomers",jobRepository)
//                .flow(step1(jobRepository,transactionManager)).end().build();
//    }
//
//    @Bean
//    public TaskExecutor taskExecutor() {
//        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
//        asyncTaskExecutor.setConcurrencyLimit(10);
//        return asyncTaskExecutor;
//    }
//    private LineMapper<Customer> lineMapper() {
//        DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();
//
//        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
//        lineTokenizer.setDelimiter(",");
//        lineTokenizer.setStrict(false);
//        lineTokenizer.setNames("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob");
//
//        BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
//        fieldSetMapper.setTargetType(Customer.class);
//
//        lineMapper.setLineTokenizer(lineTokenizer);
//        lineMapper.setFieldSetMapper(fieldSetMapper);
//        return lineMapper;
//
//    }
	 
}
