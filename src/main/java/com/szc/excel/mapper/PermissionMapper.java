package com.szc.excel.mapper;

import com.szc.excel.domain.Permission;
import com.szc.excel.domain.User;

import java.util.List;

public interface PermissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Permission record);

    int insertSelective(Permission record);

    Permission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Permission record);

    int updateByPrimaryKey(Permission record);

    List<Permission> selectPermissionByUser(User user2);

    void deletePermissionByUser(User user);
}