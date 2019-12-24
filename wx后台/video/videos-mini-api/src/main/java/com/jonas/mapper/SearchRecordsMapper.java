package com.jonas.mapper;

import com.jonas.pojo.SearchRecords;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SearchRecordsMapper {

    int insert(SearchRecords record);

    List<SearchRecords> selectAll();

    List<String> getHotWords();
}