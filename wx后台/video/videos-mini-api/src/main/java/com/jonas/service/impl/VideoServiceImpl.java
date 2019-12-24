package com.jonas.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jonas.common.idworker.Sid;
import com.jonas.dto.VideoDTO;
import com.jonas.mapper.*;
import com.jonas.pojo.Comments;
import com.jonas.pojo.SearchRecords;
import com.jonas.pojo.UserLikeVideo;
import com.jonas.pojo.Video;
import com.jonas.service.VideoService;
import com.jonas.utils.PagedResult;
import com.jonas.vo.CommentVO;
import com.jonas.vo.VideoVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author cmj
 * @date 2019-03-15 14:19
 */
@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private Sid sid;
    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private SearchRecordsMapper searchRecordsMapper;
    @Autowired
    private UserLikeVideoMapper userLikeVideoMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private CommentsMapper commentsMapper;


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveVideo(Video video) {
            video.setId(sid.nextShort());
            video.setStatus("1");//默认视频发布成功
            video.setCreateTime(new Date());
            video.setLikeCunts((long)0);
            videoMapper.insert(video);

    }

    @Override
    public Video queryVideoById(String videoId) {
        Video video=null;
        if (StringUtils.isNotBlank(videoId)){
            video = videoMapper.selectByPrimaryKey(videoId);
        }
        return video;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateVideo(Video video) {
        if (StringUtils.isNotBlank(video.getId())){
            videoMapper.updateByPrimaryKey(video);
        }
    }

    @Override
    public PagedResult getAllVideos(Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<VideoVO> videoVOS = videoMapper.selectAllVideoVo();
        PageInfo<VideoVO> videoVOList = new PageInfo<>(videoVOS);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setRecords(videoVOList.getTotal());
        pagedResult.setRows(videoVOList.getList());
        pagedResult.setTotal(videoVOList.getPages());
        return pagedResult;
    }

    @Override
    public PagedResult getAllVideosByContidition(VideoDTO videoDTO) {
        Integer page = videoDTO.getPage();
        Integer pageSize = videoDTO.getPageSize();
        List<Video> videoList = videoDTO.getVideoList();
        System.out.println(videoList);
        // 获取查询的视频描述信息
        String videoDesc = videoList.get(0).getVideoDesc();
        // 保存搜索记录
        SearchRecords searchRecords = new SearchRecords();
        searchRecords.setId(sid.nextShort());
        searchRecords.setContent(videoDesc);
        searchRecordsMapper.insert(searchRecords);

        PageHelper.startPage(page,pageSize);
        List<VideoVO> videoVOS = videoMapper.selectVideoVoByDesc(videoDesc);
        PageInfo<VideoVO> videoVOList = new PageInfo<>(videoVOS);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setRecords(videoVOList.getTotal());
        pagedResult.setRows(videoVOList.getList());
        pagedResult.setTotal(videoVOList.getPages());
        return pagedResult;
    }

    @Override
    public List<String> getHotWords() {
        return searchRecordsMapper.getHotWords();
    }

    @Transactional
    @Override
    public void userLikeVideo(String userId, String videoId, String videoCreatorId) {
        if (
                (userId !=null|| !"".equals(userId) )&&
                (videoId !=null|| !"".equals(videoId))&&
                (videoCreatorId !=null|| !"".equals(videoCreatorId))

        ){
            // 保存到用户喜欢点赞的关联关系表
            UserLikeVideo userLikeVideo = new UserLikeVideo();
            userLikeVideo.setId(sid.nextShort());
            userLikeVideo.setUserId(userId);
            userLikeVideo.setVideoId(videoId);
            userLikeVideoMapper.insert(userLikeVideo);

            // 视频喜欢数量的累加
            videoMapper.addVideoLikeCount(videoId);
            // 用户受喜欢的数量累加
            userInfoMapper.addReceiveLikeCount(videoCreatorId);

        }
    }

    @Transactional
    @Override
    public void userUnLikeVideo(String userId, String videoId, String videoCreatorId) {
        if (
                (userId !=null|| !"".equals(userId) )&&
                (videoId !=null|| !"".equals(videoId))&&
                (videoCreatorId !=null|| !"".equals(videoCreatorId))

        ){
            // 删除用户喜欢点赞的关联关系
            UserLikeVideo userLikeVideo = new UserLikeVideo();
            userLikeVideo.setUserId(userId);
            userLikeVideo.setVideoId(videoId);
            userLikeVideoMapper.deleteByUserIdAndVideoId(userLikeVideo);

            // 视频喜欢数量的累加
            videoMapper.reduceVideoLikeCount(videoId);
            // 用户受喜欢的数量累加
            userInfoMapper.reduceReceiveLikeCount(videoCreatorId);

        }
    }

    @Override
    public UserLikeVideo getUserLikeVideo(UserLikeVideo userLikeVideo) {
        UserLikeVideo userLikeVideoDB = userLikeVideoMapper.selectByUserIdAndVideoId(userLikeVideo);
        return userLikeVideoDB;
    }

    @Override
    public PagedResult getPersonalVideo(VideoDTO videoDTO) {
        Integer page = videoDTO.getPage();
        Integer pageSize = videoDTO.getPageSize();
        if (page == null){
            page = 1;
        }if (pageSize == null){
            pageSize = 10;
        }
        PageHelper.startPage(page,pageSize);
        List<VideoVO> voList = null;
        if (StringUtils.isNotBlank(videoDTO.getCurrentUserId())&&StringUtils.isNotBlank(videoDTO.getStatus())){
            if (videoDTO.getStatus().equals("work")){
                voList = videoMapper.selectMyVideo(videoDTO.getCurrentUserId());
            }else if (videoDTO.getStatus().equals("like")){
                voList = videoMapper.selectMyLikeVideo(videoDTO.getCurrentUserId());
            }else if (videoDTO.getStatus().equals("follow")){
                voList = videoMapper.selectMyFollowVideo(videoDTO.getCurrentUserId());
            }
        }
        PageInfo<VideoVO> videoVOList = new PageInfo<>(voList);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setRecords(videoVOList.getTotal());
        pagedResult.setRows(videoVOList.getList());
        pagedResult.setTotal(videoVOList.getPages());
        return pagedResult;
    }

    @Override
    public void commentVideo(Comments comments) {
        comments.setId(sid.nextShort());
        comments.setCreateTime(new Date());
        commentsMapper.insert(comments);
    }

    @Override
    public PagedResult getCommentVO(VideoDTO videoDTO) {
        Integer page = videoDTO.getPage();
        Integer pageSize = videoDTO.getPageSize();
        if (videoDTO.getPage() == null){
            page = 1;
        }if (videoDTO.getPageSize() == null){
            pageSize = 10;
        }
        String videoId = videoDTO.getVideoList().get(0).getId();
        PageHelper.startPage(page,pageSize);
        List<CommentVO> commentByVideoId = commentsMapper.getCommentByVideoId(videoId);
        PageInfo<CommentVO> commentVOPageInfo = new PageInfo<>(commentByVideoId);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setRows(commentVOPageInfo.getList());
        pagedResult.setTotal(commentVOPageInfo.getPages());
        pagedResult.setRecords(commentVOPageInfo.getTotal());
        return pagedResult;
    }

    @Override
    public void delteComment(VideoDTO videoDTO) {
        commentsMapper.deleteByPrimaryKey(videoDTO.getCommentId());
    }


}
