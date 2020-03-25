package com.yuyi.tea.service;

import com.yuyi.tea.bean.CustomerType;
import com.yuyi.tea.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "customer")
public class CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Cacheable(key = "'customerTypes'")
    public List<CustomerType> getCustomerTypes(){
        List<CustomerType> customerTypes = customerMapper.getCustomerTypes();
        return customerTypes;
    }
}
