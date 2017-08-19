package com.point.service;

import com.point.entity.UserInfoBean;

/**
 * Created by hadoop on 2017-7-18.
 */
public interface UserService {

    boolean userInfoExists(String key);

    UserInfoBean getUserInfoFromReidsByKey(String key);

    boolean insertUserInfoToMongoAndRedis(String rediskey,UserInfoBean userInfoBean);


    String insertUserInfoToMongo(UserInfoBean userInfoBean);


    void insertUserTokenToReids(String token ,String uid_str,long time);

    void insertUserTokenMapsToReids(String uid_str,String now_token,String old_token,long time);

    UserInfoBean getUserInfobyToken(String token,String md5token);

    boolean updateUserInfoToMongo(String uid, String timestamp,String mobile_device_num,boolean status);

    String getNickNameByUid(String uid);

    String getUserTokenMapsFromReids(String key,String token_type);

}
