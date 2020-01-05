package com.baizhi.service;

import com.baizhi.entity.Banner;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BannerService {
    //分页查询
    Map<String,Object> selectByPage(Integer page, Integer rows);
    //添加
    String insert(Banner banner);
    //修改图片路径
    void updateSrc(String src,String id);
    //删除
    void delAll(String[] ids);
    //修改
    void updateBanner(Banner banner);
    //poi导出
    void outPoi(String path);
}
