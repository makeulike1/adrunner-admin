package com.gnm.adrunner.server.repo;

import com.gnm.adrunner.server.entity.RptDay;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional; 

public interface RptDayRepository extends CrudRepository<RptDay, Integer>{

    @Query(value="select * from rpt_day where date= ?1 and media_key = ?2 and ads_key =?3", nativeQuery = true)
    Iterable<RptDay> getDayRptByDate(String Date, String mediaKey, String adsKey);

    @Transactional
    @Modifying
    @Query(value="delete from rpt_day", nativeQuery = true)
    void removeAll();
    
}
 