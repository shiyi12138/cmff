package com.baizhi.serviceImpl;

import com.baizhi.dao.AdminDao;
import com.baizhi.entity.Admin;
import com.baizhi.service.AdminService;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminDao ad;
    @Transactional(propagation = Propagation.SUPPORTS)
    public Admin selectAdminByName(String name, String password) {
        return ad.selectAdminByName(name,password);
    }

    @Override
    public void outPoi() {
        //创建workboot
        HSSFWorkbook workbook=new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("管理员表");
        String[] title ={"id","用户名","密码","其他"};
        HSSFRow row = sheet.createRow(0);
        //创建标题行
        for (int i = 0; i < title.length; i++) {
            String s = title[i];
            row.createCell(i).setCellValue(s);
        }
        List<Admin> list = ad.selectAll();
        for (int i = 0; i < list.size(); i++) {
            HSSFRow row1 = sheet.createRow(i+1);
            row1.createCell(0).setCellValue(list.get(i).getId());
            row1.createCell(1).setCellValue(list.get(i).getUsername());
            row1.createCell(2).setCellValue(list.get(i).getPassword());
            row1.createCell(3).setCellValue(list.get(i).getOther());
        }
        try {
            workbook.write(new File("d:/admin.xls"));
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
}
