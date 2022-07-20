package com.gnm.adrunner.server.service;

import com.gnm.adrunner.server.entity.SystemConfig3;
import com.gnm.adrunner.server.object.RedisEntity2;
import com.gnm.adrunner.server.repo.AdsRepository;
import com.gnm.adrunner.server.repo.SystemConfig3Repository;
import com.gnm.adrunner.server.repo.SystemConfigRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("RedisService")
public class RedisService {

    @Autowired
    AdsRepository adsRepository;

    @Autowired
    SystemConfigRepository  systemConfigRepository;

    @Autowired
    SystemConfig3Repository systemConfig3Repository;
 
 
    public RedisEntity2 getRIndexForInsertAd(){

        Integer numberOfRedisGroup      = systemConfigRepository.findNumberOfRedisGroup();

        Integer currentRedisGroup       = -1;

        Integer currentRedisDB          = -1;

        for(int i=0; i<numberOfRedisGroup; i++){
            SystemConfig3 sc = systemConfig3Repository.findByGroupID(i);
            

            if(sc.getDb0() == -1){
                currentRedisGroup = i;
                currentRedisDB = 0;
                break;
            }

            if(sc.getDb1() == -1){
                currentRedisGroup = i;
                currentRedisDB = 1;
                break;
            }

            if(sc.getDb2() == -1){
                currentRedisGroup = i;
                currentRedisDB = 2;
                break;
            }

            if(sc.getDb3() == -1){
                currentRedisGroup = i;
                currentRedisDB = 3;
                break;
            }

            if(sc.getDb4() == -1){
                currentRedisGroup = i;
                currentRedisDB = 4;
                break;
            }

            if(sc.getDb5() == -1){
                currentRedisGroup = i;
                currentRedisDB = 5;
                break;
            }

            if(sc.getDb6() == -1){
                currentRedisGroup = i;
                currentRedisDB = 6;
                break;
            }

            if(sc.getDb7() == -1){
                currentRedisGroup = i;
                currentRedisDB = 7;
                break;
            }

            if(sc.getDb8() == -1){
                currentRedisGroup = i;
                currentRedisDB = 8;
                break;
            }

            if(sc.getDb9() == -1){
                currentRedisGroup = i;
                currentRedisDB = 9;
                break;
            }

            if(sc.getDb10() == -1){
                currentRedisGroup = i;
                currentRedisDB = 10;
                break;
            }

            if(sc.getDb11() == -1){
                currentRedisGroup = i;
                currentRedisDB = 11;
                break;
            }

            if(sc.getDb12() == -1){
                currentRedisGroup = i;
                currentRedisDB = 12;
                break;
            }

            if(sc.getDb13() == -1){
                currentRedisGroup = i;
                currentRedisDB = 13;
                break;
            }

            if(sc.getDb14() == -1){
                currentRedisGroup = i;
                currentRedisDB = 14;
                break;
            }

            if(sc.getDb15() == -1){
                currentRedisGroup = i;
                currentRedisDB = 15;
                break;
            }

            sc = null;
        }


        return new RedisEntity2(currentRedisDB, currentRedisGroup);
    }
    
}
