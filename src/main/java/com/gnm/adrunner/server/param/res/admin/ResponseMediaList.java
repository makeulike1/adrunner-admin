package com.gnm.adrunner.server.param.res.admin;

public class ResponseMediaList {

    private String  mediaKey;

    private String  mediaName;

    private Integer totalClicks;

    private Integer totalConversions;

    private Integer todaycvCount;
 
    private Integer dailyCap;

    private Integer runDailyCap;

    private Boolean isDayLimit;

    private Boolean todayLimit;

    private Boolean isDelete;

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

    public Integer getDailyCap() {
        return dailyCap;
    }

    public void setDailyCap(Integer dailyCap) {
        this.dailyCap = dailyCap;
    }

    public Integer getRunDailyCap() {
        return runDailyCap;
    }

    public void setRunDailyCap(Integer runDailyCap) {
        this.runDailyCap = runDailyCap;
    }

    public Boolean getIsDayLimit() {
        return isDayLimit;
    }

    public void setIsDayLimit(Boolean isDayLimit) {
        this.isDayLimit = isDayLimit;
    }

    public Boolean getTodayLimit() {
        return todayLimit;
    }

    public void setTodayLimit(Boolean todayLimit) {
        this.todayLimit = todayLimit;
    }

    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String toString() {
        return "ResponseMediaList [dailyCap=" + dailyCap + ", isDayLimit=" + isDayLimit + ", isDelete=" + isDelete
                + ", mediaKey=" + mediaKey + ", mediaName=" + mediaName + ", runDailyCap=" + runDailyCap
                + ", todayLimit=" + todayLimit + ", todaycvCount=" + todaycvCount + ", totalClicks=" + totalClicks
                + ", totalConversions=" + totalConversions + "]";
    } 

    

}
