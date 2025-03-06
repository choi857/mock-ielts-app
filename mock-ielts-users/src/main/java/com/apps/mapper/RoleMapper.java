package com.apps.mapper;


import com.apps.model.Role;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleMapper {
    int insert(Role record);

    int updateByPrimaryKey(Role record);

    Role selectByPrimaryKey(Integer roleId);

    int deleteByPrimaryKey(Integer roleId);
}