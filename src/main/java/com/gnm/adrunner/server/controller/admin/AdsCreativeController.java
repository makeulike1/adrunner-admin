package com.gnm.adrunner.server.controller.admin;


import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.gnm.adrunner.server.RequestResponseInterface;
import com.gnm.adrunner.server.entity.AdsCreative;
import com.gnm.adrunner.server.object.AdsCreativeFileList;
import com.gnm.adrunner.server.param.res.admin.ResponseCreativeList;
import com.gnm.adrunner.server.repo.AdminLoginRepository;
import com.gnm.adrunner.server.repo.AdsCreativeRepository;
import com.gnm.adrunner.server.repo.AdsMediaRepository;
import com.gnm.adrunner.server.repo.AdsRepository;
import com.gnm.adrunner.server.repo.LogAdsRepository;
import com.gnm.adrunner.server.repo.MediaRepository;
import com.gnm.adrunner.server.repo.ViewAdsMediaRepository;
import com.gnm.adrunner.server.service.AdminLoginService;
import com.gnm.adrunner.server.service.AdsCreativeService;
import com.gnm.adrunner.server.service.AdsService;
import com.gnm.adrunner.server.service.FileService;
import com.gnm.adrunner.server.service.LogAdsService;
import com.gnm.adrunner.server.service.MemoryDataService;
import com.gnm.adrunner.server.service.PostbackService;
import com.gnm.adrunner.server.service.RedisService;
import com.gnm.adrunner.server.service.SystemConfig3Service;
import com.gnm.adrunner.server.service.AdsMediaService;
import com.gnm.adrunner.util.storageObject;
import com.gnm.adrunner.util.timeBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller 
public class AdsCreativeController extends RequestResponseInterface{




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
    PostbackService postbackService;

    @Autowired
    RedisService    redisService;

    @Autowired
    SystemConfig3Service systemConfig3Service;

    @Autowired
    AdsCreativeRepository adsCreativeRepository;

    @Autowired
    AdsCreativeService adsCreativeService;
 
    @Autowired
    FileService fileService;
 

    // 이미지, 소재 등록
    @CrossOrigin(origins = "*")
    @GetMapping("/ads/creative/list") 
    public @ResponseBody ResponseEntity<String> creativeList(
        @RequestParam(value="ads_key",    required=true) String adsKey,
        HttpServletRequest request){

        HttpHeaders responseHeaders = new HttpHeaders();

        String token = request.getHeader("token");

         // 유효하지 않은 토큰인 경우 203 에러 
         if(adminLoginService.chkToken(token) == 203){
            return ResponseEntity.status(203)
                .headers(responseHeaders)
                .body(getStatusMessage(203));
        }

        AdsCreative ac = adsCreativeRepository.findByAdsKey(adsKey);

        ResponseCreativeList rcl = new ResponseCreativeList();

        if(ac != null){
            String S3_FILE_URL = storageObject.endPoint + "/" + storageObject.bucketName + "/" + ac.getCreatetime() + "-" + ac.getAdsKey() +"/";
            rcl.setFileURL1(S3_FILE_URL+"f1."+ac.getExt1());
            rcl.setFileURL2(S3_FILE_URL+"f2."+ac.getExt2());
            rcl.setFileURL3(S3_FILE_URL+"f3."+ac.getExt3());
            rcl.setFileURL4(S3_FILE_URL+"f4."+ac.getExt4());
            rcl.setFileURL5(S3_FILE_URL+"f5."+ac.getExt5());
            rcl.setFileURL6(S3_FILE_URL+"f6."+ac.getExt6());
            rcl.setFileURL7(S3_FILE_URL+"f7."+ac.getExt7());
            rcl.setFileURL8(S3_FILE_URL+"f8."+ac.getExt8());
            rcl.setFileURL9(S3_FILE_URL+"f9."+ac.getExt9());
            rcl.setFileURL10(S3_FILE_URL+"f10."+ac.getExt10());
            rcl.setFileURL11(S3_FILE_URL+"f11."+ac.getExt11()); 
            rcl.setFileURL12(S3_FILE_URL+"f12."+ac.getExt12());
            S3_FILE_URL = null;
        }

        return ResponseEntity.status(200)
            .headers(responseHeaders)
            .body(gson.toJson(rcl));    
    }



