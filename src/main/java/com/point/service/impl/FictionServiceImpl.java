package com.point.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.point.entity.FictionBean;
import com.point.entity.FictionDetailBean;
import com.point.entity.FictionInfoBean;
import com.point.mongo.FictionRepository;
import com.point.redis.FictionRedis;
import com.point.service.FictionService;
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
    public void insertFictionListToRedis(String key, String update_time, String page_fiction_num, List<Long> fiction_id_List) {

        int fiction_page_num = Integer.parseInt(page_fiction_num);

        for (Long fiction_id : fiction_id_List) {

            List<FictionDetailBean> fictionBeanList = getFictionDeatilListFromMongoByKey(update_time, fiction_id);

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
     * @param update_time
     * @return
     */
    public List<FictionDetailBean> getFictionDeatilListFromMongoByKey(String update_time, Long fiction_id) {

        Query query = new Query(Criteria.where("fiction_detail_status").is(1).and("fiction_id").is(fiction_id)).with(new Sort(new Sort.Order(Sort.Direction.ASC, "actor_fiction_detail_index")));

        List<FictionDetailBean> fictionBeanList = mongoTemplate.find(query, FictionDetailBean.class);

        return fictionBeanList;
    }

    @Override
    public List<Long> getAllFictionIdListFromReidsByKey(String key) {

        List<Long> fiction_daily_Set = fictionRedis.getAllFictionIdListFromReidsByKey(key);

        return fiction_daily_Set;
    }

    @Override
    public List<Long> insertAllFictionIdListToRedis(String key) {

        List<FictionBean> fictionBeanList = mongoTemplate.find(new Query(Criteria.where("fiction_status").is(2)).with(new Sort(new Sort.Order(Sort.Direction.DESC, "update_time"))), FictionBean.class);

        List<Long> fiction_id_List = new ArrayList<Long>();

        Map<String, FictionInfoBean> fictionid_Maps = new HashMap<String, FictionInfoBean>();

        for (FictionBean fictionBean : fictionBeanList) {

            fiction_id_List.add(fictionBean.getFiction_id());

            FictionInfoBean fictionInfoBean = new FictionInfoBean();

            fictionInfoBean.setFiction_id(fictionBean.getFiction_id());
            fictionInfoBean.setFiction_name(fictionBean.getFiction_name());
            fictionInfoBean.setFiction_author_name(fictionBean.getFiction_author_name());
            fictionInfoBean.setFiction_pic_path(fictionBean.getFiction_pic_path());

            fictionid_Maps.put(String.valueOf(fictionBean.getFiction_id()), fictionInfoBean);
        }


        fictionRedis.insertAllFictionIdSetToRedis(key, fiction_id_List, fictionid_Maps);

        return fiction_id_List;
    }


    public void deleteRedisBykey(String key) {

        fictionRedis.deleteRedisBykey(key);

    }

    @Override
    public void updateFictionUserReadCount(String fiction_id) {
        mongoTemplate.updateFirst(new Query(Criteria.where("fiction_id").is(Long.parseLong(fiction_id))), new Update().inc("read_count", 1), FictionBean.class);

    }

    public void updateFictionUserLikeCount(String fiction_id) {
        mongoTemplate.updateFirst(new Query(Criteria.where("fiction_id").is(Long.parseLong(fiction_id))), new Update().inc("like_count", 1), FictionBean.class);

    }

    @Override
    public FictionInfoBean getFictionInfoByFictionidFromRedis(String key, String fiction_id) {

        return fictionRedis.getFictionInfoByFictionidFromRedis(key, fiction_id);

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


    public boolean releaseFiction(String fiction_id,String timestamp) {
        try {
            mongoTemplate.updateFirst(new Query(Criteria.where("fiction_id").is(Long.parseLong(fiction_id)).and("fiction_status").is(0)), Update.update("fiction_status", 1).set("update_time",timestamp), FictionBean.class);
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
        fields.put("fiction_pic_path", "true");

        FictionBean fictionBean = mongoTemplate.findOne(new BasicQuery(query, fields), FictionBean.class);

        String fiction_pic_path = fictionBean.getFiction_pic_path();

        return fiction_pic_path;

    }

    public void incrFictionLineNum(String fiction_id, int i) {
        try {
            mongoTemplate.upsert(new Query(Criteria.where("fiction_id").is(Long.parseLong(fiction_id))), new Update().inc("fiction_line_num", i), FictionBean.class);
        } catch (Exception e) {
            logger.error("incrFictionLineNum is error,fiction_id={}", fiction_id);
        }
    }


}
