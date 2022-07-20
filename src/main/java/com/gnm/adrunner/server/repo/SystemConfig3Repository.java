package com.gnm.adrunner.server.repo;

import com.gnm.adrunner.server.entity.SystemConfig3;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SystemConfig3Repository extends CrudRepository<SystemConfig3, Integer>{

    @Query(value="select * from system_config_3 where groupid=?1", nativeQuery = true)
    public SystemConfig3 findByGroupID(Integer groupid);
    
}
