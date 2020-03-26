package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.CustomerType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CustomerMapper {

    @Select("select * from customerType")
    List<CustomerType> getCustomerTypes();
}