package com.point.controller;

import com.google.gson.Gson;
import com.point.entity.FictionBean;
import com.point.entity.PushInfo;
import com.point.service.FictionService;
import com.point.service.UserFictionService;
import com.point.util.PushUtil;
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

    @Autowired
    UserFictionService userFictionService;

    @Scheduled(cron = "0 10 2 * * ?")
    public void executePutFictionDetailToRedis() {

//        fictionService.deleteRedisBykey("fiction_idlist_all");
//        fictionService.deleteRedisBykey("fiction_info_deatil_");
//        fictionService.deleteRedisBykey("fiction_page_info_");
//        fictionService.deleteRedisBykey("fiction_sensitivewords");
//        List<Long> fiction_id_List =  fictionService.insertAllFictionIdListToRedis("fiction_idlist_all");
//        //小说具体内容存储到redis
//        fictionService.insertFictionListToRedis("fiction_info_deatil_", "20",fiction_id_List);

        // fictionService.getMongoPicToRedis(key+"pics");
        fictionService.deleteRedisBykey("fiction_sensitivewords");
        fictionService.getMongoSensitiveWordsToRedis("fiction_sensitivewords");

        logger.info("PonitFictionScheduled----->executePutFictionDetailToRedis is End !!!");
    }

    @Scheduled(cron = "0 */15 * * * ?")
    public void executePutFictionInfoToRedis() {

        fictionService.deleteRedisBykey("fiction_info_all");

        fictionService.setFictionInfoAll();
        fictionService.deleteRedisBykey("fiction_idlist_all");
        fictionService.deleteRedisBykey("fiction_info_deatil_");
        fictionService.deleteRedisBykey("fiction_page_info_");
        List<Long> fiction_id_List = fictionService.insertAllFictionIdListToRedis("fiction_idlist_all");
        //小说具体内容存储到redis
        fictionService.insertFictionListToRedis("fiction_info_deatil_", "20", fiction_id_List);

        logger.info("PonitFictionScheduled----->executePutFictionInfoToRedis is End !!!");
    }



    //@Scheduled(cron = "0 0 09,12,15,18,21 * * ?")
   @Scheduled(cron = "*/30 * * * * ?")
    public void push() throws Exception {

        List<PushInfo> list = userFictionService.getpushInfo();

        if (null != list && list.size() > 0) {

            PushUtil pushUtil = new PushUtil();

            for (PushInfo pushInfo : list) {

                String fiction_name = pushInfo.getFiction_name();
                String actor_name = pushInfo.getActor_name();
                String actor_fiction_detail = pushInfo.getActor_fiction_detail();

                FictionBean fictionBean = pushInfo.getFictionBean();

                long fiction_num = fictionBean.getFiction_line_num();
                long fiction_detail_index = pushInfo.getFiction_detail_index();

                String push_alert = actor_name + "(" + fiction_name + ")" + ": " + actor_fiction_detail ;

                if (pushUtil.iosPush(push_alert, new Gson().toJson(fictionBean),pushInfo.getDevice_token(),pushInfo.getPush_num())) {

                    if(fiction_num>fiction_detail_index){
                        userFictionService.updateDataForPush(pushInfo.getMobile_device_num(),fiction_detail_index,fictionBean.getFiction_id(),(pushInfo.getPush_num()+1));
                    }

                }

            }
        }
    }
}
