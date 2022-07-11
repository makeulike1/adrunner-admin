package com.gnm.adrunner.server.param.res.admin;

import java.util.List;

import com.gnm.adrunner.server.entity.Ads;

public class ResponseListAds1 {
 
    private Ads         ads;

    private String      affName;

    private Integer     click;

    private Integer     conversion;

    private Double      conversionRate;
 
    private List<ResponseListAds2>  mediaData;

    public Ads getAds() {
        return ads;
    }

    public void setAds(Ads ads) {
        this.ads = ads;
    }

    public String getAffName() {
        return affName;
    }

    public void setAffName(String affName) {
        this.affName = affName;
    }

    public Integer getClick() {
        return click;
    }

    public void setClick(Integer click) {
        this.click = click;
    }

    public Integer getConversion() {
        return conversion;
    }

    public void setConversion(Integer conversion) {
        this.conversion = conversion;
    }

    public Double getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(Double conversionRate) {
        this.conversionRate = conversionRate;
    }

    public List<ResponseListAds2> getMediaData() {
        return mediaData;
    }

    public void setMediaData(List<ResponseListAds2> mediaData) {
        this.mediaData = mediaData;
    }

    @Override
    public String toString() {
        return "ResponseListAds1 [ads=" + ads + ", affName=" + affName + ", click=" + click + ", conversion="
                + conversion + ", conversionRate=" + conversionRate + ", mediaData=" + mediaData + "]";
    }
 
    

    
}
