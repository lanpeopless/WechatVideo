package com.jonas.mapper;

import com.jonas.pojo.UserReport;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserReportMapper {
    int deleteByPrimaryKey(String id);

    int insert(UserReport record);

    UserReport selectByPrimaryKey(String id);

    List<UserReport> selectAll();

    int updateByPrimaryKey(UserReport record);
}