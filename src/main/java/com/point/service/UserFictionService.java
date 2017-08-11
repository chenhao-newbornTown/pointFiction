package com.point.service;

import com.point.entity.FictionActorBean;
import com.point.entity.FictionBean;
import com.point.entity.FictionDetailBean;
import com.point.entity.UserFictionBean;

import java.util.List;

/**
 * Created by hadoop on 2017-7-18.
 */
public interface UserFictionService {

    List<UserFictionBean> getUserFictionList(String uid);

    boolean insertUserFictionToMongo(UserFictionBean userFictionBean);

    boolean redisUserFictionExists(String key);

    List<UserFictionBean> getUserFictionListFromRedis(String key);

    void insertUserFictionListToRedis(String key, List<UserFictionBean> userFictionBeans);

    List<Long> getUserReadFictionSetForRedis(String key);

    List<Long> getUserReadFictionSetForMongo(String key, String uid);

    List<FictionDetailBean> getFictionDetailInfoByIdForRedis(String key, String fiction_id, String fiction_page_num);

    List<FictionBean> getMyFictionByUid(String uid);

    boolean delMyFiction(String fiction_id, String uid);

    List<FictionDetailBean> getFictionEndDeatil(String fiction_id, long fiction_detail_num);

    List<FictionDetailBean> getFictionPreviousDetailFromMongo(String fiction_id, long start_fiction_detail_num, long end_fiction_detail_num);

    String insertOneFictionDetail(FictionDetailBean fictionDetailBean);

    boolean updateOneFictionDetail(String id, String actor_fiction_detail);

    boolean delOneFictionDetail(String id);

    boolean addActorintoFictionInfo(FictionActorBean fictionActorBean);

    boolean delActorintoFictionInfo(String fiction_id, String actor_id);

    boolean releaseFictionDetail(String fiction_id);

    boolean actordetailisExists(String fiction_id, String actor_id);

    List<FictionDetailBean> getFictionDetailInfoByIdForMongo(String fiction_id, String fiction_page_num, int page_num);

    boolean updateActorintoFictionInfo(String fiction_id,String actor_id,String action_name);

    List<FictionDetailBean> getFictionDetailList(String fiction_ids);


}
