package com.point.service.impl;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.point.entity.*;
import com.point.mongo.FictionDeatilRepository;
import com.point.mongo.PicRepostitory;
import com.point.mongo.UserFictionRepository;
import com.point.redis.UserFictionRedis;
import com.point.service.UserFictionService;
import org.bson.types.ObjectId;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hadoop on 2017-7-18.
 */

@Service
public class UserFictionServiceImpl implements UserFictionService {

    protected static Logger logger = LoggerFactory.getLogger(UserFictionServiceImpl.class);

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    UserFictionRedis userFictionRedis;

    @Autowired
    FictionDeatilRepository fictionDeatilRepository;

    @Autowired
    PicRepostitory picRepostitory;


    @Override
    public List<UserFictionBean> getUserFictionList(String uid) {

        DBObject queryObject = new BasicDBObject();
        queryObject.put("uid", Long.parseLong(uid));

        DBObject fields = new BasicDBObject();
        fields.put("_id", false);

        fields.put("fiction_id", true);
        fields.put("user_read_timestamp", true);


        List<UserFictionBean> userFictionBeanList = null;

        try {
            userFictionBeanList = mongoTemplate.find(new BasicQuery(queryObject, fields).with(new Sort(new Sort.Order(Sort.Direction.DESC, "user_read_timestamp"))).limit(20), UserFictionBean.class);
        } catch (Exception e) {
            logger.error("UserFictionServiceImpl,getUserFictionList is error uid={}", uid);
        }
        return userFictionBeanList;
    }

    @Override
    public boolean insertUserFictionToMongo(UserFictionBean userFictionBean) {

        try {
            mongoTemplate.upsert(new Query(Criteria.where("uid").is(userFictionBean.getUid()).and("fiction_id").is(userFictionBean.getFiction_id()).and("fiction_name").is(userFictionBean.getFiction_name())),
                    Update.update("user_read_timestamp", userFictionBean.getUser_read_timestamp()).set("user_read_line",userFictionBean.getUser_read_line()).set("user_like_count",userFictionBean.getUser_like_count()), userFictionBean.getClass());

            //  String key = "user_read_list_" + userFictionBean.getUid();
            // userFictionRedis.removeUserFictionByUid(key);
            //userFictionRedis.insertUserFictionBeanListToRedis(key, getUserFictionList(String.valueOf(userFictionBean.getUid())));
            return true;
        } catch (Exception e) {
            logger.error("insertUserFictionToMongoAndRedis is exception,UserFictionBean={}", userFictionBean);
            return false;
        }

    }

    @Override
    public boolean redisUserFictionExists(String key) {
        return userFictionRedis.redisExist(key);
    }

    @Override
    public List<UserFictionBean> getUserFictionListFromRedis(String key) {
        return userFictionRedis.getUserFictionListByKey(key);
    }

    @Override
    public void insertUserFictionListToRedis(String key, List<UserFictionBean> userFictionBeans) {

        userFictionRedis.insertUserFictionList(key, userFictionBeans);
    }

    public List<Long> getUserReadFictionSetForRedis(String key) {
        List<Long> userReadFictionSet = userFictionRedis.getUserReadFictionSet(key);

        return userReadFictionSet;
    }

    public List<Long> getUserReadFictionSetForMongo(String key, String uid) {

        Query query = new Query(Criteria.where("uid").is(Long.parseLong(uid))).with(new Sort(new Sort.Order(Sort.Direction.DESC, "user_read_timestamp")));

        List<UserFictionBean> mongoList = mongoTemplate.find(query, UserFictionBean.class);

        List<Long> userReadFictionSet = new ArrayList<Long>();

        for (UserFictionBean userFictionBean : mongoList) {

            userReadFictionSet.add(userFictionBean.getFiction_id());

        }
        //  userFictionRedis.insertUserReadFictionSetToRedis(key, userReadFictionSet);
        return userReadFictionSet;

    }

