package com.yuyi.tea;

import com.yuyi.tea.bean.OrderStatus;
import com.yuyi.tea.mapper.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class TeaClubBootApplicationTests {

    @Autowired
    ActivityMapper activityMapper;

    @Autowired
    EnterpriseMapper enterpriseMapper;

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    OrderMapper orderMapper;
    @Test
    void contextLoads() {
        List<OrderStatus> orderStatusHistory = orderMapper.getOrderStatusHistory(2);
        System.out.println(orderStatusHistory);
        OrderStatus orderCurrentStatus = orderMapper.getOrderCurrentStatus(2);
        System.out.println(orderCurrentStatus);
//        List<ActivityRule> activityRulesByProduct = activityMapper.getActivityRulesByProduct(1);
//        System.out.println(activityRulesByProduct);
    }

}
