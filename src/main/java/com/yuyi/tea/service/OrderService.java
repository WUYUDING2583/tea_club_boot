package com.yuyi.tea.service;

import com.yuyi.tea.bean.*;
import com.yuyi.tea.common.CommConstants;
import com.yuyi.tea.common.utils.AmountUtil;
import com.yuyi.tea.common.utils.TimeUtil;
import com.yuyi.tea.common.TimeRange;
import com.yuyi.tea.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        redisClerk.setPassword(null);
        redisClerk.setAvatar(null);
        order.setClerk(redisClerk);
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
        order.setAmount(AmountUtil.computeAmount(order));
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
        OrderStatus complete=new OrderStatus(0,order.getUid(), CommConstants.OrderStatus.COMPLETE,time,order.getClerk());
        orderMapper.saveOrderStatus(pickUpStatus);
        orderMapper.saveOrderStatus(complete);
        //从缓存中获取该订单信息
        Order redisOrder = getRedisOrder(order.getUid());
        log.info("从缓存中获取订单信息"+redisOrder);
        redisOrder.setStatus(complete);
        redisOrder.getOrderStatusHistory().add(pickUpStatus);
        redisOrder.getOrderStatusHistory().add(complete);
        //更新redis数据
        redisService.set("orders:order:"+order.getUid(),redisOrder);
        log.info("更新缓存中的订单信息"+redisOrder);
        return redisOrder;
    }
}