    @Override
    public List<FictionDetailBean> getFictionDetailInfoByIdForRedis(String key, String fiction_id, String fiction_page_num) {

        List<FictionDetailBean> fictionDetailBeanList = userFictionRedis.getFictionDetailInfoByIdForRedis(key, fiction_id, fiction_page_num);

        return fictionDetailBeanList;
    }

    /**
     * @param fiction_id       小说id
     * @param fiction_page_num 第几页
     * @param page_num         每页多少行
     * @return
     */
    public List<FictionDetailBean> getFictionDetailInfoByIdForMongo(String fiction_id, String fiction_page_num, int page_num) {

        List<FictionDetailBean> fictionDetailBeanList = mongoTemplate.find(new Query(Criteria.where("fiction_id").is(Long.parseLong(fiction_id)).and("actor_fiction_detail_index").gt((Long.parseLong(fiction_page_num) - 1) * page_num).lte((Long.parseLong(fiction_page_num)) * page_num)), FictionDetailBean.class);

        return fictionDetailBeanList;
    }


    @Override
    public List<FictionBean> getMyFictionByUid(String uid) {

        List<FictionBean> fictionBeanList = mongoTemplate.find(new Query(Criteria.where("fiction_author_id").is(uid)).with(new Sort(new Sort.Order(Sort.Direction.DESC, "update_time"))), FictionBean.class);

        return fictionBeanList;
    }


    public List<FictionBean> getMyFictionByUidLimit(String uid, int page_num) {

        List<FictionBean> fictionBeanList = mongoTemplate.find(new Query(Criteria.where("fiction_author_id").is(uid)).with(new Sort(new Sort.Order(Sort.Direction.DESC, "update_time"))).limit(20).skip(20 * (page_num - 1)), FictionBean.class);

        return fictionBeanList;
    }


    public boolean delMyFiction(String fiction_id, String uid) {

        try {
            mongoTemplate.remove(new Query(Criteria.where("fiction_id").is(Long.parseLong(fiction_id)).and("fiction_author_id").is(uid)), FictionBean.class);

            mongoTemplate.remove(new Query(Criteria.where("fiction_id").is(Long.parseLong(fiction_id))), FictionDetailBean.class);

            mongoTemplate.remove(new Query(Criteria.where("fiction_id").is(Long.parseLong(fiction_id))), FictionActorBean.class);

            return true;
        } catch (Exception e) {
            logger.error("delMyFiction is error,fiction={},uid={}", fiction_id, uid);
            return false;
        }


    }

    /**
     * @param fiction_id
     * @param fiction_detail_num
     */
    public List<FictionDetailBean> getFictionEndDeatil(String fiction_id, long fiction_detail_num) {

        List<FictionDetailBean> fictionDetailBeanList = mongoTemplate.find(new Query(Criteria.where("fiction_id").is(Long.parseLong(fiction_id)).and("actor_fiction_detail_index").gt(fiction_detail_num)), FictionDetailBean.class);

        return fictionDetailBeanList;

    }


    public List<FictionDetailBean> getFictionPreviousDetailFromMongo(String fiction_id, long start_fiction_detail_num, long end_fiction_detail_num) {

        List<FictionDetailBean> fictionDetailBeanList = mongoTemplate.find(new Query(Criteria.where("fiction_id").is(Long.parseLong(fiction_id)).and("actor_fiction_detail_index").gt(start_fiction_detail_num).lte(end_fiction_detail_num)).with(new Sort(new Sort.Order(Sort.Direction.ASC, "actor_fiction_detail_index"))), FictionDetailBean.class);

        return fictionDetailBeanList;
    }

    public String insertOneFictionDetail(FictionDetailBean fictionDetailBean) {
        FictionDetailBean fictionDetailMongoBean = fictionDeatilRepository.insert(fictionDetailBean);

        String id = fictionDetailMongoBean.getId();

        return id;
    }

