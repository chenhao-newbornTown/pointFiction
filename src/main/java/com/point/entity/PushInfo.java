package com.point.entity;

import sun.dc.pr.PRError;

/**
 * Created by feifei on 2017-9-20.
 */
public class PushInfo {

    private String device_token;
    private String fiction_name;
    private String actor_name;
    private String actor_fiction_detail;
    private long fiction_detail_index;
    private String mobile_device_num;
    private FictionBean fictionBean;
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

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public long getFiction_detail_index() {
        return fiction_detail_index;
    }

    public void setFiction_detail_index(long fiction_detail_index) {
        this.fiction_detail_index = fiction_detail_index;
    }

    public FictionBean getFictionBean() {
        return fictionBean;
    }

    public void setFictionBean(FictionBean fictionBean) {
        this.fictionBean = fictionBean;
    }

    public String getFiction_name() {
        return fiction_name;
    }

    public void setFiction_name(String fiction_name) {
        this.fiction_name = fiction_name;
    }

    public String getActor_name() {
        return actor_name;
    }

    public void setActor_name(String actor_name) {
        this.actor_name = actor_name;
    }

    public String getActor_fiction_detail() {
        return actor_fiction_detail;
    }

    public void setActor_fiction_detail(String actor_fiction_detail) {
        this.actor_fiction_detail = actor_fiction_detail;
    }
}
