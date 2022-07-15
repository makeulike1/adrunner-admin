package com.gnm.adrunner.server.controller.admin;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.gnm.adrunner.server.RequestResponseInterface;
import com.gnm.adrunner.server.entity.AdsMedia;
import com.gnm.adrunner.server.entity.Media;
import com.gnm.adrunner.server.param.req.admin.RequestSaveAdsMedia;
import com.gnm.adrunner.server.param.res.admin.ResponseMediaList;
import com.gnm.adrunner.server.repo.AdminLoginRepository;
import com.gnm.adrunner.server.repo.AdsMediaRepository;
import com.gnm.adrunner.server.repo.AdsRepository;
import com.gnm.adrunner.server.repo.LogAdsRepository;
import com.gnm.adrunner.server.repo.MediaRepository;
import com.gnm.adrunner.server.repo.SystemConfigRepository;
import com.gnm.adrunner.server.repo.ViewAdsMediaRepository;
import com.gnm.adrunner.server.service.AdminLoginService;
import com.gnm.adrunner.server.service.AdsService;
import com.gnm.adrunner.server.service.LogAdsService;
import com.gnm.adrunner.server.service.MemoryDataService;
import com.gnm.adrunner.server.service.PostbackService;
import com.gnm.adrunner.server.service.AdsMediaService;
import com.gnm.adrunner.util.redisUtil;
import com.gnm.adrunner.util.timeBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller 
public class AdsMediaController extends RequestResponseInterface{




    @Autowired  
    AdsRepository adsRepository;

    @Autowired
    AdsService adsService;

    @Autowired
    AdsMediaService adsMediaService;

    @Autowired
    AdminLoginService adminLoginService;

    @Autowired
    AdsMediaRepository adsMediaRepository;

    @Autowired
    MediaRepository mediaRepository;

    @Autowired
    ViewAdsMediaRepository viewAdsMediaRepository;
    
    @Autowired
    AdminLoginRepository adminLoginRepository;

    @Autowired
    LogAdsRepository logAdsRepository;

    @Autowired
    LogAdsService logAdsService;

    @Autowired
    MemoryDataService memoryDataService;

    @Autowired
    SystemConfigRepository systemConfigRepository;

    @Autowired
    PostbackService postbackService;
 
  

    // 광고에 연동되어 있는 매체사 목록  
    @CrossOrigin(origins = "*")
    @GetMapping("/ads/media/list")
    public @ResponseBody ResponseEntity<String> list_media(@RequestParam(value="adsKey", required=true) String adsKey, HttpServletRequest request) {
    
            
        HttpHeaders responseHeaders = new HttpHeaders();
            
        // 유효하지 않은 토큰인 경우 203 에러 
        if(adminLoginService.chkToken(request.getHeader("token")) == 203){
            return ResponseEntity.status(203)
                .headers(responseHeaders)
                .body(getStatusMessage(203));
        }
            

        return ResponseEntity.status(200)
            .headers(responseHeaders)
            .body(gson.toJson(viewAdsMediaRepository.findByAdsKey(adsKey)));
        
    }



    // 광고에 연동된 매체사 등록
    @CrossOrigin(origins = "*")
    @PostMapping("/ads/media/save") 
    public @ResponseBody ResponseEntity<String> save_media(
        @RequestBody RequestSaveAdsMedia req, HttpServletRequest request){

        HttpHeaders responseHeaders = new HttpHeaders();
        

        String token = request.getHeader("token");


        // 유효하지 않은 토큰인 경우 203 에러 
        if(adminLoginService.chkToken(token) == 203){
            return ResponseEntity.status(203)
                .headers(responseHeaders)
                .body(getStatusMessage(203));
        }


        String adsKey       = req.getAdsKey();

        String mediaName    = req.getMediaName();

        String mediaKey     =   mediaRepository.getKeyByName(mediaName);


        Integer amid = adsMediaRepository.selectIdByAdsKeyAndMediaKey(adsKey, mediaKey);


        // 만약 사전에 연동되어 있던 매체사인 경우
        if(amid != null){
            adsMediaRepository.recoverByAdsKeyAndMediaKey(adsKey, mediaKey);

            // 클릭 서버 : 메모리 데이터 삭제
            memoryDataService.deleteMemoryData("ads-media", amid);

        }else{


            // 광고에 연동된 매체사 신규 추가
            AdsMedia adsMedia = new AdsMedia();
            adsMedia.setAdsKey(adsKey);
            adsMedia.setMediaKey(mediaKey);
            adsMedia.setMediaCost(0);
            adsMedia.setMediaDailyCap(0);
            adsMedia.setIsDayLimit(false);
            adsMedia.setRunDailyCap(0);
            adsMedia.setIsDelete(false);
            adsMedia.setCreatetime(timeBuilder.getCurrentTime());


            Integer amId = adsMediaService.saveAdsMedia(adsMedia);

            // 메모리 데이터 추가
            memoryDataService.addMemoryData("ads-media", amId);
        
        }

        // 매체사 추가 연동에 대해서 광고 이력에 기록
        String adminId = adminLoginRepository.findAdminIdByToken(token);
        
        // 광고 변경 이력 갱신
        logAdsService.insert(adsKey, "", adminId, "media-new", "", "", mediaName);


        return ResponseEntity.status(200)
            .headers(responseHeaders)
            .body(getStatusMessage(200));

    }


