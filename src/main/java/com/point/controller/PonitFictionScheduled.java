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

    @Scheduled(cron = "0 10 2 * * ?")
    public void executePutFictionDetailToRedis(){

        fictionService.deleteRedisBykey("fiction_idlist_all");
        fictionService.deleteRedisBykey("fiction_info_deatil_");
        fictionService.deleteRedisBykey("fiction_page_info_");
        fictionService.deleteRedisBykey("fiction_sensitivewords");
        List<Long> fiction_id_List =  fictionService.insertAllFictionIdListToRedis("fiction_idlist_all");
        //小说具体内容存储到redis
        fictionService.insertFictionListToRedis("fiction_info_deatil_", "20",fiction_id_List);

       // fictionService.getMongoPicToRedis(key+"pics");

        fictionService.getMongoSensitiveWordsToRedis("fiction_sensitivewords");

        logger.info("PonitFictionScheduled----->executePutFictionDetailToRedis is End !!!");
    }

    @Scheduled(cron = "0 */15 * * * ?")
    public void executePutFictionInfoToRedis() {

        fictionService.deleteRedisBykey("fiction_info_all");

        fictionService.setFictionInfoAll();

        logger.info("PonitFictionScheduled----->executePutFictionInfoToRedis is End !!!");

    }
}
