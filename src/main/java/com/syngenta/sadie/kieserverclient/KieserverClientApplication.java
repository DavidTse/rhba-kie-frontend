package com.syngenta.sadie.kieserverclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
public class KieserverClientApplication extends org.springframework.boot.web.servlet.support.SpringBootServletInitializer {

	@Autowired
	ThreadPoolTaskExecutor threadPoolTaskExecutor;
	public static void main(String[] args) {
		SpringApplication.run(KieserverClientApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(KieserverClientApplication.class);
	}
}
