package com.szc.excel.mapper;

import com.szc.excel.domain.SysRole;
import com.szc.excel.domain.UserInfo;

import java.util.List;


public interface SysRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    List<SysRole> getRoleList(UserInfo userInfo);
}