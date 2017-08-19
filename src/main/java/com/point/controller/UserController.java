package com.point.controller;

import com.google.gson.Gson;
import com.point.constant.Constant;
import com.point.entity.UserInfoBean;
import com.point.service.UserService;
import com.point.util.PublicUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 肥肥 on 2017/7/17 0017.
 */

@RestController
@RequestMapping("/login")
public class UserController extends BaseController {

    protected static Logger logger = LoggerFactory.getLogger(UserController.class);

    Gson gson = new Gson();

    @Autowired
    UserService userService;

    /**
     * 判断用户是否存在，存在的话，则更新登录时间，
     * 不存在的话，则为新用户
     *
     * @param request
     * @return
     */
    @RequestMapping("/login")
    @ResponseBody
    public String login(HttpServletRequest request) {

        String token = request.getParameter("token");
        String uid_str = request.getParameter("uid");
        String mobile_device_num = request.getParameter("mobile_device_num");

        if (StringUtils.isEmpty(token)) {
            return returnJsonData(Constant.DataError, "", Constant.HttpErrorTokenError);
        }
        if (StringUtils.isEmpty(mobile_device_num)) {
            return returnJsonData(Constant.DataError, "", Constant.HttpErrorMobileDeviceNumError);
        }

        String md5_token = PublicUtil.makeMD5(token);
        String token_device = PublicUtil.makeMD5(token+mobile_device_num);
        String timestamp = String.valueOf(request.getAttribute("timestamp"));

        UserInfoBean userInfoBean = userService.getUserInfobyToken(token, md5_token);

        Map<String, String> map = new HashMap<String, String>();

        //新增用户
        if (null == userInfoBean) {
            int gendar = Integer.parseInt(request.getParameter("gendar"));
            String source_platform = request.getParameter("source_platform");

            String nick_name = request.getParameter("nick_name");

            userInfoBean = new UserInfoBean();

            userInfoBean.setGendar(gendar);
            userInfoBean.setSource_platform(source_platform);
            userInfoBean.setLogin_timestamp(timestamp);
            userInfoBean.setRegistered_timestamp(timestamp);
            userInfoBean.setToken(md5_token);
            userInfoBean.setMobile_device_num(mobile_device_num);
            userInfoBean.setNick_name(nick_name);
            userInfoBean.setToken_device(token_device);
            uid_str = userService.insertUserInfoToMongo(userInfoBean);

            //userService.insertUserTokenToReids(md5_token, uid_str, Constant.REDIS_1_DAYS);

            userService.insertUserTokenMapsToReids(uid_str,md5_token,"",Constant.REDIS_1_DAYS);

            map.put("token", md5_token);
            map.put("uid", uid_str);
        } else {
            String mongo_uid_str = String.valueOf(userInfoBean.getUid());
            String mongo_token = userInfoBean.getToken();//md5_token
            String mongo_mobile_device_num = userInfoBean.getMobile_device_num();

            String now_token = userService.getUserTokenMapsFromReids(uid_str,"now_token");
            String old_token = userService.getUserTokenMapsFromReids(uid_str,"old_token");

            if((mongo_token.equals(token) || mongo_token.equals(md5_token))&&mongo_uid_str.equals(uid_str)){//同一用户登录

                if(mobile_device_num.equals(mongo_mobile_device_num)){//同一部手机登录，只更新登录时间
                    userService.updateUserInfoToMongo(mongo_uid_str, timestamp, mobile_device_num, false);

                    userService.insertUserTokenMapsToReids(uid_str,md5_token,"",Constant.REDIS_1_DAYS);
                }else{

                    if(StringUtils.isEmpty(old_token)){
                        userService.insertUserTokenMapsToReids(uid_str,md5_token,now_token,Constant.REDIS_1_DAYS);
                    }

                    userService.updateUserInfoToMongo(mongo_uid_str, timestamp, mobile_device_num, true);
                }
            }else{
                return Constant.HttpErrorCodeSocialUserNotExist;
            }










//
//            if ((mongo_token.equals(token) || mongo_token.equals(md5_token)) && mobile_device_num.equals(mongo_mobile_device_num)) {//同一部手机登录，只更新登录时间
//                userService.updateUserInfoToMongo(mongo_uid_str, timestamp, mobile_device_num, false);
//                userService.insertUserTokenToReids(mongo_token, mongo_uid_str, Constant.REDIS_1_DAYS);
//            } else if ((mongo_token.equals(token) || mongo_token.equals(md5_token)) && !mobile_device_num.equals(mongo_mobile_device_num)) {//同一帐号，不同平台
//                userService.updateUserInfoToMongo(mongo_uid_str, timestamp, mobile_device_num, true);
//                userService.insertUserTokenToReids(mongo_token, mongo_uid_str, Constant.REDIS_1_DAYS);
//            } else if ((mongo_token.equals(token) || mongo_token.equals(md5_token) && !StringUtils.isEmpty(uid_str) && !uid_str.equals(mongo_uid_str))) {//token相同，但是uid不相同，则用户不存在
//                return Constant.HttpErrorCodeSocialUserNotExist;
//            }

            map.put("token", mongo_token);
            map.put("uid", mongo_uid_str);
        }

        return gson.toJson(map);
    }

}
