package com.szc.excel.mapper;

import java.util.HashMap;
import java.util.List;

import com.szc.excel.domain.Archives;

public interface ArchivesMapper {
    int deleteByPrimaryKey(Integer virtualId);

    int insert(Archives record);

    int insertSelective(Archives record);

    Archives selectByPrimaryKey(Integer virtualId);

    int updateByPrimaryKeySelective(Archives record);

    int updateByPrimaryKey(Archives record);

	List<Archives> selectByColumSelective(HashMap<String, Object> sel);

	int selectByColumSelectiveCount(HashMap<String, Object> sel);
}