package com.yuyi.tea.service;

import com.yuyi.tea.bean.Customer;
import com.yuyi.tea.bean.CustomerType;
import com.yuyi.tea.bean.EnterpriseCustomerApplication;
import com.yuyi.tea.bean.Photo;
import com.yuyi.tea.common.utils.TimeUtil;
import com.yuyi.tea.mapper.CustomerMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CustomerService {

    public static String REDIS_CUSTOMERS_NAME="customers";
    public static String REDIS_CUSTOMER_TYPES_NAME=REDIS_CUSTOMERS_NAME+":customerTypes";

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private RedisService redisService;

    /**
     * 获取客户类型
     * @return
     */
    public List<CustomerType> getCustomerTypes(){
        boolean hasKey=redisService.exists(REDIS_CUSTOMER_TYPES_NAME);
        List<CustomerType> customerTypes;
        if(hasKey){
            customerTypes= (List<CustomerType>) redisService.get(REDIS_CUSTOMER_TYPES_NAME);
            log.info("从redis中获取客户类型列表"+customerTypes);
        }else{
            log.info("从数据库获取客户类型列表");
            customerTypes = customerMapper.getCustomerTypes();
            log.info("将客户类型列表存入reids"+customerTypes);
            redisService.set(REDIS_CUSTOMER_TYPES_NAME,customerTypes);
        }
        return customerTypes;
    }

    //获取企业用户申请
    public List<EnterpriseCustomerApplication> getEnterpriseCustomerApplications(boolean isFetchAll) {
        List<EnterpriseCustomerApplication> applications=new ArrayList<>();
        if(isFetchAll){
            applications = customerMapper.getAllEnterpriseCustomerApplications();
        }else{
            applications = customerMapper.getLast3MonthsEnterpriseCustomerApplications(TimeUtil.getCurrentTimestamp() - (long) 1000 * 60 * 60 * 24 * 90);
         }
        for(EnterpriseCustomerApplication enterpriseCustomerApplication:applications){
            enterpriseCustomerApplication.getEnterprise().setBusinessLicense(new Photo());
            enterpriseCustomerApplication.getApplicant().setAvatar(new Photo());
        }
        return applications;
    }

    //企业客户申请开始审核
    public void startEnterpriseCustomerApplication(int uid) {
        customerMapper.startEnterpriseCustomerApplication(uid);
    }

    public EnterpriseCustomerApplication getEnterpriseCustomerApplication(int uid) {
        EnterpriseCustomerApplication enterpriseCustomerApplication = customerMapper.getEnterpriseCustomerApplication(uid);
        return enterpriseCustomerApplication;
    }

    //通过企业客户申请
    public void approveEnterpriseCustomerApplication(int uid) {
        customerMapper.approveEnterpriseCustomerApplication(uid);
    }

    //拒绝企业客户申请
    public void rejectEnterpriseCustomerApplication(int uid) {
        customerMapper.rejectEnterpriseCustomerApplication(uid);
    }

    //获取客户列表
    public List<Customer> getCustomers() {
        List<Customer> customers = customerMapper.getCustomers();
        for(Customer customer:customers){
            customer.setPassword(null);
            customer.setAvatar(null);
        }
        return customers;
    }

    //将客户升级为超级vip
    public Customer setSuperVIP(int uid) {
        customerMapper.setSuperVIP(uid);
        Customer customer = customerMapper.getCustomerByUid(uid);
        customer.setAvatar(null);
        customer.setPassword(null);
        return customer;
    }

    //从redis中获取客户信息
    public Customer getRedisCustomer(int uid){
        Customer customer=null;
        boolean hasKey = redisService.exists("customers:customer:"+uid);
        if(hasKey){
            //获取缓存
            customer= (Customer) redisService.get("customers:customer:"+uid);
            log.info("从缓存获取的数据"+ customer);
        }else{
            //从数据库中获取信息
            log.info("从数据库中获取数据");
            customer = customerMapper.getCustomerByUid(uid);
            //数据插入缓存（set中的参数含义：key值，user对象，缓存存在时间10（long类型），时间单位）
            redisService.set("customers:customer:"+uid,customer);
            log.info("数据插入缓存" + customer);
        }
        return customer;
    }


}
