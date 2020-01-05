package com.baizhi.serviceImpl;

import com.baizhi.annotation.ClearCache;
import com.baizhi.annotation.MyInclude;
import com.baizhi.dao.ChapterDao;
import com.baizhi.entity.Chapter;
import com.baizhi.service.ChapterService;
import com.baizhi.vo.VoChapter;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
@Transactional
public class ChapterServiceImpl implements ChapterService {
    @Autowired
    private ChapterDao chapterDao;
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    @MyInclude
    public Map<String, Object> selectByPage(Integer page, Integer count,String aid) {
        //数据
        List<Chapter> chapters = chapterDao.selectByPage((page - 1) * count, count,aid);
        //总条数
        Integer  sumCount= chapterDao.selectCount(aid);
        //总页数
        Integer total=sumCount%count==0?sumCount/count:sumCount/count+1;
        Map<String,Object> map=new HashMap<>();
        map.put("rows",chapters);
        map.put("page",page);
        map.put("total",total);
        map.put("records",sumCount);
        return map;
    }
    public void insert(Chapter chapter,String aid) {
        chapter.setAlbum_id(aid);
        chapterDao.insert(chapter);
    }

    @Override
    @ClearCache
    public void append(String id, String size, String time, String src) {
        chapterDao.append(id,size,time,src);
    }

    @Override
    @ClearCache
    public void delAll(String[] id) {
        chapterDao.delAll(id);
    }

    @Override
    @ClearCache
    public void updateChapter(Chapter chapter) {
        chapterDao.updateChapter(chapter);
    }

    @Override
    @MyInclude
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<VoChapter> selectAllByAid(String id) {
        List<VoChapter> voChapters = chapterDao.selectAllByAid(id);
        for (VoChapter voChapter : voChapters) {
            voChapter.setDownload_url("http://音频_upload"+voChapter.getDownload_url());
        }
        return voChapters;
    }

    //文件下载
    public void download(HttpServletRequest request, String src, HttpServletResponse response) {
        String path = request.getSession().getServletContext().getRealPath("音频_upload");
        FileInputStream is= null;
        ServletOutputStream outputStream=null;
        try {
            is = new FileInputStream(new File(path,src));
            outputStream = response.getOutputStream();
            //去掉拼接的时间戳
            int i = src.indexOf("_");
            String substring = src.substring(i+1);
            String encode = URLEncoder.encode(substring, "UTF-8");
            response.setHeader("content-disposition","attachment;fileName="+encode);
            //设置文件的响应类型
            String extension = FilenameUtils.getExtension(src);
            String mimeType = request.getSession().getServletContext().getMimeType("."+extension);
            response.setContentType(mimeType);
            byte[] bytes=new byte[1024];
            int len=0;
            while ((len=is.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
