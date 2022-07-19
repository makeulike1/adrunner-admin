package com.gnm.adrunner.server.object;

public class RedisEntity2 {

    private Integer id;

    private Integer group;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroup() {
        return group;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }

    public RedisEntity2(Integer id, Integer group){
        this.id = id;
        this.group = group;
    }


    @Override
    public String toString() {
        return "RedisEntity2 [group=" + group + ", id=" + id + "]";
    }

}
