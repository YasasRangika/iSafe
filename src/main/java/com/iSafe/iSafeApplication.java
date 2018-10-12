package com.iSafe;

import java.util.Date;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableScheduling
public class iSafeApplication {

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+5:30")); // Set India Standard Time -> Time zone in Sri Lanka
																// (GMT+5:30)
		System.out.println("Spring boot application running in UTC timezone :" + new Date());
	}

	public static void main(String[] args) {
		SpringApplication.run(iSafeApplication.class, args);
	}
}
