package com.gnm.adrunner.server.repo;

import java.util.List;

import com.gnm.adrunner.server.entity.LogAds;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface LogAdsRepository extends CrudRepository<LogAds, Integer> {

    @Query(value="select * from log_ads where ads_key=?1", nativeQuery = true)
    public List<LogAds> findByAdsKey(String adsKey);

    @Transactional
    @Modifying
    @Query(value="delete from log_ads", nativeQuery = true)
    public void removeAll();
    
}
