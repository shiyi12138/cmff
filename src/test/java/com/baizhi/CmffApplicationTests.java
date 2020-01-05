package com.baizhi;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.baizhi.dao.*;
import com.baizhi.entity.Echarts;
import com.baizhi.entity.Student;
import com.baizhi.entity.Teacher;
import com.baizhi.entity.Vo;
import com.baizhi.service.UserService;
import com.baizhi.util.AliDaYu;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.*;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class CmffApplicationTests {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserService userService;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private TeacherMapper teacherMapper;

    @Test
    public void test1() throws Exception {
        //根据要读入的excle文件，创建java的excel对象
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream("D:/学生.xls"));
        //根据workbook对象获取工作簿（要指定表的名称或下标）
        HSSFSheet sheet = workbook.getSheet("学生表");
        //获取最后一行数据的下标
        int lastRowNum = sheet.getLastRowNum();
        //循环获取每一行的数据
        ArrayList<Student> list = new ArrayList<>();
        for (int i = 1; i <= lastRowNum; i++) {
            //获取每一行数据，分别处理
            HSSFRow row = sheet.getRow(i);
            Student student = new Student();
            //获取第一列赋值给id
            int id = (int) row.getCell(0).getNumericCellValue();
            student.setId(id);
            //获取第二列赋值给name....
            String name = row.getCell(1).getStringCellValue();
            student.setName(name);
            String sex = row.getCell(2).getStringCellValue();
            student.setSex(sex);
            Date bir = row.getCell(3).getDateCellValue();
            student.setBir(bir);
            String clazz = row.getCell(4).getStringCellValue();
            student.setClazz(clazz);
            //将所以的对象都放入集合，方便以后加入数据库
            list.add(student);
        }
        for (Student student : list) {
            System.out.println(student);
        }
        //调用方法，将数据批量插入数据库
        studentMapper.insertAll(list);
        //释放资源
        workbook.close();
    }

    @Test
    public void test2() throws IOException {
        //获取数据
        List<Student> list = studentMapper.secectAll();
        //创建一个excel对象
        HSSFWorkbook workbook = new HSSFWorkbook();
        //根据excel对象创建工作表
        HSSFSheet sheet = workbook.createSheet("我的表");
        //创建标题行
        String[] title = {"id", "姓名", "性别", "生日", "班级"};
        HSSFRow row = sheet.createRow(0);
        //遍历数据为每一个单元格赋值
        for (int i = 0; i < title.length; i++) {
            String s = title[i];
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(s);
        }
        //处理日期格式
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        HSSFDataFormat dataFormat = workbook.createDataFormat();
        short format = dataFormat.getFormat("yyyy-MM-dd");
        cellStyle.setDataFormat(format);
        //设置宽度和居中,字体颜色等等
        HSSFFont font = workbook.createFont();
        font.setColor(Font.COLOR_RED);
        cellStyle.setFont(font);
        sheet.setColumnWidth(3, 20 * 256);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        //遍历数据创建单元格并插入数据
        for (int i = 0; i < list.size(); i++) {
            HSSFRow row1 = sheet.createRow(i + 1);
            row1.createCell(0).setCellValue(list.get(i).getId());
            row1.createCell(1).setCellValue(list.get(i).getName());
            row1.createCell(2).setCellValue(list.get(i).getSex());
            HSSFCell cell = row1.createCell(3);
            cell.setCellValue(list.get(i).getBir());
            cell.setCellStyle(cellStyle);
            row1.createCell(4).setCellValue(list.get(i).getClazz());
        }
        //将excel对象写入文件
        workbook.write(new File("D:/我的表.xls"));
        workbook.close();
    }

    @Test
    public void test3() throws Exception {
        ImportParams importParams = new ImportParams();
        //指定标题行有1行
        importParams.setTitleRows(1);
        //指定表头有2行
        importParams.setHeadRows(2);
        List<Teacher> list = ExcelImportUtil.importExcel(new FileInputStream("d:/TeacherPoI.xls"), Teacher.class, importParams);
        teacherMapper.insertAll(list);
        for (Teacher teacher : list) {
            List<Student> students = teacher.getStudents();
            //对数据进行筛选，将null去掉
            for (int i = 0; i < students.size(); i++) {
                if (students.get(i).getId() == null) {
                    students.remove(i);
                }
            }
            if (students.size() != 0) {
                //再插入学生
                studentMapper.insertAll(students);
            }
        }
    }

    @Test
    public void test4() throws IOException {
        //获取到数据
        List<Teacher> teachers = teacherMapper.secectAll();
        //遍历修改路径为全路径
        for (Teacher teacher : teachers) {
            teacher.setSrc("E:\\End\\cmff\\src\\main\\webapp\\img\\" + teacher.getSrc());
        }
        //cv过来的
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("180班教师", "teacher表"),
                Teacher.class, teachers);
        //将workbook写入文件并释放资源
        workbook.write(new FileOutputStream("d:/TeacherPoI.xls"));
        workbook.close();
    }

    @Test
    public void test5() throws ClientException {
        //设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化ascClient需要的几个参数
        final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
        final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
        //替换成你的AK
        final String accessKeyId = "LTAIvZOy1y67DvoJ";//你的accessKeyId,参考本文档步骤2
        final String accessKeySecret = "FYjlH0bMN9bYTKyC2BX2G2NAxC6f7v";//你的accessKeySecret，参考本文档步骤2
        //初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
                accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);
        //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为国际区号+号码，如“85200000000”
        request.setPhoneNumbers("18608616556");
        //必填:短信签名-可在短信控制台中找到
        request.setSignName("小黑人");
        //必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
        request.setTemplateCode("SMS_164270184");
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        request.setTemplateParam("{\"name\":\"Tom\", \"code\":\"7777\"}");
        //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("yourOutId");
        //请求失败这里会抛ClientException异常
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            //请求成功时，获取数据
            String code = sendSmsResponse.getCode();
            String message = sendSmsResponse.getMessage();
            System.out.println(code+"---"+message);
        }
    }
    @Test
    public void test6(){
        AliDaYu.note("LTAIvZOy1y67DvoJ","FYjlH0bMN9bYTKyC2BX2G2NAxC6f7v","18608616556","小黑人","SMS_164270184","6767");
    }
}
