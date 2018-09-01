package com.iSafe.helpers;

import java.text.ParseException;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Cron {

	@Scheduled(cron = "000**SUN") // every Sunday at midnight
	public void cron() throws ParseException {
		
	}
}
