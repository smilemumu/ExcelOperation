package com.szc.excel.mapper;

import com.szc.excel.domain.Archives;
import com.szc.excel.domain.Archives2;
import com.szc.excel.domain.Statistics;
import com.szc.excel.domain.TypeStatistics;

import java.util.HashMap;
import java.util.List;

public interface Archives2Mapper {
    int deleteByPrimaryKey(Integer virtualId);

    int insert(Archives2 record);

    int insertSelective(Archives2 record);

    Archives2 selectByPrimaryKey(Integer virtualId);

    int updateByPrimaryKeySelective(Archives2 record);

    int updateByPrimaryKey(Archives2 record);

    List<Archives2> selectByColumSelective(HashMap<String, Object> sel);

    int selectByColumSelectiveCount(HashMap<String, Object> sel);

    List<Archives2> selectExpiredData();

    Statistics selectStatistics(HashMap<String,Object> sel);

    List<TypeStatistics> selectTypeStatistics(HashMap<String,Object> sel);
}