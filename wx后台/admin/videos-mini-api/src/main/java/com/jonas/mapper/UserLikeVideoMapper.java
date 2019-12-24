package com.jonas.mapper;

import com.jonas.pojo.UserLikeVideo;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserLikeVideoMapper {

    int deleteByPrimaryKey(String id);

    int insert(UserLikeVideo record);

    UserLikeVideo selectByPrimaryKey(String id);

    List<UserLikeVideo> selectAll();

    int updateByPrimaryKey(UserLikeVideo record);

    void deleteByUserIdAndVideoId(UserLikeVideo userLikeVideo);

    UserLikeVideo selectByUserIdAndVideoId(UserLikeVideo userLikeVideo);

}