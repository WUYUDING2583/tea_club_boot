package com.yuyi.tea.common;

import com.yuyi.tea.bean.BillDescription;
import com.yuyi.tea.bean.Customer;
import com.yuyi.tea.common.utils.StringUtil;

public class CommConstants {

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
///    public static final String[] FilterUrl={"/admin/*","/verifyToken"};
}
