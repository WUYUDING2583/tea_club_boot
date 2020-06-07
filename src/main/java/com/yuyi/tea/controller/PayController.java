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
import com.yuyi.tea.service.interfaces.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PayController {

    @Autowired
    private WebSocketBalanceServer ws;

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

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
            Amount currentBalance = customerService.getCustomerBalance(customerId);
            Result result=new Result(currentBalance);
            ws.sendInfo(new Gson().toJson(result), customerId + "");
        }catch (Exception e){
            throw new GlobalException(CodeMsg.FAIL_IN_PAYMENT);
        }
    }

    /**
     * 小程序模拟充值
     * @param customerId
     * @param value
     * @return
     */
    @PostMapping("/mp/simulateCharge/{customerId}/{value}")
    public String mpSimulatePay(@PathVariable int customerId,@PathVariable float value){
        try{
            //添加充值记录
            balanceService.recharge(customerId,value);
            //改变账户余额
            Amount balance = customerService.addBalance(customerId, value);
        }catch (Exception e){
            e.printStackTrace();
            throw new GlobalException(CodeMsg.FAIL_IN_PAYMENT);
        }
        return "success";
    }

    /**
     * 小程序支付订单
     * @param customerId
     * @param orderId
     * @return
     */
    @PostMapping("/mp/pay/{customerId}/{orderId}")
    @Transactional(rollbackFor = Exception.class)
    public String mpPay(@PathVariable int customerId,@PathVariable int orderId){
        Order order;
        if(orderId==-1){
            //为最近一次未付款订单付款
            order=orderService.getLatestUnpayOrder(customerId);
        }else{
            order=orderService.getOrder(orderId);
        }
        //查询账户余额
        Amount balance = customerService.getCustomerBalance(customerId);
        float ingot = order.getIngot();
        float credit = order.getCredit();
        //检查账户余额
        customerService.checkBalance(ingot,credit,balance);
        //扣费
        customerService.pay(ingot,credit,customerId);
        //改变订单状态
        orderService.updateOrderPayed(order);
        return "success";
    }

    /**
     * 小程序支付由购物车生成的订单
     * @param order
     * @return
     */
    @PostMapping("/mp/payCart")
    public String payCart(@RequestBody Order order){
        Order currentOrder=orderService.getOrder(order.getUid());
        currentOrder.setBuyerPs(order.getBuyerPs());
        currentOrder.setDeliverMode(order.getDeliverMode());
        currentOrder.setPlaceOrderWay(order.getPlaceOrderWay());
        currentOrder.setAddress(order.getAddress());
        //设置收货地址和买家留言
        orderService.updateOrderAddressAndPs(currentOrder); //查询账户余额
        Amount balance = customerService.getCustomerBalance(currentOrder.getCustomer().getUid());
        float ingot = currentOrder.getIngot();
        float credit = currentOrder.getCredit();
        //检查账户余额
        customerService.checkBalance(ingot,credit,balance);
        //扣费
        customerService.pay(ingot,credit,currentOrder.getCustomer().getUid());
        //改变订单状态
        orderService.updateOrderPayed(currentOrder);
        //删除购物车相应内容
        cartService.deleteCartProductByOrder(currentOrder);
        return "success";
    }

}
