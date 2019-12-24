package com.jonas.controller;

import com.github.pagehelper.Page;
import com.jonas.dto.LikeVideoDTO;
import com.jonas.dto.VideoDTO;
import com.jonas.pojo.*;
import com.jonas.service.BgmService;
import com.jonas.service.UserService;
import com.jonas.service.VideoService;
import com.jonas.utils.FetchVideoCover;
import com.jonas.utils.JSONResult;
import com.jonas.utils.MergeVideoMp3;
import com.jonas.utils.PagedResult;
import com.jonas.vo.VideoVO;
import io.swagger.annotations.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

/**
 * @author cmj
 * @date 2019-03-14 22:18
 */
@RestController
@Api(value = "视频相关业务的接口",tags = {"视频相关业务的controller"})
@RequestMapping("/video")
public class VideoController extends BasicController {

    @Autowired
    private BgmService bgmService;
    @Autowired
    private VideoService videoService;
    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户上传视频以及视频封面的接口", notes = "用户上传视频以及视频封面的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "bgmId", value = "背景音乐ID", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoSeconds", value = "视频时长", required = true, dataType = "Double", paramType = "form"),
            @ApiImplicitParam(name = "videoWidth", value = "视频宽度", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "videoHeight", value = "视频高度", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "desc", value = "视频描述", required = false, dataType = "String", paramType = "form")
    })
    @PostMapping(value = "/uploadVideo",produces={"application/json;charset=UTF-8"},headers = {"content-type=multipart/form-data"})
    JSONResult uploadVideo(String userId, String bgmId,
                           Float videoSeconds, Integer videoWidth,
                           Integer videoHeight, String desc,
                           @ApiParam(value = "短视频",required = true)
                           MultipartFile files) throws Exception{
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("上传出错");
        }else {

            //文件实际保存路径--绝对路径
//            String filePath = "/Users/chenmingjun/开发文件夹/Video/UserFile/";
            //文件上传路径--相对路径
            String uploadPathDB = "/"+userId+"/video/";
            String coverPathDB = "/"+userId+"/video/";
            String finalVideoPath = "";
            FileOutputStream fileOutputStream = null;
            InputStream inputStream = null;
            try {
                    String fileName = files.getOriginalFilename();
                    String fileNamePrefix = fileName.split("\\.")[0];
                    if (StringUtils.isNotBlank(fileName)){
                        //文件上传最终保存的绝对路径
                        finalVideoPath = FILE_PATH + uploadPathDB +fileName;
                        //数据库最终保存的相对路径
                        uploadPathDB += fileName;
                        coverPathDB += fileNamePrefix+".jpg";
                        File file = new File(finalVideoPath);
                        if (file.getParentFile()!=null || !file.getParentFile().isDirectory()){
                            //如果父级文件夹不存在则创建文件夹
                            file.getParentFile().mkdirs();
                        }
                        fileOutputStream = new FileOutputStream(file);
                        inputStream  = files.getInputStream();
                        IOUtils.copy(inputStream,fileOutputStream);
                    }

            } catch (Exception e) {
                e.printStackTrace();
                return JSONResult.errorMsg("上传出错");
            } finally {
                if (fileOutputStream != null){
                    fileOutputStream.flush();
                }
                fileOutputStream.close();
                inputStream.close();
            }
            // 如果BGMId不为空，则说明选择了BGM
            // 查询BGM，并合并将视频和音频进行合并
            if (StringUtils.isNotBlank(bgmId)){
                Bgm bgm = bgmService.queryBgmById(bgmId);
                String musicInputPath = BGM_FILE_PATH + bgm.getPath();
                MergeVideoMp3 mergeVideoMp3 = new MergeVideoMp3(FFMPEG);
                String videoInPutPath = finalVideoPath;
                String videoOutPutName = UUID.randomUUID().toString().replaceAll("-","")+".mp4";
                uploadPathDB = "/"+userId+"/video/"+videoOutPutName;
                finalVideoPath = FILE_PATH + uploadPathDB;
                mergeVideoMp3.convertor(videoInPutPath,musicInputPath,videoSeconds,finalVideoPath);
            }

            //生成视频截图
            FetchVideoCover fetchVideoCover = new FetchVideoCover(FFMPEG);
            fetchVideoCover.getCover(finalVideoPath,FILE_PATH+coverPathDB);

            Video video = new Video();
            video.setAudioId(bgmId);
            video.setUserId(userId);
            video.setVideoSeconds(videoSeconds);
            video.setVideoHeight(videoHeight);
            video.setVideoWidth(videoWidth);
            video.setVideoDesc(desc);
            video.setVideoPath(uploadPathDB);
            video.setCoverPath(coverPathDB);
            videoService.saveVideo(video);
            System.out.println(video);
            return JSONResult.ok(video);
        }

    }

    @ApiOperation(value = "所有用户查询视频的接口", notes = "所有用户查询视频的接口")
    @PostMapping(value = "/selectAllVideo",produces={"application/json;charset=UTF-8"})
    JSONResult selectAllVideo(@RequestBody VideoDTO videoDTO) {
        Integer page = videoDTO.getPage();
        Integer pageSize = videoDTO.getPageSize();
        if (page == null){
            page = 1;
        }if (pageSize == null){
            pageSize = 10;
        }
        PagedResult allVideos = videoService.getAllVideos(page, pageSize);
        String currentUserId = videoDTO.getCurrentUserId();
        if (StringUtils.isNotBlank(currentUserId)){
            // 如果当前用户为登录状态，附加用户相关属性的逻辑
            // 否则按匿名用户进行处理
            List<VideoVO> videosRows = (List<VideoVO>) allVideos.getRows();
            for (VideoVO videoVO:videosRows) {
                UserLikeVideo userLikeVideo = new UserLikeVideo();
                String videoId = videoVO.getId();
                userLikeVideo.setVideoId(videoId);
                userLikeVideo.setUserId(currentUserId);
                UserLikeVideo likeVideo = videoService.getUserLikeVideo(userLikeVideo);
                // 则将用户的喜欢添加进视频中去
                if (likeVideo!=null) {
                    String likeVideoVideoId = likeVideo.getVideoId();
                    if (likeVideoVideoId.equals(videoId)){
                        // 如果查询出来当前登录用户喜欢的视频于查询出来的有相同的，则将likevideo设置为true
                        videoVO.setLikeVildeo(true);
                    }
                }
                if (!currentUserId.equals(videoVO.getUserId())){
                    UsersFans usersFans = new UsersFans();
                    usersFans.setUserId(videoVO.getUserId());
                    usersFans.setFanId(currentUserId);
                    UsersFans userFansRelation = userService.selectUserFansRelation(usersFans);
                    if (userFansRelation != null){
                        // 如果查询的值不为空则存在关注关系
                        videoVO.setLikeUser(true);
                    }else {
                        videoVO.setLikeUser(false);
                    }
                }else {
                    // 本人不需要加关注按钮
                    videoVO.setLikeUser(true);
                }
            }
            //重新设置allVides
            allVideos.setRows(videosRows);
        }
        return JSONResult.ok(allVideos);
    }

    @ApiOperation(value = "所有用户按视频描述查询视频的接口", notes = "所有用户按视频描述查询视频的接口")
    @PostMapping(value = "/selectVideoByCondition",produces={"application/json;charset=UTF-8"})
    JSONResult selectVideoByCondition(@RequestBody VideoDTO videoDTO) {
        Integer page = videoDTO.getPage();
        Integer pageSize = videoDTO.getPageSize();
        if (page == null){
            videoDTO.setPage(1);
        }if (pageSize == null){
            videoDTO.setPageSize(10);
        }
        PagedResult allVideos = videoService.getAllVideosByContidition(videoDTO);
        String currentUserId = videoDTO.getCurrentUserId();
        if (StringUtils.isNotBlank(currentUserId)){
            // 如果当前用户为登录状态，附加用户相关属性的逻辑
            // 否则按匿名用户进行处理
            List<VideoVO> videosRows = (List<VideoVO>) allVideos.getRows();
            for (VideoVO videoVO:videosRows) {
                UserLikeVideo userLikeVideo = new UserLikeVideo();
                String videoId = videoVO.getId();
                userLikeVideo.setVideoId(videoId);
                userLikeVideo.setUserId(currentUserId);
                UserLikeVideo likeVideo = videoService.getUserLikeVideo(userLikeVideo);
                // 则将用户的喜欢添加进视频中去
                if (likeVideo!=null) {
                    String likeVideoVideoId = likeVideo.getVideoId();
                    if (likeVideoVideoId.equals(videoId)){
                        // 如果查询出来当前登录用户喜欢的视频于查询出来的有相同的，则将likevideo设置为true
                        videoVO.setLikeVildeo(true);
                    }
                }
                if (!currentUserId.equals(videoVO.getUserId())){
                    UsersFans usersFans = new UsersFans();
                    usersFans.setUserId(videoVO.getUserId());
                    usersFans.setFanId(currentUserId);
                    UsersFans userFansRelation = userService.selectUserFansRelation(usersFans);
                    if (userFansRelation != null){
                        // 如果查询的值不为空则存在关注关系
                        videoVO.setLikeUser(true);
                    }else {
                        videoVO.setLikeUser(false);
                    }
                }else {
                    // 本人不需要加关注按钮
                    videoVO.setLikeUser(true);
                }
            }
            //重新设置allVides
            allVideos.setRows(videosRows);
        }
        return JSONResult.ok(allVideos);
    }

    @ApiOperation(value = "查询热搜词的接口", notes = "查询热搜词的接口")
    @PostMapping(value = "/selectHotWords",produces={"application/json;charset=UTF-8"})
    JSONResult selectHotWords() {
        return JSONResult.ok(videoService.getHotWords());
    }

    @ApiOperation(value = "查询视频的喜欢状态接口", notes = "查询视频的喜欢状态接口")
    @PostMapping(value = "/changeVideoLikeStatus",produces={"application/json;charset=UTF-8"})
    JSONResult changeVideoLikeStatus(@RequestBody LikeVideoDTO likeVideoDTO) {
        String userId = likeVideoDTO.getUserId();
        String videoId = likeVideoDTO.getVideoId();
        String videoCreatorId = likeVideoDTO.getVideoCreatorId();
        String likeStatus = likeVideoDTO.getLikeStatus();
        if (likeStatus.equals("like")){
            // 该用户喜欢该视频
            videoService.userLikeVideo(userId,videoId,videoCreatorId);
        }else if (likeStatus.equals("unlike")){
            // 该用户不喜欢该视频
            videoService.userUnLikeVideo(userId,videoId,videoCreatorId);
        }
        return JSONResult.ok(videoService.getHotWords());
    }

    @ApiOperation(value = "查询个人的视频列表页面", notes = "查询个人的视频列表页面")
    @PostMapping(value = "/getPersonalVideo",produces={"application/json;charset=UTF-8"})
    JSONResult getPersonalVideo(@RequestBody VideoDTO videoDTO){
        PagedResult personalVideo = videoService.getPersonalVideo(videoDTO);

        return JSONResult.ok(personalVideo);
    }

    @ApiOperation(value = "视频评论", notes = "视频评论")
    @PostMapping(value = "/commentVideo",produces={"application/json;charset=UTF-8"})
    JSONResult commentVideo(@RequestBody Comments comments){
        videoService.commentVideo(comments);
        return JSONResult.ok("评论成功");
    }

    @ApiOperation(value = "获取视频评论", notes = "获取视频评论")
    @PostMapping(value = "/getCommentVideo",produces={"application/json;charset=UTF-8"})
    JSONResult getCommentVideo(@RequestBody VideoDTO videoDTO){
        PagedResult commentVO = videoService.getCommentVO(videoDTO);
        return JSONResult.ok(commentVO);
    }

    @ApiOperation(value = "删除视频评论", notes = "获取视频评论")
    @PostMapping(value = "/deleteCommentVideo",produces={"application/json;charset=UTF-8"})
    JSONResult deleteCommentVideo(@RequestBody VideoDTO videoDTO){
        videoService.delteComment(videoDTO);
        return JSONResult.ok("删除评论成功");
    }


}
