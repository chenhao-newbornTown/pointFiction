package com.point.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by hadoop on 2017-7-21.
 */
@Repository
public class FictionDatailRedis extends BaseRedis{


    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    public boolean redisFictionListExists(String key) {
        return redisTemplate.hasKey(key);
    }
}
