package com.gnm.adrunner.server.controller.admin;

import java.text.ParseException;
 

import javax.servlet.http.HttpServletRequest;

import com.gnm.adrunner.server.RequestResponseInterface;
 
import com.gnm.adrunner.server.repo.AdsRepository;
import com.gnm.adrunner.server.repo.AdminRepository;
import com.gnm.adrunner.server.repo.AffParamRepository;
import com.gnm.adrunner.server.repo.AffRepository;
import com.gnm.adrunner.server.repo.MediaRepository;
import com.gnm.adrunner.server.repo.ServerInstanceRepository;
import com.gnm.adrunner.server.service.AdminLoginService;
import com.gnm.adrunner.server.service.AdrunnerAdminService;
import com.gnm.adrunner.server.service.SchedulerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
 
@Controller 
@RequestMapping(path="/adrunner")
public class AdrunnerController extends RequestResponseInterface{


    @Autowired
    AdminLoginService               adminLoginService; 

    @Autowired
    SchedulerService                schedulerService;


    @Autowired
    AffParamRepository              affParamRepository;

    
    @Autowired
    AdsRepository                   adsRepository;


    @Autowired
    AffRepository                   affRepository;

    
    @Autowired
    MediaRepository                 mediaRepository;


    @Autowired
    ServerInstanceRepository        serverInstanceRepository;


    @Autowired
    AdminRepository                 adminRepository;

    @Autowired
    AdrunnerAdminService            adrunnerAdminService;

 

    /////////////////// 관리자 전용
    // 일일 리포트 신규 삽입
    @CrossOrigin(origins = "*")
    @GetMapping("/admin/0") 
    public @ResponseBody ResponseEntity<String>  adminAPI0(HttpServletRequest request) throws ParseException {
        
        HttpHeaders responseHeaders = new HttpHeaders();

        String token = request.getHeader("token");


        // 유효하지 않은 토큰인 경우 203 에러 
        if(adminLoginService.chkToken(token) == 203){
            return ResponseEntity.status(203)
                .headers(responseHeaders)
                .body(getStatusMessage(203));
        } 

        
        schedulerService.updateRptDay(true);
        
        return ResponseEntity.status(200)
                .headers(responseHeaders)
                .body(getStatusMessage(200));
    }





    // 주간 리포트 신규 삽입
    @CrossOrigin(origins = "*")
    @GetMapping("/admin/1") 
    public @ResponseBody ResponseEntity<String>  adminAPI1(HttpServletRequest request) throws ParseException {
        
        HttpHeaders responseHeaders = new HttpHeaders();

        String token = request.getHeader("token");


        // 유효하지 않은 토큰인 경우 203 에러 
        if(adminLoginService.chkToken(token) == 203){
            return ResponseEntity.status(203)
                .headers(responseHeaders)
                .body(getStatusMessage(203));
        } 

        
        schedulerService.insertWeeklyReport();
        
        return ResponseEntity.status(200)
                .headers(responseHeaders)
                .body(getStatusMessage(200));
    }




    // 모든 광고와 광고에 대한 리포트, 포스트백 삭제 (테스트 서버에서만 활용)
    @CrossOrigin(origins = "*")
    @GetMapping("/admin/2") 
    public @ResponseBody ResponseEntity<String>  adminAPI2(HttpServletRequest request) throws ParseException {
        
        HttpHeaders responseHeaders = new HttpHeaders();

        String token = request.getHeader("token");


        // 유효하지 않은 토큰인 경우 203 에러 
        if(adminLoginService.chkToken(token) == 203){
            return ResponseEntity.status(203)
                .headers(responseHeaders)
                .body(getStatusMessage(203));
        } 
        

        adrunnerAdminService.removeAllAds();
        
        
        return ResponseEntity.status(200)
                .headers(responseHeaders)
                .body(getStatusMessage(200));
    }
    

    
}

