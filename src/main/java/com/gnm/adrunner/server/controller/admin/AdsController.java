package com.gnm.adrunner.server.controller.admin;


import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.gnm.adrunner.config.GlobalConstant;
import com.gnm.adrunner.server.RequestResponseInterface;
import com.gnm.adrunner.server.entity.Ads;
import com.gnm.adrunner.server.entity.AdsMedia;
import com.gnm.adrunner.server.param.req.admin.RequestSaveAds;
import com.gnm.adrunner.server.param.req.admin.RequestSaveAds1;
import com.gnm.adrunner.server.param.req.admin.RequestSaveAdsMedia;
import com.gnm.adrunner.server.param.res.admin.ResponseListAds;
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
import com.gnm.adrunner.server.service.AdsMediaService;
import com.gnm.adrunner.util.keyBuilder;
import com.gnm.adrunner.util.redisUtil;
import com.gnm.adrunner.util.scriptBuilder;
import com.gnm.adrunner.util.timeBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
@RequestMapping(path="/ads")
public class AdsController extends RequestResponseInterface{




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

 

    // ?????? ?????? ??????
    @CrossOrigin(origins = "*")
    @PutMapping("/update/{adid}/{status}")
    public @ResponseBody ResponseEntity<String> updateStatus(
        @PathVariable Integer adid, @PathVariable Integer status, HttpServletRequest request){
        

        HttpHeaders responseHeaders = new HttpHeaders();

        String token = request.getHeader("token");

         // ???????????? ?????? ????????? ?????? 203 ?????? 
         if(adminLoginService.chkToken(token) == 203){
            return ResponseEntity.status(203)
                .headers(responseHeaders)
                .body(getStatusMessage(203));
        }


        
        Ads ads = adsService.findById(adid);
        
        String adminId = adminLoginRepository.findAdminIdByToken(token);



        // ?????? ?????? ?????? ??????
        logAdsService.insert(ads.getAdsKey(),   request.getRemoteAddr(),   adminId.toString(),    "status",     ads.getStatus().toString(), status.toString());



        // ?????? ????????? ?????? ?????? ???????????? 
        adsService.updateAdsByStatus(adid, status);


        // ????????? ????????? ????????????
        memoryDataService.updateMemoryData("ads", adid);

        
        return ResponseEntity.status(200)
                .headers(responseHeaders)
                .body(getStatusMessage(200));    
    }

