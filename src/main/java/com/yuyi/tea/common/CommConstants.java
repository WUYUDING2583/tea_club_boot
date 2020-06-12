package com.yuyi.tea.common;

import com.yuyi.tea.bean.*;
import com.yuyi.tea.common.utils.TimeUtil;

public class CommConstants {

    public static final boolean CLOSE_SMS = false;

    public static class UserException{
        public static final int USER_EXCEPTION_CODE=606;
        public static final class SHOP_INFO_NOT_EXIST{
            public static final String error="shop info not exist";
            public static final String msg="门店信息不存在";
        }
    }

    public static class OrderStatus{
        public static final String SHIPPED="shipped";
        public static final String PAYED="payed";
        public static final String UNPAY="unpay";
        public static  final String COMPLETE="complete";
        public static final String REQUEST_REFUND="requestRefund";
        public static final String REFUND="refunded";
        public static final String REJECT_REFUND="rejectRefund";
        public static final String CUSTOMER_PICK_UP="customerPcikUp";
    }

    public static class EnterpriseCustomerApplication{
        public static final String SUBMIT="submit";
        public static final String PENDING="pending";
        public static final String APPROVE="approve";
        public static final String REJECT="reject";
    }

    public static class CustomerType{
        public static final com.yuyi.tea.bean.CustomerType REGISTER_USER=new com.yuyi.tea.bean.CustomerType(1,"注册用户");
        public static final com.yuyi.tea.bean.CustomerType CHARGE_USER=new com.yuyi.tea.bean.CustomerType(2,"充值用户");
        public static final com.yuyi.tea.bean.CustomerType SUPER_VIP=new com.yuyi.tea.bean.CustomerType(3,"超级vip用户");
        public static final com.yuyi.tea.bean.CustomerType ENTERPRISE_USER=new com.yuyi.tea.bean.CustomerType(4,"企业用户");
    }

    public static class BillDescription{
        public static final com.yuyi.tea.bean.BillDescription BUY_PRODUCT=new com.yuyi.tea.bean.BillDescription(1,"购买商品");
        public static final com.yuyi.tea.bean.BillDescription RESERVATION=new com.yuyi.tea.bean.BillDescription(2,"预约包厢");
        public static final com.yuyi.tea.bean.BillDescription REFUND=new com.yuyi.tea.bean.BillDescription(3,"退款");
        public static final com.yuyi.tea.bean.BillDescription CHARGE=new com.yuyi.tea.bean.BillDescription(4,"充值");
        public static final com.yuyi.tea.bean.BillDescription PRESENT=new com.yuyi.tea.bean.BillDescription(5,"活动赠送");
    }

    public static class Notification{
        public static  com.yuyi.tea.bean.Notification RESERVATION_CLOSE(Order order){
            String content="您预约的包厢"+order.getReservations().get(0).getBox().getName()+
                    " 预约时间：";
            for(Reservation reservation:order.getReservations()){
                content+=TimeUtil.convertTimestampToyyyMMdd(reservation.getReservationTime())+" "+TimeUtil.convertTimestampToHHmm(reservation.getReservationTime())+"~"+TimeUtil.convertTimestampToHHmm(reservation.getReservationTime()+reservation.getBox().getDuration()*1000*60)+" ";
            }
            content+="已临近预约时间，请不要忘记哦";
            return new com.yuyi.tea.bean.Notification(false,0,"预约临近",content, TimeUtil.getCurrentTimestamp(),(Customer)order.getCustomer());
        }
        public static com.yuyi.tea.bean.Notification REFUND_SUCCESS(Order order){
            String content="您的订单，编号："+order.getUid()+"退款成功，退款"+order.getIngot()+"元宝"+order.getCredit()+"积分，请查收";
            return new com.yuyi.tea.bean.Notification(false,1,"退款成功",content, TimeUtil.getCurrentTimestamp(),(Customer)order.getCustomer());
        }
        public static com.yuyi.tea.bean.Notification ORDER_SHIPPED(Order order){
            String content="您购买的商品已发货 " +
                    "订单编号："+order.getUid()+" " ;
            if(order.getTrackInfo().getCompanyName()!=null&&!order.getTrackInfo().getCompanyName().equals("")){
                content+="物流公司："+order.getTrackInfo().getCompanyName()+" " +
                        "物流单号："+order.getTrackInfo().getTrackingId()+" " ;
            }else{
                content+="配送人联系方式："+order.getTrackInfo().getPhone()+" " +
                        "配送人信息："+order.getTrackInfo().getDescription()+" ";
            }
            content+="请查收";
            return new com.yuyi.tea.bean.Notification(false,2,"订单发货",content, TimeUtil.getCurrentTimestamp(),(Customer)order.getCustomer());
        }
        public static com.yuyi.tea.bean.Notification ORDER_PREPARED(Order order){
            String content="您购买的商品备好 " +
                    "订单编号："+order.getUid()+" ";
            if(order.getBoxOrder()==null){
                content+="可前往"+order.getPlaceOrderWay().getName()+" " +
                                "地址："+order.getPlaceOrderWay().getAddress()+" " +
                                "领取";
            }
            return new com.yuyi.tea.bean.Notification(false,3,"订单备好",content, TimeUtil.getCurrentTimestamp(),(Customer)order.getCustomer());
        }
        public static com.yuyi.tea.bean.Notification CHARGE(float value,long timestamp,int customerId){
            String content="您于"+TimeUtil.convertTimestampToTimeFormat(timestamp)+"在研茶充值"+value+"元，若非本人操作，请与管理员联系";
            return new com.yuyi.tea.bean.Notification(false,4,"充值完成",content, TimeUtil.getCurrentTimestamp(),new Customer(customerId));
        }

        public static com.yuyi.tea.bean.Notification ACTIVITY_PRESENT(String activityType, Amount amount, long currentTimestamp, int customerId) {
            String content="您于"+TimeUtil.convertTimestampToTimeFormat(currentTimestamp)+"完成"+activityType+"任务，赠送您";
            if(amount.getIngot()>0){
                content+=amount.getIngot()+"元宝";
            }
            if(amount.getCredit()>0){
                content+=amount.getCredit()+"积分";
            }
            return new com.yuyi.tea.bean.Notification(false,5,"活动赠送",content, currentTimestamp,new Customer(customerId));
        }

        public static com.yuyi.tea.bean.Notification REFUND_REJECT(Order order) {
            String content="您的订单，编号："+order.getUid()+"申请退款失败，如有疑问请联系商家";
            return new com.yuyi.tea.bean.Notification(false,6,"退款失败",content, TimeUtil.getCurrentTimestamp(),(Customer)order.getCustomer());
        }
    }
}
