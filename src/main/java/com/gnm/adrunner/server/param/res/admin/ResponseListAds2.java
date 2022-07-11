package com.gnm.adrunner.server.param.res.admin;

public class ResponseListAds2 {

    private String  mediaKey;

    private String  mediaName;

    private Integer totalClicks;

    private Integer totalConversions;

    public String getMediaKey() {
        return mediaKey;
    }

    public void setMediaKey(String mediaKey) {
        this.mediaKey = mediaKey;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public Integer getTotalClicks() {
        return totalClicks;
    }

    public void setTotalClicks(Integer totalClicks) {
        this.totalClicks = totalClicks;
    }

    public Integer getTotalConversions() {
        return totalConversions;
    }

    public void setTotalConversions(Integer totalConversions) {
        this.totalConversions = totalConversions;
    }

    @Override
    public String toString() {
        return "ResponseListAds2 [mediaKey=" + mediaKey + ", mediaName=" + mediaName + ", totalClicks=" + totalClicks
                + ", totalConversions=" + totalConversions + "]";
    }
 
    
}
