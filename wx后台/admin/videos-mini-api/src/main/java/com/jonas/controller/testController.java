package com.jonas.controller;

import com.jonas.pojo.UserInfo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cmj
 * @date 2019-02-06 21:01
 */
@RestController
public class testController {
    @RequestMapping("/hello")
    public String TestConn(){
        return "connect success";
    }

    @RequestMapping(value = "/helloUser",produces={"application/json;charset=UTF-8"})
    public String helloUser(@RequestBody UserInfo userInfo){
        System.out.println(userInfo);
        return "connect success";
    }
}
