package com.baizhi.service;

import com.baizhi.entity.Admin;

public interface AdminService {
    //查询用户
    Admin selectAdminByName(String name, String password);
    //导出
    void outPoi();
}
