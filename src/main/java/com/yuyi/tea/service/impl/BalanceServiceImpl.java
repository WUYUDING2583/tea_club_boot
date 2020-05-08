package com.yuyi.tea.service.impl;

import com.yuyi.tea.common.utils.TimeUtil;
import com.yuyi.tea.mapper.RechargeMapper;
import com.yuyi.tea.service.interfaces.BalanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class BalanceServiceImpl implements BalanceService {

    @Autowired
    private RechargeMapper rechargeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recharge(int customerId, float amount) {
        long time= TimeUtil.getCurrentTimestamp();
        rechargeMapper.recharge(customerId,amount,time);
    }
}
