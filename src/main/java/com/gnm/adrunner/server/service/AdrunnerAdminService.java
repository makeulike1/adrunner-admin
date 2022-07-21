package com.gnm.adrunner.server.service;

import com.gnm.adrunner.server.repo.AdsCreativeRepository;
import com.gnm.adrunner.server.repo.AdsMediaRepository;
import com.gnm.adrunner.server.repo.AdsRepository;
import com.gnm.adrunner.server.repo.LogAdsRepository;
import com.gnm.adrunner.server.repo.PostbackRepository;
import com.gnm.adrunner.server.repo.RptDayRepository;
import com.gnm.adrunner.server.repo.RptWeekRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("AdrunnerAdminService")
public class AdrunnerAdminService {

    @Autowired
    AdsRepository adsRepository;

    @Autowired
    AdsCreativeRepository adsCreativeRepository;

    @Autowired
    AdsMediaRepository  adsMediaRepository;

    @Autowired
    LogAdsRepository logAdsRepository;

    @Autowired
    PostbackRepository postbackRepository;

    @Autowired
    RptDayRepository rptDayRepository;

    @Autowired
    RptWeekRepository rptWeekRepository;
    
    public void removeAllAds() {

        // 모든 광고 삭제
        adsRepository.removeAll();

        // 모든 광고 소재 삭제
        adsCreativeRepository.removeAll();

        // 모든 광고에 연동된 매체 삭제
        adsMediaRepository.removeAll();

        // 모든 광고 로그 삭제
        logAdsRepository.removeAll();

        // 모든 포스트백 로그 삭제
        postbackRepository.removeAll();

        // 모든 일일 리포트 삭제
        rptDayRepository.removeAll();

        // 모든 주간 리포트 삭제
        rptWeekRepository.removeAll();
         

    }
    
}
