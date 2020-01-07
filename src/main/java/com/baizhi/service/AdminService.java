package com.baizhi.service;

import com.baizhi.entity.Admin;

import java.util.List;

public interface AdminService {
    //查询用户
    Admin selectAdminByName(String name, String password);
    //导出
    void outPoi();
    //查询用户所有的角色
    List<String> selectAllRole(String username);
    //查询用户所有的权限
    List<String> selectAllPremission(String role);
}
