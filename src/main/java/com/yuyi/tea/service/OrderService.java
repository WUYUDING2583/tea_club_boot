package com.yuyi.tea.service;

import com.yuyi.tea.bean.Activity;
import com.yuyi.tea.bean.ActivityRule;
import com.yuyi.tea.bean.Order;
import com.yuyi.tea.bean.OrderProduct;
import com.yuyi.tea.common.utils.AmountUtil;
import com.yuyi.tea.component.TimeRange;
import com.yuyi.tea.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    //获取客户的订单列表
    public List<Order> getOrdersByCustomer(int customerId, TimeRange timeRange){
        List<Order> ordersByCustomer = orderMapper.getOrdersByCustomer(customerId, timeRange);
        for(Order order:ordersByCustomer){
//            order.getCustomer().setAvatar(null);
            order.getCustomer().setPassword(null);
            order.getClerk().setAvatar(null);
            order.getClerk().setPasswrod(null);
            for(OrderProduct orderProduct:order.getProducts()){
//                orderProduct.getProduct().setActivityRules(null);
                orderProduct.getProduct().setActivities(null);
                orderProduct.getProduct().setActivityRules(null);
                orderProduct.getProduct().setPhotos(null);
                if(orderProduct.getActivityRule()!=null) {
                    orderProduct.getActivityRule().setActivityApplyForProduct(null);
                    orderProduct.getActivityRule().setActivity(null);
                }
//                for(ActivityRule activityRule:orderProduct.getProduct().getActivityRules()){
//                    activityRule.setActivityApplyForProduct(null);
//                    activityRule.setActivity(null);
//                }
            }
//            order.setActivityRules(null);
//            for(ActivityRule activityRule:order.getActivityRules()){
////                activityRule.setActivityApplyForCustomerTypes(null);
//                activityRule.setActivityApplyForProduct(null);
//                activityRule.getActivity().setActivityRules(null);
//                activityRule.getActivity().setPhotos(null);
//                activityRule.getActivity().setMutexActivities(null);
//            }
            order.setAmount(AmountUtil.computeAmount(order));
        }
        return ordersByCustomer;
    }
}
