package com.gnm.adrunner.server.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import com.gnm.adrunner.server.entity.AdsMedia;
import com.gnm.adrunner.server.param.req.admin.RequestSaveAds1;
import com.gnm.adrunner.server.repo.AdsMediaRepository;
import com.gnm.adrunner.server.repo.AdsRepository;
import com.gnm.adrunner.server.repo.MediaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("AdsMediaService")
public class AdsMediaService {

    @Autowired
    AdsMediaRepository adsMediaRepository;

    @Autowired
    MediaRepository mediaRepository;

    @Autowired
    LogAdsService logAdsService;

    @Autowired
    AdsRepository adsRepository;

    @PersistenceContext
	private EntityManager entityManager;

    @Transactional
    public Integer saveAdsMedia(AdsMedia adsMedia){
        entityManager.persist(adsMedia);
        return (int)(long)adsMedia.getId();
    }

    @Transactional
    public void updateTodayLimit(Boolean value, String adsKey, String mediaKey){
        adsMediaRepository.updateTodayLimit(value, adsKey, mediaKey);
    }

    @Transactional
    public void modifyAdsMedia(AdsMedia am, RequestSaveAds1 e, String remoteAddr, String adminId, String mediaKey) {
    
        
        String  name                = e.getName();

        Integer REQ_MEDIA_COST          = e.getCost();
        Integer REQ_MEDIA_DAILYCAP      = e.getDailycap();  
        Boolean REQ_IS_LIMIT            = e.getIsLimit();          

        String  adsKey                  = am.getAdsKey();

        Integer PREV_MEDIA_COST         = am.getMediaCost();
        Integer PREV_MEDIA_DAILYCAP     = am.getMediaDailyCap();
        Boolean PREV_IS_LIMIT           = am.getIsDayLimit();


        // 특정 매체사키에 대하여 매체사 단가와 데일리캡을 업데이트
        adsMediaRepository.updateMediaCostAndDailyCap(adsKey, mediaKey, REQ_MEDIA_COST, REQ_MEDIA_DAILYCAP);  
        
        

        // 데일리캡이 업데이트되면 runDailyCap도 업데이트 시킴
        adsMediaRepository.resetRunDailyCap(adsKey, mediaKey);

        

        // 매체사 단가 업데이트에 대해서 업데이트 로그
        if(!PREV_MEDIA_COST.equals(REQ_MEDIA_COST))
            logAdsService.insert(adsKey, remoteAddr, adminId, name+"의 매체사 단가",        PREV_MEDIA_COST.toString(),     REQ_MEDIA_COST.toString());   




        // 매체사 데일리캡 업데이트에 대해서 업데이트 로그
        if(!PREV_MEDIA_DAILYCAP.equals(REQ_MEDIA_DAILYCAP))
            logAdsService.insert(adsKey, remoteAddr, adminId, name+" 매체사 일일 한도",     PREV_MEDIA_DAILYCAP.toString(), REQ_MEDIA_DAILYCAP.toString());   
     

            
        // 매체사 일일한도 
        if(!PREV_IS_LIMIT.equals(REQ_IS_LIMIT))
            logAdsService.insert(adsKey, remoteAddr, adminId, name+" 매체사 데일리캡 사용여부",     PREV_IS_LIMIT.toString(),       REQ_IS_LIMIT.toString());   
            


    }

    public void deleteById(Integer amid) {
        adsMediaRepository.deleteById(amid);
    }

}
