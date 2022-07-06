package com.gnm.adrunner.server.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "media")
public class Media {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer     id;

    private String      name;

    private String      mediaKey;
    
    private Boolean     isDelete;

    private Boolean     isPostback;

    private String      postbackInstall;

    private String      postbackEvent;
 
    private String      createtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMediaKey() {
        return mediaKey;
    }

    public void setMediaKey(String mediaKey) {
        this.mediaKey = mediaKey;
    }

    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    public Boolean getIsPostback() {
        return isPostback;
    }

    public void setIsPostback(Boolean isPostback) {
        this.isPostback = isPostback;
    }

    public String getPostbackInstall() {
        return postbackInstall;
    }

    public void setPostbackInstall(String postbackInstall) {
        this.postbackInstall = postbackInstall;
    }

    public String getPostbackEvent() {
        return postbackEvent;
    }

    public void setPostbackEvent(String postbackEvent) {
        this.postbackEvent = postbackEvent;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    @Override
    public String toString() {
        return "Media [createtime=" + createtime + ", id=" + id + ", isDelete=" + isDelete + ", isPostback="
                + isPostback + ", mediaKey=" + mediaKey + ", name=" + name + ", postbackEvent=" + postbackEvent
                + ", postbackInstall=" + postbackInstall + "]";
    }

    
    
}
