package com.baizhi.service;

import com.baizhi.entity.Chapter;
import com.baizhi.vo.VoChapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface ChapterService {
    //分页查询
    Map<String,Object> selectByPage(Integer page, Integer count,String aid);
    //添加
    void insert(Chapter chapter,String aid);
    //添加2
    void append(String id,String size,String time,String src);
    //删除
    void delAll(String[] id);
    //修改
    void updateChapter(Chapter chapter);
    //根据专辑名查询所有单曲
    List<VoChapter> selectAllByAid(String id);
    //文件下载
    void download(HttpServletRequest request, String src, HttpServletResponse response);
}
