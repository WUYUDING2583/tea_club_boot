package com.yuyi.tea.controller;

import com.google.gson.Gson;
import com.yuyi.tea.bean.Order;
import com.yuyi.tea.common.Amount;
import com.yuyi.tea.common.CodeMsg;
import com.yuyi.tea.common.Result;
import com.yuyi.tea.exception.GlobalException;
import com.yuyi.tea.service.CustomerService;
import com.yuyi.tea.service.OrderService;
import com.yuyi.tea.service.interfaces.BalanceService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PayController {

    @Autowired
    private WebSocketPayServer ws;

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/mobile/simulatePay/{customerId}/{value}")
    public void simulatePay(@PathVariable int customerId,@PathVariable float value){
        try {
            //添加充值记录
            balanceService.recharge(customerId,value);
            //改变账户余额
            Amount balance = customerService.addBalance(customerId, value);
            //自动扣费所有未付款包厢预约订单，从最新记录开始
            //查询所有未付款预约包厢订单
            List<Order> unpayReserationOrders=orderService.getUnpayReservationOrder(customerId);
            for(Order order:unpayReserationOrders){
                float ingot = order.getIngot();
                float credit = order.getCredit();
                //检查账户余额
                customerService.checkBalance(ingot,credit,balance);
                //扣费
                balance=customerService.pay(ingot,credit,customerId);
                //改变订单状态
                orderService.updateReservationComplete(order);
            }
            Result result=new Result("支付成功");
            ws.sendInfo(new Gson().toJson(result), customerId + "");
        }catch (Exception e){
            throw new GlobalException(CodeMsg.FAIL_IN_PAYMENT);
        }
    }
}