package com.fever.remains.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class FollowConfig {
	@Bean
	public RelationClient relationClient(){
		return new RelationClient(Profile.DEV);
	}
}
