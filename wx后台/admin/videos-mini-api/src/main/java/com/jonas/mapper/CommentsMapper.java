package com.jonas.mapper;

import com.jonas.pojo.Comments;
import com.jonas.vo.CommentVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommentsMapper {
    int deleteByPrimaryKey(String id);

    int insert(Comments record);

    Comments selectByPrimaryKey(String id);

    List<Comments> selectAll();

    int updateByPrimaryKey(Comments record);

    List<CommentVO> getCommentByVideoId(@Param("videoId") String videoId);
}