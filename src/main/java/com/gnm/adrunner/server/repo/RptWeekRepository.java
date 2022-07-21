package com.gnm.adrunner.server.repo;

import com.gnm.adrunner.server.entity.RptWeek;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RptWeekRepository extends CrudRepository<RptWeek, Integer>{

    @Transactional
    @Modifying
    @Query(value="delete * from rpt_week", nativeQuery = true)
    void removeAll();
    
}
