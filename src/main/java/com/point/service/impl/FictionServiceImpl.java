package com.point.service.impl;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.point.entity.*;
import com.point.mongo.FictionRepository;
import com.point.redis.FictionRedis;
import com.point.service.FictionService;
import javafx.scene.shape.Circle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.xml.soap.SAAJMetaFactory;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by hadoop on 2017-7-19.
 */
@Service
public class FictionServiceImpl implements FictionService {

    protected static Logger logger = LoggerFactory.getLogger(FictionServiceImpl.class);

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    FictionRedis fictionRedis;

    @Autowired
    FictionRepository fictionRepository;


    @Override
    public boolean redisFictionListExists(String key) {
        return fictionRedis.redisFictionListExists(key);
    }

    @Override
    public void insertFictionListToRedis(String key, String page_fiction_num, List<Long> fiction_id_List) {

        int fiction_page_num = Integer.parseInt(page_fiction_num);

        for (Long fiction_id : fiction_id_List) {

            insertFictionDetailToRedis(fiction_id, fiction_page_num, key);
        }
    }


    public void insertFictionDetailToRedis(Long fiction_id, int fiction_page_num, String key) {
        List<FictionDetailBean> fictionBeanList = getFictionDeatilListFromMongoByKey(fiction_id);

        if (null != fictionBeanList && fictionBeanList.size() > 0) {
            Map<String, List<FictionDetailBean>> map = new HashMap<String, List<FictionDetailBean>>();

            List<FictionDetailBean> fictionDeatilBeanList = null;

            for (int i = 0; i < fictionBeanList.size(); i++) {

                FictionDetailBean fictionDetailBean = fictionBeanList.get(i);

                String mapkey = String.valueOf((i / fiction_page_num) + 1) + "_" + fictionDetailBean.getFiction_id();

                if (map.containsKey(mapkey)) {
                    fictionDeatilBeanList = map.get(mapkey);
                } else {
                    fictionDeatilBeanList = new ArrayList<FictionDetailBean>();
                }
                fictionDeatilBeanList.add(fictionDetailBean);
                map.put(mapkey, fictionDeatilBeanList);
            }

            fictionRedis.insertFictionListToRedis(key, map);
        }
    }

    @Override
    public List<FictionBean> getFictionListFromReidsByKey(String key, String page_num) {

        return fictionRedis.getFictionListFromReidsByKey(key, page_num);
    }

    @Override
    public List<FictionBean> getFictionListFromMongoByKey(String updatetime) {
        return null;
    }

    /**
     * 获取已加入推荐池部分的小说
     *
     * @return
     */
    public List<FictionDetailBean> getFictionDeatilListFromMongoByKey(Long fiction_id) {

        Query query = new Query(Criteria.where("fiction_detail_status").is(1).and("fiction_id").is(fiction_id)).with(new Sort(new Sort.Order(Sort.Direction.ASC, "actor_fiction_detail_index")));

        List<FictionDetailBean> fictionBeanList = mongoTemplate.find(query, FictionDetailBean.class);

        return fictionBeanList;
    }

    @Override
    public List<Long> getAllFictionIdListFromReidsByKey(String key) {

        List<Long> fiction_daily_Set = fictionRedis.getAllFictionIdListFromReidsByKey(key);

        return fiction_daily_Set;
    }

    /**
     * 获取加入推荐池子的小说，并存储到redis中
     *
     * @param key
     * @return
     */
    @Override
    public List<Long> insertAllFictionIdListToRedis(String key) {

        List<FictionBean> fictionBeanList = mongoTemplate.find(new Query(Criteria.where("fiction_status").is(2)).with(new Sort(new Sort.Order(Sort.Direction.DESC, "update_time"))), FictionBean.class);

        List<Long> fiction_id_List = new ArrayList<Long>();


        for (FictionBean fictionBean : fictionBeanList) {

            fiction_id_List.add(fictionBean.getFiction_id());

        }

        fictionRedis.insertAllFictionIdSetToRedis(key, fiction_id_List, null);

        return fiction_id_List;
    }

