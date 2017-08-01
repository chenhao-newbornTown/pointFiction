package com.point.redis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.point.entity.*;
import org.apache.poi.ss.formula.functions.T;
import org.parboiled.common.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by hadoop on 2017-7-19.
 */
@Repository
public class FictionRedis extends BaseRedis {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    public boolean redisFictionListExists(String key) {
        return redisTemplate.hasKey(key);
    }

    public List<FictionBean> getFictionListFromReidsByKey(String key, String page_fiction_num) {
        String fictionBeanJson = String.valueOf(redisTemplate.opsForHash().get(key, page_fiction_num));

        List<FictionBean> fictionBeanList = null;

        Gson gson = new Gson();

        if (StringUtils.isNotEmpty(fictionBeanJson)) {

            fictionBeanList = gson.fromJson(fictionBeanJson, new TypeToken<List<FictionBean>>() {
            }.getType());

        }

        return fictionBeanList;
    }



    public void insertFictionListToRedis(String key, Map<String, List<FictionDetailBean>> map) {

        for (Map.Entry<String, List<FictionDetailBean>> listEntry : map.entrySet()) {
            redisTemplate.opsForHash().put(key+listEntry.getKey().split("\\_")[1], listEntry.getKey().split("\\_")[0], new Gson().toJson(listEntry.getValue()));
        }
    }

    public List<Long> getAllFictionIdListFromReidsByKey(String key) {

        String fictionidSetJson = redisTemplate.opsForSet().members(key).toString();

        List<Long> fiction_daily_Set = new ArrayList<Long>();

        String[] fictionidSetJsonSplit = fictionidSetJson.replaceAll("\\[","").replaceAll("\\]","").split(",");

        for (String s : fictionidSetJsonSplit) {

            fiction_daily_Set.add(Long.parseLong(s));
        }

        return fiction_daily_Set;
    }

    public void insertAllFictionIdSetToRedis(String key, List<Long> fiction_id_List, Map<String, FictionInfoBean> fictionid_Maps) {
        redisTemplate.opsForSet().add(key,new Gson().toJson(fiction_id_List));

        for (Map.Entry<String, FictionInfoBean> fictionInfoBeanEntry : fictionid_Maps.entrySet()) {

            redisTemplate.opsForHash().put("fiction_info_all", fictionInfoBeanEntry.getKey(), new Gson().toJson(fictionInfoBeanEntry.getValue()));
        }
    }

    public void deleteRedisBykey(String key){

        Set<String> rediskeys =  redisTemplate.keys(key+"*");

        for (String rediskey : rediskeys) {
            redisTemplate.delete(rediskey);
        }
    }

    public FictionInfoBean getFictionInfoByFictionidFromRedis(String key,String fiction_id){

      String fictionInfoJson = String.valueOf(redisTemplate.opsForHash().get(key,fiction_id));

        FictionInfoBean fictionInfo = new FictionInfoBean();

        Gson gson = new Gson();

        if (StringUtils.isNotEmpty(fictionInfoJson)) {

            fictionInfo = gson.fromJson(fictionInfoJson, new TypeToken<FictionInfoBean>(){}.getType());

        }

        return fictionInfo;


    }



}
