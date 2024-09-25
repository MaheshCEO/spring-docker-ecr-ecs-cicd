package com.batch.spring_batch_process.config;

public class MyRetryableException extends Exception {

	 public MyRetryableException(String message) {
	        super(message);
	    }

	    public MyRetryableException(String message, Throwable cause) {
	        super(message, cause);
	    }
	
}
