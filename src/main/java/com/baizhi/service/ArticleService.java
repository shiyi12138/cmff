package com.baizhi.service;

import com.baizhi.entity.Article;
import com.baizhi.vo.Cover;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface ArticleService {
    //分页查询
    Map<String,Object> selectByPage(Integer page,Integer rows);
    //删除
    void delAll(String[] id);
    //添加文章
    boolean insert(Article article,String path);
    //通过id查询
    Article selectById(String id, String path, HttpSession session);
    //修改
    void update(Article article,String path, HttpSession session);
    //最近发布3篇
    List<Cover> select3Time(String uid);
    //查询自己上师的文章
    List<Cover> selectByID(String uid);
    //查询其他上师的文章
    List<Cover> selectByNoID(String uid);
}
