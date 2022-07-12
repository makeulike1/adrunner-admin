package com.gnm.adrunner.server.param.res.admin;

import com.gnm.adrunner.server.entity.Adv;

public class ResponseAdvList3 {

    private Adv e;

    private Boolean chk;

    public Adv getE() {
        return e;
    }

    public void setE(Adv e) {
        this.e = e;
    }

    public Boolean getChk() {
        return chk;
    }

    public void setChk(Boolean chk) {
        this.chk = chk;
    }

    @Override
    public String toString() {
        return "ResponseAdvList3 [chk=" + chk + ", e=" + e + "]";
    }

    
    
}
