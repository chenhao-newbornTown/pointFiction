package com.point.constant;

/**
 * Created by hadoop on 2017-7-20.
 */
public class Constant {


    public static final String BaseUrl = "http://www.indiandian.com";

        public static final String PicPath = "/data/pointPic";
//    public static final String PicPath = "E:\\xiaoshuo";

    public static final long REDIS_1_DAYS = 60 * 24 * 1;

    public static final String DataDefault = "00000";
    public static final String FictionIdsError = "10000";//fiction_ids为空
    public static final String FictionPageNumError = "10001";//小说页数为空
    public static final String FictionUidError = "10002";//uid为空
    public static final String DataError = "99999";
    public static final String AddFictionSuccessed = "小说创建成功";
    public static final String AddFictionFailed = "小说创建失败";
    public static final String DelFictionSuccessed = "小说删除成功";
    public static final String DelFictionFailed = "小说删除失败";
    public static final String ReleaseFictionSuccessed = "发布小说成功";
    public static final String ReleaseFictionFailed = "发布小说失败";
    public static final String AddActorSuccessed = "人物增加成功";
    public static final String AddActorFailed = "人物增加失败";
    public static final String delActorSuccessed = "人物删除成功";
    public static final String delActorFailed = "人物删除失败";
    public static final String editActorSuccessed = "人物修改成功";
    public static final String editActorFailed = "人物修改失败";
    public static final String addOneLineFictionDetailSuccessed = "新增一句小说成功";
    public static final String addOneLineFictionDetailFailed = "新增一句小说失败";
    public static final String editOneLineFictionDetailSuccessed = "更新一句小说成功";
    public static final String editOneLineFictionDetailFailed = "更新一句小说失败";
    public static final String delOneLineFictionDetailSuccessed = "删除一句小说成功";
    public static final String delOneLineFictionDetailFailed = "删除一句小说失败";

    public static final String HttpErrorCodeSocialUserNotExist = "20112";     // 社交账号不存在直接跳转注册

    public static final String HttpErrorTokenError = "20002";   // token为空
    public static final String HttpErrorMobileDeviceNumError = "20003";   // 设备号为空


    public static final String DelAcrotNameError = "小说角色发生过对话";   // 小说角色发生过对话，删除失败


    public static final String FictionStatusDefault = "000000";//默认状态
    public static final String FictionStatusPicError = "100001";//小说图片违规被删
    public static final String FictionStatusDeatilError = "100002";//小说内容违规被删


}
