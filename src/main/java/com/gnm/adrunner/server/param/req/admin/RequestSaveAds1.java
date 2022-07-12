package com.gnm.adrunner.server.param.req.admin;

public class RequestSaveAds1 {

    private String      name;

    private Integer     cost;

    private Integer     dailycap;

    private Boolean     isLimit;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getDailycap() {
        return dailycap;
    }

    public void setDailycap(Integer dailycap) {
        this.dailycap = dailycap;
    }

    public Boolean getIsLimit() {
        return isLimit;
    }

    public void setIsLimit(Boolean isLimit) {
        this.isLimit = isLimit;
    }

    @Override
    public String toString() {
        return "RequestSaveAds1 [cost=" + cost + ", dailycap=" + dailycap + ", isLimit=" + isLimit + ", name=" + name
                + "]";
    }

    
    
    
    
}
