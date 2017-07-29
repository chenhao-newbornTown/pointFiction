package com.point.redis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.point.entity.FictionDetailBean;
import com.point.entity.UserFictionBean;
import org.parboiled.common.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by hadoop on 2017-7-18.
 */

@Repository
public class UserFictionRedis extends BaseRedis{

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void lpushUserFiction(String key, UserFictionBean userFictionBean) {
        Gson gson = new Gson();

        redisTemplate.opsForList().leftPush(key, gson.toJson(userFictionBean));
    }

    public List<UserFictionBean> getUserFictionListByKey(String key) {

        Gson gson = new Gson();

        List<UserFictionBean> userFictionBeanList = null;

        String userFictionJson = redisTemplate.opsForValue().get(key);

        if (StringUtils.isNotEmpty(userFictionJson)) {

            userFictionBeanList = gson.fromJson(userFictionJson, new TypeToken<List<UserFictionBean>>() {
            }.getType());

        }


        return userFictionBeanList;
    }

    public void insertUserFictionList(String key, List<UserFictionBean> userFictionBeans){

        Gson gson = new Gson();

        redisTemplate.opsForValue().set(key,gson.toJson(userFictionBeans));
    }

    public void removeUserFictionByUid(String uid){

        redisTemplate.delete(uid);
    }

    public void insertUserFictionBeanListToRedis(String key,List<UserFictionBean> userFictionBeanList){

        redisTemplate.opsForValue().set(key,new Gson().toJson(userFictionBeanList));
    }

    public List<Long> getUserReadFictionSet(String key){

        Set<String> userReadFictionStrSet =redisTemplate.opsForSet().members(key);

        List<Long> userReadFictionSet = new ArrayList<Long>();

        for (String s : userReadFictionStrSet) {

            userReadFictionSet.add(Long.parseLong(s.replaceAll("\\[","").replaceAll("\\]","")));

        }

        return userReadFictionSet;
    }

    public void insertUserReadFictionSetToRedis(String key,List<Long> userReadFictionSet){
        redisTemplate.opsForValue().set(key,new Gson().toJson(userReadFictionSet));
    }



    public List<FictionDetailBean> getFictionDetailInfoByIdForRedis(String key, String fiction_id, String fiction_page_num){

        List<FictionDetailBean> fictionDetailBeanList = null;

       String fictionDetailBeanJson =  String.valueOf(redisTemplate.opsForHash().get(key+"_"+fiction_id,fiction_page_num));

       if(StringUtils.isNotEmpty(fictionDetailBeanJson)){

           fictionDetailBeanList = new Gson().fromJson(fictionDetailBeanJson,new TypeToken<List<FictionDetailBean>>() {
           }.getType());
       }

       return fictionDetailBeanList;


    }



}
