package com.yuyi.tea;

import com.yuyi.tea.mapper.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
//        List<ActivityRule> activityRulesByProduct = activityMapper.getActivityRulesByProduct(1);
//        System.out.println(activityRulesByProduct);
    }

}
