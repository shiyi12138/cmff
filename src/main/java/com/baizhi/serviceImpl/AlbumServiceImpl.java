package com.baizhi.serviceImpl;

import com.baizhi.dao.AlbumDao;
import com.baizhi.entity.Album;
import com.baizhi.service.AlbumService;
import com.baizhi.vo.Cover;
import com.baizhi.vo.VoAlbum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AlbumServiceImpl implements AlbumService {
    @Autowired
    private AlbumDao albumDao;
    @Transactional(propagation = Propagation.SUPPORTS)
    public Map<String,Object> selectByPage(Integer page, Integer count) {
        //数据
        List<Album> albums = albumDao.selectByPage((page - 1) * count, count);
        //总条数
        Integer  sumCount= albumDao.selectCount();
        //总页数
        Integer total=sumCount%count==0?sumCount/count:sumCount/count+1;
        Map<String,Object> map=new HashMap<>();
        map.put("rows",albums);
        map.put("page",page);
        map.put("total",total);
        map.put("records",sumCount);
        return map;
    }

    @Override
    public List<Cover> select6Time() {
        List<Cover> covers = albumDao.select6Time();
        for (Cover cover : covers) {
            cover.setThumbnail("http://wenwen.soso.com/p/20111105/"+cover.getThumbnail());
            cover.setAuthor("");
            cover.setType(0);
        }
        return covers;
    }

    @Override
    public List<Cover> selectAll() {
        List<Cover> covers = albumDao.selectAll();
        for (Cover cover : covers) {
            cover.setThumbnail("http://wenwen.soso.com/p/20111105/"+cover.getThumbnail());
            cover.setAuthor("");
            cover.setType(0);
        }
        return covers;
    }

    @Override
    public VoAlbum selectById(String id) {
        VoAlbum voAlbum = albumDao.selectById(id);
        voAlbum.setThumbnail("http://"+voAlbum.getThumbnail());
        return voAlbum;
    }
}
