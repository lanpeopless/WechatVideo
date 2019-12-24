package com.jonas.controller;

import com.jonas.common.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * 基础controller，用于存放一些公用变量和公共对象
 * @author cmj
 * @date 2019-02-06 21:01
 */
@RestController
public class BasicController {

    /**
     * redis操作对象
     */
    @Autowired
    public RedisOperator redisOperator;

    /**
     * session前缀格式
     */
    public final String USER_REDIS_SESSION="user-redis-session";

    /**
     * 个人用户文件地址
     */
//    public final String FILE_PATH="/Users/chenmingjun/开发文件夹/Video/UserFile/";
    public final String FILE_PATH="/Video/UserFile/";

    /**
     * 背景音乐存放地址
     */
//    public final String BGM_FILE_PATH="/Users/chenmingjun/开发文件夹/Video/BGM/";
    public final String BGM_FILE_PATH="/Video/BGM/";

    /**
     * 视频剪辑处理工具
     */
//    public final String FFMPEG="/Users/chenmingjun/开发文件夹/Video/ffmpeg/bin/ffmpeg";
    public final String FFMPEG="/Video/ffmpeg/bin/ffmpeg";

}
