package com.yuyi.tea.service;

import com.google.gson.Gson;
import com.sun.org.apache.bcel.internal.classfile.Code;
import com.yuyi.tea.bean.*;
import com.yuyi.tea.common.*;
import com.yuyi.tea.common.utils.AmountUtil;
import com.yuyi.tea.common.utils.TimeUtil;
import com.yuyi.tea.controller.WebSocketBalanceServer;
import com.yuyi.tea.exception.GlobalException;
import com.yuyi.tea.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class OrderService {

    public static final String REDIS_ORDERS_NAME="orders";
    public static final String REDIS_ORDER_NAME=REDIS_ORDERS_NAME+":order";

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ClerkMapper clerkMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ClerkService clerkService;

    @Autowired
    private ShopBoxMapper shopBoxMapper;

    @Autowired
    private WebSocketBalanceServer ws;

    /**
     * 获取客户的订单列表
     * @param customerId
     * @param timeRange
     * @return
     */
    public List<Order> getOrdersByCustomer(int customerId, TimeRange timeRange){
        List<Order> ordersByCustomer = orderMapper.getOrdersByCustomer(customerId, timeRange);
        //查询缓存中订单信息
        getRedisOrdersDetail(ordersByCustomer);
        for(Order order:ordersByCustomer){
            for(OrderProduct orderProduct:order.getProducts()){
                orderProduct.getProduct().setPhotos(null);
            }
        }
        return ordersByCustomer;
    }

    /**
     * 获取未完成的订单列表
     * @return
     */
    public List<Order> getUncompleteOrders() {
        List<Order> uncompleteOrders = orderMapper.getUncompleteOrders();
        //查询缓存中订单信息
        getRedisOrdersDetail(uncompleteOrders);
        for(Order order:uncompleteOrders){
            order.getCustomer().setAvatar(null);
        }
        for(Order order:uncompleteOrders){
            for(OrderProduct orderProduct:order.getProducts()){
                orderProduct.getProduct().setPhotos(null);
            }
        }
        return uncompleteOrders;
    }

    /**
     * 根据条件获取订单列表
     * @param status
     * @param timeRange
     * @return
     */
    public List<Order> getOrders(String status, TimeRange timeRange) {
        List<Order> orders = orderMapper.getOrders(status, timeRange);
        //查询缓存中订单信息
        getRedisOrdersDetail(orders);
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
            order.getClerk().setPassword(null);
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
        order.getClerk().setPassword(null);
    }

    /**
     * 根据uid获取订单详细信息
     * @param uid
     * @return
     */
    public Order getOrder(int uid) {
        Order redisOrder = getRedisOrder(uid);
        return redisOrder;
    }

    /**
     * 将订单状态更新为已发货
     * @param order
     * @return
     */
    public Order updateOrderShipped(Order order) {
        //在orderStatus表插入最新状态
        long time=TimeUtil.getCurrentTimestamp();
        OrderStatus status=new OrderStatus(0,order.getUid(), CommConstants.OrderStatus.SHIPPED,time,order.getClerk());
        orderMapper.saveOrderStatus(status);
        //在trackInfo表插入物流信息
        orderMapper.saveTrackInfo(order.getTrackInfo());
        //更新orders表的trackingId
        orderMapper.updateOrderTrackingId(order);
        //从缓存中获取该订单信息
        Order redisOrder = getRedisOrder(order.getUid());
        log.info("从缓存中获取订单信息"+redisOrder);
        redisOrder.setStatus(status);
        redisOrder.getOrderStatusHistory().add(status);
        redisOrder.setTrackInfo(order.getTrackInfo());
        //更新redis数据
        redisService.set("orders:order:"+order.getUid(),redisOrder);
        log.info("更新缓存中的订单信息"+redisOrder);
        return redisOrder;
    }

    /**
     * 从缓存中查询订单信息
     * @param uid
     * @return
     */
    public Order getRedisOrder(int uid){
        if(uid==0){
            return null;
        }
        Order order=null;
        boolean hasKey = redisService.exists(REDIS_ORDER_NAME+":"+uid);
        if(hasKey){
            //获取缓存
            order= (Order) redisService.get(REDIS_ORDER_NAME+":"+uid);
            log.info("从缓存获取的数据"+ order);
        }else{
            //从数据库中获取信息
            log.info("从数据库中获取数据");
            order = orderMapper.getOrder(uid);
            getRedisOrderDetail(order);
            redisService.set(REDIS_ORDER_NAME+":"+uid,order);
            log.info("数据插入缓存" + order);
        }
        return order;
    }

    /**
     * 从缓存中查询订单内部信息
     * @param order
     */
    public void getRedisOrderDetail(Order order){
        Customer redisCustomer = customerService.getRedisCustomer(order.getCustomer().getUid());
        redisCustomer.setPassword(null);
//            redisCustomer.setAvatar(null);
        order.setCustomer(redisCustomer);
        //查询服务员信息
        Clerk redisClerk = clerkService.getRedisClerk(order.getClerk().getUid());
        if(redisClerk!=null) {
            redisClerk.setPassword(null);
            redisClerk.setAvatar(null);
            order.setClerk(redisClerk);
        }
        //查询产品信息
        for(OrderProduct orderProduct:order.getProducts()){
            Product redisProduct = productService.getRedisProduct(orderProduct.getProduct().getUid());
            ActivityRule redisActivityRule = activityService.getRedisActivityRule(orderProduct.getActivityRule().getUid());
            ActivityService.clearActivityRule(redisActivityRule);
            orderProduct.setActivityRule(redisActivityRule);
            orderProduct.setProduct(redisProduct);
        }
        //查询订单的全局优惠规则
        ActivityRule redisActivityRule = activityService.getRedisActivityRule(order.getActivityRule().getUid());
        ActivityService.clearActivityRule(redisActivityRule);
        order.setActivityRule(redisActivityRule);
//        order.setAmount(AmountUtil.computeAmount(order));
    }

    //从缓存中查询订单列表内部信息
    public void getRedisOrdersDetail(List<Order> orders){
        for(Order order:orders){
            getRedisOrderDetail(order);
        }
    }

    /**
     * 卖家主动退款
     * @param order
     * @return
     */
    public Order updateOrderRefunded(Order order) {
        //在orderStatus表插入最新状态
        long time=TimeUtil.getCurrentTimestamp();
        OrderStatus status=new OrderStatus(0,order.getUid(), CommConstants.OrderStatus.REFUND,time,order.getClerk());
        Order redisOrder = updateRedisOrderRefundStatus(order, status);
//        orderMapper.saveOrderStatus(status);
//        //更新卖家留言
//        orderMapper.saveSellerPs(order);
//        //从缓存中获取该订单信息
//        Order redisOrder = getRedisOrder(order.getUid());
//        log.info("从缓存中获取订单信息"+redisOrder);
//        redisOrder.setStatus(status);
//        redisOrder.getOrderStatusHistory().add(status);
//        redisOrder.setSellerPs(order.getSellerPs());
//        //更新redis数据
//        redisService.set("orders:order:"+order.getUid(),redisOrder);
//        log.info("更新缓存中的订单信息"+redisOrder);
        return redisOrder;
    }

    /**
     * 卖家拒绝买家申请退款
     * @param order
     * @return
     */
    public Order updateOrderRejectRefunded(Order order) {
        //在orderStatus表插入最新状态
        long time=TimeUtil.getCurrentTimestamp();
        OrderStatus status=new OrderStatus(0,order.getUid(), CommConstants.OrderStatus.REJECT_REFUND,time,order.getClerk());
        Order redisOrder = updateRedisOrderRefundStatus(order, status);
        return redisOrder;
    }

    private Order updateRedisOrderRefundStatus(Order order,OrderStatus status){
        orderMapper.saveOrderStatus(status);
        //更新卖家留言
        orderMapper.saveSellerPs(order);
        //从缓存中获取该订单信息
        Order redisOrder = getRedisOrder(order.getUid());
        log.info("从缓存中获取订单信息"+redisOrder);
        redisOrder.setStatus(status);
        redisOrder.getOrderStatusHistory().add(status);
        redisOrder.setSellerPs(order.getSellerPs());
        //更新redis数据
        redisService.set("orders:order:"+order.getUid(),redisOrder);
        log.info("更新缓存中的订单信息"+redisOrder);
        return redisOrder;
    }

    /**
     * 根据客户id获取其未完成订单列表
     * @param customerId
     * @return
     */
    public List<Order> getUncompleteOrders(int customerId) {
        List<Order> uncompleteOrders = orderMapper.getCustomerUncompleteOrders(customerId);
        //查询缓存中订单信息
        getRedisOrdersDetail(uncompleteOrders);
        for(Order order:uncompleteOrders){
            order.getCustomer().setAvatar(null);
        }
        for(Order order:uncompleteOrders){
            for(OrderProduct orderProduct:order.getProducts()){
                orderProduct.getProduct().setPhotos(null);
            }
        }
        return uncompleteOrders;
    }

    /**
     * 更新订单信息为客户自提
     * @param order
     * @return
     */
    public Order updateMobileOrderShipped(Order order) {
        //在orderStatus表插入最新状态
        long time=TimeUtil.getCurrentTimestamp();
        OrderStatus pickUpStatus=new OrderStatus(0,order.getUid(), CommConstants.OrderStatus.CUSTOMER_PICK_UP,time,order.getClerk());
        orderMapper.saveOrderStatus(pickUpStatus);
        //从缓存中获取该订单信息
        Order redisOrder = getRedisOrder(order.getUid());
        log.info("从缓存中获取订单信息"+redisOrder);
        redisOrder.setStatus(pickUpStatus);
        redisOrder.getOrderStatusHistory().add(pickUpStatus);
        //更新redis数据
        redisService.set("orders:order:"+order.getUid(),redisOrder);
        log.info("更新缓存中的订单信息"+redisOrder);
        return redisOrder;
    }

    /**
     * 保存包厢预约信息
     * @param order
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveReservation(Order order) {
        try{
            long time=TimeUtil.getCurrentTimestamp();
            order.setOrderTime(time);
            orderMapper.saveReservationOrder(order);
            OrderStatus orderStatus=new OrderStatus(order.getUid(),CommConstants.OrderStatus.UNPAY,time,order.getClerk());
            orderMapper.saveOrderStatus(orderStatus);
            order.setStatus(orderStatus);
            order.getOrderStatusHistory().add(orderStatus);
            for(Reservation reservation:order.getReservations()){
                orderMapper.saveReservation(reservation,order.getUid());
            }
        }catch (Exception e){
            e.printStackTrace();
            String msg="以下时间段：";
            for(Reservation reservation:order.getReservations()){
                Reservation result = shopBoxMapper.findReservation(reservation.getReservationTime(), reservation.getBoxId());
                if (result!=null){
                    msg+=TimeUtil.convertTimestampToTimeFormat(reservation.getReservationTime())+"\n";
                }
            }
            msg+="已被预约，请重新选择";
            throw new GlobalException(CodeMsg.RESERVATION_DUPLICATE(msg));
        }
    }


    /**
     * 查询未付款预约包厢订单
     * @param customerId
     * @return
     */
    public List<Order> getUnpayReservationOrder(int customerId) {
        List<Order> result=orderMapper.getUnpayReservationOrder(customerId);
        return result;
    }

    /**
     * 设置包厢预约订单为付款完成
     * @param order
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateReservationComplete(Order order) {
        long time=TimeUtil.getCurrentTimestamp();
        OrderStatus pay=new OrderStatus(order.getUid(),CommConstants.OrderStatus.PAYED,time,order.getClerk());
        orderMapper.saveOrderStatus(pay);
        OrderStatus complete=new OrderStatus(order.getUid(),CommConstants.OrderStatus.COMPLETE,time,order.getClerk());
        orderMapper.saveOrderStatus(complete);
    }

    /**
     * 保存订单信息
     * @param order
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrder(Order order) {
        try{
            long time= TimeUtil.getCurrentTimestamp();
            order.setOrderTime(time);
            orderMapper.saveOrder(order);
            for(OrderProduct orderProduct:order.getProducts()) {
                orderMapper.saveOrderProduct(orderProduct,order.getUid());
            }
            //TODO 保存店员优惠
        }catch (Exception e){
            e.printStackTrace();
            throw new GlobalException(CodeMsg.PLACE_ORDER_FAIL);
        }
    }

    /**
     * 保存订单状态
     * @param status
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderStatus(OrderStatus status) {
        try {
            orderMapper.saveOrderStatus(status);
        }catch (Exception e){
            throw new GlobalException(CodeMsg.UPDATE_ORDER_STATUS_FAIL);
        }
    }

    /**
     * 计算订单总价
     * @param order
     */
    public float calculateAmount(Order order){
        //计算总价
        float ingot=0;
        float credit=0;
        Map<Integer,List<OrderProduct>> moneyOffMap=new HashMap<>();
        for(OrderProduct orderProduct:order.getProducts()){
            //获取商品详情
            Product product = productService.getProduct(orderProduct.getProduct().getUid());
            ActivityRule activityRule = orderProduct.getActivityRule();
            if(activityRule==null){
                //此产品不参与优惠活动
                ingot+=orderProduct.getNumber()*product.getPrice().getIngot();
                credit+=orderProduct.getNumber()*product.getPrice().getCredit();
            }else{
                //产品参与优惠活动
                ActivityRule redisActivityRule = activityService.getRedisActivityRule(activityRule.getUid());
                ActivityRule2 activityRule2 = redisActivityRule.getActivityRule2();
                if(activityRule2==null){
                    //折扣
                    ingot+=orderProduct.getNumber()*product.getPrice().getIngot()*(100-redisActivityRule.getActivityRule1())/100;
                    credit+=orderProduct.getNumber()*product.getPrice().getCredit();
                }else{
                    //购物
                    if(moneyOffMap.containsKey(activityRule.getUid())){
                        List<OrderProduct> products = moneyOffMap.get(activityRule.getUid());
                        products.add(orderProduct);
                    }else{
                        List<OrderProduct> products=new ArrayList<>();
                        products.add(orderProduct);
                        moneyOffMap.put(activityRule.getUid(),products);
                    }
                }
            }
        }
        //计算购物活动的总额
        float giftCredit=0;
        for(Integer key : moneyOffMap.keySet()){
            ActivityRule redisActivityRule = activityService.getRedisActivityRule(key);
            List<OrderProduct> orderProducts = moneyOffMap.get(key);
            float moneyOffIngot=0;
            float moneyOffCredit=0;
            for(OrderProduct orderProduct: orderProducts){  //获取商品详情
                Product product = productService.getProduct(orderProduct.getProduct().getUid());
                moneyOffCredit+=orderProduct.getNumber()*product.getPrice().getCredit();
                moneyOffIngot+=orderProduct.getNumber()*product.getPrice().getIngot();
            }
            if(moneyOffIngot>redisActivityRule.getActivityRule1()){
                //满足条件
                ActivityRule2 activityRule2 = redisActivityRule.getActivityRule2();
                if(activityRule2.getCurrency().equals("ingot")){//TODO 规则2改为元宝满减，积分满赠
                    //满xx减xx元宝
                    moneyOffIngot-=activityRule2.getNumber();
                }else{
                    //满xx赠xx积分
                    giftCredit+=activityRule2.getNumber();
                }
            }
            ingot+=moneyOffIngot;
            credit+=moneyOffCredit;
        }
        // 计算店员优惠,只用于元宝
        ingot*=order.getClerkDiscount()/100.0;
        order.setCredit(credit);
        order.setIngot(ingot);
        return giftCredit;
    }

    public Amount placeOrder(Order order){
        try{
            //计算总价
            float giftCredit = calculateAmount(order);
            float ingot = order.getIngot();
            float credit = order.getCredit();
            //保存订单
            saveOrder(order);
            //TODO 减少产品库存
            //设置订单状态为未付款
            OrderStatus unpay=new OrderStatus(order.getUid(), CommConstants.OrderStatus.UNPAY, TimeUtil.getCurrentTimestamp(),order.getClerk());
            saveOrderStatus(unpay);
            //查询客户余额
            Amount customerBalance = customerService.getCustomerBalance(order.getCustomer().getUid());
            //检查客户余额
            try {
                if (customerService.checkBalance(ingot, credit, customerBalance)) {
                    //余额充足,自动扣费
                    customerService.pay(ingot, credit, order.getCustomer().getUid());
                    //设置订单状态为付款
                    OrderStatus payed = new OrderStatus(order.getUid(), CommConstants.OrderStatus.PAYED, TimeUtil.getCurrentTimestamp(), order.getClerk());
                    saveOrderStatus(payed);
//                    OrderStatus complete = new OrderStatus(order.getUid(), CommConstants.OrderStatus.COMPLETE, TimeUtil.getCurrentTimestamp(), order.getClerk());
//                    saveOrderStatus(complete);
                }
            }catch (GlobalException e){
                if(e.getCodeMsg().getCode()!=500700){
                    throw  e;
                }else{
                    //设置订单有效时间为15分钟，15分钟后若还没有付款则自动取消订单
                    Runnable runnable = new Runnable() {
                        public void run() {
                            // task to run goes here
                            Order currentOrder = orderMapper.getOrder(order.getUid());
                            if(currentOrder.getStatus().getStatus().equals(CommConstants.OrderStatus.UNPAY)){
                                log.info("未付款，取消订单");
                                for(OrderProduct orderProduct:currentOrder.getProducts()){
                                    orderMapper.deleteOrderProduct(orderProduct.getUid());
                                }
                                orderMapper.deleteOrder(currentOrder.getUid());
                                //TODO 恢复产品库存
                            }
                        }
                    };
                    ScheduledThreadPoolExecutor executor=new ScheduledThreadPoolExecutor(2);
                    executor.schedule(runnable, 15,  TimeUnit.MINUTES);
                    throw e;
                }
            }
            //赠送积分
            if(giftCredit>0){
                customerService.addCredit(order.getCustomer().getUid(),giftCredit);
            }
            //查询账户余额
            Amount currentBalance = customerService.getCustomerBalance(order.getCustomer().getUid());
            return currentBalance;
        }catch (GlobalException e){
            throw e;
        }catch (Exception e){
            e.printStackTrace();
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
    }

    /**
     * 获取最近一次未付费订单
     * @param customerId
     * @return
     */
    public Order getLatestUnpayOrder(int customerId) {
        Order latestUnpayOrder=orderMapper.getLatestUnpayOrder(customerId);
        return latestUnpayOrder;
    }

    /**
     * 将订单状态更改为已付款
     * @param order
     */
    public void updateOrderPayed(Order order) {
        OrderStatus payed=new OrderStatus(order.getUid(),CommConstants.OrderStatus.PAYED,TimeUtil.getCurrentTimestamp());
        orderMapper.saveOrderStatus(payed);
    }

    /**
     * 取消订单
     * @param orderId
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteOrder(int orderId) {
        Order order = orderMapper.getOrder(orderId);
        if(order==null){
            throw new GlobalException(CodeMsg.ORDER_DELETED);
        }
        for(OrderProduct orderProduct:order.getProducts()){
            orderMapper.deleteOrderProduct(orderProduct.getUid());
        }
        orderMapper.deleteOrder(orderId);
        redisService.remove(REDIS_ORDER_NAME+":"+orderId);
    }

    public void deleteOrderProduct(int uid) {
        orderMapper.deleteOrderProduct(uid);
    }

    /**
     * 设置收货地址和买家留言
     * @param order
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderAddressAndPs(Order order) {
        orderMapper.updateOrderAddressAndPs(order);
    }


    /**
     * 小程序获取客户所有状态的订单（10条）
     * @param page
     * @param customerId
     * @return
     */
    public List<Order> getAllOrders(int page, int customerId) {
        List<Order> orders=orderMapper.getAllOrders(page*10,customerId);
        return orders;
    }

    /**
     * 小程序获取客户所有未付款的订单（10条）
     * @param page
     * @param customerId
     * @return
     */
    public List<Order> getUnpayOrders(int page, int customerId) {
        List<Order> orders=orderMapper.getUnapyOrders(page*10,customerId);
        return orders;
    }

    /**
     * 小程序获取客户所有已付款的订单（10条）
     * @param page 分页数
     * @param customerId 客户id
     * @return
     */
    public List<Order> getPayedOrders(int page, int customerId) {
        List<Order> orders=orderMapper.getPayedOrders(page*10,customerId);
        return orders;
    }

    /**
     * 小程序获取客户所有已发货的订单（10条）
     * @param page 分页数
     * @param customerId 客户id
     * @return
     */
    public List<Order> getShippedOrders(int page, int customerId) {
        List<Order> orders=orderMapper.getShippedOrders(page*10,customerId);
        return orders;
    }

    public void clearOrders(List<Order> orders){
        for(Order order:orders){
            Clerk processer=new Clerk();
            processer.setUid(order.getClerk().getUid());
            processer.setName(order.getClerk().getName());
            order.setClerk(processer);
            order.setCustomer(null);
            for(OrderProduct orderProduct:order.getProducts()){
                Photo photo=orderProduct.getProduct().getPhotos().get(0);
                List<Photo> photos=new ArrayList<>();
                photos.add(photo);
                orderProduct.getProduct().setPhotos(photos);
                orderProduct.getProduct().setProductDetails(null);
                orderProduct.getProduct().setShop(null);
                orderProduct.getProduct().setActivityRules(null);
                orderProduct.getProduct().setActivities(null);
            }
        }
    }
}
