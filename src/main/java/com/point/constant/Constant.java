package com.point.constant;

/**
 * Created by hadoop on 2017-7-20.
 */
public class Constant {


    public static final String BaseUrl = "http://www.indiandian.com";

//    public static final String PicPath = "/data/pointPic";
public static final String PicPath = "E:\\xiaoshuo";

    public static final long REDIS_7_DAYS = 60 * 24 * 7;

    public static final String DataDefault = "00000";
    public static final String FictionIdsError = "10000";//fiction_ids为空
    public static final String FictionPageNumError = "10001";//小说页数为空
    public static final String FictionUidError = "10002";//uid为空
    public static final String DataError = "99999";

    public static final String HttpErrorCodeSocialUserNotExist = "20112";     // 社交账号不存在直接跳转注册

    public static final String HttpErrorTokenError = "20002";   // token为空
    public static final String HttpErrorMobileDeviceNumError = "20003";   // 设备号为空


    public static final String DelAcrotNameError = "20004";   // 小说角色发生过对话，删除失败





    public static final String FictionStatusDefault="000000";//默认状态
    public static final String FictionStatusPicError="100001";//小说图片违规被删
    public static final String FictionStatusDeatilError="100002";//小说内容违规被删





}
