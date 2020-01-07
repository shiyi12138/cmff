package com.baizhi;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class CmffApplicationTests {
    @Test
    public void test1() throws Exception {
        Md5Hash md5Hash=new Md5Hash("admin","admin",1024);
        String s = md5Hash.toHex();
        System.out.println(s);
    }
}
