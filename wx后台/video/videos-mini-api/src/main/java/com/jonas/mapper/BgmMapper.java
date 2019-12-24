package com.jonas.mapper;

import com.jonas.pojo.Bgm;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BgmMapper {
    int deleteByPrimaryKey(String id);

    int insert(Bgm record);

    Bgm selectByPrimaryKey(String id);

    List<Bgm> selectAll();

    int updateByPrimaryKey(Bgm record);
}