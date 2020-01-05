package com.baizhi.dao;

import com.baizhi.entity.Article;
import com.baizhi.vo.Cover;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleDao {
    //分页查询(传入起始条数和每页张数条数)
    List<Article> selectByPage(@Param("start") Integer start, @Param("count") Integer count);
    //查询总条数
    Integer selectCount();
    //删除
    void delAll(String[] ids);
    //添加文章
    void insert(Article article);
    //通过id查询
    Article selectById(String id);
    //修改
    void update(Article article);
    //最近发布3篇
    List<Cover> select3Time(String id);
    //查询自己上师的文章
    List<Cover> selectByID(String gid);
    //查询其他上师的文章
    List<Cover> selectByNoID(String gid);
}