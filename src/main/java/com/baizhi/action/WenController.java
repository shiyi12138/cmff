package com.baizhi.action;

import com.baizhi.service.AlbumService;
import com.baizhi.service.ArticleService;
import com.baizhi.service.BannerService;
import com.baizhi.service.ChapterService;
import com.baizhi.vo.VoAlbum;
import com.baizhi.vo.VoChapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/detail")
public class WenController {
    @Autowired
    private AlbumService albumService;
    @Autowired
    private ChapterService chapterService;
    @RequestMapping("/wen")
    @ResponseBody
    public Map<String,Object> login(String id, String uid){
        Map<String,Object> map=new HashMap<>();
        //准备专辑详情
        VoAlbum voAlbum = albumService.selectById(id);
        map.put("introduction",voAlbum);
        //准备单曲的具体信息
        List<VoChapter> voChapters = chapterService.selectAllByAid(id);
        map.put("list",voChapters);
        return map;
    }
}
