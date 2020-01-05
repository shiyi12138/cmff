package com.baizhi.action;

import com.baizhi.entity.Admin;
import com.baizhi.entity.Banner;
import com.baizhi.service.AdminService;
import com.baizhi.service.AlbumService;
import com.baizhi.service.ArticleService;
import com.baizhi.service.BannerService;
import com.baizhi.vo.Cover;
import com.baizhi.vo.VoBanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping
public class FirstController {
    @Autowired
    private BannerService bannerService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private ArticleService articleService;
    @RequestMapping("/first_page")
    @ResponseBody
    public Map<String,Object> main(String uid, String type, String sub_type){

        Map<String,Object> map=new HashMap<>();
        if ("all".equals(type)){
            //准备轮播图
            Map<String, Object> map1 = bannerService.selectByPage(1, 5);
            List<Banner> rows = (List<Banner>) map1.get("rows");
            List<VoBanner> list = new ArrayList<>();
            for (Banner banner : rows) {
                VoBanner voBanner = new VoBanner();
                voBanner.setThumbnail(banner.getImg());
                voBanner.setDesc(banner.getTitle());
                voBanner.setId(banner.getId());
                list.add(voBanner);
            }
            map.put("header",list);
            //准备吉祥妙音
            List<Cover> album = albumService.select6Time();
            //准备甘露妙宝
            List<Cover> article = articleService.select3Time(uid);
            for (Cover cover : article) {
                album.add(cover);
            }
            //将将他们一起加到body中
            map.put("body",album);
        }
        if ("wen".equals(type)){
            //准备吉祥妙音
            List<Cover> album = albumService.selectAll();
            //以album返回
            map.put("album",album);
        }
        if ("si".equals(type)){
            //判断查询的是上师言教还是显密法要\
            //上师言教
            if("ssyj".equals(sub_type)){
                List<Cover> covers = articleService.selectByID(uid);
                map.put("artical",covers);
            }
            //显密法要
            if ("xmfy".equals(sub_type)){
                List<Cover> covers = articleService.selectByNoID(uid);
                map.put("artical",covers);
            }
        }
        return map;
    }
}