    // 광고에 연동된 매체사 삭제
    @CrossOrigin(origins = "*")
    @DeleteMapping("/ads/media/delete") 
    public @ResponseBody ResponseEntity<String> delete_media(
        @RequestParam(value="adsKey", required=true) String adsKey, 
        @RequestParam(value="mediaName", required=true) String mediaName, HttpServletRequest request){

        HttpHeaders responseHeaders = new HttpHeaders();
        

        
        String token = request.getHeader("token");


        // 유효하지 않은 토큰인 경우 203 에러 
        if(adminLoginService.chkToken(token) == 203){
            return ResponseEntity.status(203)
                .headers(responseHeaders)
                .body(getStatusMessage(203));
        }
        

        String mediaKey = mediaRepository.getKeyByName(mediaName);


        // 광고에 연동된 매체사 삭제
        Integer amid = adsMediaRepository.selectIdByAdsKeyAndMediaKey(adsKey, mediaKey);
        adsMediaRepository.deleteByAdskeyAndMediaKey(adsKey, mediaKey);



        // 클릭 서버 : 메모리 데이터 삭제
        memoryDataService.deleteMemoryData("ads-media", amid);


        // 해제된 매체사에 대해서 광고 이력에 기록
        String adminId = adminLoginRepository.findAdminIdByToken(token);



        // 광고 변경 이력 갱신
        logAdsService.insert(adsKey, "", adminId, "media-remove", "", "", mediaName);

        return ResponseEntity.status(200)
            .headers(responseHeaders)
            .body(getStatusMessage(200));
    }




    // 광고에 연동되어 있는 매체사 목록  
    @CrossOrigin(origins = "*")
    @GetMapping("/ads/media/list2")
    public @ResponseBody ResponseEntity<String> list_media2(@RequestParam(value="adsKey", required=true) String adsKey, HttpServletRequest request) {
    
            
        HttpHeaders responseHeaders = new HttpHeaders();
            
        // 유효하지 않은 토큰인 경우 203 에러 
        if(adminLoginService.chkToken(request.getHeader("token")) == 203){
            return ResponseEntity.status(203)
                .headers(responseHeaders)
                .body(getStatusMessage(203));
        }
        


        Integer redisIndex = adsRepository.getRedisIndexByAdsKey(adsKey);

        List<Media> mediaList = mediaRepository.listAll();

        
        // 광고에 연동된 매체사 키 리스트 : 매체사별로 클릭 수 조회
        List<ResponseMediaList> result = new ArrayList<ResponseMediaList>();
        String[] mediaKeyList = adsMediaRepository.mediaKeyListByAdsKey(adsKey);
        for(String mediaKey : mediaKeyList){

            AdsMedia am = adsMediaRepository.findByAdsKeyAndMediakey(adsKey, mediaKey);
            
            Integer ckcount     = redisUtil.getCkCount(adsKey, mediaKey, redisIndex);
            Integer cvcount     = postbackService.countTotalPostbackByAdsKeyAndMediaKey(adsKey, mediaKey);
            Integer todaycv     = postbackService.countTodayTotalPostbackByAdsKeyAndMediaKey(adsKey, mediaKey);

            Integer dailycap    = am.getMediaDailyCap();
            Integer runDailyCap = am.getRunDailyCap();
            Boolean isDayLimit  = am.getIsDayLimit();
            Boolean todayLimit  = am.getTodayLimit();
            Boolean isDelete    = am.getIsDelete();
            


            ResponseMediaList tmp = new ResponseMediaList();
            tmp.setMediaKey(mediaKey);
            tmp.setTotalClicks(ckcount);
            tmp.setTotalConversions(cvcount);
            tmp.setTodaycvCount(todaycv);
            tmp.setDailyCap(dailycap);
            tmp.setRunDailyCap(runDailyCap);
            tmp.setIsDayLimit(isDayLimit);
            tmp.setTodayLimit(todayLimit);
            tmp.setIsDelete(isDelete);

            for(Media e1 : mediaList){
                if(mediaKey.equals(e1.getMediaKey()))
                    tmp.setMediaName(e1.getName());
            }
                
            result.add(tmp);
                
        }
        

        return ResponseEntity.status(200)
            .headers(responseHeaders)
            .body(gson.toJson(result));
          
    }




    // 매체사 데일리캡 추가 확보
    @CrossOrigin(origins = "*")
    @GetMapping("/ads/media/add-daily-cap")
    public @ResponseBody ResponseEntity<String> add_daily_cap(
        @RequestParam(value="adsKey", required=true) String adsKey, 
        @RequestParam(value="mediaKey", required=true) String mediaKey, 
        @RequestParam(value="value", required=true) Integer value, HttpServletRequest request) {
    
            
        HttpHeaders responseHeaders = new HttpHeaders();
            
        String token = request.getHeader("token");

        // 유효하지 않은 토큰인 경우 203 에러 
        if(adminLoginService.chkToken(request.getHeader("token")) == 203){
            return ResponseEntity.status(203)
                .headers(responseHeaders)
                .body(getStatusMessage(203));
        }
            

        // 데일리캡 도달 여부를 false로 수정
        adsMediaRepository.updateTodayLimit(false, adsKey, mediaKey);

        
        // 데일리 캡을 추가 부여 
        adsMediaRepository.addDailyCap(value, adsKey, mediaKey);


        Integer amId = adsMediaRepository.selectIdByAdsKeyAndMediaKey(adsKey, mediaKey);


        // 메모리 데이터 업데이트
        memoryDataService.updateMemoryData("ads-media", amId);

        String adminId = adminLoginRepository.findAdminIdByToken(token);


        // 데일리 캡 추가 확보 이력 기록
        logAdsService.insert(adsKey, "", adminId, "add-daily-cap", "", "", value.toString());


        return ResponseEntity.status(200)
            .headers(responseHeaders)
            .body(getStatusMessage(200));
          
    }
 

}