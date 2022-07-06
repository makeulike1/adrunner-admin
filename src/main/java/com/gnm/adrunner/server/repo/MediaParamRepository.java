package com.gnm.adrunner.server.repo;

import com.gnm.adrunner.server.entity.MediaParam;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MediaParamRepository extends CrudRepository<MediaParam, Integer>{

    
    @Query(value="select * from media_param where media_key=?1", nativeQuery = true)
    Iterable<MediaParam> findByMediaKey(String mediaKey);


}