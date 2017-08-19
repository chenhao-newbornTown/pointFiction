package com.point.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by hadoop on 2017-7-18.
 */
@Document(collection = "user_fiction")
public class UserFictionBean {

    @Id
    private String id;

    private long uid;

    private long fiction_id;

    private String fiction_name;

    private String user_read_timestamp;

    private String user_like_count;

    private long user_read_line;//用户读到第多少行

    public String getUser_like_count() {
        return user_like_count;
    }

    public void setUser_like_count(String user_like_count) {
        this.user_like_count = user_like_count;
    }

    public long getUser_read_line() {
        return user_read_line;
    }

    public void setUser_read_line(long user_read_line) {
        this.user_read_line = user_read_line;
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

    public long getFiction_id() {
        return fiction_id;
    }

    public void setFiction_id(long fiction_id) {
        this.fiction_id = fiction_id;
    }

    public String getUser_read_timestamp() {
        return user_read_timestamp;
    }

    public void setUser_read_timestamp(String user_read_timestamp) {
        this.user_read_timestamp = user_read_timestamp;
    }

    public String getFiction_name() {
        return fiction_name;
    }

    public void setFiction_name(String fiction_name) {
        this.fiction_name = fiction_name;
    }


    @Override
    public String toString() {
        return "UserFictionBean{" +
                "id='" + id + '\'' +
                ", uid=" + uid +
                ", fiction_id=" + fiction_id +
                ", fiction_name='" + fiction_name + '\'' +
                ", user_read_timestamp='" + user_read_timestamp + '\'' +
                '}';
    }
}
