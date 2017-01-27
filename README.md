# SendDiff

Simple back end for service API example:

package com.prj.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by vvinton on 1/27/2017.
 */
@Controller
public class TestController {

    public static class Model {
        public String param1;
        public String param2;
    }

    @RequestMapping(value = "/testPost", method = RequestMethod.POST)
    public @ResponseBody String testPost(@RequestParam("param1") String param1, @RequestParam("param2") String param2) {
        System.out.println("SuccessPost");
        System.out.println(param1);
        System.out.println(param2);
        return "SuccessPost";
    }

    @RequestMapping(value = "/testGet", method = RequestMethod.GET)
    public @ResponseBody String testGet(@RequestParam("param1") String param1, @RequestParam("param2") String param2) {
        System.out.println("SuccessGet");
        System.out.println(param1);
        System.out.println(param2);
        return "SuccessGet";
    }
}