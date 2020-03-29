package com.yuyi.tea;

import com.yuyi.tea.bean.*;
import com.yuyi.tea.common.TimeUtils;
import com.yuyi.tea.component.Result;
import com.yuyi.tea.mapper.*;
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

    @Autowired
    EnterpriseMapper enterpriseMapper;

    @Autowired
    CustomerMapper customerMapper;
    @Test
    void contextLoads() {
        List<EnterpriseCustomerApplication> last3MonthsEnterpriseCustomerApplications = customerMapper.getLast3MonthsEnterpriseCustomerApplications(TimeUtils.getCurrentTimestamp()- (long)1000*60*60*24*90);
        System.out.println(last3MonthsEnterpriseCustomerApplications);

    }

}
