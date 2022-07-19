package com.gnm.adrunner.server;

import com.gnm.adrunner.config.GlobalConstant;
import com.gnm.adrunner.config.RedisConfig;
import com.gnm.adrunner.server.repo.ServerInstanceRepository;
import com.gnm.adrunner.server.repo.SystemConfigRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class Application {


	@Autowired
	ServerInstanceRepository serverInstanceRepository;

	@Autowired
	SystemConfigRepository systemConfigRepository;

	public static void main(String[] args) {	
		SpringApplication.run(Application.class, args);
	}
 
	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {

		// 관리자 서버 호스트 조회
		GlobalConstant.SERVER_HOST_ADMIN = serverInstanceRepository.getServerHost(GlobalConstant.SERVER_TYPE_ADMIN);

		// 클릭 서버 호스트 조회
		GlobalConstant.SERVER_HOST_CLICK = serverInstanceRepository.getServerHost(GlobalConstant.SERVER_TYPE_CLICK);

		// 포스트백 서버 호스트 조회
		GlobalConstant.SERVER_HOST_PBACK = serverInstanceRepository.getServerHost(GlobalConstant.SERVER_TYPE_PBACK);

	 
		// Redis 그룹 개수 조회
		GlobalConstant.NUMBER_OF_REDIS_GROUP = systemConfigRepository.findNumberOfRedsiGroup();
		for(int i=0; i<GlobalConstant.NUMBER_OF_REDIS_GROUP; i++)
			GlobalConstant.SERVER_HOST_REDIS.add(serverInstanceRepository.getServerClientIpWithGroup(GlobalConstant.SERVER_TYPE_REDIS, i));
	

		// Redis 시작
		RedisConfig.init();
	}
}
