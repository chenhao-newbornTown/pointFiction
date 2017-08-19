package com.point.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

/**
 * Created by 肥肥 on 2017/7/17 0017.
 */

@Document(collection = "user_info")
public class UserInfoBean {


    @Id
    private String id;

    @NotNull
    @Indexed(unique = true)
    @AutoIncKey
    private long uid=0L;//用户id，自增不可重复

    private String nick_name;//用户昵称

    private int gendar;//用户性别，1，女，2，男
    private String source_platform;//登录平台，1，QQ，2，WX，3，WB

    private String login_timestamp;//登录时间戳

    private String registered_timestamp;//注册时间戳

    @NotNull
    @Indexed(unique = true)
    private String token;

    @NotNull
    private String mobile_device_num;//手机设备号


    private String token_device;//token和手机设备号的字符串，1，判断用户是否是在一个设备上登录的，2，判断单点登录


    public String getToken_device() {
        return token_device;
    }

    public void setToken_device(String token_device) {
        this.token_device = token_device;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getGendar() {
        return gendar;
    }

    public void setGendar(int gendar) {
        this.gendar = gendar;
    }

    public String getSource_platform() {
        return source_platform;
    }

    public void setSource_platform(String source_platform) {
        this.source_platform = source_platform;
    }

    public String getLogin_timestamp() {
        return login_timestamp;
    }

    public void setLogin_timestamp(String login_timestamp) {
        this.login_timestamp = login_timestamp;
    }

    public String getRegistered_timestamp() {
        return registered_timestamp;
    }

    public void setRegistered_timestamp(String registered_timestamp) {
        this.registered_timestamp = registered_timestamp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMobile_device_num() {
        return mobile_device_num;
    }

    public void setMobile_device_num(String mobile_device_num) {
        this.mobile_device_num = mobile_device_num;
    }

    @Override
    public String toString() {
        return "UserInfoBean{" +
                "id='" + id + '\'' +
                ", uid=" + uid +
                ", nick_name='" + nick_name + '\'' +
                ", gendar=" + gendar +
                ", source_platform='" + source_platform + '\'' +
                ", login_timestamp='" + login_timestamp + '\'' +
                ", registered_timestamp='" + registered_timestamp + '\'' +
                ", token='" + token + '\'' +
                ", mobile_device_num='" + mobile_device_num + '\'' +
                ", token_device='" + token_device + '\'' +
                '}';
    }
}
