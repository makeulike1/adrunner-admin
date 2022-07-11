package com.gnm.adrunner.server.param.res.admin;

public class ResponseListAds2 {

    private String  mediaKey;

    private String  mediaName;

    private Integer totalClicks;

    private Integer totalConversions;

    private Integer todaycvCount;
 
    private Integer dayLimit;

    private Boolean isDayLimit;

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

    public Integer getTodaycvCount() {
        return todaycvCount;
    }

    public void setTodaycvCount(Integer todaycvCount) {
        this.todaycvCount = todaycvCount;
    }

    public Integer getDayLimit() {
        return dayLimit;
    }

    public void setDayLimit(Integer dayLimit) {
        this.dayLimit = dayLimit;
    }

    public Boolean getIsDayLimit() {
        return isDayLimit;
    }

    public void setIsDayLimit(Boolean isDayLimit) {
        this.isDayLimit = isDayLimit;
    }

    @Override
    public String toString() {
        return "ResponseListAds2 [dayLimit=" + dayLimit + ", isDayLimit=" + isDayLimit + ", mediaKey=" + mediaKey
                + ", mediaName=" + mediaName + ", todaycvCount=" + todaycvCount + ", totalClicks=" + totalClicks
                + ", totalConversions=" + totalConversions + "]";
    } 
 
}
