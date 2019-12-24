package com.jonas.controller;

import com.jonas.pojo.UserInfo;
import com.jonas.service.UserService;
import com.jonas.utils.JSONResult;
import com.jonas.utils.MD5Utils;
import com.jonas.vo.UserInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author cmj
 * @date 2019-02-11 09:34
 */
@RestController
@Api(value = "用户登录的接口",tags = {"注册和登录的controller"})
public class RegisterController extends BasicController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户注册的接口", notes = "用户注册的接口")
    @PostMapping(value = "/register",produces={"application/json;charset=UTF-8"})
    JSONResult register(@RequestBody UserInfo userInfo){
//        1.判断用户是否为空
        if (StringUtils.isBlank(userInfo.getUsername())
                ||StringUtils.isBlank(userInfo.getPassword())){
            return JSONResult.errorMap("用户名和密码不为空");
        }
//        2.判断用户是否存在
        boolean usernameIsExist = userService.queryUsernameIsExist(userInfo.getUsername());
//        3.保存用户注册信息
        if (!usernameIsExist){
            try {
                userInfo.setNickname(userInfo.getUsername());
                userInfo.setPassword(MD5Utils.getMD5Str(userInfo.getPassword()));
                userInfo.setFaceImage(null);
                userInfo.setFansCounts("0");
                userInfo.setFollowCounts("0");
                userInfo.setReceiveLikeCounts("0");
                userService.saveUser(userInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            return JSONResult.errorMsg("用户名已存在");
        }
        //不将用户密码进行暴露
        userInfo.setPassword("");
        return JSONResult.ok(setUserSession(userInfo));
    }

    public UserInfoVO setUserSession(UserInfo userInfo){
        String uniqueToken = UUID.randomUUID().toString();
        redisOperator.set(USER_REDIS_SESSION + ":" + userInfo.getId() ,uniqueToken,1000*30*60);
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setId(userInfo.getId());
        userInfoVO.setUsername(userInfo.getUsername());
        userInfoVO.setFansCounts(userInfo.getFansCounts());
        userInfoVO.setFaceImage(userInfo.getFaceImage());
        userInfoVO.setFollowCounts(userInfo.getFollowCounts());
        userInfoVO.setNickname(userInfo.getNickname());
        userInfoVO.setReceiveLikeCounts(userInfo.getReceiveLikeCounts());
        userInfoVO.setUserToken(uniqueToken);
        return userInfoVO;
    }

    @ApiOperation(value = "用户登录的接口", notes = "用户登录的接口")
    @PostMapping(value = "/login",produces={"application/json;charset=UTF-8"})
    JSONResult login(@RequestBody UserInfo userInfo){
        UserInfo userInfoDB = userService.canLogin(userInfo);
        System.out.println(userInfoDB);
        if (userInfoDB!=null){
            return JSONResult.ok(setUserSession(userInfoDB));
        }else {
            return JSONResult.errorMsg("用户名或密码不存在");
        }

    }

    @ApiOperation(value = "用户注销的接口", notes = "用户注销的接口")
    @ApiImplicitParam(name = "userId",value = "用户ID",required = true,dataType = "String",paramType = "query")
    @PostMapping(value = "/logout",produces={"application/json;charset=UTF-8"})
    JSONResult logout(@RequestBody String userId){
        System.out.println(userId);
        redisOperator.del(USER_REDIS_SESSION + ":" + userId);
        return JSONResult.ok("注销成功");
    }
}
