package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.Customer;
import com.yuyi.tea.bean.CustomerType;
import com.yuyi.tea.bean.EnterpriseCustomerApplication;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface CustomerMapper {

    /**
     * 获取客户类型
     * @return
     */
    @Select("select * from customerType")
    List<CustomerType> getCustomerTypes();

    //获取所有的企业客户申请
    @Select("select * from enterpriseCustomerApplication")
    @Results({
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column = "enterpriseId",property = "enterprise",
                    one = @One(select="com.yuyi.tea.mapper.EnterpriseMapper.getEnterPriseByUid",
                            fetchType = FetchType.LAZY)),
            @Result(column = "applicantId",property = "applicant",
                    one = @One(select="com.yuyi.tea.mapper.CustomerMapper.getCustomerByUid",
                            fetchType = FetchType.LAZY))
    })
    List<EnterpriseCustomerApplication> getAllEnterpriseCustomerApplications();

    //根据uid获取客户信息
    @Select("select * from customer where uid=#{uid}")
    @Results({
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column = "uid",property = "avatar",
                    one = @One(select="com.yuyi.tea.mapper.PhotoMapper.getAvatarByCustomerId",
                            fetchType = FetchType.LAZY)),
            @Result(column = "type",property = "customerType",
                    one = @One(select="com.yuyi.tea.mapper.CustomerMapper.getCustomerType",
                            fetchType = FetchType.LAZY))
    })
    Customer getCustomerByUid(int uid);

    //获取近三月的企业用户申请
    @Select("select * from enterpriseCustomerApplication where applyTime>#{timestamp}")
    @Results({
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column = "enterpriseId",property = "enterprise",
                    one = @One(select="com.yuyi.tea.mapper.EnterpriseMapper.getEnterPriseByUid",
                            fetchType = FetchType.LAZY)),
            @Result(column = "applicantId",property = "applicant",
                    one = @One(select="com.yuyi.tea.mapper.CustomerMapper.getCustomerByUid",
                            fetchType = FetchType.LAZY))
    })
    List<EnterpriseCustomerApplication> getLast3MonthsEnterpriseCustomerApplications(long timestamp);

    //企业客户申请开始审核
    @Update("update enterpriseCustomerApplication set status='pending' where uid=#{uid}")
    void startEnterpriseCustomerApplication(int uid);

    //根据uid获取企业客户申请的详细信息
    @Select("select * from enterpriseCustomerApplication where uid=#{uid}")
    @Results({
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column = "enterpriseId",property = "enterprise",
                    one = @One(select="com.yuyi.tea.mapper.EnterpriseMapper.getEnterPriseByUid",
                            fetchType = FetchType.LAZY)),
            @Result(column = "applicantId",property = "applicant",
                    one = @One(select="com.yuyi.tea.mapper.CustomerMapper.getCustomerByUid",
                            fetchType = FetchType.LAZY))
    })
    EnterpriseCustomerApplication getEnterpriseCustomerApplication(int uid);

    //通过企业客户申请
    @Update("update enterpriseCustomerApplication set status='approve' where uid=#{uid}")
    void approveEnterpriseCustomerApplication(int uid);

    //拒绝企业客户申请
    @Update("update enterpriseCustomerApplication set status='reject' where uid=#{uid}")
    void rejectEnterpriseCustomerApplication(int uid);

    //获取客户列表
    @Select("select * from customer")
    @Results({
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column = "uid",property = "avatar",
                    one = @One(select="com.yuyi.tea.mapper.PhotoMapper.getAvatarByCustomerId",
                            fetchType = FetchType.LAZY)),
            @Result(column = "type",property = "customerType",
                    one = @One(select="com.yuyi.tea.mapper.CustomerMapper.getCustomerType",
                            fetchType = FetchType.LAZY))
    })
    List<Customer> getCustomers();

    //根据uid获取客户类型
    @Select("select * from customerType where uid=#{uid}")
    CustomerType getCustomerType(int uid);

    //将客户升级为超级vip
    @Update("update customer set type=3 where uid=#{uid}")
    void setSuperVIP(int uid);
}