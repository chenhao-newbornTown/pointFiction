package com.point.redis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.point.entity.UserInfoBean;
import org.parboiled.common.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by 肥肥 on 2017/7/17 0017.
 */

@Repository
public class UserRedis extends BaseRedis {

    @Autowired
    @Qualifier(value = "redisTemplate")
    private RedisTemplate<String, String> redisTemplate;

    public void addUser(String key, UserInfoBean user) {
        Gson gson = new Gson();
        redisTemplate.opsForValue().set(key, gson.toJson(user));
    }


    public void addUserList(String key, List<UserInfoBean> userList) {
        Gson gson = new Gson();
        redisTemplate.opsForValue().set(key, gson.toJson(userList));
    }

    public UserInfoBean getUserByKey(String key) {
        Gson gson = new Gson();
        UserInfoBean user = null;
        String userJson = redisTemplate.opsForValue().get(key);

        if (StringUtils.isNotEmpty(userJson)) {
            user = gson.fromJson(userJson, UserInfoBean.class);
        }
        return user;
    }

    public List<UserInfoBean> getUserListByKey(String key) {
        Gson gson = new Gson();
        List<UserInfoBean> userList = null;
        String userJson = redisTemplate.opsForValue().get(key);

        if (StringUtils.isNotEmpty(userJson)) {
            userList = gson.fromJson(userJson, new TypeToken<List<UserInfoBean>>() {
            }.getType());
        }
        return userList;
    }


    /**
     * 把用户的登录信息放入reids，默认保存1天，每次操作，都会校验是否登录
     *
     * @param token
     * @param uid_str
     */
    public void insertUserTokenToReids(String token, String uid_str, long time) {
        redisTemplate.opsForValue().set(token, uid_str, time, TimeUnit.MINUTES);
    }


    public void insertUserTokenMapsToReids(String uid_str, String now_token, String old_token, long time) {

        redisTemplate.opsForHash().put(uid_str, "now_token", now_token);
        redisTemplate.opsForHash().put(uid_str, "old_token", old_token);
        redisTemplate.expire(uid_str, time, TimeUnit.MINUTES);
    }

    public String getUserTokenMapsFromReids(String key, String token_type) {

        Object token_values = redisTemplate.opsForHash().get(key, token_type);

        String token_value = String.valueOf(token_values);
        return token_value;


    }

}
