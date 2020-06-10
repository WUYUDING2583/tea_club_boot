package com.yuyi.tea.service;

import com.yuyi.tea.bean.Company;
import com.yuyi.tea.controller.CompanyController;
import com.yuyi.tea.mapper.CompanyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanyService {

    private final Logger log = LoggerFactory.getLogger(CompanyController.class);

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private RedisService redisService;

    /**
     * 获取公司信息
     * @return
     */
    public Company getCompanyInfo(){
        //查询缓存中是否存在
        boolean hasKey = redisService.exists("company:company");
        Company companyInfo=null;
        if(hasKey){
            //获取缓存
            companyInfo= (Company) redisService.get("company:company");
            log.info("从缓存获取的数据"+ companyInfo);
        }else{
            //从数据库中获取信息
            log.info("从数据库中获取数据");
            companyInfo = companyMapper.getCompany();
            //数据插入缓存（set中的参数含义：key值，user对象，缓存存在时间10（long类型），时间单位）
            redisService.set("company:company",companyInfo);
            log.info("数据插入缓存" + companyInfo);
        }
        return companyInfo;
    }


    /**
     * 更新公司信息
     * @param company
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Company updateCompany(Company company) {
        companyMapper.updateCompany(company);
        //更新缓存
        redisService.set("company:company",company);
        log.info("更新缓存数据"+company);
        return company;
    }

    /**
     * 获取兑换比例
     * @return
     */
    public float getRechargeRate() {
        Company company = companyMapper.getCompany();
        return company.getRechargeRate();
    }
}
