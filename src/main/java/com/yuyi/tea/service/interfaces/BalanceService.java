package com.yuyi.tea.service.interfaces;

public interface BalanceService {

    /**
     * 添加充值记录
     * @param customerId
     * @param amount
     */
    void recharge(int customerId,float amount);


}
