package com.baizhi.controller;

import com.baizhi.entity.Admin;
import com.baizhi.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @RequestMapping("/login")
    @ResponseBody
    public String login(HttpServletRequest request,String username, String password, String code){
        HttpSession session = request.getSession();
        String code1 = (String) session.getAttribute("code");
        if (!code1.equals(code)){
            return "验证码输入错误";
        }
        Admin admin = adminService.selectAdminByName(username, password);
        if (admin!=null){
            request.getSession().setAttribute("admin",admin);
            return "ok";
        }else {
            return "用户名或密码有误";
        }
    }
    @RequestMapping("/outPoi")
    @ResponseBody
    public void outPoi(){
        adminService.outPoi();
    }
}
