package com.yuyi.tea.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

public interface RechargeMapper {


    /**
     * 添加充值记录
     * @param customerId
     * @param amount
     * @param time
     */
    @Insert("insert into recharge(customerId,amount,time) values(#{customerId},#{amount},#{time})")
    void recharge(int customerId, float amount, long time);
}
