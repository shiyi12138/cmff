package com.baizhi.dao;

import com.baizhi.entity.Student;

import java.util.List;

public interface StudentMapper {
    List<Student> secectAll();
    //批量插入
    void insertAll(List<Student> list);
}