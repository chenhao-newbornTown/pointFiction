package com.point.entity;

/**
 * 存储小说名称，作者，图片的实体存入redis
 * Created by hadoop on 2017-7-25.
 */
public class FictionInfoBean {

    private long fiction_id;
    private String fiction_author_name;
    private String fiction_name;
    private String fiction_pic_path;

    public long getFiction_id() {
        return fiction_id;
    }

    public void setFiction_id(long fiction_id) {
        this.fiction_id = fiction_id;
    }

    public String getFiction_author_name() {
        return fiction_author_name;
    }

    public void setFiction_author_name(String fiction_author_name) {
        this.fiction_author_name = fiction_author_name;
    }

    public String getFiction_name() {
        return fiction_name;
    }

    public void setFiction_name(String fiction_name) {
        this.fiction_name = fiction_name;
    }

    public String getFiction_pic_path() {
        return fiction_pic_path;
    }

    public void setFiction_pic_path(String fiction_pic_path) {
        this.fiction_pic_path = fiction_pic_path;
    }

    @Override
    public String toString() {
        return "FictionInfoBean{" +
                "fiction_id=" + fiction_id +
                ", fiction_author_name='" + fiction_author_name + '\'' +
                ", fiction_name='" + fiction_name + '\'' +
                ", fiction_pic_path='" + fiction_pic_path + '\'' +
                '}';
    }
}
