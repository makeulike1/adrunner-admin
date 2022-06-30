package com.gnm.adrunner.server.param.req.admin;

public class RequestSaveAffParam {

    private Integer paramType;

    private String  paramKey;

    private String  paramValue;

    private String  passValue;

    public Integer getParamType() {
        return paramType;
    }

    public void setParamType(Integer paramType) {
        this.paramType = paramType;
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getPassValue() {
        return passValue;
    }

    public void setPassValue(String passValue) {
        this.passValue = passValue;
    }

    @Override
    public String toString() {
        return "RequestSaveAffParam [paramKey=" + paramKey + ", paramType=" + paramType + ", paramValue=" + paramValue
                + ", passValue=" + passValue + "]";
    }
 
    
}
