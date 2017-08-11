package com.point.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by hadoop on 2017-8-3.
 */
@Document(collection = "pic_info")
public class PicBean {

    @Id
    public String id;

    public String pic_name;

    public String pic_upload_time;

    public long use_pic_num;

    public String pic_status;//1,admin,2,user

    public String getPic_status() {
        return pic_status;
    }

    public void setPic_status(String pic_status) {
        this.pic_status = pic_status;
    }

    public String getPic_upload_time() {
        return pic_upload_time;
    }

    public void setPic_upload_time(String pic_upload_time) {
        this.pic_upload_time = pic_upload_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPic_name() {
        return pic_name;
    }

    public void setPic_name(String pic_name) {
        this.pic_name = pic_name;
    }

    public long getUse_pic_num() {
        return use_pic_num;
    }

    public void setUse_pic_num(long use_pic_num) {
        this.use_pic_num = use_pic_num;
    }
}
