package com.gnm.adrunner.server.repo;

import com.gnm.adrunner.server.entity.AdsCreative;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
 
public interface AdsCreativeRepository extends CrudRepository<AdsCreative, Integer> {

    @Transactional
    @Modifying
    @Query(value="delete from ads_creative", nativeQuery = true)
    public void removeAll();

    @Query(value="select * from ads_creative where ads_key=?1", nativeQuery = true)
    public AdsCreative findByAdsKey(String adsKey);

  
}
