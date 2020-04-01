package com.yuyi.tea.controller;

import com.yuyi.tea.bean.Order;
import com.yuyi.tea.component.TimeRange;
import com.yuyi.tea.service.OrderService;
import com.yuyi.tea.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {
    private final Logger log = LoggerFactory.getLogger(CompanyController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisService redisService;

    //获取客户的订单列表
    @GetMapping("/ordersByCustomer/{customerId}/{startDate}/{endDate}")
    public List<Order> getOrdersByCustomer(@PathVariable int customerId,@PathVariable long startDate,@PathVariable long endDate){
       TimeRange timeRange=new TimeRange(startDate,endDate);
        List<Order> ordersByCustomer = orderService.getOrdersByCustomer(customerId, timeRange);
        return ordersByCustomer;
    }

    //获取未完成的订单列表
    @GetMapping("/uncompleteOrders")
    public List<Order> getUncompleteOrders(){
        //查询缓存中是否存在
        boolean hasKey = redisService.exists("order:uncompleteOrders");
        List<Order> uncompleteOrders=null;
        if(hasKey){
            //获取缓存
            uncompleteOrders= (List<Order>) redisService.get("order:uncompleteOrders");
            log.info("从缓存获取的数据"+ uncompleteOrders);
        }else{
            //从数据库中获取信息
            log.info("从数据库中获取数据");
            uncompleteOrders = orderService.getUncompleteOrders();
            //数据插入缓存（set中的参数含义：key值，user对象，缓存存在时间10（long类型），时间单位）
            redisService.set("order:uncompleteOrders",uncompleteOrders);
            log.info("数据插入缓存" + uncompleteOrders);
        }
        return uncompleteOrders;
    }

    //根据条件获取订单列表
    @GetMapping("/orders/{status}/{startDate}/{endDate}")
    public List<Order> getOrders(@PathVariable String status,@PathVariable long startDate,@PathVariable long endDate){
        TimeRange timeRange=new TimeRange(startDate,endDate);
        List<Order> orders = orderService.getOrders(status, timeRange);
        return orders;
    }

    //根据uid获取订单详细信息
    @GetMapping("/order/{uid}")
    public Order getOrder(@PathVariable int uid){
        Order order = orderService.getOrder(uid);
        return order;
    }

    //将订单状态更新为已发货
    @PutMapping("/ordershipped")
    @Transactional(rollbackFor = Exception.class)
    public Order updateOrderShipped(@RequestBody Order order){
        Order updatedOrder = orderService.updateOrderShipped(order);
        return updatedOrder;
    }

}
