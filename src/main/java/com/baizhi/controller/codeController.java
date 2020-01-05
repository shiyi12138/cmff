package com.baizhi.controller;

import com.baizhi.util.ValidateImageCodeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
@RequestMapping("code")
public class codeController {
    @RequestMapping("getCode")
    public void code(HttpServletResponse response, HttpSession session){
        //通过工具类获取字
        String securityCode = ValidateImageCodeUtils.getSecurityCode();
        session.setAttribute("code",securityCode);
        //通过字生成图片
        BufferedImage image = ValidateImageCodeUtils.createImage(securityCode);
        //通过流打印回页面
        ServletOutputStream outputStream=null;
        try {
            outputStream= response.getOutputStream();
            ImageIO.write(image,"png",outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
           if (outputStream!=null){
               try {
                   outputStream.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
        }
    }
}
