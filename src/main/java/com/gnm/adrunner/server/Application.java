package com.gnm.adrunner.server;

import java.util.ArrayList;
import java.util.List;

import com.gnm.adrunner.config.GlobalConstant;
import com.gnm.adrunner.config.RedisConfig;
import com.gnm.adrunner.server.repo.ServerInstanceRepository;
import com.gnm.adrunner.server.repo.SystemConfig2Repository;
import com.gnm.adrunner.server.service.SystemConfig3Service;
import com.gnm.adrunner.util.aes256;
import com.gnm.adrunner.util.storageObject;

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
	SystemConfig3Service systemConfig3Service;

	@Autowired
	SystemConfig2Repository systemConfig2Repository;

	public static void main(String[] args) {	
		for(int i=0;i<args.length;i++){
			if(i==0)
				GlobalConstant.RUNNING_MODE = args[i];			
		}

		System.out.println("RUNNING MODE : " + GlobalConstant.RUNNING_MODE);

		SpringApplication.run(Application.class, args);
	}
 
	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() throws Exception {

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
		if(GlobalConstant.RUNNING_MODE.equals("dev")){
			GlobalConstant.NUMBER_OF_REDIS_GROUP = 1;
		}else GlobalConstant.NUMBER_OF_REDIS_GROUP = systemConfig3Service.countTotalRedisGroup();
		
		System.out.println("NUMBER OF REDIS GROUP : " 	+ GlobalConstant.NUMBER_OF_REDIS_GROUP);
		for(int i=0; i<GlobalConstant.NUMBER_OF_REDIS_GROUP; i++){

			List<String> REDIS_CLIENT_IP = new ArrayList<String>();


			// 개발 모드일 때 REDIS는 로컬을 참조하도록
			if(GlobalConstant.RUNNING_MODE.equals("dev")){
				REDIS_CLIENT_IP = serverInstanceRepository.getDevServerClientIpWithGroup(GlobalConstant.SERVER_TYPE_REDIS);
			}else REDIS_CLIENT_IP = serverInstanceRepository.getServerClientIpWithGroup(GlobalConstant.SERVER_TYPE_REDIS, i);


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


		// 버킷 액세스키 
		storageObject.setAccessKey(
			aes256.decrypt(systemConfig2Repository.findByConfigKey("bucket_access_key")));

		// 버킷 시크릿키 
		storageObject.setSecretKey(
			aes256.decrypt(systemConfig2Repository.findByConfigKey("bucket_secret_key")));

		// 버킷 엔드포인트 
		storageObject.setEndPoint(
			systemConfig2Repository.findByConfigKey("bucket_endpoint"));

		// 버킷 리전 
		storageObject.setRegionName(
			systemConfig2Repository.findByConfigKey("bucket_region_name"));

		// 버킷 이름
		storageObject.setBucketName(
			systemConfig2Repository.findByConfigKey("bucket_name"));


		// 버킷 초기화
		storageObject.bucketInit();
	}
}
