package com.baizhi.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student implements Serializable {
    @Excel(name = "编号")
    private Integer id;
    @Excel(name = "姓名")
    private String name;
    @Excel(name = "性别")
    private String sex;
    @Excel(name = "入职时间",format = "yyyy年MM月dd号",width = 50)
    private Date bir;
    @Excel(name = "部门编号")
    private String clazz;
    @Excel(name = "教师id")
    private Integer t_id;
}