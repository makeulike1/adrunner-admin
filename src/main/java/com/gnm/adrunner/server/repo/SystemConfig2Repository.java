package com.gnm.adrunner.server.repo;

import com.gnm.adrunner.server.entity.SystemConfig2;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SystemConfig2Repository extends CrudRepository<SystemConfig2, Integer>{

    @Query(value="select config_value from system_config_2 where config_key=?1", nativeQuery = true)
    public String findByConfigKey(String key);
    
}
