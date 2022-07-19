package com.gnm.adrunner.server.service;

import java.text.ParseException;
import java.util.Date;

import com.gnm.adrunner.config.GlobalConstant;
import com.gnm.adrunner.server.entity.Ads;
import com.gnm.adrunner.server.entity.AdsMedia;
import com.gnm.adrunner.server.entity.RptDay;
import com.gnm.adrunner.server.entity.RptWeek;
import com.gnm.adrunner.server.repo.AdsMediaRepository;
import com.gnm.adrunner.server.repo.AdsRepository;
import com.gnm.adrunner.server.repo.RptDayRepository;
import com.gnm.adrunner.server.repo.RptWeekRepository;
import com.gnm.adrunner.server.repo.SystemConfigRepository;
import com.gnm.adrunner.util.redisUtil;
import com.gnm.adrunner.util.timeBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SchedulerService {
    

    @Autowired
    AdsRepository           adsRepository;

    @Autowired
    PostbackService         postbackService;

    @Autowired
    AdsMediaRepository      adsMediaRepository;

    @Autowired
    RptDayRepository        rptDayRepository;

    @Autowired
    LogAdsService           logAdsService;

    @Autowired
    AdsService              adsService;

    @Autowired
    AdsMediaService         adsMediaService;

    @Autowired
    MemoryDataService       memoryDataService;

    @Autowired
    SystemConfigRepository  systemConfigRepository;

    @Autowired
    RptWeekRepository       rptWeekRepository;

    @Autowired
    RedisService            redisService;


    // 일일 리포트 스케쥴러
    @Scheduled(cron = "0 0 0 * * * ")
    public void insertDailyReport(){
        updateRptDay(false);
    }



    // 주간 리포트 스케쥴러
    @Scheduled(cron = "0 0 0 * * MON")
    public void insertWeeklyReport(){


        String currentDate = timeBuilder.getTodayDate();

        //Redis 에서 광고에 대한 클릭 수 조회
        Iterable<Ads> list = adsRepository.listForScheduler();

        for(Ads e1 : list){

            String adsKey = e1.getAdsKey();
            
            String advKey = e1.getAdvKey();


            String[] list1 = adsMediaRepository.mediaKeyListByAdsKey(adsKey);

            
            for(String mediaKey : list1){

                String  startDate    = "";
                String  endDate      = "";
                Integer totalClicks         = 0;
                Integer totalConversions    = 0;
                Integer totalCost1          = 0;
                Integer totalCost2          = 0;
                Float   conversionRate      = (float)0.0;
                
                for(int i=-1; i>=-7; i--){
                        
                    String previousDate = timeBuilder.getPreviousDate(i);

                    if(i == -1)endDate      = previousDate;

                    if(i == -7)startDate    = previousDate;


                    Iterable<RptDay> list2 = rptDayRepository.getDayRptByDate(previousDate, mediaKey, adsKey);

                    for(RptDay e : list2){
                        totalClicks += e.getClicks();
                        totalConversions += e.getConversions();
                        totalCost1 += e.getCost1();
                        totalCost2 += e.getCost2();
                    }
                }



                // 매체사별로 주별 리포트 삽입
                RptWeek rw = new RptWeek();
                rw.setAdsKey(adsKey);
                rw.setAdvKey(advKey);
                rw.setDate(currentDate);
                rw.setStartdate(startDate);
                rw.setEnddate(endDate);
                rw.setTotalClicks(totalClicks);
                rw.setTotalConversions(totalConversions);
                rw.setMediaKey(mediaKey);
                rw.setTotalCost1(totalCost1);
                rw.setTotalCost2(totalCost2);

                if(totalConversions != 0)
                    conversionRate = totalConversions/(float)totalClicks;
                else conversionRate = (float)0.0;

                rw.setConversionRate(conversionRate);
                rptWeekRepository.save(rw);


            }
            
        }


        

    }




    @Scheduled(cron = "*/1 * * * * * ")
    public void refreshStatus() throws ParseException{



        
        Date currentDate    = timeBuilder.toDate(timeBuilder.getCurrentTime());


        // 루프백 기간의 광고에만 해당
        for(Ads e : adsRepository.listByStatus(GlobalConstant.ADS_STATUS_LOOPBACK)){

            Date loopbackDate   = timeBuilder.toDate(e.getLoopbackdate());

            if(loopbackDate.compareTo(currentDate) == -1){
                adsService.updateAdsByStatus(e.getId(), GlobalConstant.ADS_STATUS_DISMISS);
                logAdsService.insert(e.getAdsKey(), "", "system", "time-up", "", "", "");
                memoryDataService.updateMemoryData("ads", e.getId());
                redisUtil.flushDB(e.getRedisGroup(), e.getRedisDb());

                // 광고가 삭제된 후에 Redis DB 가용이 확보되면, 해당 데이터베이스를 사용
                redisService.updateRIndexAfterDeleteAd(e.getRedisGroup(), e.getRedisDb());
                    
            }
            
        }


        // 라이브중인 광고에만 해당
        for(Ads e : adsRepository.listByStatus(GlobalConstant.ADS_STATUS_LIVE)){

            Date endDate      = timeBuilder.toDate(e.getEnddate());

            // 만약 광고 종료시간이 현재 시간보다 전이면 Loopback 상태
            if(endDate.compareTo(currentDate) == -1){
                adsService.updateAdsByStatus(e.getId(), GlobalConstant.ADS_STATUS_LOOPBACK);
                logAdsService.insert(e.getAdsKey(), "", "system", "loopback", "", "", "");
                memoryDataService.updateMemoryData("ads", e.getId());
            }
 
        }




        // 시작대기인 광고에만 해당
        for(Ads e : adsRepository.listByStatus(GlobalConstant.ADS_STATUS_READY)){

            Date startDate      = timeBuilder.toDate(e.getStartdate());

            // 만약 광고 시작시간이 현재 시간을 경과하였으면. 
            if(startDate.compareTo(currentDate) == -1){

                // 그리고 광고 자동시작여부가 체크되어 있으면 자동으로 광고 시작
                if(e.getAutostart()){
                    adsService.updateAdsByStatus(e.getId(), GlobalConstant.ADS_STATUS_LIVE);
                    logAdsService.insert(e.getAdsKey(), "", "system", "start-up", "", "", "");
                    memoryDataService.updateMemoryData("ads", e.getId());
                }
            }
 
        }
        
    }





    // 일일 리포트 스케쥴러 : 테스트가 많이 돼서 함수를 별도로 분리함
    public void updateRptDay(Boolean isTest){
        
        //Redis 에서 광고에 대한 클릭 수 조회
        Iterable<Ads> list = adsRepository.listForScheduler();


        for(Ads ads : list){
            
            String[] mediaKeyList = adsMediaRepository.mediaKeyListByAdsKey(ads.getAdsKey());


            String  adsKey  =   ads.getAdsKey();

            String  advKey  =   ads.getAdvKey();
 
            String date = "";
            String starttime = "";
            String endtime = "";

            

            if(!isTest){
                date         = timeBuilder.getYesterdayDate();
                starttime    = timeBuilder.getYesterdayStartTime();
                endtime      = timeBuilder.getYesterdayEndTime();
            }else {
                date         = timeBuilder.getTodayDate();
                starttime    = timeBuilder.getTodayStartTime();
                endtime      = timeBuilder.getTodayEndTime();
            }




            //광고에 대한 매체사별로 키를 조회하여 클릭 수 및 전환 수 집계
            for(String mediaKey : mediaKeyList){


                
                // 광고 일일 한도 도달 여부 초기화
                AdsMedia am = adsMediaRepository.findByAdsKeyAndMediakey(adsKey, mediaKey);
                adsMediaService.updateTodayLimit(false, adsKey, mediaKey);
                adsMediaRepository.resetRunDailyCap(adsKey, mediaKey);
                memoryDataService.updateMemoryData("ads-media", am.getId());




                //  클릭키 리스트 식별자는 [광고키]:[일자]:[매체키]
                String clkKeyListID         = ads.getAdsKey() + ":" + date + ":" + mediaKey;

        
                //일별 클릭 수
                Integer clickCount = 0;

                //일별 전환 수
                Integer conversionCount = 0;

            
                clickCount = redisUtil.getListSize(clkKeyListID, ads.getRedisGroup(), ads.getRedisDb());

    



            
                // 포스트백 테이블에서 전환 수 조회
                conversionCount = postbackService.countPostbackByDate(adsKey,    mediaKey,       starttime,    endtime);

           
 
                //일별 전환 률
                Float   conversionRate = (float)0.0;
                if(conversionCount != 0)
                    conversionRate = conversionCount/(float)clickCount;




                // 무한대로 수렴할 경우 0으로 보임
                if(conversionRate.isInfinite())
                    conversionRate = (float)0.0;



                    


                //일별 광고 집행 매출
                Integer cost1 = postbackService.getSumAdvCostByDate(adsKey, mediaKey, starttime, endtime);






                //일별 매체사 매출
                Integer cost2 = postbackService.getSumMediaCostByDate(adsKey, mediaKey, starttime, endtime);
    


                


                // 일별 매체사별 리포트 삽입
                RptDay rd = new RptDay();
                rd.setDate(date);
                rd.setClicks(clickCount);
                rd.setConversions(conversionCount);
                rd.setConversionRate(conversionRate);
                rd.setCost1(cost1);
                rd.setCost2(cost2);
                rd.setAdsKey(adsKey);
                rd.setMediaKey(mediaKey);
                rd.setAdvKey(advKey);
                rptDayRepository.save(rd);
                
            }

            
        }
 
    }

}
