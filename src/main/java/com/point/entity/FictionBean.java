package com.point.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by hadoop on 2017-7-19.
 */
@Document(collection = "fiction_info")
public class FictionBean {

    @Id
    private String id;

    @NotNull @Indexed(unique = true)@AutoIncKey
    private long fiction_id;//小说id，自增，不可重复
    @NotNull
    private String fiction_name;//小说名称,不可为null
    private String fiction_author_id;//小说作者id
    private String fiction_author_name;//小说作者名称
    private String update_time;//小说上传/更新时间时间
    private String update_date;//小说上传/更新时间时间
    private String fiction_pic_path;//小说封面配图名称

    private long read_count;//小说阅读数
    private long like_count;//小说点赞数
 //   private List<String> fiction_actors;//小说人物列表，[旁白默认不存]

    private long fiction_line_num;//小说行数

    private int fiction_status; //0，未发布，1，已发布，2加入推荐池子

    private String status;//小说状态--->图片被删，小说被删，默认00000

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getFiction_line_num() {
        return fiction_line_num;
    }

    public void setFiction_line_num(long fiction_line_num) {
        this.fiction_line_num = fiction_line_num;
    }

    public int getFiction_status() {
        return fiction_status;
    }

    public void setFiction_status(int fiction_status) {
        this.fiction_status = fiction_status;
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

    public String getFiction_name() {
        return fiction_name;
    }

    public void setFiction_name(String fiction_name) {
        this.fiction_name = fiction_name;
    }

    public String getFiction_author_id() {
        return fiction_author_id;
    }

    public void setFiction_author_id(String fiction_author_id) {
        this.fiction_author_id = fiction_author_id;
    }

    public String getFiction_author_name() {
        return fiction_author_name;
    }

    public void setFiction_author_name(String fiction_author_name) {
        this.fiction_author_name = fiction_author_name;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getFiction_pic_path() {
        return fiction_pic_path;
    }

    public void setFiction_pic_path(String fiction_pic_path) {
        this.fiction_pic_path = fiction_pic_path;
    }

    public long getRead_count() {
        return read_count;
    }

    public void setRead_count(long read_count) {
        this.read_count = read_count;
    }

    public long getLike_count() {
        return like_count;
    }

    public void setLike_count(long like_count) {
        this.like_count = like_count;
    }


    @Override
    public String toString() {
        return "FictionBean{" +
                "id='" + id + '\'' +
                ", fiction_id=" + fiction_id +
                ", fiction_name='" + fiction_name + '\'' +
                ", fiction_author_id='" + fiction_author_id + '\'' +
                ", fiction_author_name='" + fiction_author_name + '\'' +
                ", update_time='" + update_time + '\'' +
                ", fiction_pic_path='" + fiction_pic_path + '\'' +
                ", read_count=" + read_count +
                ", like_count=" + like_count +
                ", fiction_line_num=" + fiction_line_num +
                ", fiction_status=" + fiction_status +
                '}';
    }
}
