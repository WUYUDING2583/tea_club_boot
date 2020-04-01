package com.yuyi.tea;

import com.yuyi.tea.bean.*;
import com.yuyi.tea.mapper.*;
import com.yuyi.tea.service.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    @Autowired
    CompanyMapper companyMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    RedisService redisService;

    @Test
    void contextLoads() {
        Customer customerByUid = customerMapper.getCustomerByUid(1);
        System.out.println(customerByUid);
    }

}
