package com.gnm.adrunner.server.param.res.admin;

import com.gnm.adrunner.server.entity.Aff;

public class ResponseAffList3 {
        
    private Aff e;

    private Boolean chk;

    public Aff getE() {
        return e;
    }

    public void setE(Aff e) {
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
        return "ResponseAffList3 [chk=" + chk + ", e=" + e + "]";
    }

    
}
