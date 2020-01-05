package com.baizhi.controller;

import com.baizhi.entity.Article;
import com.baizhi.service.ArticleService;
import org.apache.commons.io.FilenameUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @RequestMapping("selectByPage")
    @ResponseBody
    public Map<String,Object> selectByPage(Integer page, int rows){
        Map<String, Object> map = articleService.selectByPage(page, rows);
        return map;
    }
    @RequestMapping("edit")
    @ResponseBody
    public Map<String,Object> edit(Article article, String oper,String[] id){
        if ("del".equals(oper)){
            articleService.delAll(id);
            return null;
        }
        return null;
    }
    @RequestMapping("insert")
    @ResponseBody
    public Map<String,Object> insert(Article article,HttpSession session){
        String path = session.getServletContext().getRealPath("Kindeditor_content");
        boolean insert = articleService.insert(article,path);
        Map<String,Object> map=new HashMap<>();
        if (insert){
            map.put("toten","添加成功");
        }else {
            map.put("toten","当前用户表示上师，不能发表文章");
        }
        return map;
    }
    @RequestMapping("update")
    @ResponseBody
    public void update(Article article,HttpSession session){
        String path = session.getServletContext().getRealPath("Kindeditor_content");
        System.out.println(article);
        articleService.update(article,path,session);
    }
    @RequestMapping("selectById")
    @ResponseBody
    public Article selectById(String id,HttpSession session){
        Article article = articleService.selectById(id, session.getServletContext().getRealPath("Kindeditor_content"),session);
        System.out.println(article);
        System.out.println( session.getServletContext().getRealPath("Kindeditor_content"));
        return article;
    }
    @RequestMapping("uploadImg")
    @ResponseBody
    public Map<String,Object> uploadImg(HttpServletRequest request,MultipartFile img){
        //上传文件
        String path = request.getSession().getServletContext().getRealPath("Kindeditor_upload");
        File file = new File(path);
        if (!file.exists()){
            file.mkdirs();
        }
        String filename = img.getOriginalFilename();
        String newName=new Date().getTime()+"_"+filename;
        Map<String,Object> map=new HashMap<>();
        try {
            img.transferTo(new File(path+"/"+newName));
            map.put("error",0);
            //获取http
            String scheme = request.getScheme();
            //获取本机ip
            InetAddress localHost = InetAddress.getLocalHost();
            String localhost = localHost.toString().split("/")[1];
            //获取项目名
            String contextPath = request.getContextPath();
            //获取端口号
            int serverPort = request.getServerPort();
            //拼接
            String url = scheme + "://" + localhost + ":" + serverPort + contextPath + "/Kindeditor_upload/" + newName;
            map.put("url",url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
    @RequestMapping("getAllImgs")
    @ResponseBody
        /*
    *
    *       需要返回的数据，但是它不需要接受参数
    * {
    "moveup_dir_path": "",
    "current_dir_path": "",
    "current_url": "/ke4/php/../attached/",
    "total_count": 5,
    "file_list": [
        {
            "is_dir": false,
            "has_file": false,
            "filesize": 208736,
            "dir_path": "",
            "is_photo": true,
            "filetype": "jpg",
            "filename": "1241601537255682809.jpg",
            "datetime": "2018-06-06 00:36:39"
        }
     ]
}*/
    public Map<String,Object> getAllImgs(HttpServletRequest request){
        Map<String,Object> map=new HashMap<>();
        String path = request.getSession().getServletContext().getRealPath("Kindeditor_upload");
        File file = new File(path);
        String[] list = file.list();
        //创建一个存放所以图片信息的集合
        List<Object> imgs=new ArrayList<>();
        for (String s : list) {
            Map<String,Object> hashMap=new HashMap<>();
            File file1 = new File(path,s);
            hashMap.put("is_dir",false);
            hashMap.put("has_file",false);
            hashMap.put("filesize",file1.length());
            hashMap.put("dir_path","");
            hashMap.put("is_photo",true);
            String extension = FilenameUtils.getExtension(s);
            hashMap.put("filetype",extension);
            hashMap.put("filename",s);
            Long aLong = Long.valueOf(s.split("_")[0]);
            Date date = new Date(aLong);
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String format = simpleDateFormat.format(date);
            hashMap.put("datetime",format);
            imgs.add(hashMap);
        }
        map.put("file_list",imgs);
        map.put("moveup_dir_path","");
        map.put("current_dir_path","");
        map.put("total_count",imgs.size());
        //获取http
        String scheme = request.getScheme();
        //获取本机ip
        InetAddress localHost = null;
        try {
            localHost = InetAddress.getLocalHost();
            String localhost = localHost.toString().split("/")[1];
            //获取项目名
            String contextPath = request.getContextPath();
            //获取端口号
            int serverPort = request.getServerPort();
            //拼接
            String url = scheme + "://" + localhost + ":" + serverPort + contextPath + "/Kindeditor_upload/";
            map.put("current_url",url);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return map;
    }
}
