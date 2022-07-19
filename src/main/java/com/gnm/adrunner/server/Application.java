package com.gnm.adrunner.server;

import java.util.List;

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
		System.out.println("######################################################################");
		GlobalConstant.SERVER_HOST_ADMIN = serverInstanceRepository.getServerHost(GlobalConstant.SERVER_TYPE_ADMIN);
		System.out.println("SERVER HOST(ADMIN) : " 		+ GlobalConstant.SERVER_HOST_ADMIN);

		// 클릭 서버 호스트 조회
		GlobalConstant.SERVER_HOST_CLICK = serverInstanceRepository.getServerHost(GlobalConstant.SERVER_TYPE_CLICK);
		System.out.println("SERVER HOST(CLICK)) : " 	+ GlobalConstant.SERVER_HOST_CLICK);

		// 포스트백 서버 호스트 조회
		GlobalConstant.SERVER_HOST_PBACK = serverInstanceRepository.getServerHost(GlobalConstant.SERVER_TYPE_PBACK);
		System.out.println("SERVER HOST(POSTBACK) : " 	+ GlobalConstant.SERVER_HOST_PBACK);

	 
		// Redis 그룹 개수 조회
		GlobalConstant.NUMBER_OF_REDIS_GROUP = systemConfigRepository.findNumberOfRedisGroup();
		System.out.println("NUMBER OF REDIS GROUP : " 	+ GlobalConstant.NUMBER_OF_REDIS_GROUP);
		for(int i=0; i<GlobalConstant.NUMBER_OF_REDIS_GROUP; i++){
			List<String> REDIS_CLIENT_IP = serverInstanceRepository.getServerClientIpWithGroup(GlobalConstant.SERVER_TYPE_REDIS, i);
			GlobalConstant.SERVER_HOST_REDIS.add(REDIS_CLIENT_IP);
			System.out.print("GROUP NUMBER : #"+i+" - ");
			for(String e : REDIS_CLIENT_IP)System.out.print("["+e+"]");
			System.out.println();
			REDIS_CLIENT_IP = null;
		}

		System.out.println("######################################################################");
		System.out.println();
	

		// Redis 시작
		RedisConfig.init();
	}
}