    // ?????? ??????
    @CrossOrigin(origins = "*")
    @PostMapping("/save") 
    public @ResponseBody ResponseEntity<String> add(
        @RequestBody RequestSaveAds req, HttpServletRequest request) {
    

        Integer redisIndex = systemConfigRepository.findRedisIndex();

        HttpHeaders responseHeaders = new HttpHeaders();


        // ????????? ?????? 16????????? ?????? ?????? ??????
        if(redisIndex.equals(17)){
            return ResponseEntity.status(217)
                .headers(responseHeaders)
                .body(getStatusMessage(217));
        }



        String token = request.getHeader("token");


        // ???????????? ?????? ????????? ?????? 203 ?????? 
        if(adminLoginService.chkToken(token) == 203){
            return ResponseEntity.status(203)
                .headers(responseHeaders)
                .body(getStatusMessage(203));
        }



        Ads Ad  = new Ads();

        /*
         *      AD_AFF              : ????????? ?????????
         *      AD_NAME             : ?????? ??????
         *      AD_TYPE             : ?????? ??????
         *      AD_OS               : 0: ???????????????, 1: iOS, 2: ???
         *      AD_SUPPLY_DMENAD    : 0 : ????????????, 1 : ?????????
         *      AD_STARTDATE        : ?????? ????????????
         *      AD_ENDDATE          : ?????? ????????????
         *      AD_DESCRIPTION      : ?????? ??????
         *      AD_COST1            : ????????? ??????
         *      AD_COST2            : ???????????? ??????
         *      AD_COST3            : ????????? ??????
         *      AD_TARGET_IMP       : ??? ?????? ??????
         *      AD_IS_DAILYCAP      : ???????????? ?????? (Y/N)
         *      AD_TARGETIMP        : ??? ?????? ??????
         *      AD_TRACKING_URL     : ????????? URL
         *      AD_DAYLIMIT         : 1??? ?????? ??????
         *      AD_EVENT_NAME       : ????????? ??????
         *      AD_MEDIA            : ????????? 
         *      AD_AUTOSTART        : ?????? ?????? ?????? (Y/N)
         *      AD_AUTODOWN         : ?????? ?????? ?????? (Y/N)
         *      AD_LOOPBACK         : ?????? loopback ??????
         */

        String      AD_NAME                                 = req.getName();
        Integer     AD_AFF                                  = req.getAff();
        Integer     AD_TYPE                                 = req.getType();            
        String      AD_STARTDATE                            = req.getStartdate();       
        Boolean     AD_SUPPLY_DEMAND                        = req.getSupplyDemand();
        String      AD_ENDDATE                              = req.getEnddate();
        String      AD_DESCRIPTION                          = req.getDescription();
        Boolean     AD_IS_DAILYCAP                          = req.getIsDailyCap();
        Integer     AD_TARGET_IMP                           = req.getTargetImp();
        String      AD_TRACKING_URL                         = req.getTrackingUrl();
        Integer     AD_OS                                   = req.getOs();
        Boolean     AD_AUTOSTART                            = req.getAutostart();
        Boolean     AD_AUTODOWN                             = req.getAutodown();
        Integer     AD_COST1                                = req.getCost1();
        Integer     AD_COST2                                = req.getCost2();
        String      AD_EVENT_NAME                           = req.getEventName();
        String      AD_LOOPBACK                             = req.getLoopback();
        List<RequestSaveAds1>    AD_MEDIA                   = req.getMedia();
        
        String ADS_KEY = keyBuilder.buildUUID().substring(0, 12).toUpperCase();
    


        


        if(AD_EVENT_NAME == null)
            AD_EVENT_NAME = "";
        
        Ad.setSupplyDemand(AD_SUPPLY_DEMAND);
        Ad.setName(AD_NAME);
        Ad.setType(AD_TYPE);
        Ad.setStartdate(AD_STARTDATE);
        Ad.setEnddate(AD_ENDDATE);
        Ad.setDescription(AD_DESCRIPTION);
        Ad.setIsDailyCap(AD_IS_DAILYCAP);
        Ad.setTargetImp(AD_TARGET_IMP);
        Ad.setStatus(GlobalConstant.ADS_STATUS_PAUSE);
        Ad.setIsDelete(false);
        Ad.setCreatetime(timeBuilder.getCurrentTime());
        Ad.setUpdatetime(timeBuilder.getCurrentTime());
        Ad.setDeletetime("1111-11-11 11:11:11");
        Ad.setTrackingUrl(AD_TRACKING_URL);
        Ad.setAff(AD_AFF);
        Ad.setOs(AD_OS); 
        Ad.setAdsKey(ADS_KEY);
        Ad.setEventName(AD_EVENT_NAME);
        Ad.setCost1(AD_COST1);
        Ad.setCost2(AD_COST2);
        Ad.setAutostart(AD_AUTOSTART);
        Ad.setAutodown(AD_AUTODOWN);
        Ad.setLoopbackdate(AD_LOOPBACK);
        Ad.setRedisIndex(redisIndex);
        Ad.setAdsKey(ADS_KEY);
        Ad.setAdvKey(req.getAdvKey());
                
 
        
        Integer adid = adsService.saveAds(Ad);

        systemConfigRepository.updateRedisIndex((redisIndex + 1));

 
        // ????????? ????????? ????????????
        memoryDataService.addMemoryData("ads", adid);

        // ?????? ????????? ?????? ???????????? ??????
        for(RequestSaveAds1 e : AD_MEDIA){
            AdsMedia am = new AdsMedia();
            am.setAdsKey(ADS_KEY);
            String mediaKey = mediaRepository.getKeyByName(e.getName());
            am.setMediaKey(mediaKey);
            am.setCreatetime(timeBuilder.getCurrentTime());
            am.setMediaCost(e.getCost());
            am.setMediaDailyCap(e.getDailycap());
            am.setIsDayLimit(false);
            Integer amId = adsMediaService.saveAdsMedia(am);
            // ????????? ????????? ??????
            memoryDataService.addMemoryData("ads-media", amId);
        }


 



        // ?????? ???????????? ?????? ????????? ??????
        String adminId = adminLoginRepository.findAdminIdByToken(token);




        // ?????? ?????? ?????? ??????
        logAdsService.insert(ADS_KEY, "", adminId, "new", "", "");
        

        


        return ResponseEntity.status(200)
                .headers(responseHeaders)
                .body(getStatusMessage(200));
    }



    
    

