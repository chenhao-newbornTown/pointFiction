package com.point.constant;

/**
 * Created by hadoop on 2017-7-20.
 */
public class Constant {

    public static final long REDIS_7_DAYS = 60 * 24 * 7;

    public static final String HttpErrorCodeDefault = "0";                 // 默认

    public static final String HttpErrorCodeSessionInvalid = "10004";

    public static final String HttpErrorCodeNetError = "1001";      // 网络失败
    public static final String HttpErrorCodeJsonParse = "1000";      // 接收结果解析失败
    public static final String HttpErrorCodeUidIlegal = "10111";     // UID非法，可能该用户已经被KO了
    public static final String HttpErrorCodeSocialUserNotExist = "20112";     // 社交账号不存在直接跳转注册

    public static final String HttpErrorCodeUserSinglePoString = "20113";        // 单点登录，会进行单独处理
    public static final String HttpErrorCodeNotPermit = "20114";        // 用户权限，是否达到关注/拉黑上限之类

    public static final String HttpErrorCodeUserNotExist = "20106";   // 用户不存在
    public static final String HttpErrorCodeUserAlreadyExsit = "20208";   // 用户已经注册
    public static final String HttpErrorCodePasswordWrong = "20107";   // 密码错误
    public static final String HttpErrorCodeAccountPasswordWrong = "20101";   // 账号和密码错误
    public static final String HttpErrorCodeSystemError = "10001";   // 系统错误

    public static final String HttpErrorTokenError = "10002";   // token不存在

    public static final String DelAcrotNameError = "10003";   // 小说角色发生过对话，删除失败


    public static final String HttpErrorCodeUserUnbindFailed = "21103";       // 解绑定失败由于您使用了社交账号的登录

    public static final String HttpErrorCodeReceiptConfirmError = "21201"; // confirm订单失败，原因在于咱们自己系统
    public static final String HttpErrorCodeReceiptIllegal = "21204"; // 订单非法，就是apple告诉我数据不合法
    public static final String HttpErrorCodeReceiptConfrimFailFromApple = "21205"; // 订单验证失败，就是apple告诉我其他非0的status

    public static final String FeedErrorCodeIsDeleted = "21005";    // 动态被删除
    public static final String FeedErrorCodeCommentUserPermit = "21008";    // 評論/贊 的權限限制，由於對方拉黑你導致無法評論
    public static final String FeedErrorCodeCommentFriendPermit = "21011";    // 好友才可以評論
    public static final String FeedErrorCodeFollowPermit = "21012";    // 粉絲才可以評論
    public static final String FeedErrorCodeLikeFriendPermit = "21013";    // 好友才可以贊
    public static final String FeedErrorCodeLikeFollowPermit = "21014";    // 粉絲才可以贊
    public static final String FeedErrorCodeLikeUserPermit = "21015";     // 拉黑不能贊

    public static final String TranslateParamError = "21501";
    public static final String TranslateLimitError = "21502";         //请求限制
    public static final String TranslateUnsupportError = "21503";
    public static final String TranslateFailed = "21504";


}
