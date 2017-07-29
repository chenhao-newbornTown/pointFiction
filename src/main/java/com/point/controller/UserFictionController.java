package com.point.controller;

import com.google.gson.Gson;
import com.point.constant.Constant;
import com.point.entity.*;
import com.point.service.FictionService;
import com.point.service.UserFictionService;
import com.point.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hadoop on 2017-7-18.
 */

@RestController
@RequestMapping("/userfiction")
public class UserFictionController {

    protected static Logger logger = LoggerFactory.getLogger(UserFictionController.class);

    String REDIS_KEY = "userfiction_";

    Gson gson = new Gson();

    @Autowired
    UserFictionService userFictionService;

    @Autowired
    FictionService fictionService;

    @Autowired
    UserService userService;

    /**
     * 获取用户最近看的前20位按时间倒序的小说
     * //先从redis中获取，没有的话，从mongo中获取，mongo中有的话，则返回，并插入reids，没有则返回[]
     *
     * @param request
     * @return
     */
    @RequestMapping("/getlist")
    @ResponseBody
    public String getUserFictionList(HttpServletRequest request) {

        String uid = request.getParameter("uid");

        List<UserFictionBean> userFictionBeanList = userFictionService.getUserFictionList(uid);

        List<UserReadFictionBean> userReadFictionBeanArrayList = new ArrayList<UserReadFictionBean>();

        for (UserFictionBean userFictionBean : userFictionBeanList) {

            UserReadFictionBean userReadFictionBean = new UserReadFictionBean();

            userReadFictionBean.setFiction_id(userFictionBean.getFiction_id());
            userReadFictionBean.setFiction_name(userFictionBean.getFiction_name());
            userReadFictionBean.setFiction_pic_path(fictionService.getFictionPicPathByid(userFictionBean.getFiction_id()));
            userReadFictionBean.setUser_read_timestamp(userFictionBean.getUser_read_timestamp());

            userReadFictionBeanArrayList.add(userReadFictionBean);
        }
        return gson.toJson(userReadFictionBeanArrayList);
    }

    /**
     * 返回当天,用户没有看过的小说的fictionList
     *
     * @param request
     * @return
     */
    @RequestMapping("/getuserunreadfictionSet")
    @ResponseBody
    public String getFictionListWithoutReaded(HttpServletRequest request) {

        String uid = request.getParameter("uid");

        List<Long> userReadFictionSet = null;

        List<Long> allFictionSet = fictionService.getAllFictionIdListFromReidsByKey("fiction_idlist_all");

        List<Long> userUnread = new ArrayList<Long>();//用户未读小说的fictionSet

        if (StringUtils.isEmpty(uid)) {//用户未注册，则传回所有的fictionlist

            userUnread.addAll(allFictionSet);

        } else {
//            if (userFictionService.redisUserFictionExists(REDIS_KEY + "readlist")) {
//
//                userReadFictionSet = userFictionService.getUserReadFictionSetForRedis(REDIS_KEY + "readlist_" + uid);
//            } else {
                userReadFictionSet = userFictionService.getUserReadFictionSetForMongo(REDIS_KEY + "readlist_" + uid, uid);
        //    }

            userUnread.addAll(allFictionSet);
            userUnread.removeAll(userReadFictionSet);
        }

        return gson.toJson(userUnread);
    }

