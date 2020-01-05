package com.baizhi.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.baizhi.dao.UserDao;
import com.baizhi.entity.Echarts;
import com.baizhi.entity.Vo;
import com.baizhi.service.UserService;
import io.goeasy.GoEasy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public int[] select7Num() {
        List<Vo> list = userDao.select7Num();
        int[] count =new int[7];
        int sum=0;
        for (Vo vo : list) {
            Integer month = vo.getMonth();
            count[month]=vo.getCount()+sum;
            sum=count[month];//5
        }
        return count;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public int[] selectMonthNum() {
        List<Vo> vos = userDao.selectMonthNum();
        int[] count= new int[12];
        for (Vo vo : vos) {
            Integer month = vo.getMonth();
            count[month-1]=vo.getCount();
        }
        return count;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Echarts> selectTotalCount() {
        return userDao.selectTotalCount();
    }

    @Override
    public void del(String id) {
        userDao.del(id);
    }
}
