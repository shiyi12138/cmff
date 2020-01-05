package com.baizhi.dao;

import com.baizhi.entity.Banner;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BannerDao {
    //分页查询(传入起始条数和每页张数条数)
    List<Banner> selectByPage(@Param("start") Integer start, @Param("count") Integer count);
    //查询总条数
    Integer selectCount();
    //添加
    void insert(Banner banner);
    //修改图片路径
    void updateSrc(@Param("src")String src,@Param("id")String id);
    //删除
    void delAll(String[] ids);
    //修改
    void updateBanner(Banner banner);
    //查询所有
    List<Banner> selectAll();
}
