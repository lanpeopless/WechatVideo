package com.jonas.mapper;

import com.jonas.pojo.UsersFans;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UsersFansMapper {
    int deleteByPrimaryKey(String id);

    int insert(UsersFans record);

    UsersFans selectByPrimaryKey(String id);

    List<UsersFans> selectAll();

    int updateByPrimaryKey(UsersFans record);

    void deleteByUserIdAndFanId(UsersFans usersFans);

    UsersFans selectByUserIdAndFanId(UsersFans usersFans);
}