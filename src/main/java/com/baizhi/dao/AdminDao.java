package com.baizhi.dao;

import com.baizhi.entity.Admin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminDao {
    //查询用户
    Admin selectAdminByName(@Param("name") String name,@Param("password") String password);
    //查询用户
    Admin selectAdmin(String name);
    //查询所有
    List<Admin> selectAll();
    //查询用户所有的角色
    List<String> selectAllRole(String username);
    //查询用户所有的权限
    List<String> selectAllPremission(String role);
}
