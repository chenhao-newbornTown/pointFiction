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

    UserInfoBean getUserInfobyToken(String token,String md5token);

    boolean updateUserInfoToMongo(String uid, String timestamp,String mobile_device_num,boolean status);

    String getNickNameByUid(String uid);

}
