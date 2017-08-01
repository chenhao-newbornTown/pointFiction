package com.point.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by hadoop on 2017-8-1.
 */

@Controller
@RequestMapping("/")
public class WebController {

    @RequestMapping("/index")
    public String index(){

        return "index";

    }

}
