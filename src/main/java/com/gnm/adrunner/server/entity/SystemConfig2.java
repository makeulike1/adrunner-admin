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

    private Integer     redisDb;

    private Integer     redisGroup;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRedisDb() {
        return redisDb;
    }

    public void setRedisDb(Integer redisDb) {
        this.redisDb = redisDb;
    }

    public Integer getRedisGroup() {
        return redisGroup;
    }

    public void setRedisGroup(Integer redisGroup) {
        this.redisGroup = redisGroup;
    }

    @Override
    public String toString() {
        return "SystemConfig2 [id=" + id + ", redisDb=" + redisDb + ", redisGroup=" + redisGroup + "]";
    }
  
}
