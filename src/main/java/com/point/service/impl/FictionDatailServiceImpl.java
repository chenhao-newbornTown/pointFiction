package com.point.service.impl;

import com.point.redis.FictionDatailRedis;
import com.point.service.FictionDatailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by hadoop on 2017-7-21.
 */
@Service
public class FictionDatailServiceImpl implements FictionDatailService {


    @Autowired
    FictionDatailRedis fictionDatailRedis;

    public boolean redisFictionListExists(String key) {
        return fictionDatailRedis.redisFictionListExists(key);
    }






}
