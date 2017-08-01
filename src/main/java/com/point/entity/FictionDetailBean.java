package com.point.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 小说详细内容页
 * Created by hadoop on 2017-7-24.
 */
@Document(collection = "fiction_detail")
public class FictionDetailBean {

    @Id
    private String id;

    private long fiction_id;

    private String actor_fiction_detail;//小说具体内容

    private String actor_id;//小说角色id

    private String actor_name;//小说角色名

    private long actor_fiction_detail_index;//当前小说的行数

    private int fiction_detail_status; //0，未发布，1，已发布

    public String getActor_id() {
        return actor_id;
    }

    public void setActor_id(String actor_id) {
        this.actor_id = actor_id;
    }

    @Override
    public String toString() {
        return "FictionDetailBean{" +
                "id='" + id + '\'' +
                ", fiction_id=" + fiction_id +
                ", actor_fiction_detail='" + actor_fiction_detail + '\'' +
                ", actor_name='" + actor_name + '\'' +
                ", actor_fiction_detail_index=" + actor_fiction_detail_index +
                ", fiction_detail_status=" + fiction_detail_status +
                '}';
    }

    public int getFiction_detail_status() {
        return fiction_detail_status;
    }

    public void setFiction_detail_status(int fiction_detail_status) {
        this.fiction_detail_status = fiction_detail_status;
    }

    public String getActor_name() {
        return actor_name;
    }

    public void setActor_name(String actor_name) {
        this.actor_name = actor_name;
    }

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

    public String getActor_fiction_detail() {
        return actor_fiction_detail;
    }

    public void setActor_fiction_detail(String actor_fiction_detail) {
        this.actor_fiction_detail = actor_fiction_detail;
    }

    public long getActor_fiction_detail_index() {
        return actor_fiction_detail_index;
    }

    public void setActor_fiction_detail_index(long actor_fiction_detail_index) {
        this.actor_fiction_detail_index = actor_fiction_detail_index;
    }
}
