package com.point.controller;

import com.point.service.FictionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by hadoop on 2017-8-11.
 */

@Component
public class PonitFictionScheduled {

    protected static Logger logger = LoggerFactory.getLogger(PonitFictionScheduled.class);

    @Autowired
    FictionService fictionService;

    @Scheduled(cron = "0 10 02 * * ?")
    public void executePutDataToRedis(){

        String key = "fiction_";
        fictionService.deleteRedisBykey(key);
        List<Long> fiction_id_List =  fictionService.insertAllFictionIdListToRedis("fiction_idlist_all");
        //小说具体内容存储到redis
        fictionService.insertFictionListToRedis(key + "info_deatil_", "20",fiction_id_List);

        logger.info("PonitFictionScheduled----->executePutDataToRedis is End !!!");
    }

}
