package com.point.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.point.constant.Constant;
import com.point.entity.*;
import com.point.mongo.FictionRepository;
import com.point.mongo.PicRepostitory;
import com.point.redis.FictionRedis;
import com.point.util.PublicUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
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

    static String BaseUrl = Constant.BaseUrl;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    FictionRepository fictionRepository;

    @Autowired
    PicRepostitory picRepostitory;

    @RequestMapping("/admin_login")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");
        return modelAndView;

    }

    @RequestMapping("/main")
    public ModelAndView main(@RequestParam("username") String username, @RequestParam("userPassword") String pwd) {

        ModelAndView modelAndView = null;

        if (username.equals("admin") && pwd.equals("x1jsZ!&BI&t#Ew13")) {
            modelAndView = new ModelAndView("main");
            modelAndView.addObject("username", username);
        } else {
            modelAndView = new ModelAndView("index");
            modelAndView.addObject("msg", "用户名或密码错误!!!");
        }
        return modelAndView;

    }

    @RequestMapping("/menu")
    public ModelAndView menu() {

        ModelAndView modelAndView = new ModelAndView("menu");
        return modelAndView;

    }

    @RequestMapping("/hotfictionlist")
    public ModelAndView getHotFictionList() {

        List<FictionBean> fictionBeanList = mongoTemplate.find(new Query(Criteria.where("status").ne(Constant.FictionStatusDeatilError)).with(new Sort(new Sort.Order(Sort.Direction.DESC, "update_time"))), FictionBean.class);
        //List<FictionBean> fictionBeanList = mongoTemplate.find(new Query(Criteria.where("status").ne(Constant.FictionStatusDeatilError)).with(new Sort(new Sort.Order(Sort.Direction.DESC, "update_time"))), FictionBean.class);

        ModelAndView modelAndView = new ModelAndView("getHotFictionList");
        modelAndView.addObject("fictionBeanList", fictionBeanList);
        modelAndView.addObject("baseurl", BaseUrl);
        modelAndView.addObject("uid", "888888");
        modelAndView.addObject("fictionBeanListSize",fictionBeanList.size());
        return modelAndView;
    }

    /**
     * 查询小说
     *
     * @param fictionBean
     * @return
     */
    @RequestMapping("/searchhotfictionlist")
    public ModelAndView searchHotFictionList(FictionBean fictionBean,HttpServletRequest request) {

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


        queryObject.put("status",new BasicDBObject("$ne",Constant.FictionStatusDeatilError));

        List<FictionBean> fictionBeanList = mongoTemplate.find(new BasicQuery(queryObject).with(new Sort(new Sort.Order(Sort.Direction.DESC, "update_time"))), FictionBean.class);

        ModelAndView modelAndView = new ModelAndView("getHotFictionList");
        modelAndView.addObject("fictionBeanList", fictionBeanList);
        modelAndView.addObject("fictionBean", fictionBean);
        modelAndView.addObject("baseurl", BaseUrl);
        modelAndView.addObject("uid", "888888");
        modelAndView.addObject("fictionBeanListSize",request.getParameter("fictionBeanListSize"));
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
        queryObject.put("status",new BasicDBObject("$ne",Constant.FictionStatusDeatilError));
        List<FictionBean> fictionBeanList = mongoTemplate.find(new BasicQuery(queryObject).with(new Sort(new Sort.Order(Sort.Direction.DESC, "update_time"))), FictionBean.class);

        ModelAndView modelAndView = new ModelAndView("getHotFictionList");
        modelAndView.addObject("fictionBeanList", fictionBeanList);
        modelAndView.addObject("fictionBean", fictionBean);
        modelAndView.addObject("uid", "888888");
        modelAndView.addObject("baseurl", BaseUrl);
        return modelAndView;
    }

    @RequestMapping("/uploadpic")
    public ModelAndView uploadPic() {
        List<PicBean> picBeanMongoList = mongoTemplate.find(new Query(Criteria.where("pic_status").is("2")).with(new Sort(new Sort.Order(Sort.Direction.DESC, "pic_upload_time"))), PicBean.class);

        ModelAndView modelAndView = new ModelAndView("uploadPic");

        modelAndView.addObject("baseurl", BaseUrl);
        modelAndView.addObject("picBeanMongoList", picBeanMongoList);
        return modelAndView;
    }

    /**
     * 上传图片
     * @param request
     * @return
     */
    @RequestMapping(value = "/batchupload")
    public ModelAndView handleFileUpload(HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        MultipartFile file = null;

        for (int i = 0; i < files.size(); ++i) {
            file = files.get(i);
            if (!file.isEmpty()) {
                PicBean picBean = PublicUtil.uploadPic(file);
                if (null != picBean) {
                    picBean.setPic_status("2");
                    mongoTemplate.insert(picBean);
                }
            }
        }
        List<PicBean> picBeanMongoList = mongoTemplate.find(new Query(Criteria.where("pic_status").is("2")).with(new Sort(new Sort.Order(Sort.Direction.DESC, "pic_upload_time"))), PicBean.class);

        ModelAndView modelAndView = new ModelAndView("uploadPic");

        modelAndView.addObject("picBeanMongoList", picBeanMongoList);
        modelAndView.addObject("baseurl", BaseUrl);

        return modelAndView;
    }


    @RequestMapping("/uploadpicandfiction")
    public ModelAndView fileAndFictionUpload(HttpServletRequest request) {

        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");

        MultipartFile file = null;
        BufferedOutputStream stream = null;

        String fiction_pic_path = null;

        boolean uploadpic = true;

        for (int i = 0; i < files.size(); ++i) {
            file = files.get(i);

            if (!file.isEmpty()) {

                String filetype = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
                if (!filetype.equals("xlsx")) {
                    PicBean picBean = PublicUtil.uploadPic(file);
                    if (null != picBean && uploadpic) {
                        picBean.setPic_status("1");
                        picBean.setUse_pic_num(1);
                        picBean.setUid(Long.parseLong("888888"));
                        PicBean picMongoBean = picRepostitory.insert(picBean);
                        fiction_pic_path = picMongoBean.getPic_name();
                    }
                } else {
                    if (null == fiction_pic_path && !filetype.equals("xlsx")) {
                        PicBean picBean = PublicUtil.uploadPic(file);
                        if (null != picBean && uploadpic) {
                            picBean.setPic_status("1");
                            picBean.setUse_pic_num(1);
                            picBean.setUid(Long.parseLong("888888"));
                            PicBean picMongoBean = picRepostitory.insert(picBean);
                            fiction_pic_path = picMongoBean.getPic_name();
                            uploadpic = false;
                        }
                    }

                    new PublicUtil().uploadXlsx(file, request.getParameter("fiction_name"), request.getParameter("fiction_author_name"), fiction_pic_path, fictionRepository, mongoTemplate, "add", "");

                }
            }
        }

        ModelAndView modelAndView = new ModelAndView("editFictionAll");
        modelAndView.addObject("msg", "上传成功!!!");
        modelAndView.addObject("addOrUpdate","add");

        return modelAndView;
    }

    /**
     * 更新小说的图片或者内容
     * @param request
     * @return
     */
    @RequestMapping("/updatepicandfiction")
    public ModelAndView fileAndFictionUpdate(HttpServletRequest request) {

        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");

        String fiction_id = request.getParameter("fiction_id");

        MultipartFile file = null;
        BufferedOutputStream stream = null;

        String fiction_pic_path = null;

        boolean uploadpic = true;

        for (int i = 0; i < files.size(); ++i) {
            file = files.get(i);

            if (!file.isEmpty()) {
                String filetype = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
                if (!filetype.equals("xlsx")) {
                    PicBean picBean = PublicUtil.uploadPic(file);
                    if (null != picBean && uploadpic) {
                        picBean.setPic_status("1");
                        picBean.setUse_pic_num(1);
                        picBean.setUid(Long.parseLong("888888"));
                        PicBean picMongoBean = picRepostitory.insert(picBean);
                        fiction_pic_path = picMongoBean.getPic_name();

                        mongoTemplate.updateFirst(new Query(Criteria.where("fiction_id").is(Long.parseLong(fiction_id))), Update.update("fiction_pic_path", fiction_pic_path), FictionBean.class);

                    }
                } else {
                    if (null == fiction_pic_path && !filetype.equals("xlsx")) {
                        PicBean picBean = PublicUtil.uploadPic(file);
                        if (null != picBean && uploadpic) {
                            picBean.setPic_status("1");
                            picBean.setUse_pic_num(1);
                            picBean.setUid(Long.parseLong("888888"));
                            PicBean picMongoBean = picRepostitory.insert(picBean);
                            fiction_pic_path = picMongoBean.getPic_name();
                            uploadpic = false;
                        }
                    }
                    if (!StringUtils.isEmpty(fiction_pic_path)) {
                        mongoTemplate.updateFirst(new Query(Criteria.where("fiction_id").is(Long.parseLong(fiction_id))), Update.update("fiction_pic_path", fiction_pic_path), FictionBean.class);
                    }

                    mongoTemplate.remove(new Query(Criteria.where("fiction_id").is(Long.parseLong(fiction_id))), FictionDetailBean.class);
                    mongoTemplate.remove(new Query(Criteria.where("fiction_id").is(Long.parseLong(fiction_id))), FictionActorBean.class);

                    new PublicUtil().uploadXlsx(file, request.getParameter("fiction_name"), request.getParameter("fiction_author_name"), fiction_pic_path, fictionRepository, mongoTemplate, "update", fiction_id);
                }
            }
        }
        mongoTemplate.updateFirst(new Query(Criteria.where("fiction_id").is(Long.parseLong(fiction_id))), Update.update("fiction_name", request.getParameter("fiction_name")).set("fiction_author_name", request.getParameter("fiction_author_name")), FictionBean.class);

        ModelAndView modelAndView = new ModelAndView("editFictionAll");
        modelAndView.addObject("msg", "更新成功!!!");
        modelAndView.addObject("baseurl", BaseUrl);
        return modelAndView;
    }


    @RequestMapping("/delpic")
    public ModelAndView delPic(@RequestParam("id") String id) {

        mongoTemplate.remove(new Query(Criteria.where("id").is(new ObjectId(id))), PicBean.class);

        List<PicBean> picBeanMongoList = mongoTemplate.find(new Query().with(new Sort(new Sort.Order(Sort.Direction.DESC, "pic_upload_time"))), PicBean.class);

        ModelAndView modelAndView = new ModelAndView("uploadPic");

        modelAndView.addObject("picBeanMongoList", picBeanMongoList);
        modelAndView.addObject("baseurl", BaseUrl);

        return modelAndView;
    }

    @RequestMapping("/delfiction")
    public ModelAndView delFiction(FictionBean fictionBean, @RequestParam("id") String id, @RequestParam("fiction_id") long fiction_id, @RequestParam("fiction_pic_path") String fiction_pic_path, @RequestParam("type") String type, @RequestParam("del_status") boolean del_status) {

        if (type.equals("fiction")&&!del_status) {//小说作者和admin的id不同
            mongoTemplate.updateFirst(new Query(Criteria.where("id").is(new ObjectId(id))), Update.update("status", Constant.FictionStatusDeatilError).set("fiction_pic_path", "default.png").set("fiction_status", 0), FictionBean.class);
            mongoTemplate.remove(new Query(Criteria.where("fiction_id").is(fiction_id)), FictionDetailBean.class);//删除小说内容
            mongoTemplate.remove(new Query(Criteria.where("fiction_id").is(fiction_id)), FictionActorBean.class);//删除小说角色
           // mongoTemplate.remove(new Query(Criteria.where("pic_name").is(fiction_pic_path)), PicBean.class);//删除小说图片
        } else if (type.equals("pic")) {
            mongoTemplate.updateFirst(new Query(Criteria.where("id").is(new ObjectId(id))), Update.update("status", Constant.FictionStatusPicError).set("fiction_pic_path", "default.png").set("fiction_status", 0), FictionBean.class);
           // mongoTemplate.remove(new Query(Criteria.where("pic_name").is(fiction_pic_path)), PicBean.class);//删除小说图片
        }else if(type.equals("fiction")&&del_status){//小说作者和admin的id相同
            mongoTemplate.remove(new Query(Criteria.where("id").is(new ObjectId(id))),FictionBean.class);
            mongoTemplate.remove(new Query(Criteria.where("fiction_id").is(fiction_id)), FictionDetailBean.class);//删除小说内容
            mongoTemplate.remove(new Query(Criteria.where("fiction_id").is(fiction_id)), FictionActorBean.class);//删除小说角色
        }


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

        queryObject.put("status",new BasicDBObject("$ne",Constant.FictionStatusDeatilError));
        List<FictionBean> fictionBeanList = mongoTemplate.find(new BasicQuery(queryObject).with(new Sort(new Sort.Order(Sort.Direction.DESC, "update_time"))), FictionBean.class);

        ModelAndView modelAndView = new ModelAndView("getHotFictionList");
        modelAndView.addObject("fictionBeanList", fictionBeanList);
        modelAndView.addObject("fictionBean", fictionBean);
        modelAndView.addObject("uid", "888888");
        modelAndView.addObject("baseurl", BaseUrl);
        return modelAndView;
    }

    /**
     * 上传小说页面
     *
     * @return
     */
    @RequestMapping("/editfictionall")
    public ModelAndView editFictionAll() {
        ModelAndView modelAndView = new ModelAndView("editFictionAll");
        modelAndView.addObject("addOrUpdate", "add");
        modelAndView.addObject("baseurl", BaseUrl);
        return modelAndView;
    }

    @RequestMapping("/updatefictiondetail")
    public ModelAndView updateFictionDetail(@RequestParam("id") String id, @RequestParam("fiction_id") long fiction_id, @RequestParam("fiction_pic_path") String fiction_pic_path, @RequestParam("fiction_author_name") String fiction_author_name, @RequestParam("fiction_name") String fiction_name) {

        ModelAndView modelAndView = new ModelAndView("editFictionAll");
        FictionBean fictionBean = new FictionBean();

        fictionBean.setId(id);
        fictionBean.setFiction_id(fiction_id);
        fictionBean.setFiction_pic_path(fiction_pic_path);
        fictionBean.setFiction_author_name(fiction_author_name);
        fictionBean.setFiction_name(fiction_name);

        modelAndView.addObject("fictionBean", fictionBean);
        modelAndView.addObject("addOrUpdate", "update");
        modelAndView.addObject("baseurl", BaseUrl);

        return modelAndView;
    }

    /**
     * 上传敏感词
     *
     * @param request
     * @return
     */
    @RequestMapping("/uploadwords")
    public ModelAndView uploadWords(HttpServletRequest request) {

        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        SensitiveWordsBean sensitiveWordsBean = new SensitiveWordsBean();

        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");

        MultipartFile file = files.get(0);

        File uploadDir = new File(Constant.PicPath);
        //创建一个目录 （它的路径名由当前 File 对象指定，包括任一必须的父路径。）
        if (!uploadDir.exists()) uploadDir.mkdirs();
        //新建一个文件
        File tempFile = new File(Constant.PicPath + File.separator + file.getOriginalFilename());
        //初始化输入流
        InputStream is = null;
        try {
            //将上传的文件写入新建的文件中
            file.transferTo(tempFile);

            //根据新建的文件实例化输入流
            is = new FileInputStream(tempFile);

            //根据版本选择创建Workbook的方式
            Workbook wb = null;
            //根据文件名判断文件是2003版本还是2007版本
            wb = new XSSFWorkbook(is);
            Sheet sheet = wb.getSheetAt(0);

            List<String> wordList = new ArrayList<String>();

            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                Row row = sheet.getRow(i);
                wordList.add(row.getCell(0).getStringCellValue());
            }

            sensitiveWordsBean.setUpdate_time(date);
            sensitiveWordsBean.setWords(wordList);
            mongoTemplate.dropCollection(SensitiveWordsBean.class);
            mongoTemplate.save(sensitiveWordsBean);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    is = null;
                    e.printStackTrace();
                }
            }
        }

        ModelAndView modelAndView = new ModelAndView("uploadSensitiveWords");
        modelAndView.addObject("sensitivewordsbean", sensitiveWordsBean);
        return modelAndView;
    }

    @RequestMapping("/getwords")
    public ModelAndView uploadWords() {
        SensitiveWordsBean sensitiveWordsBean = mongoTemplate.findOne(new Query(), SensitiveWordsBean.class);

        ModelAndView modelAndView = new ModelAndView("uploadSensitiveWords");

        modelAndView.addObject("sensitivewordsbean", sensitiveWordsBean);
        return modelAndView;
    }

}