    public void setFictionInfoAll(){
        List<FictionBean> fictionBeanList = mongoTemplate.find(new Query(Criteria.where("fiction_status").is(2)).with(new Sort(new Sort.Order(Sort.Direction.DESC, "update_time"))), FictionBean.class);

        Map<String, FictionBean> fictionid_Maps = new HashMap<String, FictionBean>();

        for (FictionBean fictionBean : fictionBeanList) {

            fictionid_Maps.put(String.valueOf(fictionBean.getFiction_id()), fictionBean);
        }

        fictionRedis.insertAllFictionIdSetToRedis(null, null, fictionid_Maps);



    }



    public void updateAllFictionIdListToRedis(String key) {
        List<FictionBean> fictionBeanList = mongoTemplate.find(new Query(Criteria.where("fiction_status").is(2)).with(new Sort(new Sort.Order(Sort.Direction.DESC, "update_time"))), FictionBean.class);

        List<Long> fiction_id_List = new ArrayList<Long>();
        for (FictionBean fictionBean : fictionBeanList) {

            fiction_id_List.add(fictionBean.getFiction_id());
        }

        fictionRedis.updateAllFictionIdListToRedis(key, fiction_id_List);

    }


    public void deleteRedisBykey(String key) {

        fictionRedis.deleteRedisBykey(key);

    }

    @Override
    public void updateFictionUserReadCount(String fiction_id) {

        long read_count_int = new Random().nextInt(5);
        mongoTemplate.updateFirst(new Query(Criteria.where("fiction_id").is(Long.parseLong(fiction_id))), new Update().inc("read_count", read_count_int), FictionBean.class);

        fictionRedis.incReadCount(fiction_id,read_count_int);


    }

    public void updateFictionUserLikeCount(String fiction_id) {

        long like_count_int = new Random(5).nextInt(5);

        mongoTemplate.updateFirst(new Query(Criteria.where("fiction_id").is(Long.parseLong(fiction_id))), new Update().inc("like_count", like_count_int), FictionBean.class);

        fictionRedis.incLikeCount(fiction_id,like_count_int);

    }

    public Long getLikeCountFromRedis(String fiction_id) {

        return fictionRedis.getLikeCount(fiction_id);

    }

    @Override
    public FictionBean getFictionInfoByFictionidFromRedis(String key, String fiction_id) {

        return fictionRedis.getFictionInfoByFictionidFromRedis(key, fiction_id);
    }


    public List<FictionBean> getFictionInfoByFictionidFromMongo(List<UserFictionBean> userFictionBeanList) {

        BasicDBList values = new BasicDBList();

        for (UserFictionBean userFictionBean : userFictionBeanList) {
            values.add(new BasicDBObject("fiction_id", userFictionBean.getFiction_id()));
        }
        DBObject queryObject = new BasicDBObject("$or", values);

        List<FictionBean> fictionBeanList = mongoTemplate.find(new BasicQuery(queryObject), FictionBean.class);

        return fictionBeanList;
    }

    public long getReadAndLikeCountByFictionidFromMongo(String fiction_id, String readOrLike) {

        DBObject queryObject = new BasicDBObject();
        queryObject.put("fiction_id", Long.parseLong(fiction_id));

        DBObject fields = new BasicDBObject();
        fields.put("_id", false);
        fields.put(readOrLike, true);

        FictionBean fictionBean = mongoTemplate.findOne(new BasicQuery(queryObject, fields), FictionBean.class);

        long count = 0L;

        if (readOrLike.equals("read_count")) {
            count = fictionBean.getRead_count();
        } else if (readOrLike.equals("like_count")) {
            count = fictionBean.getLike_count();
        }
        return count;
    }

    public FictionBean saveFiction(FictionBean fictionBean) {

        FictionBean fictionMongoBean = fictionRepository.insert(fictionBean);

        return fictionMongoBean;

    }


