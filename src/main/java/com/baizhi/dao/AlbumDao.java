package com.baizhi.dao;

import com.baizhi.entity.Album;
import com.baizhi.vo.Cover;
import com.baizhi.vo.VoAlbum;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AlbumDao {
    //分页查询(传入起始条数和每页张数条数)
    List<Album> selectByPage(@Param("start") Integer start, @Param("count") Integer count);
    //查询总条数
    Integer selectCount();
    //查询最新上架的6本专辑
    List<Cover> select6Time();
    //查询所有专辑
    List<Cover> selectAll();
    //查询专辑详情
    VoAlbum selectById(String id);
}