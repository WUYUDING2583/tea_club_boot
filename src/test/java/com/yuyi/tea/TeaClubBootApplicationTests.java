package com.yuyi.tea;

import com.yuyi.tea.bean.Employee;
import com.yuyi.tea.mapper.EmployeeMapper;
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
    EmployeeMapper employeeMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;//k-v都是字符串

//    @Autowired
//    RedisTemplate redisTemplate;//k-v都是对象

    @Autowired
    RedisTemplate<Object, Object> redisTemplate;//经过配置可将对象以json形式存入redis

    /**
     * 测试字符串
     */
    @Test
    public void test(){
//        stringRedisTemplate.opsForValue().append("msg","hello world");
        String msg = stringRedisTemplate.opsForValue().get("msg");
        System.out.println(msg);
    }

    /**
     * 测试redis操作对象
     */
    @Test
    public void testObj(){
        Employee empById = employeeMapper.getEmpById(1);
        List<String> str=new ArrayList<>();
        str.add("1");
        str.add("2");
        str.add("3");
//        redisTemplate.opsForValue().set("emp",empById);
        redisTemplate.opsForValue().set("list",str);
    }

    @Test
    void contextLoads() {
        Employee empById = employeeMapper.getEmpById(1);
        System.out.println(empById);
    }

}
