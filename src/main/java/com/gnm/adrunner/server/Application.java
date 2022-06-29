package com.gnm.adrunner.server;

import com.gnm.adrunner.config.RedisConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		
		//REDIS 시작
		RedisConfig.init();
		
		SpringApplication.run(Application.class, args);
	}
 
}
