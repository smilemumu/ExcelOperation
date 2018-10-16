package com.szc.excel.mapper;

import com.szc.excel.domain.SysPermission;
import com.szc.excel.domain.SysRole;

import java.util.List;


public interface SysPermissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysPermission record);

    int insertSelective(SysPermission record);

    SysPermission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysPermission record);

    int updateByPrimaryKey(SysPermission record);

    List<SysPermission> getPermissions(SysRole role);
}