package com.baizhi.controller;

import com.baizhi.entity.Echarts;
import com.baizhi.entity.Vo;
import com.baizhi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/echarts")
public class EchartsController {
    @Autowired
    private UserService userService;

    @RequestMapping("/timeNum")
    @ResponseBody
    public int[] timeNum(){
        int[] ints = userService.select7Num();
        return ints;
    }
    @RequestMapping("/monthNum")
    @ResponseBody
    public int[] monthNum(){
        return userService.selectMonthNum();
    }
    @RequestMapping("/totleCount")
    @ResponseBody
    public List<Echarts> totleCount(){
        List<Echarts> echarts = userService.selectTotalCount();
        return echarts;
    }
    @RequestMapping("/del")
    @ResponseBody
    public void del(String id){
        System.out.println("------------");
        userService.del(id);
    }
}
