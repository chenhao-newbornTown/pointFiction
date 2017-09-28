package com.point.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by feifei on 2017-9-20.
 */
@Document(collection = "data_push")
public class DataForPush {

    @Id
    private  String id;

    private long fiction_id;
    private String device_token;
    private long user_read_line;

    private String mobile_device_num;

    private int push_num;

    public int getPush_num() {
        return push_num;
    }

    public void setPush_num(int push_num) {
        this.push_num = push_num;
    }

    public String getMobile_device_num() {
        return mobile_device_num;
    }

    public void setMobile_device_num(String mobile_device_num) {
        this.mobile_device_num = mobile_device_num;
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

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public long getUser_read_line() {
        return user_read_line;
    }

    public void setUser_read_line(long user_read_line) {
        this.user_read_line = user_read_line;
    }
}
