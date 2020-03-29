package com.yuyi.tea.service;

import com.yuyi.tea.bean.CustomerType;
import com.yuyi.tea.bean.EnterpriseCustomerApplication;
import com.yuyi.tea.bean.Photo;
import com.yuyi.tea.common.TimeUtils;
import com.yuyi.tea.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    //获取企业用户申请
    public List<EnterpriseCustomerApplication> getEnterpriseCustomerApplications(boolean isFetchAll) {
        List<EnterpriseCustomerApplication> applications=new ArrayList<>();
        if(isFetchAll){
            applications = customerMapper.getAllEnterpriseCustomerApplications();
        }else{
            applications = customerMapper.getLast3MonthsEnterpriseCustomerApplications(TimeUtils.getCurrentTimestamp() - (long) 1000 * 60 * 60 * 24 * 90);
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
}
