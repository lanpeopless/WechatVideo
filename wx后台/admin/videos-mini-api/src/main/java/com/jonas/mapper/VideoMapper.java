package com.jonas.mapper;

import com.jonas.pojo.Video;
import com.jonas.vo.VideoVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface VideoMapper {
    int deleteByPrimaryKey(String id);

    int insert(Video record);

    Video selectByPrimaryKey(String id);

    List<Video> selectAll();

    int updateByPrimaryKey(Video record);

    List<VideoVO> selectAllVideoVo();

    List<VideoVO> selectVideoVoByDesc(@Param("videoDesc")String videoDesc);

    void addVideoLikeCount(@Param("videoId")String videoId);

    void reduceVideoLikeCount(@Param("videoId")String videoId);

    List<VideoVO> selectMyVideo(@Param("userId") String userId);

    List<VideoVO> selectMyLikeVideo(@Param("userId") String userId);

    List<VideoVO> selectMyFollowVideo(@Param("userId") String userId);
}