    // ?????? ??????  
    @CrossOrigin(origins = "*")
    @GetMapping("/list")
    public @ResponseBody ResponseListAds list(
        @RequestParam(value="page", required=false, defaultValue="0") int page, 
        @RequestParam(value="pagesize", required=false, defaultValue="7") int pagesize,
        @RequestParam(value="name", required=false)String name,
        @RequestParam(value="status",   required=true)Integer status,

        HttpServletRequest request) throws ParseException {

            
        // ???????????? ?????? ????????? ?????? 203 ?????? 
        if(adminLoginService.chkToken(request.getHeader("token")) == 203){
            return new ResponseListAds(203);
        }
     

        Integer totalAdsCount = adsService.countTotalAds(name, status);

        Integer liveCount = adsService.countTotalLiveAds();
        return new ResponseListAds(
                200, 
                adsService.listAds(
                    pagesize,
                    (page * pagesize), 
                    name,
                    status),
                    totalAdsCount,
                    liveCount
                );
    }


    // ????????? ????????? ????????? ??????
    @CrossOrigin(origins = "*")
    @PostMapping("/media/save") 
    public @ResponseBody ResponseEntity<String> save_media(
        @RequestBody RequestSaveAdsMedia req, HttpServletRequest request){

        HttpHeaders responseHeaders = new HttpHeaders();
        

        String token = request.getHeader("token");


        // ???????????? ?????? ????????? ?????? 203 ?????? 
        if(adminLoginService.chkToken(token) == 203){
            return ResponseEntity.status(203)
                .headers(responseHeaders)
                .body(getStatusMessage(203));
        }


        String adsKey       = req.getAdsKey();

        String mediaName    = req.getMediaName();

        String mediaKey     =   mediaRepository.getKeyByName(mediaName);



        // ????????? ????????? ????????? ?????? ??????
        AdsMedia adsMedia = new AdsMedia();
        adsMedia.setAdsKey(adsKey);
        adsMedia.setMediaKey(mediaKey);
        adsMedia.setMediaCost(0);
        adsMedia.setMediaDailyCap(0);
        adsMedia.setIsDayLimit(false);
        adsMedia.setCreatetime(timeBuilder.getCurrentTime());


        Integer amId = adsMediaService.saveAdsMedia(adsMedia);

        // ????????? ????????? ??????
        memoryDataService.addMemoryData("ads-media", amId);


        // ????????? ?????? ????????? ????????? ?????? ????????? ??????
        String adminId = adminLoginRepository.findAdminIdByToken(token);

        
        // ?????? ?????? ?????? ??????
        logAdsService.insert(adsKey, "", adminId, "media-new", "", mediaName);


        return ResponseEntity.status(200)
            .headers(responseHeaders)
            .body(getStatusMessage(200));

    }


