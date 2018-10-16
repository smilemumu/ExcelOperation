package com.szc.excel.mapper;

import com.szc.excel.domain.User;

import java.util.HashMap;
import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByUser(User user);

    List<User> selectAllUser();

    List<User> selectByLike( HashMap<String,String> map);
}