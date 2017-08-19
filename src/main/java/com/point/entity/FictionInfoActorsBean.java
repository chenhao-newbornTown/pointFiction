package com.point.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by hadoop on 2017-7-19.
 */
public class FictionInfoActorsBean {

    private FictionBean fictionBean;
    private List<FictionActorBean> fiction_actors;//小说人物列表，[旁白默认不存]


    public FictionBean getFictionBean() {
        return fictionBean;
    }

    public void setFictionBean(FictionBean fictionBean) {
        this.fictionBean = fictionBean;
    }

    public List<FictionActorBean> getFiction_actors() {
        return fiction_actors;
    }

    public void setFiction_actors(List<FictionActorBean> fiction_actors) {
        this.fiction_actors = fiction_actors;
    }
}
