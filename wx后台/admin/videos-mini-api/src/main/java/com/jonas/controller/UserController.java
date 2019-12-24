package com.jonas.controller;

import com.jonas.dto.BgmDTO;
import com.jonas.dto.UserDTO;
import com.jonas.pojo.UserInfo;
import com.jonas.pojo.UserReport;
import com.jonas.pojo.UsersFans;
import com.jonas.service.UserService;
import com.jonas.utils.JSONResult;
import com.jonas.utils.PagedResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author cmj
 * @date 2019-03-10 15:36
 */
@RestController
@RequestMapping("/user")
@Api(value = "用户相关业务的接口",tags = {"用户相关业务的controller"})
public class UserController extends BasicController {
    @Autowired
    private UserService userService;


    @ApiOperation(value = "用户上传头像的接口", notes = "用户上传头像的接口")
    @PostMapping(value = "/uploadFaceImage",produces={"application/json;charset=UTF-8"})
    JSONResult uploadFaceImage(String userId, @RequestParam("file") MultipartFile[] files) throws Exception{
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("上传出错");
        }else {

            //文件实际保存路径--绝对路径
            String filePath = "/Users/chenmingjun/开发文件夹/Video/UserFile";
            //文件上传路径--相对路径
            String uploadPathDB = "/"+userId+"/face/";
            FileOutputStream fileOutputStream = null;
            InputStream inputStream = null;

            try {
                if (files !=null && files.length>0){

                    String fileName = files[0].getOriginalFilename();
                    if (StringUtils.isNotBlank(fileName)){
                        //文件上传最终保存的绝对路径
                        String finalFacePath = filePath + uploadPathDB +fileName;
                        //数据库最终保存的相对路径
                        uploadPathDB += fileName;

                        File file = new File(finalFacePath);
                        if (file.getParentFile()!=null || !file.getParentFile().isDirectory()){
                            //如果父级文件夹不存在则创建文件夹
                            file.getParentFile().mkdirs();
                        }
                        fileOutputStream = new FileOutputStream(file);
                        inputStream  = files[0].getInputStream();
                        IOUtils.copy(inputStream,fileOutputStream);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return JSONResult.errorMsg("上传出错");
            } finally {
                if (fileOutputStream != null){
                    fileOutputStream.flush();
                }
                fileOutputStream.close();
                inputStream.close();
            }
            UserInfo userInfo = new UserInfo();
            userInfo.setId(userId);
            userInfo.setFaceImage(uploadPathDB);
            userService.updateUserInfo(userInfo);
            return JSONResult.ok(uploadPathDB);
        }

    }


    @ApiOperation(value = "用户查询个人信息的接口", notes = "用户查询个人信息的接口")
    @PostMapping(value = "/queryUserInfoById",produces={"application/json;charset=UTF-8"})
    JSONResult queryUserInfoById(@RequestBody UserInfo userInfo){
        UserInfo userInfoQuery = userService.searchUserInfoById(userInfo);
        if (userInfoQuery!=null){
            return JSONResult.ok(userInfoQuery);
        }else {
            return JSONResult.errorMsg("查询失败");
        }
    }

    @ApiOperation(value = "用户发起关注取关的接口", notes = "用户发起关注取关的接口")
    @PostMapping(value = "/changeUserLikeStatus",produces={"application/json;charset=UTF-8"})
    JSONResult changeUserLikeStatus(@RequestBody UserDTO userDTO) {
        String userId = userDTO.getUserId();
        String fanId = userDTO.getFanId();
        boolean likeUserStatus = userDTO.isLikeUserStatus();
        if (likeUserStatus){
            // 用户发起关注的行为
            userService.saveUserFansRelation(userId,fanId);
        }else {
            // 用户发起取关的行为
            userService.deleteUserFansRelation(userId,fanId);
        }
        return JSONResult.ok();
    }

    @ApiOperation(value = "查询关注状态的接口", notes = "查询关注状态的接口")
    @PostMapping(value = "/getUserLikeStatus",produces={"application/json;charset=UTF-8"})
    JSONResult getUserLikeStatus(@RequestBody UserDTO userDTO) {
        String userId = userDTO.getUserId();
        String fanId = userDTO.getFanId();
        UsersFans usersFans = new UsersFans();
        usersFans.setUserId(userId);
        usersFans.setFanId(fanId);
        UsersFans userFansRelation = userService.selectUserFansRelation(usersFans);
        if (userFansRelation != null){
            // 如果查询的值不为空则存在关注关系
            userDTO.setLikeUserStatus(true);
        }else {
            userDTO.setLikeUserStatus(false);
        }
        return JSONResult.ok(userDTO);
    }

    @ApiOperation(value = "举报用户的接口", notes = "举报用户的接口")
    @PostMapping(value = "/reportUser",produces={"application/json;charset=UTF-8"})
    JSONResult reportUser(@RequestBody UserReport userReport) {
        userService.reportUser(userReport);
        return JSONResult.ok("举报成功");
    }

    @ApiOperation(value = "用户管理查询接口(分页+条件)", notes = "用户管理查询接口(分页+条件)")
    @PostMapping(value = "/getUserInfoByConfition",produces={"application/json;charset=UTF-8"})
    JSONResult getUserInfoByConfition(@RequestBody UserDTO userDTO){
        System.out.println(userDTO.getUserInfo());
        PagedResult userInfoByCondition = userService.getUserInfoByCondition(userDTO);
        return JSONResult.ok(userInfoByCondition);
    }



}
