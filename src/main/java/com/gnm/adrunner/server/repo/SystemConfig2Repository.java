package com.gnm.adrunner.server.repo;

import com.gnm.adrunner.server.entity.SystemConfig2;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;


public interface SystemConfig2Repository extends CrudRepository<SystemConfig2, Integer>{
 
    @Query(value="select redis_db from system_config_2 where redis_group=?1", nativeQuery = true)
    public Integer findRedisDB(Integer redisGroup);

    @Transactional
    @Modifying
    @Query(value="update system_config_2 set redis_db=?1 where redis_group=?2", nativeQuery = true)
    public void updateRedisDB(Integer redisDB, Integer redisGroup);


}
