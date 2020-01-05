package com.baizhi.controller;

import com.baizhi.entity.Chapter;
import com.baizhi.service.ChapterService;
import com.baizhi.serviceImpl.BannerServiceImpl;
import org.apache.commons.io.FilenameUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("chapter")
public class ChapterController {
    @Autowired
    private ChapterService chapterService;
    @RequestMapping("selectByPage")
    @ResponseBody
    public Map<String,Object> selectByPage(Integer page,int rows,String aid){
        Map<String, Object> map =chapterService.selectByPage(page, rows,aid);
        return map;
    }
    @RequestMapping("editChapter")
    @ResponseBody
    public Map<String,Object> editChapter(String oper,Chapter chapter,String aid,String[] id){
        if ("add".equals(oper)){
            String myid = BannerServiceImpl.getID();
            chapter.setId(myid);
            chapterService.insert(chapter,aid);
            Map<String,Object> map=new HashMap<>();
            map.put("id",myid);
            return map;
        }
        if ("del".equals(oper)){
            chapterService.delAll(id);
        }
        if ("edit".equals(oper)){
            chapterService.updateChapter(chapter);
            Map<String,Object> map=new HashMap<>();
            if ("".equals(chapter.getSrc())){
                map.put("id","-1");
                return map;
            }else {
                map.put("id",chapter.getId());
                return map;
            }
        }
        return null;
    }
    @RequestMapping("append")
    @ResponseBody
    public void append(HttpSession session, String id, MultipartFile src){
        //上传文件
        String path = session.getServletContext().getRealPath("音频_upload");
        File file = new File(path);
        if (!file.exists()){
            file.mkdirs();
        }
        String filename = src.getOriginalFilename();
        String newName=new Date().getTime()+"_"+filename;
        try {
            src.transferTo(new File(path+"/"+newName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取文件大小
        long size = src.getSize();
        BigDecimal bigDecimal=new BigDecimal(size);
        BigDecimal bmb=new BigDecimal(1024*1024);
        BigDecimal divide = bigDecimal.divide(bmb, 2, BigDecimal.ROUND_HALF_UP);
        String time =divide+"MB";
        System.out.println(time);
        //获取时长
        try {
            AudioFile read = AudioFileIO.read(new File(path, newName));
            AudioHeader audioHeader = read.getAudioHeader();
            int trackLength = audioHeader.getTrackLength();
            String second = trackLength % 60 + "秒";
            String minute = trackLength / 60 + "分";
            //修改数据库数据
            chapterService.append(id,time,minute+second,newName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //文件下载
    @RequestMapping("download")
    public void download(HttpServletRequest request, String src, HttpServletResponse response){
        chapterService.download(request,src,response);
    }
}
