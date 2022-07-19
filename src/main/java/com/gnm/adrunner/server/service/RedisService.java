package com.gnm.adrunner.server.service;

import javax.transaction.Transactional;

import com.gnm.adrunner.server.object.RedisEntity2;
import com.gnm.adrunner.server.repo.SystemConfig2Repository;
import com.gnm.adrunner.server.repo.SystemConfigRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("RedisService")
public class RedisService {


    @Autowired
    SystemConfigRepository  systemConfigRepository;

    @Autowired
    SystemConfig2Repository systemConfig2Repository;

    @Transactional
    public RedisEntity2 getRIndexForInsertAd(){

        Integer currentRedisGroup       = systemConfigRepository.getCurrentRedisGroup();

        Integer currentRedisDB          = systemConfig2Repository.findRedisDB(currentRedisGroup);

        // Redis DB가 16개까지 꽉 차있으면 다음 그룹으로 넘어감 
        if(currentRedisDB == 16){
            currentRedisGroup++;
            currentRedisDB = 0;
            systemConfigRepository.updateCurrentRedisGroup(currentRedisGroup);
            systemConfig2Repository.updateRedisDB((currentRedisDB+1), currentRedisGroup);
        }else systemConfig2Repository.updateRedisDB((currentRedisDB+1), currentRedisGroup);
   
        return new RedisEntity2(currentRedisDB, currentRedisGroup);
    }

    
    @Transactional
    public void updateRIndexAfterDeleteAd(Integer adsRedisGroup, Integer adsRedisDB){

        // 광고 삭제시 REDIS 참조 인덱스 업데이트 
        Integer currentRedisGroup   = systemConfigRepository.getCurrentRedisGroup();
        Integer currentRedisDB   = systemConfig2Repository.findRedisDB(currentRedisGroup);

        if(currentRedisGroup.compareTo(adsRedisGroup) < 0){
            if(currentRedisDB.compareTo(adsRedisDB) < 0)
                systemConfig2Repository.updateRedisDB(adsRedisDB, currentRedisGroup);

        }else{
            systemConfigRepository.updateCurrentRedisGroup(adsRedisGroup);
            systemConfig2Repository.updateRedisDB(adsRedisDB, adsRedisGroup);
        }
    }

    
}
