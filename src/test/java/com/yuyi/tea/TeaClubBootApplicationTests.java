package com.yuyi.tea;

import com.yuyi.tea.bean.Activity;
import com.yuyi.tea.bean.Employee;
import com.yuyi.tea.bean.Product;
import com.yuyi.tea.bean.Shop;
import com.yuyi.tea.component.Result;
import com.yuyi.tea.mapper.ActivityMapper;
import com.yuyi.tea.mapper.EmployeeMapper;
import com.yuyi.tea.mapper.ShopMapper;
import com.yuyi.tea.service.ShopService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class TeaClubBootApplicationTests {

    @Autowired
    ActivityMapper activityMapper;
    @Test
    void contextLoads() {
        Activity activity = activityMapper.getActivity(11);
        System.out.println(activity);

    }

}
