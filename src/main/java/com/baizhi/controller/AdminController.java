package com.baizhi.controller;

import com.baizhi.service.AdminService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.subject.Subject;
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
        Subject subject = SecurityUtils.getSubject();
        AuthenticationToken authenticationToken=new UsernamePasswordToken(username,password);
        try {
            subject.login(authenticationToken);
            return "ok";
        }catch (Exception e){
            return "用户名或密码有误";
        }
    }
    @RequestMapping("/outPoi")
    @ResponseBody
    public void outPoi(){
        adminService.outPoi();
    }
    @RequestMapping("/logOut")
    public String logOut(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "jsp/login.jsp";
    }
}