    // ????????? ????????? ????????? ??????
    @CrossOrigin(origins = "*")
    @DeleteMapping("/media/delete") 
    public @ResponseBody ResponseEntity<String> delete_media(
        @RequestParam(value="adsKey", required=true) String adsKey, 
        @RequestParam(value="mediaName", required=true) String mediaName, HttpServletRequest request){

        HttpHeaders responseHeaders = new HttpHeaders();
        

        
        String token = request.getHeader("token");


        // ???????????? ?????? ????????? ?????? 203 ?????? 
        if(adminLoginService.chkToken(token) == 203){
            return ResponseEntity.status(203)
                .headers(responseHeaders)
                .body(getStatusMessage(203));
        }
        

        String mediaKey = mediaRepository.getKeyByName(mediaName);


        // ????????? ????????? ????????? ??????
        Integer amid = adsMediaRepository.selectIdByAdsKeyAndMediaKey(adsKey, mediaKey);
        adsMediaService.deleteById(amid);

        // ????????? ????????? ??????
        memoryDataService.deleteMemoryData("ads-media", amid);


        // ????????? ???????????? ????????? ?????? ????????? ??????
        String adminId = adminLoginRepository.findAdminIdByToken(token);



        // ?????? ?????? ?????? ??????
        logAdsService.insert(adsKey, "", adminId, "media-remove", "", mediaName);

        return ResponseEntity.status(200)
            .headers(responseHeaders)
            .body(getStatusMessage(200));
    }




    // ????????? ???????????? ?????? ????????? ??????  
    @CrossOrigin(origins = "*")
    @GetMapping("/media/list")
    public @ResponseBody ResponseEntity<String> list_media(@RequestParam(value="adsKey", required=true) String adsKey, HttpServletRequest request) {
    
            
        HttpHeaders responseHeaders = new HttpHeaders();
            
        // ???????????? ?????? ????????? ?????? 203 ?????? 
        if(adminLoginService.chkToken(request.getHeader("token")) == 203){
            return ResponseEntity.status(203)
                .headers(responseHeaders)
                .body(getStatusMessage(203));
        }
            

        return ResponseEntity.status(200)
            .headers(responseHeaders)
            .body(gson.toJson(viewAdsMediaRepository.findByAdsKey(adsKey)));
          
    }

    




    

    // ???????????? ???????????? ?????? ??????  
    @CrossOrigin(origins = "*")
    @GetMapping("/find/{adid}")
    public @ResponseBody ResponseEntity<String> find(@PathVariable Integer adid, HttpServletRequest request) {
        

        HttpHeaders responseHeaders = new HttpHeaders();

         // ???????????? ?????? ????????? ?????? 203 ?????? 
         if(adminLoginService.chkToken(request.getHeader("token")) == 203){
                return ResponseEntity.status(203)
                    .headers(responseHeaders)
                    .body(getStatusMessage(203));
        }


        return ResponseEntity.status(200)
                .headers(responseHeaders)
                .body(gson.toJson(adsRepository.findById(adid).get()));
    }











    // ?????? ?????? 
    @CrossOrigin(origins = "*")
    @DeleteMapping("/delete/{adid}")
    public @ResponseBody ResponseEntity<String>  delete(@PathVariable Integer adid, HttpServletRequest request){


        HttpHeaders responseHeaders = new HttpHeaders();


         //???????????? ?????? ????????? ?????? 203 ?????? 
         if(adminLoginService.chkToken(request.getHeader("token")) == 203){
            return ResponseEntity.status(203)
                .headers(responseHeaders)
                .body(getStatusMessage(203));
        }



        try{

            // ?????? ?????? ???????????? ????????? ?????? ??????
            adsService.deleteAds(adid);

            // ????????? ????????? ??????
            memoryDataService.deleteMemoryData("ads", adid);

            // ????????? ????????? ?????? Redis DB ????????? ????????????, ?????? ????????????????????? ??????
            Integer redisIndex = systemConfigRepository.findRedisIndex();
            Ads ads = adsService.findById(adid);
            Integer adsRedisIndex = ads.getRedisIndex();
            if(adsRedisIndex.compareTo(redisIndex) < 0)
                systemConfigRepository.updateRedisIndex(adsRedisIndex);

            // Redis ???????????? ??????
            redisUtil.flushDB(adsRedisIndex);

        }catch(EmptyResultDataAccessException e){


            // ?????? ???????????? ?????? ????????? ???????????? ?????? ?????? 201 ??????
            return ResponseEntity.status(201)
                .headers(responseHeaders)
                .body(getStatusMessage(201));
        }


        return ResponseEntity.status(200)
                .headers(responseHeaders)
                .body(getStatusMessage(200));

 
    }








    


