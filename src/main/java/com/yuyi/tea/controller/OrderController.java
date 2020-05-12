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
            //保存订单
            orderService.saveOrder(order);
            //设置订单状态为未付款
            OrderStatus unpay=new OrderStatus(order.getUid(), CommConstants.OrderStatus.UNPAY, TimeUtil.getCurrentTimestamp(),order.getClerk());
            orderService.saveOrderStatus(unpay);
            //查询客户余额
            Amount customerBalance = customerService.getCustomerBalance(order.getCustomer().getUid());
            //检查客户余额
            try {
                if (customerService.checkBalance(ingot, credit, customerBalance)) {
                    //余额充足,自动扣费
                    customerService.pay(ingot, credit, order.getCustomer().getUid());
                    //设置订单状态为付款、完成
                    OrderStatus payed = new OrderStatus(order.getUid(), CommConstants.OrderStatus.PATED, TimeUtil.getCurrentTimestamp(), order.getClerk());
                    orderService.saveOrderStatus(payed);
                    OrderStatus complete = new OrderStatus(order.getUid(), CommConstants.OrderStatus.COMPLETE, TimeUtil.getCurrentTimestamp(), order.getClerk());
                    orderService.saveOrderStatus(complete);
                }
            }catch (GlobalException e){
                if(e.getCodeMsg().getCode()!=500700){
                    throw  e;
                }
            }
            //赠送积分
            if(giftCredit>0){
                customerService.addCredit(order.getCustomer().getUid(),giftCredit);
            }
            //查询账户余额
            Amount currentBalance = customerService.getCustomerBalance(order.getCustomer().getUid());
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


}
