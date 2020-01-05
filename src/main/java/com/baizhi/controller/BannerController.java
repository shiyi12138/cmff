package com.baizhi.controller;

import com.baizhi.entity.Banner;
import com.baizhi.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.lang.model.element.VariableElement;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("banner")
public class BannerController {
    @Autowired
    private BannerService bannerService;
    @RequestMapping("selectByPage")
    @ResponseBody
    public Map<String,Object> selectByPage(Integer page,int rows){
        Map<String, Object> map = bannerService.selectByPage(page, rows);
        return map;
    }
    @RequestMapping("edit")
    @ResponseBody
    public Map<String,Object> edit(Banner banner,String oper){
        if ("add".equals(oper)){
            String id= bannerService.insert(banner);
            Map<String,Object> map=new HashMap<>();
            map.put("id",id);
            return map;
        }
        if ("del".equals(oper)){
            String[] split = banner.getId().split(",");
            bannerService.delAll(split);
        }
        if ("edit".equals(oper)){
            bannerService.updateBanner(banner);
            Map<String,Object> map=new HashMap<>();
            if (!banner.getImg().equals("")){
                map.put("id",banner.getId());
            }else {
                map.put("token","ok");
            }
            return map;
        }
        return null;
    }
    @RequestMapping("updateSrc")
    @ResponseBody
    public void updateSrc(HttpSession session, String bannerId, MultipartFile img) {
        //上传文件
        String path = session.getServletContext().getRealPath("image_upload");
        String filename = img.getOriginalFilename();
        String newName=new Date().getTime()+"_"+filename;
        try {
            img.transferTo(new File(path+"/"+newName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //修改数据库数据
        bannerService.updateSrc(newName,bannerId);
    }
    @RequestMapping("outPoi")
    @ResponseBody
    public void outPoi(HttpSession session) {
        String path = session.getServletContext().getRealPath("image_upload");
        bannerService.outPoi(path);
    }
}
