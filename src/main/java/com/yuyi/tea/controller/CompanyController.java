package com.yuyi.tea.controller;

import com.yuyi.tea.bean.Company;
import com.yuyi.tea.bean.Employee;
import com.yuyi.tea.service.CompanyService;
import com.yuyi.tea.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class CompanyController {

    private final Logger log = LoggerFactory.getLogger(CompanyController.class);

    @Autowired
    CompanyService companyService;

    @Autowired
    RedisService redisService;

    @GetMapping("/company")
    public Company getCompany(){
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
            companyInfo = companyService.getCompanyInfo();
            //数据插入缓存（set中的参数含义：key值，user对象，缓存存在时间10（long类型），时间单位）
            redisService.set("company:company",companyInfo);
            log.info("数据插入缓存" + companyInfo);
        }
        return companyInfo;
    }

    @PutMapping("/company")
    @Transactional(rollbackFor = Exception.class)
    public Company updateCompany(@RequestBody Company company){
        System.out.println("update company"+company);
        companyService.updateCompany(company);
        return company;
    }
}
