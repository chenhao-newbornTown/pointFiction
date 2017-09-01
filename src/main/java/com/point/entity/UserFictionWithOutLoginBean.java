package com.point.entity;

/**
 * Created by hadoop on 2017-7-18.
 */
public class UserFictionWithOutLoginBean {

    private long fiction_id;

    private String fiction_name;

    private String user_read_timestamp;

    private long user_read_line;//用户读到第多少行



    public long getFiction_id() {
        return fiction_id;
    }

    public void setFiction_id(long fiction_id) {
        this.fiction_id = fiction_id;
    }

    public String getFiction_name() {
        return fiction_name;
    }

    public void setFiction_name(String fiction_name) {
        this.fiction_name = fiction_name;
    }

    public String getUser_read_timestamp() {
        return user_read_timestamp;
    }

    public void setUser_read_timestamp(String user_read_timestamp) {
        this.user_read_timestamp = user_read_timestamp;
    }

    public long getUser_read_line() {
        return user_read_line;
    }

    public void setUser_read_line(long user_read_line) {
        this.user_read_line = user_read_line;
    }

    @Override
    public String toString() {
        return "UserFictionWithOutLoginBean{" +
                ", fiction_id=" + fiction_id +
                ", fiction_name='" + fiction_name + '\'' +
                ", user_read_timestamp='" + user_read_timestamp + '\'' +
                ", user_read_line=" + user_read_line +
                '}';
    }
}
