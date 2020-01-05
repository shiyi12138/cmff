package com.baizhi.serviceImpl;

import com.baizhi.annotation.ClearCache;
import com.baizhi.annotation.MyInclude;
import com.baizhi.dao.ArticleDao;
import com.baizhi.dao.UserDao;
import com.baizhi.entity.Article;
import com.baizhi.service.ArticleService;
import com.baizhi.vo.Cover;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private UserDao userDao;
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    @MyInclude
    public Map<String, Object> selectByPage(Integer page, Integer rows) {
        Map<String, Object> map=new HashMap<>();
        int start=(page-1)*rows;
        //数据
        List<Article> articles= articleDao.selectByPage(start, rows);
        //总条数
        Integer counts = articleDao.selectCount();
        //总页数
        Integer pages=counts%rows==0?counts/rows:counts/rows+1;
        map.put("rows",articles);
        map.put("page",page);
        map.put("total",pages);
        map.put("records",counts);
        return map;
    }

    @Override
    @ClearCache
    public void delAll(String[] id) {
        articleDao.delAll(id);
    }
    @Override
    @ClearCache
    public boolean insert(Article article,String path) {
        //先去查一下有没有这个上师先省略了
        //生出id
        String id = BannerServiceImpl.getID();
        String name=new Date().getTime()+"_"+article.getTitle();
        FileOutputStream fileOutputStream=null;
        //将content存到文件中，将文件名存入数据库
        try {
            fileOutputStream=new FileOutputStream(new File(path, name));
            fileOutputStream.write(article.getContent().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        article.setId(id);
        article.setGuru_Id("0");
        article.setContent(name);
        articleDao.insert(article);
        return true;
    }

    @Override
    @ClearCache
    public Article selectById(String id, String path, HttpSession session) {
        Article article = articleDao.selectById(id);
        BufferedReader bufferedReader=null;
        StringBuilder stringBuilder=null;
        try {
            bufferedReader=new BufferedReader(new FileReader(new File(path,article.getContent())));
            stringBuilder=new StringBuilder();
            String line=null;
            while ((line=bufferedReader.readLine())!=null){
                stringBuilder.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        session.setAttribute("filename",article.getContent());
        article.setContent(stringBuilder.toString());
        return article;
    }
    //修改
    @Override
    @ClearCache
    public void update(Article article,String path, HttpSession session) {
        if (!article.getContent().equals("")){
            String  filename = (String) session.getAttribute("filename");
            //更新文件的数据
            FileOutputStream fileOutputStream=null;
            //将content存到文件中，将文件名存入数据库
            try {
                fileOutputStream=new FileOutputStream(new File(path, filename));
                fileOutputStream.write(article.getContent().getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        articleDao.update(article);
    }

    @Override
    @MyInclude
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Cover> select3Time(String uid) {
        //通过uid查询所属上师的id
        String gid = userDao.selectGid(uid);
        List<Cover> covers = articleDao.select3Time(gid);
        for (Cover cover : covers) {
            cover.setThumbnail("http://wenwen.soso.com/p/20111105/"+cover.getThumbnail());
            cover.setType(1);
        }
        return covers;
    }

    @Override
    @MyInclude
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Cover> selectByID(String uid) {
        //通过uid查询所属上师的id
        String gid = userDao.selectGid(uid);
        List<Cover> covers = articleDao.selectByID(gid);
        for (Cover cover : covers) {
            cover.setThumbnail("http://wenwen.soso.com/p/20111105/"+cover.getThumbnail());
            cover.setType(1);
        }
        return covers;
    }

    @Override
    @MyInclude
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Cover> selectByNoID(String uid) {
        //通过uid查询所属上师的id
        String gid = userDao.selectGid(uid);
        List<Cover> covers = articleDao.selectByNoID(gid);
        for (Cover cover : covers) {
            cover.setThumbnail("http://wenwen.soso.com/p/20111105/"+cover.getThumbnail());
            cover.setType(1);
        }
        return covers;
    }
}
