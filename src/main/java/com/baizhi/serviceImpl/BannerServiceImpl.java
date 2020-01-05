package com.baizhi.serviceImpl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baizhi.annotation.ClearCache;
import com.baizhi.annotation.MyInclude;
import com.baizhi.dao.BannerDao;
import com.baizhi.entity.Banner;
import com.baizhi.entity.Teacher;
import com.baizhi.service.BannerService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class BannerServiceImpl implements BannerService {
    @Autowired
    private BannerDao bannerDao;

    @Transactional(propagation = Propagation.SUPPORTS)
    @MyInclude
    public Map<String, Object> selectByPage(Integer page, Integer rows) {
        Map<String, Object> map=new HashMap<>();
        int start=(page-1)*rows;
        //数据
        List<Banner> banners = bannerDao.selectByPage(start, rows);
        //总条数
        Integer counts = bannerDao.selectCount();
        //总页数
        Integer pages=counts%rows==0?counts/rows:counts/rows+1;
        map.put("rows",banners);
        map.put("page",page);
        map.put("total",pages);
        map.put("records",counts);
        return map;
    }
    @ClearCache
    public String insert(Banner banner) {
        String id = getID();
        banner.setId(id);
        bannerDao.insert(banner);
        return id;
    }
    //修改图片路径
    @ClearCache
    public void updateSrc(String src, String id) {
        bannerDao.updateSrc(src,id);
    }

    //批量删除
    @ClearCache
    public void delAll(String[] ids) {
        bannerDao.delAll(ids);
    }

    //修改
    @ClearCache
    public void updateBanner(Banner banner) {
        bannerDao.updateBanner(banner);
    }

    @Override
    public void outPoi(String path) {
        System.out.println(path);
        //首先查询所有
        List<Banner> banners = bannerDao.selectAll();
        for (Banner banner : banners) {
            System.out.println(banner);
        }
        //修改图片路径
        for (Banner banner : banners) {
            banner.setImg(path+"\\"+banner.getImg());
        }
        //cv过来的
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("轮播图", "轮播图表"),Banner.class, banners);
        //将workbook写入文件并释放资源
        try {
            workbook.write(new FileOutputStream("d:/LBTPoI.xls"));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //随机生成id
    public static String getID(){
        StringBuilder stringBuilder=new StringBuilder();
        Random random=new Random();
        for (int i = 0; i < 10; i++) {
            int num = random.nextInt(10);
            stringBuilder.append(num);
        }
        return stringBuilder.toString();
    }
}
