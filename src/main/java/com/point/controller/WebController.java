package com.point.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.point.entity.FictionBean;
import com.point.entity.FictionDetailBean;
import com.point.entity.PicBean;
import jdk.nashorn.internal.objects.annotations.Where;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hadoop on 2017-8-1.
 */

@Controller
@RequestMapping("/")
public class WebController {

    @Autowired
    MongoTemplate mongoTemplate;

    @RequestMapping("/index")
    public ModelAndView index() {

        ModelAndView modelAndView = new ModelAndView("index");
        return modelAndView;

    }

    @RequestMapping("/main")
    public ModelAndView main(@RequestParam("username") String username, @RequestParam("userPassword") String pwd) {

        ModelAndView modelAndView = new ModelAndView("main");

        modelAndView.addObject("username", username);
        return modelAndView;

    }

    @RequestMapping("/menu")
    public ModelAndView menu() {

        ModelAndView modelAndView = new ModelAndView("menu");
        return modelAndView;

    }

    @RequestMapping("/hotfictionlist")
    public ModelAndView getHotFictionList() {

        List<FictionBean> fictionBeanList = mongoTemplate.find(new Query().with(new Sort(new Sort.Order(Sort.Direction.DESC, "update_time"))), FictionBean.class);

        ModelAndView modelAndView = new ModelAndView("getHotFictionList");
        modelAndView.addObject("fictionBeanList", fictionBeanList);
        return modelAndView;
    }

    @RequestMapping("/searchhotfictionlist")
    public ModelAndView searchHotFictionList(FictionBean fictionBean) {

        String fiction_name = fictionBean.getFiction_name();
        String fiction_author_name = fictionBean.getFiction_author_name();
        int fiction_status = fictionBean.getFiction_status();

        DBObject queryObject = new BasicDBObject();

        if (!StringUtils.isEmpty(fiction_name)) {
            queryObject.put("fiction_name", fiction_name);
        }
        if (!StringUtils.isEmpty(fiction_author_name)) {
            queryObject.put("fiction_author_name", fiction_author_name);
        }
        if (fiction_status < 999) {
            queryObject.put("fiction_status", fiction_status);
        }

        List<FictionBean> fictionBeanList = mongoTemplate.find(new BasicQuery(queryObject).with(new Sort(new Sort.Order(Sort.Direction.DESC, "update_time"))), FictionBean.class);

        ModelAndView modelAndView = new ModelAndView("getHotFictionList");
        modelAndView.addObject("fictionBeanList", fictionBeanList);
        modelAndView.addObject("fictionBean", fictionBean);
        return modelAndView;
    }

    @RequestMapping("getfictiondetail")
    public ModelAndView getFictionDetail(String fiction_id) {

        List<FictionDetailBean> fictionDetailBeanList = mongoTemplate.find(new Query(Criteria.where("fiction_id").is(Long.parseLong(fiction_id))).with(new Sort(new Sort.Order(Sort.Direction.ASC, "actor_fiction_detail_index"))), FictionDetailBean.class);
        ModelAndView modelAndView = new ModelAndView("getFictionDetail");
        modelAndView.addObject("fictionDetailBeanList", fictionDetailBeanList);
        return modelAndView;
    }

    @RequestMapping("/changefictionstatus")
    public ModelAndView changeFictionStatus(FictionBean fictionBean, @RequestParam("fiction_id_jsp") long fiction_id_jsp, @RequestParam("fiction_status_jsp") int fiction_status_jsp) {

        mongoTemplate.updateFirst(new Query(Criteria.where("fiction_id").is(fiction_id_jsp)), Update.update("fiction_status", fiction_status_jsp), FictionBean.class);

        String fiction_name = fictionBean.getFiction_name();
        String fiction_author_name = fictionBean.getFiction_author_name();
        int fiction_status = fictionBean.getFiction_status();

        DBObject queryObject = new BasicDBObject();

        if (!StringUtils.isEmpty(fiction_name)) {
            queryObject.put("fiction_name", fiction_name);
        }
        if (!StringUtils.isEmpty(fiction_author_name)) {
            queryObject.put("fiction_author_name", fiction_author_name);
        }
        if (fiction_status < 999) {
            queryObject.put("fiction_status", fiction_status);
        }

        List<FictionBean> fictionBeanList = mongoTemplate.find(new BasicQuery(queryObject).with(new Sort(new Sort.Order(Sort.Direction.DESC, "update_time"))), FictionBean.class);

        ModelAndView modelAndView = new ModelAndView("getHotFictionList");
        modelAndView.addObject("fictionBeanList", fictionBeanList);
        modelAndView.addObject("fictionBean", fictionBean);
        return modelAndView;
    }

