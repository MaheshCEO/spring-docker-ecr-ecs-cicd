package com.batch.spring_batch_process.config;

public class MySkipableException extends Exception{
	
	 public MySkipableException(String message) {
	        super(message);
	    }

	    public MySkipableException(String message, Throwable cause) {
	        super(message, cause);
	    }

}
