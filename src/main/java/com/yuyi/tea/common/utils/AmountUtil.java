package com.yuyi.tea.common.utils;

import com.yuyi.tea.bean.*;
import com.yuyi.tea.common.Amount;

import java.util.List;

public class AmountUtil {

    public static Amount computeAmount(Order order){
        List<OrderProduct> products = order.getProducts();
        float ingot=0;
        int credit=0;
        for(OrderProduct orderProduct:products){
            Product product = orderProduct.getProduct();
            if(orderProduct.getActivityRule()!=null) {
                ingot += product.getPrice().getIngot() * (100 - orderProduct.getActivityRule().getActivityRule1()) / 100;
                credit += product.getPrice().getCredit() * (100 - orderProduct.getActivityRule().getActivityRule1()) / 100;
            }else{
                ingot += product.getPrice().getIngot();
                credit += product.getPrice().getCredit();
            }
        }
        if(order.getActivityRule()!=null) {
            ActivityRule2 activityRule2 = order.getActivityRule().getActivityRule2();
            if (activityRule2.getOperation().equals("minus")) {
                if (activityRule2.getCurrency().equals("ingot")) {
                    ingot -= activityRule2.getNumber();
                } else {
                    credit -= activityRule2.getNumber();
                }
            }
        }
        return new Amount(ingot,credit);
    }
}
