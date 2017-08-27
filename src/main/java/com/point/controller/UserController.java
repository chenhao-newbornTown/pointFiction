package com.point.controller;

import com.google.gson.Gson;
import com.point.constant.Constant;
import com.point.entity.UserInfoBean;
import com.point.service.UserService;
import com.point.util.EncryptionUtils;
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

        if (StringUtils.isEmpty(token)) {
            return returnJsonData(Constant.DataError, "", Constant.HttpErrorTokenError);
        }

        String md5_token = PublicUtil.makeMD5(token);

        UserInfoBean userInfoBean = userService.getUserInfobyToken(token, md5_token);

        String timestamp = String.valueOf(request.getAttribute("timestamp"));
        String mobile_device_num = request.getParameter("mobile_device_num");

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
            uid_str = userService.insertUserInfoToMongo(userInfoBean);

            userService.insertUserTokenToReids(md5_token, uid_str, Constant.REDIS_7_DAYS);
            map.put("token", md5_token);
            map.put("uid", uid_str);

        } else {

            String mongo_uid_str = String.valueOf(userInfoBean.getUid());
            String mongo_token = userInfoBean.getToken();
            String mongo_mobile_device_num = userInfoBean.getMobile_device_num();

            if ((mongo_token.equals(token) || mongo_token.equals(md5_token)) && mobile_device_num.equals(mongo_mobile_device_num)) {//同一部手机登录，只更新登录时间
                userService.updateUserInfoToMongo(mongo_uid_str, timestamp, mobile_device_num, false);
                userService.insertUserTokenToReids(mongo_token, mongo_uid_str, Constant.REDIS_7_DAYS);
            } else if ((mongo_token.equals(token) || mongo_token.equals(md5_token)) && !mobile_device_num.equals(mongo_mobile_device_num)) {//同一帐号，不同平台
                userService.updateUserInfoToMongo(mongo_uid_str, timestamp, mobile_device_num, true);
                userService.insertUserTokenToReids(mongo_token, mongo_uid_str, Constant.REDIS_7_DAYS);
            } else if ((mongo_token.equals(token) || mongo_token.equals(md5_token) && !StringUtils.isEmpty(uid_str) && !uid_str.equals(mongo_uid_str))) {//token相同，但是uid不相同，则用户不存在
                return Constant.HttpErrorCodeSocialUserNotExist;
            }

            map.put("token", mongo_token);
            map.put("uid", mongo_uid_str);
        }

        return returnJsonData(Constant.DataDefault, map, "");
    }

    @RequestMapping("/singlelogin")
    @ResponseBody
    public String singleLogin(HttpServletRequest request) {

        String token = request.getParameter("token");//未加密的token或者ase加密之后的token|mobile_device_num
        String uid_str = request.getParameter("uid");

        if (StringUtils.isEmpty(token)) {
            return returnJsonData(Constant.DataError, "", Constant.HttpErrorTokenError);
        }

        String timestamp = String.valueOf(request.getAttribute("timestamp"));
        String mobile_device_num = request.getParameter("mobile_device_num");

        String token_temp = EncryptionUtils.decrypt(token);

        String md5_token;
        if (null != token_temp && token_temp.contains("|")) {
            md5_token = token_temp.split("\\|")[0];
        } else {
            md5_token = PublicUtil.makeMD5(token);
        }

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
            uid_str = userService.insertUserInfoToMongo(userInfoBean);

            String redis_token = EncryptionUtils.encrypt(md5_token + "|" + mobile_device_num, false);

            userService.insertUserTokenMapsToReids(uid_str, redis_token, "0", Constant.REDIS_7_DAYS);
            map.put("token", redis_token);
            map.put("uid", uid_str);

        } else {

            String mongo_uid_str = String.valueOf(userInfoBean.getUid());
            String mongo_token = userInfoBean.getToken();//aes(token|mobile_device_num)
            String mongo_mobile_device_num = userInfoBean.getMobile_device_num();


            if (StringUtils.isNotEmpty(uid_str)) {//token存在，uid不为空，而且和mongo中的uid不同，则说明该帐号不存在
                if (!mongo_uid_str.equals(uid_str)) {
                    return returnJsonData(Constant.DataError, "", Constant.HttpErrorCodeSocialUserNotExist);
                }
            }

            String redis_token = EncryptionUtils.encrypt(mongo_token + "|" + mobile_device_num, false);

            if (StringUtils.isEmpty(uid_str) || mongo_uid_str.equals(uid_str)) {//用户新换手机，uid在设备中不存在，或者是之前已经登录过的设备，则uid是存在的，并且和mongo中的uid相同

                if (mongo_mobile_device_num.equals(mobile_device_num)) {//同一设备

                    if (userService.userInfoExists(mongo_uid_str)) {//redis中用户的信息已经被缓存

                        String now_token = userService.getUserTokenMapsFromReids(mongo_uid_str, "now_token");
                        String old_token = userService.getUserTokenMapsFromReids(mongo_uid_str, "old_token");

                        if (old_token.equals("0")) {//说明7天内，一直是该设备登录

                            userService.updateUserInfoToMongo(mongo_uid_str, timestamp, mobile_device_num, true);

                            userService.insertUserTokenMapsToReids(mongo_uid_str, now_token, "0", Constant.REDIS_7_DAYS);
                            map.put("token", now_token);
                            map.put("uid", mongo_uid_str);
                        } else {
                            userService.updateUserInfoToMongo(mongo_uid_str, timestamp, mobile_device_num, false);

                            userService.insertUserTokenMapsToReids(mongo_uid_str, redis_token, old_token, Constant.REDIS_7_DAYS);
                            map.put("token", redis_token);
                            map.put("uid", mongo_uid_str);
                        }


                    } else {//用户距离上次登录，超过7天，所以redis中没有用户的缓存信息

                        userService.updateUserInfoToMongo(mongo_uid_str, timestamp, mobile_device_num, true);

                        userService.insertUserTokenMapsToReids(mongo_uid_str, redis_token, "0", Constant.REDIS_7_DAYS);
                        map.put("token", redis_token);
                        map.put("uid", mongo_uid_str);
                    }

                } else {

                    if (userService.userInfoExists(mongo_uid_str)) {//redis中用户的信息已经被缓存

                        String now_token = userService.getUserTokenMapsFromReids(mongo_uid_str, "now_token");

                        userService.updateUserInfoToMongo(mongo_uid_str, timestamp, mobile_device_num, false);

                        userService.insertUserTokenMapsToReids(mongo_uid_str, redis_token, now_token, Constant.REDIS_7_DAYS);
                        map.put("token", redis_token);
                        map.put("uid", mongo_uid_str);
                    } else {//用户距离上次登录，超过7天，所以redis中没有用户的缓存信息

                        userService.updateUserInfoToMongo(mongo_uid_str, timestamp, mobile_device_num, false);

                        userService.insertUserTokenMapsToReids(mongo_uid_str, redis_token, "0", Constant.REDIS_7_DAYS);
                        map.put("token", redis_token);
                        map.put("uid", mongo_uid_str);
                    }
                }
            }
        }
        return returnJsonData(Constant.DataDefault, map, "");
    }

    @RequestMapping("/returnerror")
    @ResponseBody
    public String returnError(){
        return returnJsonData(Constant.UserLoginFailed,"","");
    }


    @RequestMapping("/updatenickname")
    public String updateUserNickName(HttpServletRequest request){
        String uid = request.getParameter("uid");
        String nick_name=request.getParameter("nick_name");

        if(userService.updateUserNickName(uid,nick_name)){
            return returnJsonData(Constant.DataDefault,"","");
        }else {
            return returnJsonData(Constant.DataError,"","");
        }

    }


}
