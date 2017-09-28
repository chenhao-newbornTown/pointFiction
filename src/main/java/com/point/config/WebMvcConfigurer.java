package com.point.config;

import com.point.redis.UserRedis;
import com.point.util.PublicUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;

/**
 * Created by hadoop on 2017-7-18.
 */

@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

    protected static Logger logger = LoggerFactory.getLogger(WebMvcConfigurer.class);

    @Autowired
    UserRedis userRedis;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptorAdapter() {

            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                                     Object handler) throws Exception {

                request.setAttribute("timestamp", System.currentTimeMillis());


                String queryString = request.getQueryString();

                String ip = PublicUtil.getIpAddress(request);//String.valueOf(request.getAttribute("X-real-ip"));


                request.setCharacterEncoding("utf-8");
                response.setContentType("text/html;charset=utf-8");

                String query = "";

                Map<String, String[]> map = (Map<String, String[]>) request.getParameterMap();
                for (String name : map.keySet()) {
                    String[] values = map.get(name);
                    query += (name + "=" + Arrays.toString(values)) + "&";
                }

                query = query.replaceAll("\\[", "").replaceAll("\\]", "");

                if (StringUtils.isEmpty(queryString)) {
                    logger.info(request.getMethod() + "-----ip----->" + ip + "----->" + request.getRequestURL().toString() + "?" + query);
                } else {
                    // logger.info(request.getMethod() + "-----ip----->" + ip + "----" + String.valueOf(request.getAttribute("X-real-ip")) + "----->" + request.getRequestURL().toString() + "?" + request.getQueryString());
                    logger.info(request.getMethod() + "-----ip----->" + ip + "----->" + request.getRequestURL().toString() + "?" + query);
                }


//                String method = request.getServletPath();


                return true;

//                if(method.substring(1).split("/").length==1){
//                    return true;
//                }
//
//
//                if (!method.contains("returnerror") && !method.contains("getfictiondetaillist") && !method.contains("getuserunreadfictionSet")
//                        && !method.contains("increadcount") && !method.contains("getlikecount")) {
//                    String uid = request.getParameter("uid");
//                    String token = request.getParameter("token");
//
//                    String now_token = userRedis.getUserTokenMapsFromReids(uid, "now_token");
//                    String old_token = userRedis.getUserTokenMapsFromReids(uid, "old_token");
//
//
//                    if (token.equals(now_token)) {
//                        return true;
//                    } else if (token.equals(old_token)) {
//                        System.out.println(request.getContextPath());
//                        response.sendRedirect("/login/returnerror");
//                        return false;
//                    }
//                } else {
//                    return true;
//                }
//                return true;
            }
        }).addPathPatterns("/**");
    }
}
