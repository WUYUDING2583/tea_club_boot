package com.yuyi.tea.controller;

import com.google.gson.Gson;
import com.yuyi.tea.bean.*;
import com.yuyi.tea.common.*;
import com.yuyi.tea.common.utils.TimeUtil;
import com.yuyi.tea.exception.GlobalException;
import com.yuyi.tea.mapper.ShopBoxMapper;
import com.yuyi.tea.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ShopBoxMapper shopBoxMapper;

    @Autowired
    private ShopBoxService shopBoxService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private WebSocketBalanceServer ws;

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

    @PostMapping("/mobile/reserve")
    public Order reserve(@RequestBody Order order){
        try {
            //计算总价
            float ingot = 0;
            float credit = 0;
            ShopBox box = shopBoxService.getShopBoxByUid(order.getReservations().get(0).getBoxId());
            final float priceIngot = box.getPrice().getIngot();
            final float priceCredit = box.getPrice().getCredit();
            for (Reservation reservation : order.getReservations()) {
                ingot += priceIngot;
                credit += priceCredit;
            }
            order.setIngot(ingot);
            order.setCredit(credit);
            orderService.saveReservation(order);
            //检查账户余额
            customerService.checkBalance(ingot,credit,order.getCustomer().getUid());
            //扣除金额
            customerService.pay(ingot, credit, order.getCustomer().getUid());
            //保存订单状态
            orderService.updateReservationComplete(order);
            //查询账户余额
            Amount customerBalance = customerService.getCustomerBalance(order.getCustomer().getUid());
            Result result=new Result(customerBalance);
            ws.sendInfo(new Gson().toJson(result), order.getCustomer().getUid()+"");
            return order;
        }catch (GlobalException e){
            throw e;
        }catch (Exception e){
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
    }

    /**
     * 移动端店员下单
     * @param order
     */
    @PostMapping("/mobile/order")
    public void placeOrder(@RequestBody Order order){
        try{
            Amount currentBalance = orderService.placeOrder(order);
            Result result=new Result(currentBalance);
            ws.sendInfo(new Gson().toJson(result), order.getCustomer().getUid()+"");
        }catch (GlobalException e){
            throw e;
        }catch (Exception e){
            e.printStackTrace();
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
    }

    @GetMapping("/mobile/orders/{customerId}")
    public List<Order> getRefreshOrders(@PathVariable int customerId){
        //获取该用户最近3个月的订单
        long startDate = TimeUtil.getNDayAgoStartTime(90);
        long endDate = TimeUtil.getNDayAgoStartTime(-1);
        TimeRange timeRange = new TimeRange(startDate, endDate);
        List<Order> orders = orderService.getOrdersByCustomer(customerId, timeRange);
        return orders;
    }

    /**
     * 小程序直接购买商品下单
     * @param order
     * @return
     */
    @PostMapping("/mp/order/product")
    public Order placeMpOrderProduct(@RequestBody Order order){
        try{
           orderService.placeOrder(order);
            Order currentOrder = orderService.getOrder(order.getUid());
            return currentOrder;
        }catch (GlobalException e){
            throw e;
        }catch (Exception e){
            e.printStackTrace();
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
    }

    /**
     * 小程序提交购物车订单
     * @param order
     * @return
     */
    @PostMapping("/mp/order")
    public Order placeMpOrder(@RequestBody Order order){
        try{
            //计算总价
            float giftCredit = orderService.calculateAmount(order);
            float ingot = order.getIngot();
            float credit = order.getCredit();
            //保存订单
            orderService.saveOrder(order);
            //TODO 减少产品库存
            //设置订单状态为未付款
            OrderStatus unpay=new OrderStatus(order.getUid(), CommConstants.OrderStatus.UNPAY, TimeUtil.getCurrentTimestamp(),order.getClerk());
            orderService.saveOrderStatus(unpay);
            //设置订单有效时间为15分钟，15分钟后若还没有付款则自动取消订单
            Runnable runnable = new Runnable() {
                public void run() {
                    // task to run goes here
                    Order currentOrder = orderService.getOrder(order.getUid());
                    if(currentOrder.getStatus().getStatus().equals(CommConstants.OrderStatus.UNPAY)){
                        log.info("未付款，取消订单");
                        for(OrderProduct orderProduct:currentOrder.getProducts()){
                            orderService.deleteOrderProduct(orderProduct.getUid());
                        }
                        orderService.deleteOrder(currentOrder.getUid());
                        //TODO 恢复产品库存
                    }
                }
            };
            ScheduledThreadPoolExecutor executor=new ScheduledThreadPoolExecutor(2);
            executor.schedule(runnable, 15,  TimeUnit.MINUTES);
        }catch (GlobalException e){
            throw e;
        }catch (Exception e){
            e.printStackTrace();
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        return order;
    }

    /**
     * 小程序获取对应订单
     * @param orderId
     * @return
     */
    @GetMapping("/mp/order/{orderId}")
    public Order getMpOrder(@PathVariable int orderId){
        Order order = orderService.getOrder(orderId);
        return order;
    }

    /**
     * 小程序获取客户最近的未付款订单
     * @param customerId
     * @return
     */
    @GetMapping("/mp/latestUnpayOrder/{customerId}")
    public Order getLatestUnPayOrder(@PathVariable int customerId){
        Order latestUnpayOrder = orderService.getLatestUnpayOrder(customerId);
        for(OrderProduct orderProduct:latestUnpayOrder.getProducts()){
            orderProduct.getProduct().setActivityRules(null);
            orderProduct.getProduct().setShop(null);
            orderProduct.getProduct().setProductDetails(null);
        }
        return latestUnpayOrder;
    }

    /**
     * 小程序取消订单
     * @param orderId
     * @return
     */
    @PostMapping("/mp/order/cancel/{orderId}")
    public String mpCancelOrder(@PathVariable int orderId){
        orderService.deleteOrder(orderId);
        return "success";
    }


    @PostMapping("/mp/order/refund/{orderId}")
    public String mpRefundOrder(@PathVariable int orderId,@RequestBody String reason){
        Order order = orderService.getOrder(orderId);
        //更新订单状态
        orderService.updateOrderRequestRefund(orderId,reason);
        return "success";
    }

    /**
     * 小程序预约包厢
     * @param order
     * @return
     */
    @PostMapping("/mp/reserve")
    public Order mpReserve(@RequestBody Order order){
        try {
            //计算总价
            float ingot = 0;
            float credit = 0;
            ShopBox box = shopBoxService.getShopBoxByUid(order.getReservations().get(0).getBoxId());
            final float priceIngot = box.getPrice().getIngot();
            final float priceCredit = box.getPrice().getCredit();
            for (Reservation reservation : order.getReservations()) {
                ingot += priceIngot;
                credit += priceCredit;
            }
            order.setIngot(ingot);
            order.setCredit(credit);
            orderService.saveReservation(order);
            //设置订单有效时间为15分钟，15分钟后若还没有付款则自动取消订单
            Runnable runnable = new Runnable() {
                public void run() {
                    // task to run goes here
                    Order currentOrder = orderService.getOrder(order.getUid());
                    if(currentOrder.getStatus().getStatus().equals(CommConstants.OrderStatus.UNPAY)){
                        log.info("未付款，取消订单");
                        orderService.deleteOrder(order.getUid());
                    }
                }
            };
            ScheduledThreadPoolExecutor executor=new ScheduledThreadPoolExecutor(2);
            executor.schedule(runnable, 15,  TimeUnit.MINUTES);
            return order;
        }catch (GlobalException e){
            throw e;
        }catch (Exception e){
            e.printStackTrace();
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
    }

    /**
     * 小程序获取客户所有状态的订单（10条）
     * @param page 分页数
     * @param customerId 客户id
     * @return
     */
    @GetMapping("/mp/orders/all/{page}/{customerId}")
    public List<Order> getMpOrders(@PathVariable int page,@PathVariable int customerId){
        List<Order> orders = orderService.getAllOrders(page,customerId);
        orderService.clearOrders(orders);
        return orders;
    }

    /**
     * 小程序获取客户所有未付款的订单（10条）
     * @param page 分页数
     * @param customerId 客户id
     * @return
     */
    @GetMapping("/mp/orders/unpay/{page}/{customerId}")
    public List<Order> getMpUnpayOrders(@PathVariable int page,@PathVariable int customerId){
        List<Order> orders = orderService.getUnpayOrders(page,customerId);
        orderService.clearOrders(orders);
        return orders;
    }

    /**
     * 小程序获取客户所有已付款的订单（10条）
     * @param page 分页数
     * @param customerId 客户id
     * @return
     */
    @GetMapping("/mp/orders/payed/{page}/{customerId}")
    public List<Order> getMpPayedOrders(@PathVariable int page,@PathVariable int customerId){
        List<Order> orders = orderService.getPayedOrders(page,customerId);
        orderService.clearOrders(orders);
        return orders;
    }

    /**
     * 小程序获取客户所有已发货的订单（10条）
     * @param page 分页数
     * @param customerId 客户id
     * @return
     */
    @GetMapping("/mp/orders/shipped/{page}/{customerId}")
    public List<Order> getMpShippedOrders(@PathVariable int page,@PathVariable int customerId){
        List<Order> orders = orderService.getShippedOrders(page,customerId);
        orderService.clearOrders(orders);
        return orders;
    }

    /**
     * 小程序获取客户所有申请退款的订单（10条）
     * @param page 分页数
     * @param customerId 客户id
     * @return
     */
    @GetMapping("/mp/orders/refund/{page}/{customerId}")
    public List<Order> getMpRefundOrders(@PathVariable int page,@PathVariable int customerId){
        List<Order> orders = orderService.getRefundOrders(page,customerId);
        orderService.clearOrders(orders);
        return orders;
    }


}
