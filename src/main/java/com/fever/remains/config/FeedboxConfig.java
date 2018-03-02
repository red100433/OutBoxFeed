package com.fever.remains.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class FeedboxConfig {
	@Bean
	public FeedboxClient feedboxClient() {
		return new FeedboxClient(Profile.DEV);
	}
}
