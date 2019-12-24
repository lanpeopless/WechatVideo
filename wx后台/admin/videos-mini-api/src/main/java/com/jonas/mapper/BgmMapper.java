package com.jonas.mapper;

import com.jonas.pojo.Bgm;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BgmMapper {

    String getBgmId();

    int deleteByPrimaryKey(String id);

    int insert(Bgm record);

    Bgm selectByPrimaryKey(String id);

    List<Bgm> selectAll();

    int updateByPrimaryKey(Bgm record);

    List<Bgm> getBgmByCondition(Bgm bgm);

    Bgm getBgmById(String id);
}