package com.yuyi.tea.service;

import com.yuyi.tea.bean.Employee;
import com.yuyi.tea.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

@CacheConfig(cacheNames = "emp")//抽取缓存的公共配置
@Service
public class EmployeeService {

    @Autowired
    EmployeeMapper employeeMapper;

    @Cacheable
    public Employee getEmp(int id){
        System.out.println("query "+id+" emploeyy info");
        Employee empById = employeeMapper.getEmpById(id);
        return empById;
    }

    @CachePut(cacheNames = "emp",key = "#employee.id")
    public Employee updateEmp(Employee employee){
        System.out.println("updateEmp "+employee);
        employeeMapper.updateEmp(employee);;
        return employee;
    }

    @CacheEvict(cacheNames = "emp")
    public void deleteEmp(int id){
        System.out.println("delete "+id+" employee");
//        employeeMapper.deleteEmp(id);
    }

    @Caching(
            cacheable = {
                    @Cacheable(value = "emp",key="#lastName")
            },
            put = {
                    @CachePut(value = "emp",key = "#result.id"),
                    @CachePut(value = "emp",key = "#result.email")
            }
    )
    public Employee getEmpByLastName(String lastName){
        System.out.println("query by lastName:"+lastName);
        Employee empByLastName = employeeMapper.getEmpByLastName(lastName);
        return empByLastName;
    }
}
