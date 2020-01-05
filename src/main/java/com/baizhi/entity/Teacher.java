package com.baizhi.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
    public class Teacher implements Serializable {
        @Excel(name = "编号",needMerge=true)
        private int id;
        @Excel(name = "姓名",needMerge=true)
        private String name;
        @Excel(name = "照片",type = 2,width = 30,height = 20,needMerge = true)
        private String src;
        @ExcelCollection(name = "学生们")
        private List<Student> students;
}
