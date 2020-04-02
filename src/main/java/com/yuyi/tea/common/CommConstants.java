package com.yuyi.tea.common;

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
        public static final String PATED="payed";
        public static final String UNPAY="unpay";
        public static  final String COMPLETE="complete";
        public static final String REQUEST_REFUND="requestRefund";
        public static final String REFUND="refunded";
        public static final String REJECT_REFUND="rejectRefund";
    }
}
