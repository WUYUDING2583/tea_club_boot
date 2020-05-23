package com.yuyi.tea.controller;

import com.google.gson.Gson;
import com.yuyi.tea.bean.*;
import com.yuyi.tea.common.*;
import com.yuyi.tea.common.utils.TimeUtil;
import com.yuyi.tea.exception.GlobalException;
import com.yuyi.tea.mapper.ShopBoxMapper;
import com.yuyi.tea.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ShopBoxMapper shopBoxMapper;

    @Autowired
    private ShopBoxService shopBoxService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private WebSocketBalanceServer ws;

    /**
     * 获取客户的订单列表
     * @param customerId
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/admin/ordersByCustomer/{customerId}/{startDate}/{endDate}")
    public List<Order> getOrdersByCustomer(@PathVariable int customerId,@PathVariable long startDate,@PathVariable long endDate){
       TimeRange timeRange=new TimeRange(startDate,endDate);
        List<Order> ordersByCustomer = orderService.getOrdersByCustomer(customerId, timeRange);
        return ordersByCustomer;
    }

    /**
     * 获取未完成的订单列表
     * @return
     */
    @GetMapping("/admin/uncompleteOrders")
    public List<Order> getUncompleteOrders(){
        List<Order> uncompleteOrders = orderService.getUncompleteOrders();
        return uncompleteOrders;
    }

    /**
     * 根据条件获取订单列表
     * @param status
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/admin/orders/{status}/{startDate}/{endDate}")
    public List<Order> getOrders(@PathVariable String status,@PathVariable long startDate,@PathVariable long endDate){
        TimeRange timeRange=new TimeRange(startDate,endDate);
        List<Order> orders = orderService.getOrders(status, timeRange);
        return orders;
    }

    /**
     * 根据uid获取订单详细信息
     * @param uid
     * @return
     */
    @GetMapping("/admin/order/{uid}")
    public Order getOrder(@PathVariable int uid){
        Order order = orderService.getOrder(uid);
        return order;
    }

    /**
     * 将订单状态更新为已发货
     * @param order
     * @return
     */
    @PutMapping("/admin/ordershipped")
    @Transactional(rollbackFor = Exception.class)
    public Order updateOrderShipped(@RequestBody Order order){
        Order updatedOrder = orderService.updateOrderShipped(order);
        return updatedOrder;
    }

    /**
     * 卖家退款
     * @param order
     * @return
     */
    @PutMapping("/admin/orderrefunded")
    @Transactional(rollbackFor = Exception.class)
    public Order updateOrderRefunded(@RequestBody Order order){
        Order updateOrderRefunded = orderService.updateOrderRefunded(order);
        return updateOrderRefunded;
    }

    /**
     * 卖家拒绝买家申请退款
     * @param order
     * @return
     */
    @PutMapping("/admin/orderrejectRefund")
    @Transactional(rollbackFor = Exception.class)
    public Order updateOrderRejectRefunded(@RequestBody Order order){
        Order updatedOrder = orderService.updateOrderRejectRefunded(order);
        return updatedOrder;
    }

    @PutMapping("/mobile/ordershipped")
    @Transactional(rollbackFor = Exception.class)
    public Order updateMobileOrderShipped(@RequestBody Order order){
        Order updateOrderShipped = orderService.updateMobileOrderShipped(order);
        return updateOrderShipped;
    }

    @PostMapping("/mobile/reserve")
    public Order reserve(@RequestBody Order order){
        try {
            //计算总价
            float ingot = 0;
            float credit = 0;
            ShopBox box = shopBoxService.getShopBoxByUid(order.getReservations().get(0).getBoxId());
            final float priceIngot = box.getPrice().getIngot();
            final float priceCredit = box.getPrice().getCredit();
            for (Reservation reservation : order.getReservations()) {
                ingot += priceIngot;
                credit += priceCredit;
            }
            order.setIngot(ingot);
            order.setCredit(credit);
            orderService.saveReservation(order);
            //检查账户余额
            customerService.checkBalance(ingot,credit,order.getCustomer().getUid());
            //扣除金额
            customerService.pay(ingot, credit, order.getCustomer().getUid());
            //保存订单状态
            orderService.updateReservationComplete(order);
            //查询账户余额
            Amount customerBalance = customerService.getCustomerBalance(order.getCustomer().getUid());
            Result result=new Result(customerBalance);
            ws.sendInfo(new Gson().toJson(result), order.getCustomer().getUid()+"");
            return order;
        }catch (GlobalException e){
            throw e;
        }catch (Exception e){
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
    }

    /**
     * 移动端店员下单
     * @param order
     */
    @PostMapping("/mobile/order")
    public void placeOrder(@RequestBody Order order){
        try{
            Amount currentBalance = orderService.placeOrder(order);
            Result result=new Result(currentBalance);
            ws.sendInfo(new Gson().toJson(result), order.getCustomer().getUid()+"");
        }catch (GlobalException e){
            throw e;
        }catch (Exception e){
            e.printStackTrace();
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
    }

    @GetMapping("/mobile/orders/{customerId}")
    public List<Order> getRefreshOrders(@PathVariable int customerId){
        //获取该用户最近3个月的订单
        long startDate = TimeUtil.getNDayAgoStartTime(90);
        long endDate = TimeUtil.getNDayAgoStartTime(-1);
        TimeRange timeRange = new TimeRange(startDate, endDate);
        List<Order> orders = orderService.getOrdersByCustomer(customerId, timeRange);
        return orders;
    }

    /**
     * 小程序提交订单
     * @param order
     * @return
     */
    @PostMapping("/mp/order")
    public Order placeMpOrder(@RequestBody Order order){
        try{
           orderService.placeOrder(order);
            Order currentOrder = orderService.getOrder(order.getUid());
            return currentOrder;
        }catch (GlobalException e){
            throw e;
        }catch (Exception e){
            e.printStackTrace();
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
    }

    /**
     * 小程序获取对应订单
     * @param orderId
     * @return
     */
    @GetMapping("/mp/order/{orderId}")
    public Order getMpOrder(@PathVariable int orderId){
        Order order = orderService.getOrder(orderId);
        return order;
    }

    /**
     * 小程序获取客户最近的未付款订单
     * @param customerId
     * @return
     */
    @GetMapping("/mp/latestUnpayOrder/{customerId}")
    public Order getLatestUnPayOrder(@PathVariable int customerId){
        Order latestUnpayOrder = orderService.getLatestUnpayOrder(customerId);
        for(OrderProduct orderProduct:latestUnpayOrder.getProducts()){
            orderProduct.getProduct().setActivityRules(null);
            orderProduct.getProduct().setShop(null);
            orderProduct.getProduct().setProductDetails(null);
        }
        return latestUnpayOrder;
    }

    /**
     * 小程序取消订单
     * @param orderId
     * @return
     */
    @PostMapping("/mp/order/cancel/{orderId}")
    public String mpCancelOrder(@PathVariable int orderId){
        orderService.deleteOrder(orderId);
        return "success";
    }
}
