package com.gnm.adrunner.server.controller.admin;
 
import com.gnm.adrunner.server.RequestResponseInterface;
import com.gnm.adrunner.server.entity.Media;
import com.gnm.adrunner.server.entity.MediaParam;
import com.gnm.adrunner.server.param.req.admin.RequestModifyMediaUrl;
import com.gnm.adrunner.server.param.req.admin.RequestSaveMedia;
import com.gnm.adrunner.server.param.req.admin.RequestSaveMediaParam;

import org.springframework.stereotype.Controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.gnm.adrunner.server.repo.AdsMediaRepository;
import com.gnm.adrunner.server.repo.MediaParamRepository;
import com.gnm.adrunner.server.repo.MediaRepository;
import com.gnm.adrunner.server.service.AdminLoginService;
import com.gnm.adrunner.server.service.MediaService;
import com.gnm.adrunner.server.service.MemoryDataService;
import com.gnm.adrunner.util.keyBuilder;
import com.gnm.adrunner.util.timeBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller 
@RequestMapping(path="/media")
public class MediaController extends RequestResponseInterface{
    
    @Autowired
    AdminLoginService adminLoginService; 


    @Autowired
    AdsMediaRepository adsMediaRepository;
    
    @Autowired
    MediaRepository mediaRepository;

    @Autowired
    MediaService mediaService;

    @Autowired
    MemoryDataService memoryDataService;
 

    @Autowired
    MediaParamRepository mediaParamRepository;

    // 매체사 목록  
    @CrossOrigin(origins = "*")
    @GetMapping("/list")
    public @ResponseBody ResponseEntity<String> list(
        @RequestParam(value="ads_key", required=false)String adsKey, HttpServletRequest request) {
        
        
        HttpHeaders responseHeaders = new HttpHeaders();
        
        // 유효하지 않은 토큰인 경우 203 에러 
        if(adminLoginService.chkToken(request.getHeader("token")) == 203){
            return ResponseEntity.status(203)
                .headers(responseHeaders)
                .body(gson.toJson(getStatusMessage(203)));
        }

        
        List<Media> mediaList = mediaRepository.listAll();

        if(adsKey != null){            
            
            String mediaKey[] = adsMediaRepository.mediaKeyListByAdsKey(adsKey);
            
            // 특정 광고키에 대해서 연동된 매체사는 제외하여 내려줌
            for(String e1 : mediaKey){

                for(Media e : mediaList){
                    if(e.getMediaKey().equals(e1)){
                        mediaList.remove(e);
                        break;
                    }
                } 
            }

        }
        
        return ResponseEntity.status(200)
                .headers(responseHeaders)
                .body(gson.toJson(mediaList));
    }




    // 매체사 등록
    @CrossOrigin(origins = "*")
    @PostMapping("/save") 
    public @ResponseBody ResponseEntity<String> add(
        @RequestBody RequestSaveMedia req, HttpServletRequest request) {
         
        HttpHeaders responseHeaders = new HttpHeaders();
    
    
        // 유효하지 않은 토큰인 경우 203 에러 
        if(adminLoginService.chkToken(request.getHeader("token")) == 203){
            return ResponseEntity.status(203)
                .headers(responseHeaders)
                .body(getStatusMessage(203));
        } 

        
        Media m = new Media();
        m.setName(req.getName()); 
        m.setMediaKey(""); 
        m.setIsDelete(false);
        m.setIsPostback(false);
        m.setPostbackEvent("-");
        m.setPostbackInstall("-");
        m.setCreatetime(timeBuilder.getCurrentTime());

        
        Integer mediaId = mediaService.saveMedia(m);

        // 매체사 키는 [매체사번호][5자리의 대문자 난수]
        String mediaKey = keyBuilder.buildIdentifier(mediaId);

        mediaService.updateMediaKey(mediaKey.toUpperCase(), mediaId);

        // 메모리 데이터 업데이트
        memoryDataService.addMemoryData("media", mediaId);
 
        return ResponseEntity.status(200)
                .headers(responseHeaders)
                .body(mediaKey);
    }



    
    // 매체사 삭제
    @CrossOrigin(origins = "*")
    @DeleteMapping("/delete/{mediaid}") 
    public @ResponseBody ResponseEntity<String> delete(
        @PathVariable Integer mediaid, HttpServletRequest request) {
         
        HttpHeaders responseHeaders = new HttpHeaders();
    
    
        // 유효하지 않은 토큰인 경우 203 에러 
        if(adminLoginService.chkToken(request.getHeader("token")) == 203){
            return ResponseEntity.status(203)
                .headers(responseHeaders)
                .body(getStatusMessage(203));
        } 
 
    
        return ResponseEntity.status(200)
                .headers(responseHeaders)
                .body(getStatusMessage(200));
    }



    // 특정 매체사 정보 조회
    @CrossOrigin(origins = "*")
    @GetMapping("/find/{mediaid}")
    public @ResponseBody ResponseEntity<String> findMedia(
        @PathVariable Integer mediaid, HttpServletRequest request){

        
        HttpHeaders responseHeaders = new HttpHeaders();
                // 유효하지 않은 토큰인 경우 203 에러 
        if(adminLoginService.chkToken(request.getHeader("token")) == 203){
            return ResponseEntity.status(203)
                .headers(responseHeaders)
                .body(getStatusMessage(203));
        }
 
        
        Media m = mediaRepository.findByID(mediaid);

        return ResponseEntity.status(200)
            .headers(responseHeaders)
            .body(gson.toJson(m));

    }


    