    // 이미지, 소재 등록
    @CrossOrigin(origins = "*")
    @PostMapping("/ads/creative/update") 
    public @ResponseBody ResponseEntity<String> uploadCreative(
        @RequestParam(value="ads_key",    required=false) String adsKey,
        @RequestParam(value="file",    required=false) MultipartFile file,
        @RequestParam(value="file_index",    required=false) Integer fileIndex,
        HttpServletRequest request) throws IOException {

            String token = request.getHeader("token");

            HttpHeaders responseHeaders = new HttpHeaders();

            // 유효하지 않은 토큰인 경우 203 에러 
            if(adminLoginService.chkToken(token) == 203){
                return ResponseEntity.status(203)
                    .headers(responseHeaders)
                    .body(getStatusMessage(203));
            } 

            AdsCreative ac      = adsCreativeRepository.findByAdsKey(adsKey);
            
            String createtime   = ac.getCreatetime();

            String extension    = fileService.uploadFile(createtime+"-"+adsKey, file, "f"+fileIndex);

            adsCreativeService.update(fileIndex, extension, adsKey);
 

            return ResponseEntity.status(200)
                .headers(responseHeaders)
                .body(getStatusMessage(200));    
    }   




    // 이미지, 소재 등록
    @CrossOrigin(origins = "*")
    @PostMapping("/ads/creative/save") 
    public @ResponseBody ResponseEntity<String> uploadCreative(
        @RequestParam(value="ads_key",    required=false) String adsKey,
        @RequestParam(value="file1",    required=false) MultipartFile file1,
        @RequestParam(value="file2",    required=false) MultipartFile file2,
        @RequestParam(value="file3",    required=false) MultipartFile file3,
        @RequestParam(value="file4",    required=false) MultipartFile file4,
        @RequestParam(value="file5",    required=false) MultipartFile file5,
        @RequestParam(value="file6",    required=false) MultipartFile file6,
        @RequestParam(value="file7",    required=false) MultipartFile file7,
        @RequestParam(value="file8",    required=false) MultipartFile file8,
        @RequestParam(value="file9",    required=false) MultipartFile file9,
        @RequestParam(value="file10",   required=false) MultipartFile file10, 
        @RequestParam(value="file11",   required=false) MultipartFile file11, 
        @RequestParam(value="file12",   required=false) MultipartFile file12, 
        HttpServletRequest request) throws IOException {


        HttpHeaders responseHeaders = new HttpHeaders();

        String token = request.getHeader("token");


        // 유효하지 않은 토큰인 경우 203 에러 
        if(adminLoginService.chkToken(token) == 203){
            return ResponseEntity.status(203)
                .headers(responseHeaders)
                .body(getStatusMessage(203));
        } 

        AdsCreativeFileList acf = new AdsCreativeFileList();
        
        String createtime = timeBuilder.getCurrentTime2();

        fileService.makeFolder(adsKey);
        acf.setAds_key(adsKey);

        String ext1     = "";
        String ext2     = "";
        String ext3     = "";
        String ext4     = "";
        String ext5     = "";
        String ext6     = "";
        String ext7     = "";
        String ext8     = "";
        String ext9     = "";
        String ext10    = "";
        String ext11    = "";
        String ext12    = "";


        if(file1 != null)
            ext1 = fileService.uploadFile(createtime+"-"+adsKey, file1, "f1");
        if(file2 != null)
            ext2 = fileService.uploadFile(createtime+"-"+adsKey, file2, "f2");
        if(file3 != null)
            ext3 = fileService.uploadFile(createtime+"-"+adsKey, file3, "f3");
        if(file4 != null)
            ext4 = fileService.uploadFile(createtime+"-"+adsKey, file4, "f4");   
        if(file5 != null)
            ext5 = fileService.uploadFile(createtime+"-"+adsKey, file5, "f5");
        if(file6 != null)
            ext6 = fileService.uploadFile(createtime+"-"+adsKey, file6, "f6");   
        if(file7 != null)
            ext7 = fileService.uploadFile(createtime+"-"+adsKey, file7, "f7");
        if(file8 != null)
            ext8 = fileService.uploadFile(createtime+"-"+adsKey, file8, "f8");
        if(file9 != null)
            ext9 = fileService.uploadFile(createtime+"-"+adsKey, file9, "f9");
        if(file10 != null)
            ext10 = fileService.uploadFile(createtime+"-"+adsKey, file10, "f10");
        if(file11 != null)
            ext11 = fileService.uploadFile(createtime+"-"+adsKey, file11, "f11");
        if(file12 != null)
            ext12 = fileService.uploadFile(createtime+"-"+adsKey, file12, "f12");


        AdsCreative ac = new AdsCreative();
        ac.setExt1(ext1);
        ac.setExt2(ext2);
        ac.setExt3(ext3);
        ac.setExt4(ext4);
        ac.setExt5(ext5);
        ac.setExt6(ext6);
        ac.setExt7(ext7);
        ac.setExt8(ext8);
        ac.setExt9(ext9);
        ac.setExt10(ext10);
        ac.setExt11(ext11);
        ac.setExt12(ext12);
        ac.setCreatetime(createtime);
        ac.setAdsKey(adsKey);
        adsCreativeRepository.save(ac);
        

        return ResponseEntity.status(200)
                .headers(responseHeaders)
                .body(getStatusMessage(200));    
    }



}