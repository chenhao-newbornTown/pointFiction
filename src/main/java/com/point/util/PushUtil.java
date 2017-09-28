package com.point.util;

import com.point.constant.Constant;
import com.point.umeng.push.PushClient;
import com.point.umeng.push.ios.IOSUnicast;

/**
 * Created by feifei on 2017-9-20.
 */
public class PushUtil {

    private PushClient client = new PushClient();



    public boolean iosPush(String push_alert, String val,String device_token,int push_num) throws Exception {

        IOSUnicast unicast = new IOSUnicast(Constant.PushAppKey, Constant.PushAppMasterSecret);

        unicast.setDeviceToken(device_token);
        unicast.setAlert(push_alert);
        unicast.setBadge(push_num);
        unicast.setSound("default");
        // TODO set 'production_mode' to 'true' if your app is under production mode
        unicast.setProductionMode();
       // unicast.setTestMode();
        unicast.setCustomizedField("data", val);
       return client.send(unicast);
    }


}
