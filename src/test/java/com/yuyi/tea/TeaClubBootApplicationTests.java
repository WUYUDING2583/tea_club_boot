package com.yuyi.tea;

import com.auth0.jwt.interfaces.Claim;
import com.yuyi.tea.bean.*;
import com.yuyi.tea.common.utils.JwtUtil;
import com.yuyi.tea.common.utils.TimeUtil;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TeaClubBootApplicationTests {

    @Test
    void test(){
        Runnable runnable = new Runnable() {
            public void run() {
                // task to run goes here
                System.out.println("Hello !!"+ TimeUtil.getCurrentTimestamp());
            }
        };
        ScheduledThreadPoolExecutor executor=new ScheduledThreadPoolExecutor(2);
        // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
        System.out.println("start main thread"+ TimeUtil.getCurrentTimestamp());
        executor.schedule(runnable, 10,  TimeUnit.SECONDS);
        sleep(TimeUnit.SECONDS,30);
        System.out.println("end main thread"+ TimeUtil.getCurrentTimestamp());

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
