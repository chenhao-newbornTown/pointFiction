package com.point.controller;

import com.google.gson.Gson;
import com.point.config.SaveMongoEventListener;
import com.point.entity.FictionBean;
import com.point.entity.FictionDetailBean;
import com.point.entity.FictionInfoBean;
import com.point.mongo.FictionRepository;
import com.point.service.FictionService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by hadoop on 2017-7-19.
 */

@RestController
@RequestMapping("/fiction")
public class FictionController {
    protected static Logger logger = LoggerFactory.getLogger(FictionController.class);

    String REDIS_KEY = "fiction_idlist_";

    Gson gson = new Gson();

    @Autowired
    FictionService fictionService;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    FictionRepository fictionRepository;

    /**
     * 获取每天的小说的fictionidList
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/getfictionlist")
    @ResponseBody
    public String getFictionList() throws Exception {

        List<Long> fiction_daily_Set = null;

        String all_key = REDIS_KEY + "all";

        if (fictionService.redisFictionListExists(all_key)) {//查询redis中是否存在所有小说的set集合
            fiction_daily_Set = fictionService.getAllFictionIdListFromReidsByKey(all_key);
        } else {
            fiction_daily_Set = fictionService.insertAllFictionIdListToRedis(all_key);
        }
        return gson.toJson(fiction_daily_Set);
    }


    /**
     * 根据fiction_id从redis中获取小说的作者，名称，图片连接
     *
     * @param request
     * @return
     */
    @RequestMapping("/getfictioninfo")
    @ResponseBody
    public String getFictionInfoByFictionid(HttpServletRequest request) {

        String fiction_id = request.getParameter("fiction_id");

        String key = "fiction_info_all";

        FictionInfoBean fictionInfoBean = fictionService.getFictionInfoByFictionidFromRedis(key, fiction_id);

        return gson.toJson(fictionInfoBean);
    }

    /**
     * 上传小说
     *
     * @param request
     * @throws Exception
     */
    @RequestMapping("/uploadfiction")
    public void uploadExcelFictionToMongoAndRedis(HttpServletRequest request) throws Exception {

        String xls_path = "F:\\fiction\\松看风云.xlsx";

        XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(ResourceUtils.getFile(xls_path)));

        XSSFSheet sheet = wb.getSheetAt(0);

        List<FictionDetailBean> fictionDetailBeanList = new ArrayList<FictionDetailBean>();

        List<String> actor_name_list = new ArrayList<String>();

        FictionBean fictionBean = new FictionBean();

        fictionBean.setFiction_name("松看风云");
        fictionBean.setFiction_author_id(request.getParameter("fiction_author_id"));
        fictionBean.setFiction_author_name(request.getParameter("fiction_author_name"));
        fictionBean.setFiction_line_num((long) sheet.getLastRowNum());
        fictionBean.setUpdate_time(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        fictionBean.setFiction_pic_path(request.getParameter("fiction_pic_path"));
        fictionBean.setFiction_status(1);

        FictionBean fictionMongoBean =  fictionRepository.save(fictionBean);

        long fiction_id = fictionMongoBean.getFiction_id();

        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {

            FictionDetailBean fictionDetailBean = new FictionDetailBean();

            XSSFRow row = sheet.getRow(i);
            String actor_name = row.getCell(0).getStringCellValue();
            String actor_fiction_detail = row.getCell(1).getStringCellValue();

            if (!actor_name_list.contains(actor_name)&&!actor_name.equals("旁白")) {
                actor_name_list.add(actor_name);
            }

            fictionDetailBean.setFiction_id(fiction_id);
            fictionDetailBean.setActor_fiction_detail(actor_fiction_detail);
            fictionDetailBean.setActor_name(actor_name);
            fictionDetailBean.setActor_fiction_detail_index(i);
            fictionDetailBean.setFiction_detail_status(1);

            fictionDetailBeanList.add(fictionDetailBean);
        }

        mongoTemplate.insert(fictionDetailBeanList, FictionDetailBean.class);
        mongoTemplate.upsert(new Query(Criteria.where("fiction_id").is(fiction_id)), Update.update("fiction_actors",actor_name_list),FictionBean.class);
    }


    /**
     * 获取小说的readcount
     * 未放入redis
     *
     * @param request
     * @return
     */
    @RequestMapping("/getreadcount")
    public String getreadCountByFictionid(HttpServletRequest request) {

        String fiction_id = request.getParameter("fiction_id");

        long read_count = fictionService.getReadAndLikeCountByFictionidFromMongo(fiction_id, "read_count");

        return String.valueOf(read_count);
    }

    /**
     * 获取小说的likecount
     * 未放入redis
     *
     * @param request
     * @return
     */
    @RequestMapping("/getlikecount")
    public String getlikeCountByFictionid(HttpServletRequest request) {

        String fiction_id = request.getParameter("fiction_id");

        long like_count = fictionService.getReadAndLikeCountByFictionidFromMongo(fiction_id, "like_count");

        return String.valueOf(like_count);
    }


    /**
     * 小说点赞数+1
     *
     * @param request
     */
    @RequestMapping("inclikecount")
    public void incLikeCountByFictionid(HttpServletRequest request) {

        String fiction_id = request.getParameter("fiction_id");

        fictionService.updateFictionUserLikeCount(fiction_id);
    }


    /**
     * 更新所有的小说入redis
     */
    @RequestMapping("/uploadredis")
    public void flushRedisAndUpdateData() {

        String key = "fiction_";

        fictionService.deleteRedisBykey(key);

        List<Long> fiction_id_List =  fictionService.insertAllFictionIdListToRedis(REDIS_KEY + "all");

        fictionService.insertFictionListToRedis(key + "info_deatil_", "20170728", "20",fiction_id_List);
    }


}
