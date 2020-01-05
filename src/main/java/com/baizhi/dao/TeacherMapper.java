package com.baizhi.dao;

import com.baizhi.entity.Teacher;

import java.util.List;

public interface TeacherMapper {
    List<Teacher> secectAll();
    //批量插入
    void insertAll(List<Teacher> list);
}