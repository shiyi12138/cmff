package com.baizhi.dao;

import com.baizhi.entity.Echarts;
import com.baizhi.entity.User;
import com.baizhi.entity.Vo;

import java.util.List;

public interface UserDao {
    //查询7天注册人数
    List<Vo> select7Num();
    //查询1-12月份的注册人数
    List<Vo> selectMonthNum();
    //查询每个地区的总人数
    List<Echarts> selectTotalCount();
    //插入
    void insert(User user);
    //删除
    void del(String id);
    //查询用户所属上师id
    String selectGid(String uid);
}