package com.point.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 小说角色配置表
 * Created by hadoop on 2017-7-31.
 */

@Document(collection = "fiction_actor_info")
public class FictionActorBean {

    @Id
    private String id;

    private long fiction_id;

    private String fiction_actor_id;

    private String fiction_actor_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getFiction_id() {
        return fiction_id;
    }

    public void setFiction_id(long fiction_id) {
        this.fiction_id = fiction_id;
    }

    public String getFiction_actor_name() {
        return fiction_actor_name;
    }

    public void setFiction_actor_name(String fiction_actor_name) {
        this.fiction_actor_name = fiction_actor_name;
    }

    public String getFiction_actor_id() {
        return fiction_actor_id;
    }

    public void setFiction_actor_id(String fiction_actor_id) {
        this.fiction_actor_id = fiction_actor_id;
    }

    @Override
    public String toString() {
        return "FictionActorBean{" +
                "id='" + id + '\'' +
                ", fiction_id=" + fiction_id +
                ", fiction_actor_id='" + fiction_actor_id + '\'' +
                ", fiction_actor_name='" + fiction_actor_name + '\'' +
                '}';
    }
}
