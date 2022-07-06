package com.gnm.adrunner.server.param.req.admin;

public class RequestSaveMediaParam {
    
    private String mediaKey;

    private String paramKey;

    private String paramValue;

    public String getMediaKey() {
        return mediaKey;
    }

    public void setMediaKey(String mediaKey) {
        this.mediaKey = mediaKey;
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

    @Override
    public String toString() {
        return "RequestSaveMediaParam [mediaKey=" + mediaKey + ", paramKey=" + paramKey + ", paramValue=" + paramValue
                + "]";
    }

    
}
