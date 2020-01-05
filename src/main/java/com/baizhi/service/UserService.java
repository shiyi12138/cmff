package com.baizhi.service;

import com.baizhi.entity.Echarts;
import com.baizhi.entity.Vo;

import java.util.List;

public interface UserService {
    //查询七天内所有的注册人数
    int[] select7Num();
    //查询1-12月份的注册人数
    int[] selectMonthNum();
    //查询每个地区的总人数
    List<Echarts> selectTotalCount();
    //删除
    void del(String id);
}
