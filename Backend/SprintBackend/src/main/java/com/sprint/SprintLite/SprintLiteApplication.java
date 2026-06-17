package com.sprint.SprintLite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class SprintLiteApplication {

	public static void main(String[] args) {
		String tzId = TimeZone.getDefault().getID();
		if ("Asia/Calcutta".equals(tzId)) {
			TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
		}
		SpringApplication.run(SprintLiteApplication.class, args);
	}

}
