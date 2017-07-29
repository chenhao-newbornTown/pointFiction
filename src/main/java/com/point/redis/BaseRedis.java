package com.point.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by hadoop on 2017-7-18.
 */

@Repository
public class BaseRedis {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 判断该key在redis中是否存在
     * @param key
     * @return
     */
    public boolean redisExist(String key){
        boolean hasKey = redisTemplate.hasKey(key);

        return hasKey;
    }

    /**
     * 获取redis中全局的自增id，获取之后+1
     * @param key
     * @return
     */
    public String getRedisid(String key) {
        String redisId = String.valueOf(redisTemplate.opsForValue().get(key));

        redisTemplate.opsForValue().increment(key, 1);

        return redisId;
    }

    /**
     * 根据key删除redis中的数据
     * @param key
     */
    public void deleteByKey(String key) {
        redisTemplate.opsForValue().getOperations().delete(key);
    }


}
