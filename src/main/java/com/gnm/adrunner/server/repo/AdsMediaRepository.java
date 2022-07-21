package com.gnm.adrunner.server.repo;

import java.util.List;

import javax.transaction.Transactional;

import com.gnm.adrunner.server.entity.AdsMedia;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AdsMediaRepository extends CrudRepository<AdsMedia, Integer> {

    @Query(value="select media_key from ads_media where ads_key = ?1", nativeQuery = true)
    public String[] mediaKeyListByAdsKey(String adsKey);


    @Query(value="select id from ads_media where ads_key = ?1 and media_key = ?2", nativeQuery = true)
    public Integer selectIdByAdsKeyAndMediaKey(String adsKey, String mediaKey);

    @Query(value="select * from ads_media", nativeQuery = true)
    public List<AdsMedia> listAll();


    @Query(value="select * from ads_media where id = ?1", nativeQuery = true)
    public AdsMedia findByID(Integer id);


    @Query(value="select * from ads_media where ads_key=?1 and media_key = ?2", nativeQuery = true)
    public AdsMedia findByAdsKeyAndMediakey(String adsKey, String mediaKey);


    @Query(value="select media_cost from ads_media where ads_key=?1 and media_key = ?2", nativeQuery = true)
    public Integer getMediaCostByAdsKeyAndMediaKey(String adsKey, String mediaKey);

    @Query(value="select media_daily_cap from ads_media where ads_key=?1 and media_key = ?2", nativeQuery = true)
    public Integer getMediaDailyCapByAdsKeyAndMediaKey(String adsKey, String mediaKey);


    @Transactional
    @Modifying
    @Query(value="update ads_media SET today_limit=?1 where ads_key=?2 and media_key = ?3", nativeQuery = true)
    public void updateTodayLimit(Boolean value, String adsKey, String mediaKey);


    @Transactional
    @Modifying
    @Query(value="update ads_media SET media_cost=?3, media_daily_cap=?4 where media_key = ?2 and ads_key =?1", nativeQuery = true)
    public void updateMediaCostAndDailyCap(String adsKey, String mediaKey, Integer cost, Integer dailyCap);


    @Query(value="select is_day_limit from ads_media where ads_key=?1 and media_key = ?2", nativeQuery = true)
    public Boolean getIsDayLimitByAdskeyAndMediakey(String adsKey, String e);


    
    @Transactional
    @Modifying
    @Query(value="update ads_media SET is_delete=1 where ads_key=?1 and media_key = ?2", nativeQuery = true)
    public void deleteByAdskeyAndMediaKey(String adsKey, String mediaKey);


    @Transactional
    @Modifying
    @Query(value="update ads_media SET is_delete=0 where ads_key=?1 and media_key = ?2", nativeQuery = true)
    public void recoverByAdsKeyAndMediaKey(String adsKey, String mediaKey);


    @Transactional
    @Modifying
    @Query(value="update ads_media SET run_daily_cap = run_daily_cap + ?1 where ads_key=?2 and media_key = ?3", nativeQuery = true)
    public void addDailyCap(Integer value, String adsKey, String mediaKey);


    @Transactional
    @Modifying
    @Query(value="update ads_media SET run_daily_cap = media_daily_cap where ads_key =?1 and media_key =?2", nativeQuery = true)
    public void resetRunDailyCap(String adsKey, String mediaKey);


    @Query(value="select run_daily_cap from ads_media where ads_key=?1 and media_key = ?2", nativeQuery = true)
    public Integer getRunDailyCapByAdsKeyAndMediaKey(String adsKey, String mediaKey);


    @Transactional
    @Modifying
    @Query(value="update ads_media SET is_day_limit=?1 where ads_key=?2 and media_key = ?3", nativeQuery = true)
    public void updateIsLimit(Boolean value, String adsKey, String mediaKey);


    @Transactional
    @Modifying
    @Query(value="update ads_media SET media_cost=?1 where ads_key=?2 and media_key = ?3", nativeQuery = true)
    public void updateMediaCost(Integer cost, String adsKey, String mediaKey);


    @Transactional
    @Modifying
    @Query(value="update ads_media SET media_daily_cap=?1 where ads_key=?2 and media_key = ?3", nativeQuery = true)
    public void updateMediaDailycap(Integer dailycap, String adsKey, String mediaKey);


    
    @Transactional
    @Modifying
    @Query(value="delete from ads_media", nativeQuery = true)
    public void removeAll();
    


}
