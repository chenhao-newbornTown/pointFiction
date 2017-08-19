package com.point.entity;

/**
 * Created by hadoop on 2017-7-25.
 */
public class UserReadFictionBean {

    private long fiction_id;

    private String user_read_timestamp;

    private String fiction_name;//小说名称,不可为null
    // private String fiction_author_name;//小说作者名称
    private String fiction_pic_path;//小说封面配图名称

//    private long read_count;//小说阅读数
//    private long like_count;//小说点赞数

    private long user_read_line;//用户读到第多少行

    private long fiction_line_num;//小说行数

    private String user_like_count_status;//0,未点赞，1，已点赞

    public String getUser_like_count_status() {
        return user_like_count_status;
    }

    public void setUser_like_count_status(String user_like_count_status) {
        this.user_like_count_status = user_like_count_status;
    }

    public long getFiction_line_num() {
        return fiction_line_num;
    }

    public void setFiction_line_num(long fiction_line_num) {
        this.fiction_line_num = fiction_line_num;
    }

    public long getUser_read_line() {
        return user_read_line;
    }

    public void setUser_read_line(long user_read_line) {
        this.user_read_line = user_read_line;
    }

    public String getFiction_pic_path() {
        return fiction_pic_path;
    }

    public void setFiction_pic_path(String fiction_pic_path) {
        this.fiction_pic_path = fiction_pic_path;
    }

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
}
