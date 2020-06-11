package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.*;
import com.yuyi.tea.common.Amount;
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

    /**
     * 获取所有的企业客户申请
     * @return
     */
    @Select("select * from enterpriseCustomerApplication")
    @Results(id = "enterpriseCustomerApplication",
            value = {
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
    @ResultMap("customer")
    Customer getCustomerByUid(int uid);

    /**
     * 获取近三月的企业用户申请
     * @param timestamp
     * @return
     */
    @Select("select * from enterpriseCustomerApplication where applyTime>#{timestamp}")
    @ResultMap("enterpriseCustomerApplication")
    List<EnterpriseCustomerApplication> getLast3MonthsEnterpriseCustomerApplications(long timestamp);

    /**
     * 企业客户申请开始审核
     * @param uid
     */
    @Update("update enterpriseCustomerApplication set status='pending' where uid=#{uid}")
    void startEnterpriseCustomerApplication(int uid);

    /**
     * 根据uid获取企业客户申请详细信息
     * @param uid
     * @return
     */
    @Select("select * from enterpriseCustomerApplication where uid=#{uid}")
    @ResultMap("enterpriseCustomerApplication")
    EnterpriseCustomerApplication getEnterpriseCustomerApplication(int uid);

    /**
     * 通过企业客户申请
     * @param uid
     */
    @Update("update enterpriseCustomerApplication set status='approve' where uid=#{uid}")
    void approveEnterpriseCustomerApplication(int uid);

    /**
     * 拒绝企业客户申请
     * @param uid
     */
    @Update("update enterpriseCustomerApplication set status='reject' where uid=#{uid}")
    void rejectEnterpriseCustomerApplication(int uid);

    /**
     * 获取客户列表
     * @return
     */
    @Select("select * from customer")
    @Results(id="customer",
            value = {
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column = "uid",property = "avatar",
                    one = @One(select="com.yuyi.tea.mapper.PhotoMapper.getAvatarByCustomerId",
                            fetchType = FetchType.LAZY)),
            @Result(column = "type",property = "customerType",
                    one = @One(select="com.yuyi.tea.mapper.CustomerMapper.getCustomerType",
                            fetchType = FetchType.LAZY)),
            @Result(column = "uid",property = "addresses",
                    one = @One(select="com.yuyi.tea.mapper.CustomerMapper.getAddressByCustomerId",
                            fetchType = FetchType.LAZY)),
    })
    List<Customer> getCustomers();

    @Select("select * from address where customerId=#{uid}")
    List<Address> getAddressByCustomerId(int uid);

    @Select("select * from address where uid=#{uid}")
    Address getAddress(int uid);

    //根据uid获取客户类型
    @Select("select * from customerType where uid=#{uid}")
    CustomerType getCustomerType(int uid);

    //将客户升级为超级vip
    @Update("update customer set type=3 where uid=#{uid}")
    void setSuperVIP(int uid);

    @Update("update customer set ingot=ingot-#{ingot}, credit=credit-#{credit} where uid=#{uid}")
    void pay(float ingot, float credit,int uid);

    /**
     * 账户余额增加
     * @param customerId
     * @param ingot
     */
    @Update("update customer set ingot=ingot+#{ingot} where uid=#{customerId}")
    void addBalance(int customerId, float ingot);

    /**
     * 为客户账户余额新增积分
     * @param customerId
     * @param credit
     */
    @Update("update customer set credit=credit+#{credit} where uid=#{customerId}")
    void addCredit(int customerId, float credit);

    /**
     * 根据搜索信息搜索对应客户信息
     * @param searchText
     * @return
     */
    @Select("select * from customer where name like #{searchText} or contact like #{searchText} or identityId like #{searchText} or email like #{searchText} or weChatId like #{searchText} or address like #{searchText}")
    @ResultMap("customer")
    List<Customer> searchCustomers(String searchText);

    /**
     * 保存客户信息
     * @param customer
     */
    @Insert("insert into customer(name,contact,identityId,email,type,gender,password,weChatId) values(#{name},#{contact},#{identityId},#{email},#{customerType.uid},#{gender},#{password},#{weChatId})")
    @Options(useGeneratedKeys=true, keyProperty="uid")
    void saveCustomer(Customer customer);

    /**
     * 新增元宝
     * @param customerId
     * @param ingot
     */
    @Update("update customer set ingot=ingot+#{ingot} where uid=#{customerId}")
    void addIngot(int customerId, float ingot);

    /**
     * 根据手机号获取客户信息
     * @param contact
     * @return
     */
    @Select("select * from customer where contact=#{contact}")
    @ResultMap("customer")
    Customer getCustomerByContact(String contact);

    /**
     * 添加账单记录
     * @param billDetail
     */
    @Insert("insert into billDetail(time,ingot,credit,descriptionId,customerId) values(#{time},#{ingot},#{credit},#{description.uid},#{customer.uid})")
    void saveBillDetail(BillDetail billDetail);

    /**
     * 获取客户的消费记录（20条）
     * @param customerId
     * @param offset
     * @return
     */
    @Select("select * from billDetail where customerId=#{customerId} order by time desc limit #{offset},20")
    @Results(id="billDetail",
            value = {
                    @Result(id = true,column = "uid",property = "uid"),
                    @Result(column = "descriptionId",property = "description",
                            one = @One(select="com.yuyi.tea.mapper.CustomerMapper.getBillDescription",
                                    fetchType = FetchType.LAZY)),
                    @Result(column = "customerId",property = "customer",
                            one = @One(select="com.yuyi.tea.mapper.CustomerMapper.getCustomerByUid",
                                    fetchType = FetchType.LAZY))
            })
    List<BillDetail> getBillDetails(int customerId, int offset);

    /**
     * 获取消费类型描述
     * @param uid
     * @return
     */
    @Select("select * from billDescription where uid=#{uid}")
    BillDescription getBillDescription(int uid);

    /**
     * 取客户的充值记录（20条）
     * @param customerId
     * @param offset
     * @return
     */
    @Select("select * from recharge where customerId=#{customerId} order by time desc limit #{offset},20")
    List<ChargeRecord> getChargeRecords(int customerId, int offset);

    @Select("select ingot,credit from customer where uid=#{uid}")
    Amount getCustomerBalance(int uid);
}