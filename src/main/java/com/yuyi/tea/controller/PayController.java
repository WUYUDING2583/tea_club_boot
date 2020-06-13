package com.yuyi.tea.controller;

import com.google.gson.Gson;
import com.yuyi.tea.bean.*;
import com.yuyi.tea.common.Amount;
import com.yuyi.tea.common.CodeMsg;
import com.yuyi.tea.common.CommConstants;
import com.yuyi.tea.common.Result;
import com.yuyi.tea.common.utils.TimeUtil;
import com.yuyi.tea.exception.GlobalException;
import com.yuyi.tea.service.CompanyService;
import com.yuyi.tea.service.CustomerService;
import com.yuyi.tea.service.OrderService;
import com.yuyi.tea.service.SMSService;
import com.yuyi.tea.service.interfaces.BalanceService;
import com.yuyi.tea.service.interfaces.CartService;
import com.yuyi.tea.service.interfaces.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private WebSocketMINAServer webSocketMINAServer;

    @Autowired
    private SMSService smsService;

    @Autowired
    private CompanyService companyService;

    /**
     * 移动端模拟支付
     * @param customerId
     * @param value
     */
    @GetMapping("/mobile/simulatePay/{customerId}/{value}")
    public void simulatePay(@PathVariable int customerId,@PathVariable float value){
        try {
            Customer customer = customerService.getRedisCustomer(customerId);
            long timestamp = TimeUtil.getCurrentTimestamp();
            //添加充值记录
            balanceService.recharge(customerId,value);
            //添加账单记录
            float rechargeRate = companyService.getRechargeRate();
            BillDetail billDetail=new BillDetail(timestamp,value*rechargeRate,0,CommConstants.BillDescription.CHARGE,customer);
            customerService.saveBillDetail(billDetail);
            //改变账户余额
            Amount balance = customerService.addBalance(customerId, value);
            //保存通知至数据库
            Notification notification = CommConstants.Notification.CHARGE(value, timestamp,customerId);
            noticeService.saveNotification(notification);
            //发送短信通知
            smsService.sendChargeSuccess(timestamp,value,customer.getContact());
            Result result=new Result(balance);
            ws.sendInfo(new Gson().toJson(result), customerId + "");
        }catch (Exception e){
            throw new GlobalException(CodeMsg.FAIL_IN_PAYMENT);
        }
    }

    /**
     * 移动端支付订单
     * @param customerId
     * @param orderId
     * @return
     */
    @PostMapping("/mobile/pay/{customerId}/{orderId}")
    @Transactional(rollbackFor = Exception.class)
    public String mobilePay(@PathVariable int customerId,@PathVariable int orderId){
        Order order;
        if(orderId==-1){
            //为最近一次未付款订单付款
            order=orderService.getLatestUnpayOrder(customerId);
        }else{
            order=orderService.getOrder(orderId);
            if(order==null){
                throw new GlobalException(CodeMsg.ORDER_DELETED);
            }
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
        if(order.getPlaceOrderWay()!=null&&order.getDeliverMode()==null){
            //门店下单，直接将订单状态更改为完成
            OrderStatus complete = new OrderStatus(order.getUid(), CommConstants.OrderStatus.COMPLETE, TimeUtil.getCurrentTimestamp(), order.getClerk());
            orderService.saveOrderStatus(complete);
        }
        long currentTimestamp = TimeUtil.getCurrentTimestamp();
        //添加账单记录
        BillDetail billDetail=null;
        if(order.getReservations().size()>0) {
            billDetail = new BillDetail(currentTimestamp, -ingot, -credit, CommConstants.BillDescription.RESERVATION, order.getCustomer());
            //设置预约提醒
            Runnable runnable = new Runnable() {
                public void run() {
                    Order currentOrder = orderService.getOrder(order.getUid());
                    if(currentOrder!=null) {
                        smsService.sendReservationClose(currentOrder);
                        Notification notification = CommConstants.Notification.RESERVATION_CLOSE(currentOrder);
                        noticeService.saveNotification(notification);
                        try {
                            webSocketMINAServer.sendInfo(currentOrder.getCustomer().getContact());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            //提前两小时提醒
            long delay=(order.getReservations().get(0).getReservationTime()-currentTimestamp)/1000+60*60*2;
            ScheduledThreadPoolExecutor executor=new ScheduledThreadPoolExecutor(2);
            executor.schedule(runnable, delay,  TimeUnit.SECONDS);
        }else{
            billDetail = new BillDetail(currentTimestamp, -ingot, -credit, CommConstants.BillDescription.BUY_PRODUCT, order.getCustomer());
        }
        customerService.saveBillDetail(billDetail);
        return "success";
    }

    /**
     * 小程序模拟充值
     * @param customerId
     * @param value
     * @return
     */
    @PostMapping("/mp/simulateCharge/{customerId}/{value}")
    public Amount mpSimulatePay(@PathVariable int customerId,@PathVariable float value){
        try{
            long timestamp = TimeUtil.getCurrentTimestamp();
            Customer customer = customerService.getRedisCustomer(customerId);
            //添加充值记录
            balanceService.recharge(customerId,value);
            //添加账单记录
            float rechargeRate = companyService.getRechargeRate();
            BillDetail billDetail=new BillDetail(timestamp,value*rechargeRate,0,CommConstants.BillDescription.CHARGE,customer);
            customerService.saveBillDetail(billDetail);
            //改变账户余额
            Amount balance = customerService.addBalance(customerId, value);
            //保存通知至数据库
            Notification notification = CommConstants.Notification.CHARGE(value, timestamp,customerId);
            noticeService.saveNotification(notification);
            //发送短信通知
            smsService.sendChargeSuccess(timestamp,value,customer.getContact());
            //发送小程序通知
            try {
                webSocketMINAServer.sendInfo(customer.getContact());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return balance;
        }catch (Exception e){
            e.printStackTrace();
            throw new GlobalException(CodeMsg.FAIL_IN_PAYMENT);
        }
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
            if(order==null){
                throw new GlobalException(CodeMsg.ORDER_DELETED);
            }
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
        long currentTimestamp = TimeUtil.getCurrentTimestamp();
        //添加账单记录
        BillDetail billDetail=null;
        if(order.getReservations().size()>0) {
            billDetail = new BillDetail(currentTimestamp, -ingot, -credit, CommConstants.BillDescription.RESERVATION, order.getCustomer());
            //设置预约提醒
            Runnable runnable = new Runnable() {
                public void run() {
                    Order currentOrder = orderService.getOrder(order.getUid());
                    if(currentOrder!=null) {
                        smsService.sendReservationClose(currentOrder);
                        Notification notification = CommConstants.Notification.RESERVATION_CLOSE(currentOrder);
                        noticeService.saveNotification(notification);
                        try {
                            webSocketMINAServer.sendInfo(currentOrder.getCustomer().getContact());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            //提前两小时提醒
            long delay=(order.getReservations().get(0).getReservationTime()-currentTimestamp)/1000+60*60*2;
            ScheduledThreadPoolExecutor executor=new ScheduledThreadPoolExecutor(2);
            executor.schedule(runnable, delay,  TimeUnit.SECONDS);
        }else{
            billDetail = new BillDetail(currentTimestamp, -ingot, -credit, CommConstants.BillDescription.BUY_PRODUCT, order.getCustomer());
        }
        customerService.saveBillDetail(billDetail);
        return "success";
    }

    /**
     * 小程序支付订单
     * @param order
     * @return
     */
    @PostMapping("/mp/payCart/{isCart}")
    public String payCart(@RequestBody Order order,@PathVariable boolean isCart){
        long currentTimestamp = TimeUtil.getCurrentTimestamp();
        Order currentOrder=orderService.getOrder(order.getUid());
        if(currentOrder.getBoxOrder()==null) {
            currentOrder.setBuyerPs(order.getBuyerPs());
            currentOrder.setDeliverMode(order.getDeliverMode());
            currentOrder.setPlaceOrderWay(order.getPlaceOrderWay());
            currentOrder.setAddress(order.getAddress());
        }
        //设置收货地址和买家留言
        orderService.updateOrderAddressAndPs(currentOrder); //查询账户余额
        Amount balance = customerService.getCustomerBalance(currentOrder.getCustomer().getUid());
        float ingot = currentOrder.getIngot();
        float credit = currentOrder.getCredit();
        //检查账户余额
        customerService.checkBalance(ingot,credit,balance);
        //扣费
        customerService.pay(ingot,credit,currentOrder.getCustomer().getUid());
        //添加账单记录
        BillDetail billDetail=new BillDetail(currentTimestamp,-ingot,-credit,CommConstants.BillDescription.BUY_PRODUCT,order.getCustomer());
        customerService.saveBillDetail(billDetail);
        //改变订单状态
        orderService.updateOrderPayed(currentOrder);
        if(isCart){
            //删除购物车相应内容
            cartService.deleteCartProductByOrder(currentOrder);
        }
        return "success";
    }

}
