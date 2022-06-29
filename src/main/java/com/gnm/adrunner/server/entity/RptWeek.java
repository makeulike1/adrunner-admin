package com.gnm.adrunner.server.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rpt_week")
public class RptWeek {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer     id;
 
    private String      date;

    private String      startdate;

    private String      enddate;

    private String      adsKey;

    private String      mediaKey;

    private String      advKey;

    private Integer     totalClicks;

    private Integer     totalConversions;

    private Float       conversionRate;

    private Integer     totalCost1;

    private Integer     totalCost2;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getAdsKey() {
        return adsKey;
    }

    public void setAdsKey(String adsKey) {
        this.adsKey = adsKey;
    }

    public String getAdvKey() {
        return advKey;
    }

    public void setAdvKey(String advKey) {
        this.advKey = advKey;
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

    public Float getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(Float conversionRate) {
        this.conversionRate = conversionRate;
    }

    public Integer getTotalCost1() {
        return totalCost1;
    }

    public void setTotalCost1(Integer totalCost1) {
        this.totalCost1 = totalCost1;
    }

    public Integer getTotalCost2() {
        return totalCost2;
    }

    public void setTotalCost2(Integer totalCost2) {
        this.totalCost2 = totalCost2;
    }

    public String getMediaKey() {
        return mediaKey;
    }

    public void setMediaKey(String mediaKey) {
        this.mediaKey = mediaKey;
    }

    @Override
    public String toString() {
        return "RptWeek [adsKey=" + adsKey + ", advKey=" + advKey + ", conversionRate=" + conversionRate + ", date="
                + date + ", enddate=" + enddate + ", id=" + id + ", mediaKey=" + mediaKey + ", startdate=" + startdate
                + ", totalClicks=" + totalClicks + ", totalConversions=" + totalConversions + ", totalCost1="
                + totalCost1 + ", totalCost2=" + totalCost2 + "]";
    }

 
    
    
}
