package com.point.service;

import com.point.entity.FictionBean;
import com.point.entity.FictionInfoBean;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by hadoop on 2017-7-19.
 */
public interface FictionService {

    /**
     * key ficitonlist_yyyyMMdd
     * @param key
     * @return
     */
    boolean redisFictionListExists(String key);

    /**
     *
     * @param key
     * @param page_fiction_num 每页多少个小说
     */
    void insertFictionListToRedis(String key, String update_time,String page_fiction_num,List<Long> fiction_id_List);

    /**
     *
     * @param key
     * @param pagenum 取第几页的小说
     * @return
     */
    List<FictionBean> getFictionListFromReidsByKey(String key,String pagenum);

    List<FictionBean> getFictionListFromMongoByKey(String updatetime);

    List<Long> getAllFictionIdListFromReidsByKey(String key);

    List<Long> insertAllFictionIdListToRedis(String key);


    void deleteRedisBykey(String key);

    void updateFictionUserReadCount(String fiction_id);
    void updateFictionUserLikeCount(String fiction_id);

    FictionInfoBean getFictionInfoByFictionidFromRedis(String key,String fiction_id);

    long getReadAndLikeCountByFictionidFromMongo(String fiction_id,String readOrLike);


    FictionBean saveFiction(FictionBean fictionBean);

    boolean releaseFiction(String fiction_id,String timestamp);

    String getFictionPicPathByid(long fiction_id);

    void incrFictionLineNum(String fiction_id, int i);

}