    // ?????? ??????
    @CrossOrigin(origins = "*")
    @PutMapping("/modify/{adid}")
    public @ResponseBody ResponseEntity<String> modify(@PathVariable Integer adid, @RequestBody RequestSaveAds req, HttpServletRequest request){
        

        HttpHeaders responseHeaders = new HttpHeaders();

        String token = request.getHeader("token");

         // ???????????? ?????? ????????? ?????? 203 ?????? 
         if(adminLoginService.chkToken(token) == 203){
            return ResponseEntity.status(203)
                .headers(responseHeaders)
                .body(getStatusMessage(203));
        }


        
        Ads ads = adsService.findById(adid);
        
        String adminId = adminLoginRepository.findAdminIdByToken(token);


        // ?????? ??????
        adsService.modifyAds(adid, req, ads, request.getRemoteAddr(), ads.getAdsKey(), adminId);


        // ????????? ????????? ????????????
        memoryDataService.updateMemoryData("ads", adid);


        // ????????? ????????? ???????????? ?????? ??? ???????????? ???????????? ??????
        for(RequestSaveAds1 e : req.getMedia()){

            String  adsKey      = ads.getAdsKey();
            String  mediaKey    = mediaRepository.getKeyByName(e.getName());

            AdsMedia am = adsMediaRepository.findByAdsKeyAndMediakey(adsKey, mediaKey);
        
            adsMediaService.modifyAdsMedia(
                    adid, 
                    e, 
                    ads, 
                    request.getRemoteAddr(), 
                    adsKey, adminId, mediaKey,
                    am.getMediaCost(),
                    am.getMediaDailyCap());

            // ????????? ????????? ????????????
            memoryDataService.updateMemoryData("ads-media", am.getId());
        }
    
        return ResponseEntity.status(200)
                .headers(responseHeaders)
                .body(getStatusMessage(200));    
    }



    // ?????? ???????????? ???????????? 
    @CrossOrigin(origins = "*")
    @GetMapping("/download/script")
    public ResponseEntity<Resource> script_download(
        @RequestParam(value="media_key", required=true) String adsKey, 
        @RequestParam(value="ads_key", required=true) String mediaKey, 
        HttpServletRequest request) throws IOException{

        HttpHeaders responseHeaders = new HttpHeaders();

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"adr_script.js\"")
                .body(scriptBuilder.build(adsKey, mediaKey));       
    }
 

    


    

    // ?????? ????????? ????????? ?????? ?????? ??????
    @CrossOrigin(origins = "*")
    @GetMapping("/list/history")
    public @ResponseBody ResponseEntity<String> list_history(        
        @RequestParam(value="ads_key", required=true) String adsKey,
        HttpServletRequest request){

        HttpHeaders responseHeaders = new HttpHeaders();

        return ResponseEntity.status(200)
            .headers(responseHeaders)
            .body(gson.toJson(logAdsRepository.findByAdsKey(adsKey)));
    }








    // ?????? ????????? ?????? ?????? ???????????? REDIS ?????? ????????? ??????
    @CrossOrigin(origins = "*")
    @GetMapping("/list/click")
    public @ResponseBody ResponseEntity<String> list_click(        
        @RequestParam(value="ads_key", required=true) String adsKey,
        HttpServletRequest request){

        Ads ads = adsRepository.findByAdsKey(adsKey);

        String[] mediaKeyList = adsMediaRepository.mediaKeyListByAdsKey(adsKey);

        return ResponseEntity.status(200)
            .headers(new HttpHeaders())
            .body(gson.toJson(redisUtil.getLatestck(adsKey, mediaKeyList, ads.getRedisIndex())));
    }

}