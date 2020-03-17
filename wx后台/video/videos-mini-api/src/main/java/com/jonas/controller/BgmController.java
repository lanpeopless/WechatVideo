package com.jonas.controller;

import com.jonas.pojo.Bgm;
import com.jonas.service.BgmService;
import com.jonas.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author cmj
 * @date 2019-03-11 22:40
 */
@RestController
@RequestMapping("/video/bgm")
@Api(value = "BGM相关业务的接口",tags = {"用户相关业务的controller"})
public class BgmController {
    @Autowired
    private BgmService bgmService;

    @ApiOperation(value = "BGM查询接口", notes = "BGM查询接口")
    @PostMapping(value = "/queryAllBgmList",produces={"application/json;charset=UTF-8"})
    JSONResult queryAllBgmList(){
        List<Bgm> bgms = bgmService.queryBgmList();
        return JSONResult.ok(bgms);
    }
}