    public boolean updateOneFictionDetail(String id, String actor_fiction_detail) {

        try {
            mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(new ObjectId(id))), Update.update("actor_fiction_detail", actor_fiction_detail), FictionDetailBean.class);
            return true;
        } catch (Exception e) {
            logger.error("updateOneFictionDetail is error,id={},actor_fiction_detail={} ", id, actor_fiction_detail);
            return false;
        }
    }

    public boolean delOneFictionDetail(String id) {

        try {
            fictionDeatilRepository.delete(id);
            return true;
        } catch (Exception e) {
            logger.error("delOneFictionDetail is error,id={}", id);
            return false;
        }
    }

    public boolean addActorintoFictionInfo(FictionActorBean fictionActorBean) {
        try {
            //mongoTemplate.updateFirst(new Query(Criteria.where("fiction_id").is(Long.parseLong(fiction_id))), new Update().addToSet("fiction_actors", actor_name), FictionBean.class);

            mongoTemplate.insert(fictionActorBean);
            return true;
        } catch (Exception e) {
            logger.error("delOneFictionDetail is error,fictionActorBean={}", fictionActorBean.toString());
            return false;
        }
    }

    public boolean delActorintoFictionInfo(String fiction_id, String actor_id) {
        try {

            //mongoTemplate.updateFirst(new Query(Criteria.where("fiction_id").is(Long.parseLong(fiction_id))),new Update().pull("fiction_actors", actor_name),FictionBean.class);

            mongoTemplate.remove(new Query(Criteria.where("fiction_id").is(Long.parseLong(fiction_id)).and("fiction_actor_id").is(actor_id)), FictionActorBean.class);

            return true;
        } catch (Exception e) {
            logger.error("delActorintoFictionInfo is error,fiction_id={},actor_id={}", fiction_id, actor_id);
            return false;
        }
    }

    public boolean releaseFictionDetail(String fiction_id) {
        try {
            mongoTemplate.updateMulti(new Query(Criteria.where("fiction_id").is(Long.parseLong(fiction_id)).and("fiction_detail_status").is(0)), Update.update("fiction_detail_status", 1), FictionDetailBean.class);
            return true;
        } catch (Exception e) {
            logger.error("releaseFictionDetail is error,fiction_id={}", fiction_id);
            return false;
        }
    }

    public boolean actordetailisExists(String fiction_id, String actor_id) {

        FictionDetailBean fictionDetailBean = mongoTemplate.findOne(new Query(Criteria.where("fiction_id").is(Long.parseLong(fiction_id)).and("actor_id").is(actor_id)), FictionDetailBean.class);

        if (null != fictionDetailBean) {
            return false;
        }
        return true;
    }

    public boolean updateActorintoFictionInfo(String fiction_id, String actor_id, String action_name) {

        try {

            mongoTemplate.updateFirst(new Query(Criteria.where("fiction_id").is(Long.parseLong(fiction_id)).and("fiction_actor_id").is(actor_id)), Update.update("fiction_actor_name", action_name), FictionActorBean.class);
            mongoTemplate.updateMulti(new Query(Criteria.where("fiction_id").is(Long.parseLong(fiction_id)).and("actor_id").is(actor_id)), Update.update("actor_name", action_name), FictionDetailBean.class);
            return true;
        } catch (Exception e) {
            logger.error("updateActorintoFictionInfo is error,fiction_id={},actor_id={},action_name={}", fiction_id, actor_id, action_name);

            return false;
        }
    }

    public List<FictionDetailBean> getFictionDetailList(String fiction_id) {

//        String[] fiction_ids_Split = fiction_ids.split(",");
//
//        DBObject queryObject = new BasicDBObject();
//        BasicDBList values = new BasicDBList();
//
//        for (String fiction_id : fiction_ids_Split) {
//            values.add(new BasicDBObject("fiction_id", Long.parseLong(fiction_id)));
//        }
//        queryObject.put("$or",values);
//
//       List<FictionDetailBean> fictionDetailBeanList = mongoTemplate.find(new BasicQuery(queryObject).with(new Sort(new Sort.Order(Sort.Direction.ASC, "actor_fiction_detail_index"))),FictionDetailBean.class);

        List<FictionDetailBean> fictionDetailBeanList = mongoTemplate.find(new Query(Criteria.where("fiction_id").is(Long.parseLong(fiction_id))).with(new Sort(new Sort.Order(Sort.Direction.ASC, "actor_fiction_detail_index"))), FictionDetailBean.class);

        return fictionDetailBeanList;

    }

    public long getMyFictionCount(String uid) {

        long myFictionCount = mongoTemplate.count(new Query(Criteria.where("fiction_author_id").is(uid)), FictionBean.class);

        return myFictionCount;

    }

    public Map<String, Object> getUserLastestFictionInfo(String uid) {

        Map<String, Object> map = null;

        List<UserFictionBean> userFictionBeanList = mongoTemplate.find(new Query(Criteria.where("uid").is(Long.parseLong(uid))).with(new Sort(new Sort.Order(Sort.Direction.DESC, "user_read_timestamp"))), UserFictionBean.class);

        if (null == userFictionBeanList || userFictionBeanList.size()<=0) {
            return map;
        } else {
            map = new HashMap<String, Object>();
            map.put("fiction_id", userFictionBeanList.get(0).getFiction_id());
            map.put("user_read_line", userFictionBeanList.get(0).getUser_read_line());

            return map;
        }
    }


    public long getUserReadFictionPageNumInfo(String uid,String fiction_id) {

        DBObject queryObject = new BasicDBObject();
        queryObject.put("uid", Long.parseLong(uid));
        queryObject.put("fiction_id", Long.parseLong(fiction_id));

        DBObject fields = new BasicDBObject();
        fields.put("_id", false);
        fields.put("user_read_line", true);

        UserFictionBean userFictionBean = mongoTemplate.findOne(new BasicQuery(queryObject, fields), UserFictionBean.class);

        if(null == userFictionBean){
            return 1L;
        }else {
            return userFictionBean.getUser_read_line();
        }
    }

    public void updateUserLatestFictionInfo(String fiction_id, String uid, long user_read_line) {

        mongoTemplate.updateFirst(new Query(Criteria.where("fiction_id").is(Long.parseLong(fiction_id)).and("uid").is(Long.parseLong(uid))),Update.update("user_read_line",user_read_line),UserFictionBean.class);
    }

    public long getuserLatestFictionLine(String uid,long fiction_id){

        UserFictionBean userFictionBean = mongoTemplate.find(new Query(Criteria.where("uid").is(Long.parseLong(uid)).and("fiction_id").is(fiction_id)), UserFictionBean.class).get(0);

        if(null == userFictionBean){
            return 0L;
        }else {
            return userFictionBean.getUser_read_line();
        }

    }

    public List<FictionActorBean> getFictionActorListByFictionid(long fiction_id){

        List<FictionActorBean> fictionActorBeanList = mongoTemplate.find(new Query(Criteria.where("fiction_id").is(fiction_id)),FictionActorBean.class);


        if(null !=fictionActorBeanList && fictionActorBeanList.size()>0){

            return fictionActorBeanList;

        }else {
            return null;
        }
    }


    public void saveUserWithOutLoginReadFictionInfo(List<UserFictionBean> userFictionBeanList){

        for (UserFictionBean userFictionBean : userFictionBeanList) {

            mongoTemplate.upsert(new Query(Criteria.where("uid").is(userFictionBean.getUid()).and("fiction_id").is(userFictionBean.getFiction_id()).and("fiction_name").is(userFictionBean.getFiction_name())),
                    Update.update("user_read_timestamp", userFictionBean.getUser_read_timestamp()).set("user_read_line",userFictionBean.getUser_read_line()).set("user_like_count",userFictionBean.getUser_like_count()), userFictionBean.getClass());
        }

    }


    public String insertPic(PicBean picBean){

        PicBean picMongoBean = picRepostitory.insert(picBean);
        String fiction_pic_path = picMongoBean.getPic_name();

        return fiction_pic_path;

    }
}
