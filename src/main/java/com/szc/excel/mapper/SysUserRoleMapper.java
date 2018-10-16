package com.szc.excel.mapper;

import com.szc.excel.domain.SysUserRole;

public interface SysUserRoleMapper {
    int insert(SysUserRole record);

    int insertSelective(SysUserRole record);
}