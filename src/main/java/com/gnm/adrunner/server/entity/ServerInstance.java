package com.gnm.adrunner.server.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "server_instance")
public class ServerInstance {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer     id;

    private Integer     type;

    private Integer     serverGroup;

    private String      clientIp;

    private Integer     port;

    private String      fullURL;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getServerGroup() {
        return serverGroup;
    }

    public void setServerGroup(Integer serverGroup) {
        this.serverGroup = serverGroup;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getFullURL() {
        return fullURL;
    }

    public void setFullURL(String fullURL) {
        this.fullURL = fullURL;
    }

    @Override
    public String toString() {
        return "ServerInstance [clientIp=" + clientIp + ", fullURL=" + fullURL + ", id=" + id + ", port=" + port
                + ", serverGroup=" + serverGroup + ", type=" + type + "]";
    }
 
}
