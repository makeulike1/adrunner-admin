package com.gnm.adrunner.server.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "system_config_2")
public class SystemConfig2 {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer     id;

    private String      configKey;

    private String      configValue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    @Override
    public String toString() {
        return "SystemConfig2 [configKey=" + configKey + ", configValue=" + configValue + ", id=" + id + "]";
    }

}
