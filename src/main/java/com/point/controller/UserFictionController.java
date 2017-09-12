package com.point.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.point.constant.Constant;
import com.point.entity.*;
import com.point.service.FictionService;
import com.point.service.UserFictionService;
import com.point.service.UserService;
import com.point.util.PublicUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.json.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by hadoop on 2017-7-18.
 */

@RestController
@RequestMapping(method = RequestMethod.POST, value = "/userfiction")
public class UserFictionController extends BaseController {

    protected static Logger logger = LoggerFactory.getLogger(UserFictionController.class);

    @Autowired
    UserFictionService userFictionService;

    @Autowired
    FictionService fictionService;

    @Autowired
    UserService userService;

    /**
     * 根据请求的fiction_ids返回相对应顺序的小说信息
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/getfictiondetaillist")
    @ResponseBody
    public String getFictionDetailList(HttpServletRequest request) {

        String fiction_ids = request.getParameter("fiction_ids");
        String key = "fiction_info_all";

        if (StringUtils.isEmpty(fiction_ids)) {
            return returnJsonData(Constant.DataError, "", Constant.FictionIdsError);
        }

        String[] fiction_ids_Split = fiction_ids.split(",");

        if (fiction_ids_Split.length <= 0) {
            return returnJsonData(Constant.DataError, "", Constant.FictionIdsError);
        }

        String uid = request.getParameter("uid");

        // LinkedHashMap<String, Object> jsonMap = new LinkedHashMap<String, Object>();

        List<FictionBean> fictionBeanList = new ArrayList<FictionBean>();

        for (String fiction_id : fiction_ids_Split) {

            //  Map<String,Object> map = new HashMap<String,Object>();

            //   map.put("fiction_detail",userFictionService.getFictionDetailList(fiction_id));

            //   map.put("fiction_info",fictionService.getFictionInfoByFictionidFromRedis(key, fiction_id));

            // jsonMap.put(fiction_id,fictionService.getFictionInfoByFictionidFromRedis(key, fiction_id));

            FictionBean fictionBean = fictionService.getFictionInfoByFictionidFromRedis(key, fiction_id);

            if (null != fictionBean && !StringUtils.isEmpty(uid)) {
                fictionBean.setUser_like_count_status(fictionService.getUserLikeCountStatus(uid, fiction_id));
                fictionBean.setUser_read_line(userFictionService.getUserReadFictionPageNumInfo(uid, fiction_id));
            }

            fictionBeanList.add(fictionBean);
        }

        return returnJsonData(Constant.DataDefault, fictionBeanList, "");
    }

    /**
     * 返回当天,小说的fictionList,并获取
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/getuserunreadfictionSet")
    @ResponseBody
    public String getFictionListWithoutReaded(HttpServletRequest request) {

        String uid = request.getParameter("uid");

//        List<Long> userReadFictionSet = null;

        List<Long> allFictionSet = fictionService.getAllFictionIdListFromReidsByKey("fiction_idlist_all");

        Map<String, Object> JsonMap = new HashMap<String, Object>();


        if (StringUtils.isNotEmpty(uid)) {
            Map<String, Object> userReadMap = userFictionService.getUserLastestFictionInfo(uid);

            if (null != userReadMap) {
                JsonMap.put("user_latest_read", userReadMap);
                allFictionSet.remove(Long.parseLong(userReadMap.get("fiction_id").toString()));
            }
        }

        JsonMap.put("fiction_list", allFictionSet);

        return returnJsonData(Constant.DataDefault, JsonMap, "");

       /* List<Long> userUnread = new ArrayList<Long>();//用户未读小说的fictionSet

        if (StringUtils.isEmpty(uid)) {//用户未注册，则传回所有的fictionlist

            userUnread.addAll(allFictionSet);

        } else {
//            if (userFictionService.redisUserFictionExists(REDIS_KEY + "readlist")) {
//
//                userReadFictionSet = userFictionService.getUserReadFictionSetForRedis(REDIS_KEY + "readlist_" + uid);
//            } else {
            userReadFictionSet = userFictionService.getUserReadFictionSetForMongo(REDIS_KEY + "readlist_" + uid, uid);//未存储到redis
            //    }

            userUnread.addAll(allFictionSet);
            userUnread.removeAll(userReadFictionSet);
        }

        return returnJsonData(Constant.DataDefault, userUnread, "");*/
    }

    /**
     * 返回小说的某一页
     * 此处是redis中行没有，则去库中查询那一行，然后返回，并没有放在redis中
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/getfictiondetail")
    @ResponseBody
    public String getFictionDetailByfictionIdAndPageNum(HttpServletRequest request) {

        String fiction_id = request.getParameter("fiction_id");

        if (StringUtils.isEmpty(fiction_id)) {
            return returnJsonData(Constant.DataError, "", Constant.FictionIdsError);
        }

        String fiction_page_num = request.getParameter("fiction_page_num");
        String user_read_line = request.getParameter("user_read_line");

        if (StringUtils.isEmpty(fiction_page_num) && StringUtils.isEmpty(user_read_line)) {
            return returnJsonData(Constant.DataError, "", Constant.FictionPageNumError);
        }

        if (StringUtils.isEmpty(fiction_page_num)) {
            fiction_page_num = fictionService.getReadUserReadPageNum("fiction_page_info_" + fiction_id, user_read_line);
        }

        String key = "fiction_info_deatil";
        List<FictionDetailBean> fictionDetailBeanList = userFictionService.getFictionDetailInfoByIdForRedis(key, fiction_id, fiction_page_num);

        if (null == fictionDetailBeanList || fictionDetailBeanList.size() <= 0) {
            int page_num = 20;
            fictionDetailBeanList = userFictionService.getFictionDetailInfoByIdForMongo(fiction_id, fiction_page_num, page_num);
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("fiction_detail", fictionDetailBeanList);
        map.put("fiction_page_num", fiction_page_num);

        return returnJsonData(Constant.DataDefault, map, "");
    }

    /**
     * 用户退出小说前的阅读记录和阅读位置
     *
     * @param request
     */

    @RequestMapping(method = RequestMethod.POST, value = "/updateuserreadline")
    public String getUserLatestFictionInfo(HttpServletRequest request) {

        String fiction_id = request.getParameter("fiction_id");
        String uid = request.getParameter("uid");
        Long user_read_line = 1L;

        if (null != request.getParameter("user_read_line")) {
            user_read_line = Long.parseLong(request.getParameter("user_read_line"));
        }

        userFictionService.updateUserLatestFictionInfo(fiction_id, uid, user_read_line);

        return returnJsonData(Constant.DataDefault, "", "");
    }

    /**
     * 小说阅读数+1
     */
    @RequestMapping(method = RequestMethod.POST, value = "/increadcount")
    @ResponseBody
    public String incFictionUserReadCount(HttpServletRequest request) {

        String fiction_id = request.getParameter("fiction_id");

        fictionService.updateFictionUserReadCount(fiction_id);//redis中readcount+1---> key readcount_fictionid

        String uid = request.getParameter("uid");

        if (!StringUtils.isEmpty(uid)) {
            UserFictionBean userFictionBean = new UserFictionBean();
            userFictionBean.setUid(Long.parseLong(uid));
            userFictionBean.setFiction_id(Long.parseLong(fiction_id));
            userFictionBean.setFiction_name(fictionService.getFictionInfoByFictionidFromRedis("fiction_info_all", fiction_id).getFiction_name());
            userFictionBean.setUser_read_timestamp(request.getAttribute("timestamp").toString());
            userFictionBean.setUser_read_line(1L);
            userFictionBean.setUser_like_count("0");
            userFictionService.insertUserFictionToMongo(userFictionBean);
        }

        return returnJsonData(Constant.DataDefault, "", "");

    }


    /**
     * 获取用户最近看的前20位按时间倒序的小说,没有插入redis，只有当用户有阅读新的小说的时候，再退出，才会调用此接口
     * //先从redis中获取，没有的话，从mongo中获取，mongo中有的话，则返回，并插入reids，没有则返回[]
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/getuserreadfictionlist")
    @ResponseBody
    public String getUserFictionList(HttpServletRequest request) {

        String uid = request.getParameter("uid");

        if (StringUtils.isEmpty(uid)) {
            return returnJsonData(Constant.DataError, "", Constant.FictionUidError);
        }

        List<UserFictionBean> userFictionBeanList = userFictionService.getUserFictionList(uid);

        if (null == userFictionBeanList || userFictionBeanList.size() <= 0) {
            return returnJsonData(Constant.DataDefault, ArrayUtils.EMPTY_STRING_ARRAY, "");
        }

        List<FictionBean> fictionBeanList = fictionService.getFictionInfoByFictionidFromMongo(userFictionBeanList);

        List<UserReadFictionBean> userReadFictionBeanArrayList = new ArrayList<UserReadFictionBean>();

        for (UserFictionBean userFictionBean : userFictionBeanList) {

            for (FictionBean fictionBean : fictionBeanList) {

                if (userFictionBean.getFiction_id() == fictionBean.getFiction_id()) {

                    UserReadFictionBean userReadFictionBean = new UserReadFictionBean();

                    userReadFictionBean.setFiction_id(fictionBean.getFiction_id());
                    userReadFictionBean.setUser_read_timestamp(userFictionBean.getUser_read_timestamp());
                    userReadFictionBean.setFiction_name(fictionBean.getFiction_name());
//                    userReadFictionBean.setFiction_author_name(fictionBean.getFiction_author_name());
                    userReadFictionBean.setFiction_pic_path(fictionBean.getFiction_pic_path());
//                    userReadFictionBean.setRead_count(fictionBean.getRead_count());
//                    userReadFictionBean.setLike_count(fictionBean.getLike_count());
                    userReadFictionBean.setFiction_line_num(fictionBean.getFiction_line_num());
                    userReadFictionBean.setUser_read_line(userFictionService.getuserLatestFictionLine(uid, fictionBean.getFiction_id()));
                    userReadFictionBean.setUser_like_count_status(fictionService.getUserLikeCountStatus(uid, String.valueOf(fictionBean.getFiction_id())));
                    userReadFictionBeanArrayList.add(userReadFictionBean);
                }
            }
        }
        return returnJsonData(Constant.DataDefault, userReadFictionBeanArrayList, "");
    }


    /**
     * 获取用户写了几本小说
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/getmyfictioncount")
    @ResponseBody
    public String getMyFictionCount(HttpServletRequest request) {

        String uid = request.getParameter("uid");

        if (StringUtils.isEmpty(uid)) {
            return returnJsonData(Constant.DataError, "", Constant.FictionUidError);
        }

        long myFictionCount = userFictionService.getMyFictionCount(uid);

        return returnJsonData(Constant.DataDefault, myFictionCount, "");

    }


    /**
     * 2.1 profile页-我的故事
     * 获取用户自己写的小说的相关信息
     * 信息还包含小说是否发布，发布才会有阅读数和点赞数
     * 数据未存储到redis
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/getmyfictionlist")
    @ResponseBody
    public String getMyFiction(HttpServletRequest request) {

        String uid = request.getParameter("uid");

        if (StringUtils.isEmpty(uid)) {
            return returnJsonData(Constant.DataError, "", Constant.FictionUidError);
        }

        int page_num = Integer.parseInt(request.getParameter("page_num"));

        List<FictionBean> fictionBeanList = userFictionService.getMyFictionByUidLimit(uid, page_num);//getMyFictionByUid(uid);

        List<FictionInfoActorsBean> fictionInfoActorsBeanList = new ArrayList<FictionInfoActorsBean>();

        for (FictionBean fictionBean : fictionBeanList) {

            FictionInfoActorsBean fictionInfoActorsBean = new FictionInfoActorsBean();

            fictionInfoActorsBean.setFictionBean(fictionBean);
            fictionInfoActorsBean.setFiction_actors(userFictionService.getFictionActorListByFictionid(fictionBean.getFiction_id()));


            fictionInfoActorsBeanList.add(fictionInfoActorsBean);
        }

        return returnJsonData(Constant.DataDefault, fictionInfoActorsBeanList, "");
    }


    /**
     * 编辑小说的时候，返回最后的页数，以及最后一个分页的数据
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/getlastfictiondetail")
    @ResponseBody
    public String getFictionEndDeatil(HttpServletRequest request) {

        String fiction_id = request.getParameter("fiction_id");
        // long fiction_line_num = Long.parseLong(request.getParameter("fiction_line_num"));

        long page_num = 20;

//        long start_fiction_page_num = (fiction_line_num / page_num) + 1;
//
//        long fiction_detail_num = (fiction_line_num / page_num) * page_num;

        List<FictionDetailBean> fictionDetailBeanList = userFictionService.getFictionDeatil(fiction_id);//userFictionService.getFictionEndDeatil(fiction_id, fiction_detail_num);

        double fiction_detail_num = Math.ceil(fictionDetailBeanList.size() / page_num);


        int start_fiction_page_num = (int) ((fiction_detail_num - 1) * 20 + 1);


        List<FictionDetailBean> fictionDetailBeanList1 = new ArrayList<>();

        for (int i = start_fiction_page_num; i < fictionDetailBeanList.size(); i++) {

            fictionDetailBeanList1.add(fictionDetailBeanList.get(i));

        }

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("fiction_page_num", start_fiction_page_num);
        map.put("fiction_detail", fictionDetailBeanList1);

        return returnJsonData(Constant.DataDefault, map, "");
    }


    /**
     * 编辑小说的时候，获取上一页，
     * 假如获取上一页的时候，所有小说的状态都为fiction_detail_status：1，
     * 则，再获取上一页的时候，访问/getfictiondetail 方法
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/getfictionpreviousinfo")
    @ResponseBody
    public String getFictionPreviousDetail(HttpServletRequest request) {

        String fiction_id = request.getParameter("fiction_id");
        int fiction_page_num = Integer.parseInt(request.getParameter("fiction_page_num"));


        int page_num = 20;

        //   long start_fiction_detail_num = (fiction_page_num - 1) * page_num;
        //   long end_fiction_detail_num = start_fiction_detail_num + page_num;

        List<FictionDetailBean> fictionDetailBeanList = userFictionService.getFictionDeatil(fiction_id);//getFictionPreviousDetailFromMongo(fiction_id, start_fiction_detail_num, end_fiction_detail_num);

        List<FictionDetailBean> fictionDetailBeanList1 = new ArrayList<>();
        if (fiction_page_num > 0) {
            int start_fiction_detail_num = ((fiction_page_num - 1) * page_num) + 1;
            int end_fiction_detail_num = start_fiction_detail_num + page_num;

            for (int i = start_fiction_detail_num; i < end_fiction_detail_num; i++) {

                fictionDetailBeanList1.add(fictionDetailBeanList.get(i));

            }

        }


        return returnJsonData(Constant.DataDefault, fictionDetailBeanList1, "");

    }

    /**
     * 创建一本小说，并返回小说的fiction_id
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/createfiction")
    @ResponseBody
    public String createFiction(HttpServletRequest request) {

        String uid = request.getParameter("uid");

        if (StringUtils.isEmpty(uid)) {
            return returnJsonData(Constant.DataError, "", Constant.FictionUidError);
        }

        String fiction_name = request.getParameter("fiction_name");
        String fiction_pic_path = request.getParameter("fiction_pic_path");

        String update_time = request.getAttribute("timestamp").toString();

        FictionBean fictionBean = new FictionBean();
        fictionBean.setFiction_name(fiction_name);
        fictionBean.setFiction_author_id(uid);
        fictionBean.setFiction_author_name(userService.getNickNameByUid(uid));
        fictionBean.setUpdate_time(update_time);
        fictionBean.setUpdate_date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        fictionBean.setFiction_pic_path(fiction_pic_path);
        fictionBean.setFiction_line_num(0);
        fictionBean.setFiction_original_num(0);
        fictionBean.setFiction_status(0);
        fictionBean.setStatus(Constant.FictionStatusDefault);
        fictionBean.setUser_like_count_status("0");

        FictionBean fictionMongoBean = fictionService.saveFiction(fictionBean);

        if (null != fictionMongoBean) {
            return returnJsonData(Constant.DataDefault, fictionMongoBean, Constant.AddFictionSuccessed);
        } else {
            return returnJsonData(Constant.DataError, "", Constant.AddFictionFailed);
        }
    }

    /**
     * 删除用户自己写的小说
     *
     * @param request
     */
    @RequestMapping(method = RequestMethod.POST, value = "/delmyfiction")
    @ResponseBody
    public String delFiction(HttpServletRequest request) {

        String fiction_id = request.getParameter("fiction_id");
        String uid = request.getParameter("uid");
        //  String fiction_status = request.getParameter("fiction_status");
        boolean delstatus = userFictionService.delMyFiction(fiction_id, uid);
        if (delstatus) {

            if (fictionService.getFictionStatus(fiction_id)) {
                fictionService.deleteRedisBykey("fiction_info_deatil_" + fiction_id);

                fictionService.updateAllFictionIdListToRedis("fiction_idlist_all");
            }

            return returnJsonData(Constant.DataDefault, "", Constant.DelFictionSuccessed);
        } else {
            return returnJsonData(Constant.DataError, "", Constant.DelFictionFailed);
        }
    }

    /**
     * 新增一句小说
     * 返回新增行小说的id
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/addonefictiondetail")
    @ResponseBody
    public String addOneFictionDetail(HttpServletRequest request) {

        String fiction_id = request.getParameter("fiction_id");
        String actor_fiction_detail_index = request.getParameter("actor_fiction_detail_index");

        String actor_id = request.getParameter("actor_id");
        String actor_name = request.getParameter("actor_name");
        String actor_fiction_detail = request.getParameter("actor_fiction_detail");

        FictionDetailBean fictionDetailBean = new FictionDetailBean();

        fictionDetailBean.setFiction_id(Long.parseLong(fiction_id));
        fictionDetailBean.setActor_fiction_detail(actor_fiction_detail);
        fictionDetailBean.setActor_id(actor_id);
        fictionDetailBean.setActor_name(actor_name);
        fictionDetailBean.setFiction_detail_status(0);
        fictionDetailBean.setActor_fiction_detail_index(Long.parseLong(actor_fiction_detail_index));

        String id = userFictionService.insertOneFictionDetail(fictionDetailBean);

        if (null != id) {
            fictionService.incrFictionLineNum(fiction_id, 1);
            Map<String, String> map = new HashMap<String, String>();
            map.put("fiction_detail_id", id);

            return returnJsonData(Constant.DataDefault, map, Constant.addOneLineFictionDetailSuccessed);
        } else {
            return returnJsonData(Constant.DataError, "", Constant.addOneLineFictionDetailFailed);
        }
    }


    /**
     * 更新一句小说
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/updateonefictiondetail")
    @ResponseBody
    public String updateOneFictionDetail(HttpServletRequest request) {

        String id = request.getParameter("fiction_detail_id");
        String actor_fiction_detail = request.getParameter("actor_fiction_detail");
        String fiction_id = request.getParameter("fiction_id");

        if (StringUtils.isEmpty(fiction_id)) {
            return returnJsonData(Constant.DataError, "", Constant.FictionIdsError);
        }

        boolean updateStatus = userFictionService.updateOneFictionDetail(id, actor_fiction_detail);

        if (updateStatus) {

            userFictionService.updateFictionDateTime(fiction_id, request.getAttribute("timestamp").toString(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

            return returnJsonData(Constant.DataDefault, "", Constant.editOneLineFictionDetailSuccessed);
        } else {
            return returnJsonData(Constant.DataError, "", Constant.editOneLineFictionDetailFailed);
        }
    }

    /**
     * 删除一句小说
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/delonefictiondetail")
    @ResponseBody
    public String delOneFictionDetail(HttpServletRequest request) {

        String id = request.getParameter("fiction_detail_id");
        boolean updateStatus = userFictionService.delOneFictionDetail(id);
        String fiction_id = request.getParameter("fiction_id");
        if (updateStatus) {
            fictionService.incrFictionLineNum(fiction_id, -1);
            return returnJsonData(Constant.DataDefault, "", Constant.delOneLineFictionDetailSuccessed);
        } else {
            return returnJsonData(Constant.DataError, "", Constant.delOneLineFictionDetailFailed);
        }

    }

    /**
     * 增加一个小说角色
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/addactor")
    @ResponseBody
    public String addActorintoFictionInfo(HttpServletRequest request) {
        String fiction_id = request.getParameter("fiction_id");
        String actor_name = request.getParameter("actor_name");

        String fiction_actor_id = PublicUtil.makeMD5(actor_name + System.currentTimeMillis());

        FictionActorBean fictionActorBean = new FictionActorBean();
        fictionActorBean.setFiction_id(Long.parseLong(fiction_id));
        fictionActorBean.setFiction_actor_id(fiction_actor_id);
        fictionActorBean.setFiction_actor_name(actor_name);

        boolean userfictionStatus = userFictionService.addActorintoFictionInfo(fictionActorBean);
        if (userfictionStatus) {

            Map<String, String> map = new HashMap<String, String>();
            map.put("actor_name", actor_name);
            map.put("actor_id", fiction_actor_id);

            return returnJsonData(Constant.DataDefault, map, Constant.AddActorSuccessed);
        } else {
            return returnJsonData(Constant.DataError, "", Constant.AddActorFailed);
        }


    }

    /**
     * 删除一个小说角色
     *
     * @param request
     * @return 判断该角色是否参与过, 参与过则不能删除
     */
    @RequestMapping(method = RequestMethod.POST, value = "/delactor")
    @ResponseBody
    public String delActorintoFictionInfo(HttpServletRequest request) {
        String fiction_id = request.getParameter("fiction_id");

        String actor_id = request.getParameter("actor_id");

        if (userFictionService.actordetailisExists(fiction_id, actor_id)) {
            boolean addStatus = userFictionService.delActorintoFictionInfo(fiction_id, actor_id);
            if (addStatus) {
                return returnJsonData(Constant.DataDefault, "", Constant.delActorSuccessed);
            } else {
                return returnJsonData(Constant.DataError, "", Constant.delActorFailed);
            }
        } else {
            return returnJsonData(Constant.DataError, "", Constant.DelAcrotNameError);
        }
    }

    /**
     * 修改一个小说的角色名
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "updateactor")
    @ResponseBody
    public String updateActorintoFictionInfo(HttpServletRequest request) {

        String fiction_id = request.getParameter("fiction_id");

        String actor_id = request.getParameter("actor_id");

        String new_actor_name = request.getParameter("actor_name");

        boolean fictionStatus = userFictionService.updateActorintoFictionInfo(fiction_id, actor_id, new_actor_name);

        if (fictionStatus) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("actor_name", new_actor_name);
            map.put("actor_id", actor_id);
            return returnJsonData(Constant.DataDefault, map, Constant.editActorSuccessed);
        } else {
            return returnJsonData(Constant.DataError, "", Constant.editActorFailed);
        }
    }


    /**
     * 发布小说，状态置为1
     * fiction_info中fiction_status为0 的更新为1
     * fiction_detail中fiction_detail_status为0 的更新为1
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/releasefiction")
    @ResponseBody
    public String releaseFiction(HttpServletRequest request) {

        String fiction_id = request.getParameter("fiction_id");

        String timestamp = request.getAttribute("timestamp").toString();

        // String fiction_status = request.getParameter("fiction_status");

        boolean fictionDetailStatus = userFictionService.releaseFictionDetail(fiction_id);
        boolean fictionStatus = fictionService.releaseFiction(fiction_id, timestamp);

        if (fictionDetailStatus && fictionStatus) {

//            if (fiction_status.equals("2")) {
            if (fictionService.getFictionStatus(fiction_id)) {
                String key = "fiction_info_deatil_";

                fictionService.deleteRedisBykey(key + fiction_id);
                fictionService.deleteRedisBykey("fiction_page_info_" + fiction_id);

                fictionService.insertFictionDetailToRedis(Long.parseLong(fiction_id), 20, key);
            }
            return returnJsonData(Constant.DataDefault, "", Constant.ReleaseFictionSuccessed);
        } else {
            return returnJsonData(Constant.DataError, "", Constant.ReleaseFictionFailed);
        }

    }


    /**
     * 用户对已经加入热门池的小说，进行小说语句的update,del，则更新redis中的数据
     * fiction_status的状态必须是2(加入热门池)，才能调用此接口
     *
     * @param request
     * @// TODO: 2017/9/11 0011 修改小说的每页页数
     */
    @RequestMapping(method = RequestMethod.POST, value = "/updateredisfictiondetail")
    @ResponseBody
    public String updateFictionDetail(HttpServletRequest request) {

        String fiction_id = request.getParameter("fiction_id");

        if (fictionService.getFictionStatus(fiction_id)) {
            String key = "fiction_info_deatil_";
            try {
                fictionService.deleteRedisBykey(key + fiction_id);
                fictionService.deleteRedisBykey("fiction_page_info_" + fiction_id);

                fictionService.insertFictionDetailToRedis(Long.parseLong(fiction_id), 20, key);

                return returnJsonData(Constant.DataDefault, "", "");
            } catch (Exception e) {
                return returnJsonData(Constant.DataError, "", "");
            }
        }
        return returnJsonData(Constant.DataDefault, "", "");
    }


    @RequestMapping(method = RequestMethod.POST, value = "/useruploadpic")
    @ResponseBody
    public String uploadFictionPic(HttpServletRequest request, @RequestParam("pic") MultipartFile[] files) {

//        MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
//        MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);
//        DefaultMultipartHttpServletRequest defaultMultipartHttpServletRequest = new DefaultMultipartHttpServletRequest(multipartRequest);
//        MultiValueMap<String, MultipartFile> fileMap = defaultMultipartHttpServletRequest.getMultiFileMap();

        MultipartFile file = files[0];//multipartRequest.getFile("pic");

        if (file.isEmpty()) {
            return returnJsonData(Constant.DataError, "", Constant.UploadPicError);
        }

        String uid = request.getParameter("uid");

        PicBean picBean = PublicUtil.saveFile(file);//uploadPic(file);

        if (null != picBean) {
            picBean.setPic_status("2");
            picBean.setUse_pic_num(1);
            picBean.setUid(Long.parseLong(uid));
            String fiction_pic_path = userFictionService.insertPic(picBean);

            String temp = request.getParameter("temp");

            if (null != temp && temp.equals("update")) {
                String fiction_id = request.getParameter("fiction_id");
                userFictionService.updateFictionPic(fiction_pic_path, fiction_id);
            }

            return returnJsonData(Constant.DataDefault, fiction_pic_path, "");
        } else {
            return returnJsonData(Constant.DataError, "", Constant.UploadPicFailed);
        }
    }


}