    // 매체사 포스트백 송수신 상태 수정
    @CrossOrigin(origins = "*")
    @PutMapping("/update/postback-state/{mediaid}/{state}")
    public @ResponseBody ResponseEntity<String> updatePostback(
        @PathVariable Integer mediaid, @PathVariable Boolean state, HttpServletRequest request){

        
        HttpHeaders responseHeaders = new HttpHeaders();
                // 유효하지 않은 토큰인 경우 203 에러 
        if(adminLoginService.chkToken(request.getHeader("token")) == 203){
            return ResponseEntity.status(203)
                .headers(responseHeaders)
                .body(getStatusMessage(203));
        }

        mediaRepository.updatePostbackState(mediaid, state);
        
        // 메모리 데이터 업데이트
        memoryDataService.addMemoryData("media", mediaid);


        return ResponseEntity.status(200)
            .headers(responseHeaders)
            .body(getStatusMessage(200));

    }



    // 매체사 인스톨 포스트백 URL 수정
    @CrossOrigin(origins = "*")
    @PutMapping("/update/postback-install/{mediaid}")
    public @ResponseBody ResponseEntity<String> updatePostbackInstallURL(
        @RequestBody RequestModifyMediaUrl req, 
        @PathVariable Integer mediaid, HttpServletRequest request){

        
        HttpHeaders responseHeaders = new HttpHeaders();

                // 유효하지 않은 토큰인 경우 203 에러 
        if(adminLoginService.chkToken(request.getHeader("token")) == 203){
            return ResponseEntity.status(203)
                .headers(responseHeaders)
                .body(getStatusMessage(203));
        }

        mediaRepository.updatePostbackInstallURL(mediaid, req.getUrl());

        // 메모리 데이터 업데이트
        memoryDataService.addMemoryData("media", mediaid);
        
        return ResponseEntity.status(200)
            .headers(responseHeaders)
            .body(getStatusMessage(200));

    }




    // 매체사 이벤트 포스트백 URL 수정
    @CrossOrigin(origins = "*")
    @PutMapping("/update/postback-event/{mediaid}")
    public @ResponseBody ResponseEntity<String> updatePostbackEventURL(
        @RequestBody RequestModifyMediaUrl req, 
        @PathVariable Integer mediaid, HttpServletRequest request){

        
        HttpHeaders responseHeaders = new HttpHeaders();
                // 유효하지 않은 토큰인 경우 203 에러 
        if(adminLoginService.chkToken(request.getHeader("token")) == 203){
            return ResponseEntity.status(203)
                .headers(responseHeaders)
                .body(getStatusMessage(203));
        }

        mediaRepository.updatePostbackEventURL(mediaid, req.getUrl());

        // 메모리 데이터 업데이트
        memoryDataService.addMemoryData("media", mediaid);
        
        return ResponseEntity.status(200)
            .headers(responseHeaders)
            .body(getStatusMessage(200));

    }



    // 특정 매체사에 대해서 파라미터 조회
    @CrossOrigin(origins = "*")
    @GetMapping("/param/find/{mediaKey}/{type}")
    public @ResponseBody ResponseEntity<String> findMediaParam(
        @PathVariable String mediaKey,
        @PathVariable Integer type, HttpServletRequest request){

        
        HttpHeaders responseHeaders = new HttpHeaders();
                // 유효하지 않은 토큰인 경우 203 에러 
        if(adminLoginService.chkToken(request.getHeader("token")) == 203){
            return ResponseEntity.status(203)
                .headers(responseHeaders)
                .body(getStatusMessage(203));
        }
 
        
        return ResponseEntity.status(200)
            .headers(responseHeaders)
            .body(gson.toJson(mediaParamRepository.findByMediaKeyAndType(mediaKey, type)));

    }



    
    // 매체사 파라미터 등록
    @CrossOrigin(origins = "*")
    @PostMapping("/param/save")
    public @ResponseBody ResponseEntity<String> saveMediaParam(
        @RequestBody RequestSaveMediaParam req, 
        HttpServletRequest request){

        
        HttpHeaders responseHeaders = new HttpHeaders();


                // 유효하지 않은 토큰인 경우 203 에러 
        if(adminLoginService.chkToken(request.getHeader("token")) == 203){
            return ResponseEntity.status(203)
                .headers(responseHeaders)
                .body(getStatusMessage(203));
        }


        MediaParam mp = new MediaParam();
        mp.setMediaKey(req.getMediaKey());
        mp.setParamKey(req.getParamKey());
        mp.setParamValue(req.getParamValue());
        mp.setCreatetime(timeBuilder.getCurrentTime());
        mp.setType(req.getType());

        mediaParamRepository.save(mp);
        
        return ResponseEntity.status(200)
            .headers(responseHeaders)
            .body(getStatusMessage(200));

    }

}
