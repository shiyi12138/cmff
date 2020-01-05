package com.baizhi.service;

import com.baizhi.entity.Album;
import com.baizhi.vo.Cover;
import com.baizhi.vo.VoAlbum;

import java.util.List;
import java.util.Map;

public interface AlbumService {
    //分页查询
    Map<String,Object> selectByPage(Integer page, Integer count);
    //查询最新上架的6本专辑
    List<Cover> select6Time();
    //查询所有专辑
    List<Cover> selectAll();
    //查询专辑详情
    VoAlbum selectById(String id);
}
