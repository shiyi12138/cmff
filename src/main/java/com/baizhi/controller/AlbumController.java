package com.baizhi.controller;

import com.baizhi.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Map;

@Controller
@RequestMapping("album")
public class AlbumController {
    @Autowired
    private AlbumService albumService;
    @RequestMapping("selectByPage")
    @ResponseBody
    public Map<String,Object> selectByPage(Integer page,int rows){
        Map<String, Object> map =albumService.selectByPage(page, rows);
        return map;
    }
}
