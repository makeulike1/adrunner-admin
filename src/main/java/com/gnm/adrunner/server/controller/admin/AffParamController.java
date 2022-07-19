package com.gnm.adrunner.server.controller.admin;

import javax.servlet.http.HttpServletRequest;

import com.gnm.adrunner.server.RequestResponseInterface;
import com.gnm.adrunner.server.entity.AffParam;
import com.gnm.adrunner.server.param.req.admin.RequestSaveAffParam;
import com.gnm.adrunner.server.repo.AffRepository;
import com.gnm.adrunner.server.service.AdminLoginService;
import com.gnm.adrunner.server.service.AffService;
import com.gnm.adrunner.server.service.MemoryDataService;
import com.gnm.adrunner.server.service.AffParamService;
import com.gnm.adrunner.server.repo.AdsRepository;
import com.gnm.adrunner.server.repo.AffParamRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

public class AffParamController extends RequestResponseInterface{
 
 
    @Autowired
    AdminLoginService adminLoginService;


    @Autowired
    AffService affService;


    @Autowired
    AffParamService affParamService;


    @Autowired
    AdsRepository adsRepository;


    @Autowired
    AffRepository affRepository;


    @Autowired
    AffParamRepository affParamRepository;

    @Autowired
    MemoryDataService memoryDataService;
  
 
 


     // 특정 제휴사 식별자에 대한 파라미터 목록
     @CrossOrigin(origins = "*")
     @GetMapping("/aff/param/list/{affid}") 
     public @ResponseBody ResponseEntity<String> paramList(@PathVariable Integer affid, HttpServletRequest request) {
     
  
        HttpHeaders responseHeaders = new HttpHeaders();
 
         // 유효하지 않은 토큰인 경우 203 에러 
         if(adminLoginService.chkToken(request.getHeader("token")) == 203){
            return ResponseEntity.status(203)
                .headers(responseHeaders)
                .body(getStatusMessage(203));
         }

         return ResponseEntity.status(200)
                .headers(responseHeaders)
                .body(gson.toJson(affParamService.findByAffId(affid)));
     }



     // 특정 제휴사 파라미터 등록
     @CrossOrigin(origins = "*")
     @PostMapping("/aff/param/save/{affid}") 
     public @ResponseBody ResponseEntity<String> paramSave(@PathVariable Integer affid, @RequestBody RequestSaveAffParam req, HttpServletRequest request) {
     
  
        HttpHeaders responseHeaders = new HttpHeaders();
 
         // 유효하지 않은 토큰인 경우 203 에러 
         if(adminLoginService.chkToken(request.getHeader("token")) == 203){
            return ResponseEntity.status(203)
                .headers(responseHeaders)
                .body(getStatusMessage(203));
         }

         AffParam ap = new AffParam();
         ap.setParamKey(req.getParamKey());
         ap.setParamValue(req.getParamValue());
         ap.setPassValue(req.getPassValue());
         ap.setParamType(req.getParamType());
         ap.setAffId(affid);
         affParamService.saveAffParam(ap);

         // 메모리 데이터 업데이트
         memoryDataService.addMemoryData("aff-param", ap.getId());
    
         return ResponseEntity.status(200)
                .headers(responseHeaders)
                .body(getStatusMessage(200));
     }




     // 특정 제휴사 파라미터 삭제
     @CrossOrigin(origins = "*")
     @DeleteMapping("/aff/param/delete/{id}") 
     public @ResponseBody ResponseEntity<String> paramDelete(@PathVariable Integer id, HttpServletRequest request) {
     
  
        HttpHeaders responseHeaders = new HttpHeaders();
 
         // 유효하지 않은 토큰인 경우 203 에러 
         if(adminLoginService.chkToken(request.getHeader("token")) == 203){
            return ResponseEntity.status(203)
                .headers(responseHeaders)
                .body(getStatusMessage(203));
         }

         affParamService.deleteById(id);
 
         // 메모리 데이터 업데이트
         memoryDataService.deleteMemoryData("aff-param", id);

         return ResponseEntity.status(200)
                .headers(responseHeaders)
                .body(getStatusMessage(200));
     }
      
 
}