package com.jonas.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cmj
 * @date 2019-02-06 21:01
 */
@RestController
public class testController {
    @RequestMapping("/hello")
    public String TestConn(){
        return "connect";
    }
}
