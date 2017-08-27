package com.point.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.point.entity.UserInfoBean;
import com.point.mongo.UserRepository;
import com.point.redis.UserRedis;
import com.point.service.UserService;
import com.sun.corba.se.impl.oa.toa.TOA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hadoop on 2017-7-18.
 */

@Service
public class UserServiceImpl implements UserService {

    protected static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRedis userRedis;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    UserRepository userRepository;

    public boolean userInfoExists(String key) {
        return userRedis.redisExist(key);
    }

    public UserInfoBean getUserInfoFromReidsByKey(String key) {
        return userRedis.getUserByKey(key);
    }

    public boolean insertUserInfoToMongoAndRedis(String rediskey, UserInfoBean userInfoBean) {

        try {
            mongoTemplate.save(userInfoBean);

            userRedis.addUser(rediskey, userInfoBean);

            return true;

        } catch (Exception e) {
            logger.error("insertUserInfoToMongoAndRedis is exception,UserInfoBean={}", userInfoBean);
            return false;
        }

    }

    @Override
//    public String insertUserInfoToMongo(UserInfoBean userInfoBean) {
//
//        String uid = "";
//
//        try {
//            List<UserInfoBean> userInfoBeanList = mongoTemplate.find(new Query(Criteria.where("uid").is(userInfoBean.getUid())), UserInfoBean.class);
//
//            if (null != userInfoBeanList && userInfoBeanList.size() > 0) {
//
//                mongoTemplate.upsert(new Query(Criteria.where("uid").is(userInfoBean.getUid())), Update.update("login_timestamp", userInfoBean.getLogin_timestamp()), userInfoBean.getClass());
//
//            } else {
//                UserInfoBean userInfoBean1 = userRepository.save(userInfoBean);
//                uid = String.valueOf(userInfoBean1.getUid());
//
//                logger.info("insertUserInfoToMongo,userInfoBean={}", userInfoBean.toString());
//            }
//
//            return uid;
//
//        } catch (Exception e) {
//            logger.error("insertUserInfoToMongo is error,userInfoBean={}", userInfoBean.toString());
//
//            return uid;
//        }
//
//    }

    public String insertUserInfoToMongo(UserInfoBean userInfoBean) {

        String uid = "";

        try {
            UserInfoBean userInfoBean1 = userRepository.save(userInfoBean);
            uid = String.valueOf(userInfoBean1.getUid());

            logger.info("insertUserInfoToMongo,userInfoBean={}", userInfoBean.toString());

            return uid;

        } catch (Exception e) {
            logger.error("insertUserInfoToMongo is error,userInfoBean={}", userInfoBean.toString());

            return uid;
        }

    }


    @Override
    public void insertUserTokenToReids(String token, String uid_str, long time) {

        userRedis.insertUserTokenToReids(token, uid_str, time);

    }


    public UserInfoBean getUserInfobyToken(String token, String md5token) {

        UserInfoBean userInfoBean = mongoTemplate.findOne(new Query(new Criteria().orOperator(Criteria.where("token").is(token), Criteria.where("token").is(md5token))), UserInfoBean.class);

        //mongoTemplate.findOne(new Query(Criteria.where("token").is(token)), UserInfoBean.class);

        return userInfoBean;

    }

    public boolean updateUserInfoToMongo(String uid, String timestamp, String mobile_device_num, boolean status) {

        try {

            if (status) {
                mongoTemplate.upsert(new Query(Criteria.where("uid").is(Long.parseLong(uid))), Update.update("login_timestamp", timestamp), UserInfoBean.class);
            } else {
                mongoTemplate.upsert(new Query(Criteria.where("uid").is(Long.parseLong(uid))), Update.update("login_timestamp", timestamp).set("mobile_device_num", mobile_device_num), UserInfoBean.class);
            }

            logger.info("updateUserInfoToMongo,uid={}", uid);

            return true;
        } catch (Exception e) {
            logger.error("updateUserInfoToMongo is error,uid={}", uid);
            return false;
        }
    }

    public String getNickNameByUid(String uid) {

        DBObject queryObject = new BasicDBObject();
        queryObject.put("uid", Long.parseLong(uid));

        DBObject fields = new BasicDBObject();
        fields.put("_id", false);
        fields.put("nick_name", true);

        UserInfoBean userInfoBean = mongoTemplate.findOne(new BasicQuery(queryObject, fields), UserInfoBean.class);

        String nick_name = userInfoBean.getNick_name();

        return nick_name;

    }

    public void insertUserTokenMapsToReids(String uid_str, String now_token, String old_token, long time) {

        userRedis.insertUserTokenMapsToReids(uid_str, now_token, old_token, time);
    }

    public String getUserTokenMapsFromReids(String key, String token_type) {

        return userRedis.getUserTokenMapsFromReids(key, token_type);
    }

    public boolean updateUserNickName(String uid, String nick_name) {

        try {
            mongoTemplate.upsert(new Query(Criteria.where("uid").is(Long.parseLong(uid))), Update.update("nick_name", nick_name), UserInfoBean.class);
            return true;
        }catch (Exception e){
            logger.error("updateUserNickName is error,uid={},nick_name={}", uid,nick_name);
            return false;
        }


    }


}
