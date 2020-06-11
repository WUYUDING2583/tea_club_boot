package com.yuyi.tea;

import com.auth0.jwt.interfaces.Claim;
import com.yuyi.tea.bean.*;
import com.yuyi.tea.common.utils.JwtUtil;
import com.yuyi.tea.common.utils.TimeUtil;
import com.yuyi.tea.mapper.*;
import com.yuyi.tea.service.LoginService;
import com.yuyi.tea.service.RedisService;
import net.bytebuddy.asm.Advice;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TeaClubBootApplicationTests {

    @Autowired
    private ActivityMapper activityMapper;
    @Test
    void test(){
        List<Activity> shoppingActivity = activityMapper.getShoppingActivity();
        System.out.println(shoppingActivity);

    }
    private void sleep(TimeUnit unit, long time) {
        try {
            unit.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Test
    void contextLoads() {


    }

}