    public boolean releaseFiction(String fiction_id, String timestamp) {
        try {
            mongoTemplate.updateFirst(new Query(Criteria.where("fiction_id").is(Long.parseLong(fiction_id)).and("fiction_status").is(0)), Update.update("fiction_status", 1).set("update_time", timestamp), FictionBean.class);


            mongoTemplate.updateFirst(new Query(Criteria.where("fiction_id").is(Long.parseLong(fiction_id))), Update.update("update_time", timestamp).set("update_date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())), FictionBean.class);

            return true;
        } catch (Exception e) {
            logger.error("releaseFiction is error,fiction_id={}", fiction_id);
            return false;
        }


    }

    public String getFictionPicPathByid(long fiction_id) {

        DBObject query = new BasicDBObject();
        query.put("fiction_id", fiction_id);

        DBObject fields = new BasicDBObject();

        fields.put("_id", false);
        fields.put("fiction_pic_path", true);

        FictionBean fictionBean = mongoTemplate.findOne(new BasicQuery(query, fields), FictionBean.class);

        String fiction_pic_path = fictionBean.getFiction_pic_path();

        return fiction_pic_path;

    }

    public boolean getFictionStatus(String fiction_id) {

        DBObject query = new BasicDBObject();
        query.put("fiction_id", Long.parseLong(fiction_id));

        DBObject fields = new BasicDBObject();

        fields.put("_id", false);
        fields.put("fiction_status", true);

        FictionBean fictionBean = mongoTemplate.findOne(new BasicQuery(query, fields), FictionBean.class);


        if (null != fictionBean && fictionBean.getFiction_status() == 2) {
            return true;
        }
        return false;


    }


    public void incrFictionLineNum(String fiction_id, int i) {
        try {

            if (i > 0) {
                mongoTemplate.upsert(new Query(Criteria.where("fiction_id").is(Long.parseLong(fiction_id))), new Update().inc("fiction_original_num", i).inc("fiction_line_num", 1).set("update_time",String.valueOf(System.currentTimeMillis())).set("update_date",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())), FictionBean.class);
            } else {
                mongoTemplate.upsert(new Query(Criteria.where("fiction_id").is(Long.parseLong(fiction_id))), new Update().inc("fiction_original_num", i).set("update_time",String.valueOf(System.currentTimeMillis())).set("update_date",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())), FictionBean.class);
            }


        } catch (Exception e) {
            logger.error("incrFictionLineNum is error,fiction_id={}", fiction_id);
        }
    }

    public void getMongoPicToRedis(String key) {

        List<PicBean> picBeanList = mongoTemplate.find(new Query(Criteria.where("pic_status").is("2")).with(new Sort(new Sort.Order(Sort.Direction.DESC, "pic_upload_time"))), PicBean.class);

        if (null != picBeanList && picBeanList.size() > 0) {

            fictionRedis.insertPicToRedis(key, picBeanList);
        }
    }

    public List<String> getPicListFromRedis(String key) {

        List<String> picList = fictionRedis.getPicListFromRedis(key);

        return picList;
    }

    public void getMongoSensitiveWordsToRedis(String key) {

        DBObject fields = new BasicDBObject();

        fields.put("_id", false);
        fields.put("words", true);

        SensitiveWordsBean sensitiveWordsBean = mongoTemplate.findOne(new BasicQuery(new BasicDBObject(), fields), SensitiveWordsBean.class);

        if (null != sensitiveWordsBean) {
            fictionRedis.insertSensitiveWordsToRedis(key, sensitiveWordsBean.getWords());
        }


    }

    public List<String> getMongoSensitiveWordsFromRedis(String key) {

        List<String> sensitiveWordsList = fictionRedis.getSensitiveWordsFromRedis(key);

        return sensitiveWordsList;
    }

    public void insertUserLikeCount(String uid, String fiction_id) {

        mongoTemplate.upsert(new Query(Criteria.where("uid").is(Long.parseLong(uid)).and("fiction_id").is(Long.parseLong(fiction_id))), Update.update("user_like_count", "1"), UserFictionBean.class);

    }

    public String getUserLikeCountStatus(String uid, String fiction_id) {

        UserFictionBean userFictionBean = mongoTemplate.findOne(new Query(Criteria.where("uid").is(Long.parseLong(uid)).and("fiction_id").is(Long.parseLong(fiction_id))), UserFictionBean.class);

        if (null == userFictionBean) {
            return "0";
        } else {
            return userFictionBean.getUser_like_count();
        }
    }

    public String getReadUserReadPageNum(String key, String user_read_line) {

        TreeSet<Long> page_list = fictionRedis.getFictionPageInfo(key);

        int i = 0;

        if (page_list.size() > 0) {
            for (Long page_info_num : page_list) {

                if (Long.parseLong(user_read_line) <= page_info_num) {
                    break;
                }

                i++;
            }
        }

        int page_num = i+1;

        page_num = page_num>page_list.size()?page_list.size():page_num;

        return String.valueOf(page_num);
    }
}
