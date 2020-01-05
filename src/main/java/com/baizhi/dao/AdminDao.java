package com.baizhi.dao;

import com.baizhi.entity.Admin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminDao {
    //查询用户
    Admin selectAdminByName(@Param("name") String name,@Param("password") String password);
    //查询所有
    List<Admin> selectAll();
}
