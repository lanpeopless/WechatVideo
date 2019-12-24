package com.jonas.mapper;

import com.jonas.pojo.Bgm;
import com.jonas.pojo.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserInfoMapper {

    int deleteByPrimaryKey(String id);

    int insert(UserInfo record);

    UserInfo selectByPrimaryKey(String id);

    List<UserInfo> selectAll();

    int updateByPrimaryKey(UserInfo record);

    UserInfo selectOne(String userName);

    void addReceiveLikeCount(@Param("userId") String userId);

    void reduceReceiveLikeCount(@Param("userId") String userId);

    void addFansCount(@Param("userId") String userId);

    void reduceFansCount(@Param("userId") String userId);

    void addFollowCount(@Param("userId") String userId);

    void reduceFollowCount(@Param("userId") String userId);

    List<UserInfo> getUserInfoByCondition(UserInfo userInfo);
}