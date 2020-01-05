package com.baizhi.dao;

import com.baizhi.entity.Chapter;
import com.baizhi.vo.VoChapter;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ChapterDao {
    //分页查询(传入起始条数和每页张数条数)
    List<Chapter> selectByPage(@Param("start") Integer start, @Param("count") Integer count,@Param("aid") String aid);
    //查询总条数
    Integer selectCount(String aid);
    //添加
    void insert(Chapter chapter);
    //添加2
    void append(@Param("id")String id,@Param("size")String size,@Param("time")String time,@Param("src")String src);
    //删除
    void delAll(String[] id);
    //修改
    void updateChapter(Chapter chapter);
    //根据专辑名查询所有单曲
    List<VoChapter> selectAllByAid(String id);
}