package com.algaworks.brewer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BrewerApplication {
	
	private static ApplicationContext APPLICATIONCONTEXT;

	public static void main(String[] args) {
		APPLICATIONCONTEXT = SpringApplication.run(BrewerApplication.class, args);
	}
	
	public static <T> T getBean(Class<T> requiredType) {
		return APPLICATIONCONTEXT.getBean(requiredType);
	}
	 
}
