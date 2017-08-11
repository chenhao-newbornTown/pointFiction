package com.point.controller;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hadoop on 2017-8-9.
 */
public class BaseController {


    public String returnJsonData(String code, Object data,String msg){

        Map<String,Object> jsonMap = new HashMap<String,Object>();

        jsonMap.put("code",code);
        jsonMap.put("data",data);
        jsonMap.put("msg",msg);

        Gson gson = new Gson();

        return gson.toJson(jsonMap);
    }

}
