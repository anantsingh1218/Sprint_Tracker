package com.sprint.SprintLite;

import com.sprint.SprintLite.security.util.CorsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.TimeZone;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
@EnableConfigurationProperties(value = {CorsProperties.class})
public class SprintLiteApplication {

	public static void main(String[] args) {
		String tzId = TimeZone.getDefault().getID();
		if ("Asia/Calcutta".equals(tzId)) {
			TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
		}
		SpringApplication.run(SprintLiteApplication.class, args);
	}
}
