package com.jonas.service;

import com.jonas.dto.VideoDTO;
import com.jonas.pojo.Comments;
import com.jonas.pojo.UserLikeVideo;
import com.jonas.pojo.Video;
import com.jonas.utils.PagedResult;
import com.jonas.vo.CommentVO;
import com.jonas.vo.VideoVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cmj
 * @date 2019-03-15 14:17
 */
@Service
public interface VideoService {
    /**
     * 保存视频信息到数据库
     * @param video
     */
    void saveVideo(Video video);

    /**
     * 根据视频ID查询视频
     * @param videoId
     * @return
     */
    Video queryVideoById(String videoId);

    /**
     * 更新视频
     * @param video
     */
    void updateVideo(Video video);

    /**
     * 分页获取视频
     * @param page
     * @param pageSize
     * @return
     */
    PagedResult getAllVideos(Integer page,Integer pageSize);

    /**
     * 按(视频描述)条件查询视频
     * @param videoDTO
     * @return
     */
    PagedResult getAllVideosByContidition(VideoDTO videoDTO);

    /**
     * 获取热搜词
     * @return
     */
    List<String> getHotWords();

    /**
     * 用户喜欢视频的操作
     * @param userId
     * @param videoId
     * @param videoCreatorId
     */
    void userLikeVideo(String userId,String videoId,String videoCreatorId);

    /**
     * 用户不喜欢视频的操作
     * @param userId
     * @param videoId
     * @param videoCreatorId
     */
    void userUnLikeVideo(String userId,String videoId,String videoCreatorId);

    /**
     * 根据用户Id和视频Id查找是否存在用户喜欢的关联关系
     * @param userLikeVideo
     * @return
     */
    UserLikeVideo getUserLikeVideo(UserLikeVideo userLikeVideo);

    /**
     * 获取个人展示页面的相关视频信息
     * @param videoDTO
     * @return
     */
    PagedResult getPersonalVideo(VideoDTO videoDTO);

    /**
     * 视频的评论功能
     * @param comments
     */
    void commentVideo(Comments comments);

    /**
     * 获取评论信息，带分页的
     * @param videoDTO
     * @return
     */
    PagedResult getCommentVO(VideoDTO videoDTO);

    /**
     * 删除评论
     * @param videoDTO
     */
    void delteComment(VideoDTO videoDTO);
}
