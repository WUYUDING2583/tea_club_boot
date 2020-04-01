package com.yuyi.tea.service;

import com.yuyi.tea.bean.Company;
import com.yuyi.tea.mapper.CompanyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

//@CacheConfig(cacheNames = "company")//抽取缓存的公共配置
@Service
public class CompanyService {

    @Autowired
    private CompanyMapper companyMapper;

//    @Cacheable(key = "1")
    public Company getCompanyInfo(){
        Company company = companyMapper.getCompany();
        return company;
    }


//    @CachePut(key = "1")
    public Company updateCompany(Company company) {
        companyMapper.updateCompany(company);
        return company;
    }
}
