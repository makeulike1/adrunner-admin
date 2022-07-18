package com.gnm.adrunner.server.param.req.admin;

public class RequestSaveAff {
 
    private String  name;

    private String  eg;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEg() {
        return eg;
    }

    public void setEg(String eg) {
        this.eg = eg;
    }

    @Override
    public String toString() {
        return "RequestSaveAff [eg=" + eg + ", name=" + name + "]";
    }

}
