package com.yuyi.tea.controller;

import com.yuyi.tea.bean.CustomerType;
import com.yuyi.tea.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/customerTypes")
    public List<CustomerType> getCustomerTypes(){
        List<CustomerType> customerTypes = customerService.getCustomerTypes();
        return customerTypes;
    }
}
