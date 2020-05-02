package com.yuyi.tea;

import com.auth0.jwt.interfaces.Claim;
import com.yuyi.tea.bean.*;
import com.yuyi.tea.common.utils.JwtUtil;
import com.yuyi.tea.mapper.*;
import com.yuyi.tea.service.LoginService;
import com.yuyi.tea.service.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.support.JstlUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
class TeaClubBootApplicationTests {

    @Autowired
    ActivityMapper activityMapper;

    @Autowired
    EnterpriseMapper enterpriseMapper;

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    CompanyMapper companyMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    RedisService redisService;

    @Autowired
    LoginService loginService;

    @Autowired
    AuthorityMapper authorityMapper;

    @Test
    void test(){
        System.out.println("asdf");
//        try{
////            InputStream in = new FileInputStream("d:\\1.jpg");//图片路径
//            BufferedImage image = ImageIO.read(new File("d:\\1.jpg"));
//            Graphics g = image.getGraphics();
//            g.setColor(Color.RED);//画笔颜色
//            g.drawRect(100, 100, 100, 100);//矩形框(原点x坐标，原点y坐标，矩形的长，矩形的宽)
//            //g.dispose();
//            FileOutputStream out = new FileOutputStream("d:\\12.jpg");//输出图片的地址
//            ImageIO.write(image, "jpeg", out);
//        }catch (Exception e){
//
//        }
    }
    @Test
    void contextLoads() {


    }

}