    /**
     * 返回小说的某一页
     * 当uid为null，则认为是未登录用户，反之则是登录用户
     * uid为null，且获取第一页时，则增加该小说的read_count，登录的用户，同理
     * 用户的看书记录，不保存到redis
     * 当前业务逻辑是用户看小说相当于阅后即焚的，所有没有涉及到用户第二次读取该小说，readcount+1的情况
     *
     * @param request
     * @return
     */
    @RequestMapping("/getfictiondetail")
    @ResponseBody
    public String getFictionDetailByfictionIdAndPageNum(HttpServletRequest request) {

        String uid = request.getParameter("uid");
        String fiction_id = request.getParameter("fiction_id");
        String fiction_page_num = request.getParameter("fiction_page_num");

        String key = "fiction_info_deatil";
        List<FictionDetailBean> fictionDetailBeanList = userFictionService.getFictionDetailInfoByIdForRedis(key, fiction_id, fiction_page_num);

        if(null!=fictionDetailBeanList && fictionDetailBeanList.size()>0){

            int page_num = 20;

            fictionDetailBeanList = userFictionService.getFictionDetailInfoByIdForMongo(fiction_id,fiction_page_num,page_num);

        }

        if (Integer.parseInt(fiction_page_num) == 1) {//更行小说的read_count

            fictionService.updateFictionUserReadCount(fiction_id);//readcount+1

        }
        if (StringUtils.isEmpty(uid)) {
            return gson.toJson(fictionDetailBeanList);
        }
        UserFictionBean userFictionBean = new UserFictionBean();
        userFictionBean.setUid(Long.parseLong(uid));
        userFictionBean.setFiction_id(Long.parseLong(fiction_id));
        userFictionBean.setFiction_name(fictionService.getFictionInfoByFictionidFromRedis("fiction_info_all", fiction_id).getFiction_name());
        userFictionBean.setUser_read_timestamp(request.getAttribute("timestamp").toString());
        userFictionService.insertUserFictionToMongo(userFictionBean);

        return gson.toJson(fictionDetailBeanList);
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
    @RequestMapping("/getmyfictionlist")
    @ResponseBody
    public String getMyFiction(HttpServletRequest request) {

        String uid = request.getParameter("uid");

        List<FictionBean> fictionBeanList = userFictionService.getMyFictionByUid(uid);

        return gson.toJson(fictionBeanList);
    }


    /**
     * 编辑小说的时候，返回最后的页数，以及最后一个分页的数据
     *
     * @param request
     * @return
     */
    @RequestMapping("/getlastfictiondetail")
    @ResponseBody
    public String getFictionEndDeatil(HttpServletRequest request) {

        String fiction_id = request.getParameter("fiction_id");
        long fiction_line_num = Long.parseLong(request.getParameter("fiction_line_num"));

        long page_num = 20;

        long start_fiction_page_num = (fiction_line_num / page_num) + 1;

        long fiction_detail_num = (fiction_line_num / page_num) * page_num;

        List<FictionDetailBean> fictionDetailBeanList = userFictionService.getFictionEndDeatil(fiction_id, fiction_detail_num);

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("fiction_page_num", start_fiction_page_num);
        map.put("fiction_detail", fictionDetailBeanList);

        return gson.toJson(map);
    }


    /**
     * 编辑小说的时候，获取上一页，
     * 加入获取上一页的时候，所有小说的状态都为fiction_detail_status：1，
     * 则，再获取上一页的时候，访问/getfictiondetail 方法
     *
     * @param request
     * @return
     */
    @RequestMapping("/getfictionpreviousinfo")
    public String getFictionPreviousDetail(HttpServletRequest request) {

        String fiction_id = request.getParameter("fiction_id");
        long fiction_page_num = Long.parseLong(request.getParameter("fiction_page_num"));

        long page_num = 20;

        long start_fiction_detail_num = (fiction_page_num/page_num)*page_num;
        long end_fiction_detail_num = start_fiction_detail_num + page_num;

        List<FictionDetailBean> fictionDetailBeanList = userFictionService.getFictionPreviousDetailFromMongo(fiction_id, start_fiction_detail_num, end_fiction_detail_num);

        return gson.toJson(fictionDetailBeanList);

    }

    /**
     * 创建一本小说，并返回小说的fiction_id
     *
     * @param request
     * @return
     */
    @RequestMapping("/createfiction")
    public String createFiction(HttpServletRequest request) {

        String uid = request.getParameter("uid");

        String fiction_name = request.getParameter("fiction_name");
        String fiction_pic_path = request.getParameter("fiction_pic_path");

        String update_time = request.getAttribute("timestamp").toString();

        FictionBean fictionBean = new FictionBean();
        fictionBean.setFiction_name(fiction_name);
        fictionBean.setFiction_author_id(uid);
        fictionBean.setFiction_author_name(userService.getNickNameByUid(uid));
        fictionBean.setUpdate_time(update_time);
        fictionBean.setFiction_pic_path(fiction_pic_path);
        fictionBean.setFiction_status(0);

        FictionBean fictionMongoBean = fictionService.saveFiction(fictionBean);

        return gson.toJson(fictionMongoBean);
    }

    /**
     * 删除用户自己写的小说
     *
     * @param request
     */
    @RequestMapping("/delmyfiction")
    public boolean delFiction(HttpServletRequest request) {

        String fiction_id = request.getParameter("fiction_id");
        String uid = request.getParameter("uid");
        boolean delstatus = userFictionService.delMyFiction(fiction_id, uid);

        return delstatus;
    }

    /**
     * 新增一句小说
     * 返回新增行小说的id
     *
     * @param request
     * @return
     */
    @RequestMapping("/addonefictiondetail")
    @ResponseBody
    public String addOneFictionDetail(HttpServletRequest request) {

        String fiction_id = request.getParameter("fiction_id");
        String actor_fiction_detail_index = request.getParameter("actor_fiction_detail_index");

        String actor_name = request.getParameter("actor_name");
        String actor_fiction_detail = request.getParameter("actor_fiction_detail");

        FictionDetailBean fictionDetailBean = new FictionDetailBean();

        fictionDetailBean.setFiction_id(Long.parseLong(fiction_id));
        fictionDetailBean.setActor_fiction_detail(actor_fiction_detail);
        fictionDetailBean.setActor_name(actor_name);
        fictionDetailBean.setFiction_detail_status(0);
        fictionDetailBean.setActor_fiction_detail_index(Long.parseLong(actor_fiction_detail_index));

        String id = userFictionService.insertOneFictionDetail(fictionDetailBean);

        if(null!=id){
            fictionService.incrFictionLineNum(fiction_id,1);
        }

        return id;
    }


    /**
     * 更新一句小说
     *
     * @param request
     * @return
     */
    @RequestMapping("/updateonefictiondetail")
    @ResponseBody
    public boolean updateOneFictionDetail(HttpServletRequest request) {

        String id = request.getParameter("id");
        String actor_fiction_detail = request.getParameter("actor_fiction_detail");

        boolean updateStatus = userFictionService.updateOneFictionDetail(id, actor_fiction_detail);

        return updateStatus;
    }

    /**
     * 删除一句小说
     *
     * @param request
     * @return
     */
    @RequestMapping("/delonefictiondetail")
    @ResponseBody
    public boolean delOneFictionDetail(HttpServletRequest request) {

        String id = request.getParameter("id");
        boolean updateStatus = userFictionService.delOneFictionDetail(id);
        String fiction_id = request.getParameter("fiction_id");
        if(updateStatus){
            fictionService.incrFictionLineNum(fiction_id,-1);
        }

        return updateStatus;
    }

    /**
     * 增加一个小说角色
     *
     * @param request
     * @return
     */
    @RequestMapping("/addactor")
    public boolean addActorintoFictionInfo(HttpServletRequest request) {
        String fiction_id = request.getParameter("fiction_id");

        String actor_name = request.getParameter("actor_name");

        boolean addStatus = userFictionService.addActorintoFictionInfo(fiction_id, actor_name);

        return addStatus;
    }

    /**
     * 删除一个小说角色
     *
     * @param request
     * @return
     * @// TODO: 2017-7-27  并判断该角色是否参与过,参与过则不能删除
     */
    @RequestMapping("/delactor")

    public String delActorintoFictionInfo(HttpServletRequest request) {
        String fiction_id = request.getParameter("fiction_id");

        String actor_name = request.getParameter("actor_name");

        if(userFictionService.actordetailisExists(fiction_id,actor_name)){
            boolean addStatus = userFictionService.delActorintoFictionInfo(fiction_id, actor_name);
            return String.valueOf(addStatus);
        }else {
            return Constant.DelAcrotNameError;
        }
    }


    /**
     * 发布小说，状态置为1
     * fiction_info中fiction_status为0 的更新为1
     * fiction_detail中fiction_detail_status为0 的更新为1
     * @param request
     * @return
     */
    @RequestMapping("/releasefiction")
    public boolean releaseFiction(HttpServletRequest request) {

        String fiction_id = request.getParameter("fiction_id");

        String timestamp = request.getAttribute("timestamp").toString();

        boolean fictionDetailStatus = userFictionService.releaseFictionDetail(fiction_id);
        boolean fictionStatus = fictionService.releaseFiction(fiction_id,timestamp);


        return fictionDetailStatus&&fictionStatus;

    }

}
