package com.point.config;

import com.point.util.PublicUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by hadoop on 2017-7-18.
 */

@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

    protected static Logger logger = LoggerFactory.getLogger(WebMvcConfigurer.class);

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptorAdapter() {

            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                                     Object handler) throws Exception {

                request.setAttribute("timestamp",System.currentTimeMillis());

               // System.out.println("getContextPath----------->"+request.getServletPath());

                String queryString = request.getQueryString();

                String ip = PublicUtil.getIpAddress(request);

                if(StringUtils.isEmpty(queryString)){
                    logger.info(request.getMethod()+"-----ip----->"+ip+"----->"+request.getRequestURL().toString());
                }else{
                    logger.info(request.getMethod()+"-----ip----->"+ip+"----->"+request.getRequestURL().toString()+"?"+request.getQueryString());
                }




                return true;
            }
        }).addPathPatterns("/**");
    }
}
