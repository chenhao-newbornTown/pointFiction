package com.point.controller;

import com.google.gson.Gson;
import com.point.config.SaveMongoEventListener;
import com.point.constant.Constant;
import com.point.entity.FictionActorBean;
import com.point.entity.FictionBean;
import com.point.entity.FictionDetailBean;
import com.point.entity.FictionInfoBean;
import com.point.mongo.FictionRepository;
import com.point.service.FictionService;
import com.point.util.PublicUtil;
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
public class FictionController extends BaseController {
    protected static Logger logger = LoggerFactory.getLogger(FictionController.class);

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

     *  @RequestMapping("/getfictionlist")
     *   @ResponseBody public String getFictionList() throws Exception {

    List<Long> fiction_daily_Set = null;

    String all_key = "fiction_idlist_all";

    if (fictionService.redisFictionListExists(all_key)) {//查询redis中是否存在所有小说的set集合
    fiction_daily_Set = fictionService.getAllFictionIdListFromReidsByKey(all_key);
    } else {
    fiction_daily_Set = fictionService.insertAllFictionIdListToRedis(all_key);
    }
    return gson.toJson(fiction_daily_Set);
    }
     */

    /**
     * 根据fiction_id从redis中获取小说的作者，名称，图片连接
     *
     * @param request
     * @return

     * @RequestMapping("/getfictioninfo")
     * @ResponseBody public String getFictionInfoByFictionid(HttpServletRequest request) {

    String fiction_id = request.getParameter("fiction_id");

    String key = "fiction_info_all";

    FictionBean fictionBean = fictionService.getFictionInfoByFictionidFromRedis(key, fiction_id);

    return gson.toJson(fictionBean);
    }
     */
    /**
     * 获取小说的readcount
     * 未放入redis
     *
     * @param request
     * @return

     * @RequestMapping("/getreadcount") public String getreadCountByFictionid(HttpServletRequest request) {

     String fiction_id = request.getParameter("fiction_id");

     long read_count = fictionService.getReadAndLikeCountByFictionidFromMongo(fiction_id, "read_count");

     return String.valueOf(read_count);
     }*/

    /**
     * 从redis中获取小说的likecount
     *
     * @param request
     * @return
     */
    @RequestMapping("/getlikecount")
    public String getlikeCountByFictionid(HttpServletRequest request) {

        String fiction_id = request.getParameter("fiction_id");

        //long like_count = fictionService.getReadAndLikeCountByFictionidFromMongo(fiction_id, "like_count");

        long like_count = fictionService.getLikeCountFromRedis(fiction_id);

        return returnJsonData(Constant.DataDefault, like_count, "");
    }

    /**
     * 小说点赞数+1
     *
     * @param request
     */
    @RequestMapping("inclikecount")
    @ResponseBody
    public String incLikeCountByFictionid(HttpServletRequest request) {

        String fiction_id = request.getParameter("fiction_id");
        String uid = request.getParameter("uid");

        try {
            fictionService.updateFictionUserLikeCount(fiction_id);

            fictionService.insertUserLikeCount(uid, fiction_id);

            return returnJsonData(Constant.DataDefault, "", Constant.IncLikeCountSuccessed);

        } catch (Exception e) {
            return returnJsonData(Constant.DataError, "", Constant.IncLikeCountFailed);
        }
    }


    /**
     * 更新所有的小说入redis
     */
    @RequestMapping("/uploadredis")
    public void flushRedisAndUpdateData() {

        String key = "fiction_";

        fictionService.deleteRedisBykey(key);

        List<Long> fiction_id_List = fictionService.insertAllFictionIdListToRedis("fiction_idlist_all");

        //小说具体内容存储到redis
        fictionService.insertFictionListToRedis(key + "info_deatil_", "20", fiction_id_List);

        fictionService.getMongoPicToRedis(key + "pics");

        fictionService.getMongoSensitiveWordsToRedis(key + "sensitivewords");


    }

    /**
     * 拉取用户创建小说时待选的图片id
     *
     * @return
     */
    @RequestMapping("/getpiclist")
    @ResponseBody
    public String getPicListFromRedis() {

        List<String> picList = fictionService.getPicListFromRedis("fiction_pics");

        return returnJsonData(Constant.DataDefault, picList, "");
    }

    /**
     * 拉取小说内容的敏感词
     *
     * @return
     */
    @RequestMapping("/getmongosensitivewords")
    @ResponseBody
    public String getMongoSensitiveWords() {
        List<String> sensitiveWordsList = fictionService.getMongoSensitiveWordsFromRedis("fiction_sensitivewords");

        return returnJsonData(Constant.DataDefault, sensitiveWordsList, "");
    }


}
