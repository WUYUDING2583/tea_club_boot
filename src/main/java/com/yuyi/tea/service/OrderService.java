package com.yuyi.tea.service;

import com.yuyi.tea.bean.*;
import com.yuyi.tea.common.utils.AmountUtil;
import com.yuyi.tea.common.utils.TimeUtil;
import com.yuyi.tea.component.TimeRange;
import com.yuyi.tea.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    //获取客户的订单列表
    @Cacheable(cacheNames = "customerOrders",key = "#customerId")
    public List<Order> getOrdersByCustomer(int customerId, TimeRange timeRange){
        List<Order> ordersByCustomer = orderMapper.getOrdersByCustomer(customerId, timeRange);
//        clearOrderList(ordersByCustomer);
        for(Order order:ordersByCustomer){
            order.setAmount(AmountUtil.computeAmount(order));
            order.getCustomer().setPassword(null);
            order.getClerk().setAvatar(null);
            order.getClerk().setPasswrod(null);
            for(OrderProduct orderProduct:order.getProducts()){
                orderProduct.getProduct().setActivities(null);
                orderProduct.getProduct().setActivityRules(null);
                orderProduct.getProduct().setPhotos(null);
                orderProduct.getProduct().setType(null);
                if(orderProduct.getActivityRule()!=null) {
                    orderProduct.getActivityRule().setActivityApplyForProduct(null);
                    orderProduct.getActivityRule().setActivity(null);
                }
            }
        }
        return ordersByCustomer;
    }

    //获取未完成的订单列表
    public List<Order> getUncompleteOrders() {
        List<Order> uncompleteOrders = orderMapper.getUncompleteOrders();
        clearOrderList(uncompleteOrders);
        return uncompleteOrders;
    }

    //根据条件获取订单列表
    public List<Order> getOrders(String status, TimeRange timeRange) {
        List<Order> orders = orderMapper.getOrders(status, timeRange);
        clearOrderList(orders);
        return orders;
    }

    //清除订单列表中不必要项
    private void clearOrderList(List<Order> orders){
        for(Order order:orders){
            order.setAmount(AmountUtil.computeAmount(order));
            order.getCustomer().setAvatar(null);
            order.getCustomer().setPassword(null);
            order.getClerk().setAvatar(null);
            order.getClerk().setPasswrod(null);
            for(OrderProduct orderProduct:order.getProducts()){
                Product product = orderProduct.getProduct();
                product.setPhotos(null);
                product.setActivityRules(null);
                product.setActivities(null);
                orderProduct.getActivityRule().setActivityApplyForProduct(null);
                orderProduct.getActivityRule().setActivity(null);
            }
        }
    }

    //清除订单详情中不必要项
    private void clearOrder(Order order){
        for(OrderProduct orderProduct:order.getProducts()){
            Product product=orderProduct.getProduct();
            product.setActivityRules(null);
            product.setActivities(null);
            ActivityRule activityRule = orderProduct.getActivityRule();
            activityRule.setActivityApplyForProduct(null);
            activityRule.setActivityApplyForCustomerTypes(null);
            activityRule.getActivity().setPhotos(null);
            activityRule.getActivity().setMutexActivities(null);
            activityRule.getActivity().setActivityRules(null);
        }
        order.getCustomer().setAvatar(null);
        order.getCustomer().setPassword(null);
        order.getClerk().setAvatar(null);
        order.getClerk().setPasswrod(null);
    }


    //根据uid获取订单详细信息
    @Cacheable(cacheNames = "order")
    public Order getOrder(int uid) {
        Order order = orderMapper.getOrder(uid);
        order.setAmount(AmountUtil.computeAmount(order));
        clearOrder(order);
        return order;
    }

    //将订单状态更新为已发货
    @CachePut(cacheNames = "order",key = "#order.uid")
    public Order updateOrderShipped(Order order) {
        //在orderStatus表插入最新状态
        orderMapper.updateOrderStatusShipped(order.getUid(), TimeUtil.getCurrentTimestamp());
        //在trackInfo表插入物流信息
        orderMapper.saveTrackInfo(order.getTrackInfo());
        //更新orders表的trackingId
        orderMapper.updateOrderTrackingId(order);
        Order updatedOrder = orderMapper.getOrder(order.getUid());
        updatedOrder.setAmount(AmountUtil.computeAmount(updatedOrder));
        clearOrder(updatedOrder);
        return updatedOrder;
    }
}
