package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.Address;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface AddressMapper {

    /**
     * 保存收货地址
     * @param address
     */
    @Insert("insert into address(province,city,district,detail,phone,isDefaultAddress,customerId,name) values(" +
            "#{province},#{city},#{district},#{detail},#{phone},#{isDefaultAddress},#{customer.uid},#{name}" +
            ")")
    @Options(useGeneratedKeys=true, keyProperty="uid")
    void saveAddress(Address address);

    /**
     * 获取客户默认地址
     * @param customerId
     * @return
     */
    @Select("select * from address where customerId=#{customerId} and isDefaultAddress=true")
    Address getDefaultAddress(int customerId);

    /**
     * 将地址设置为非默认
     * @param uid
     */
    @Update("update address set isDefaultAddress=false where uid=#{uid}")
    void setUnDefaultAddress(int uid);

    /**
     * 获取客户的地址列表
     * @param customerId
     * @return
     */
    @Select("select * from address where customerId=#{customerId}")
    List<Address> getAddresses(int customerId);

    /**
     * 获取地址
     * @param addressId
     * @return
     */
    @Select("select * from address where uid=#{addressId}")
    Address getAddress(int addressId);

    /**
     * 更新地址
     * @param address
     */
    @Update("update address set province=#{province}, city=#{city},district=#{district},detail=#{detail},phone=#{phone},isDefaultAddress=#{isDefaultAddress},name=#{name} where uid=#{uid}")
    void updateAddress(Address address);
}