    @RequestMapping("/uploadpic")
    public ModelAndView uploadPic(){
        List<PicBean> picBeanMongoList = mongoTemplate.find(new Query().with(new Sort(new Sort.Order(Sort.Direction.DESC,"pic_upload_time"))),PicBean.class);

        ModelAndView modelAndView = new ModelAndView("uploadPic");

        modelAndView.addObject("picBeanMongoList",picBeanMongoList);
        return modelAndView;
    }


    @RequestMapping(value = "/batchupload")
    public ModelAndView handleFileUpload(HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        MultipartFile file = null;
        BufferedOutputStream stream = null;

        for (int i = 0; i < files.size(); ++i) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();

                    String filename = String.valueOf(System.currentTimeMillis())+file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                    stream = new BufferedOutputStream(new FileOutputStream(new File("F:\\fiction"+File.separator+filename)));
                    stream.write(bytes);
                    stream.close();

                    PicBean picBean = new PicBean();
                    picBean.setPic_name(filename);
                    picBean.setPic_upload_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

                    mongoTemplate.insert(picBean);
                } catch (Exception e) {
                    stream = null;
                }
            }
        }
        List<PicBean> picBeanMongoList = mongoTemplate.find(new Query().with(new Sort(new Sort.Order(Sort.Direction.DESC,"pic_upload_time"))),PicBean.class);

        ModelAndView modelAndView = new ModelAndView("uploadPic");

        modelAndView.addObject("picBeanMongoList",picBeanMongoList);

        return modelAndView;
    }

    @RequestMapping("/delpic")
    public ModelAndView delPic(@RequestParam("id") String id){

        mongoTemplate.remove(new Query(Criteria.where("id").is(new ObjectId(id))),PicBean.class);

        List<PicBean> picBeanMongoList = mongoTemplate.find(new Query().with(new Sort(new Sort.Order(Sort.Direction.DESC,"pic_upload_time"))),PicBean.class);

        ModelAndView modelAndView = new ModelAndView("uploadPic");

        modelAndView.addObject("picBeanMongoList",picBeanMongoList);

        return modelAndView;
    }

    @RequestMapping("/delfiction")
    public ModelAndView delFiction(FictionBean fictionBean,@RequestParam("id") String id){

        mongoTemplate.remove(new Query(Criteria.where("id").is(new ObjectId(id))),FictionBean.class);

        String fiction_name = fictionBean.getFiction_name();
        String fiction_author_name = fictionBean.getFiction_author_name();
        int fiction_status = fictionBean.getFiction_status();

        DBObject queryObject = new BasicDBObject();

        if (!StringUtils.isEmpty(fiction_name)) {
            queryObject.put("fiction_name", fiction_name);
        }
        if (!StringUtils.isEmpty(fiction_author_name)) {
            queryObject.put("fiction_author_name", fiction_author_name);
        }
        if (fiction_status < 999) {
            queryObject.put("fiction_status", fiction_status);
        }

        List<FictionBean> fictionBeanList = mongoTemplate.find(new BasicQuery(queryObject).with(new Sort(new Sort.Order(Sort.Direction.DESC, "update_time"))), FictionBean.class);

        ModelAndView modelAndView = new ModelAndView("getHotFictionList");
        modelAndView.addObject("fictionBeanList", fictionBeanList);
        modelAndView.addObject("fictionBean", fictionBean);
        return modelAndView;
    }

    @RequestMapping("/editfictionall")
    public ModelAndView editFictionAll(){
        ModelAndView modelAndView = new ModelAndView("editFictionAll");
        return modelAndView;
    }

}
