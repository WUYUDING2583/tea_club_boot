package com.yuyi.tea.controller;

import com.yuyi.tea.bean.Clerk;
import com.yuyi.tea.bean.Order;
import com.yuyi.tea.bean.OrderStatus;
import com.yuyi.tea.common.TimeRange;
import com.yuyi.tea.mapper.ClerkMapper;
import com.yuyi.tea.mapper.OrderMapper;
import com.yuyi.tea.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;


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

}
