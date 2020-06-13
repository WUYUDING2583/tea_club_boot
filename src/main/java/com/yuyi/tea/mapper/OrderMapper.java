package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.*;
import com.yuyi.tea.common.Amount;
import com.yuyi.tea.common.TimeRange;
import com.yuyi.tea.typehandler.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface OrderMapper {

    //获取客户订单列表
    @Select("<script>" +
            "select * from orders where customerId=#{customerId} and " +
            "<if test='timeRange.startDate!=-1'> <![CDATA[orderTime>= #{timeRange.startDate}]]> and </if>" +
            "<![CDATA[orderTime<=#{timeRange.endDate}]]> order by orderTime desc" +
            "</script>")
    @ResultMap(value = "order")
    List<Order> getOrdersByCustomer(int customerId, TimeRange timeRange);

    //根据订单id获取该订单所有产品信息
    @Select("select * from orderProduct where orderId=#{orderId}")
    @Results({
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column = "productId",property = "product",
                one=@One(select="com.yuyi.tea.mapper.ProductMapper.getProduct",
                        fetchType = FetchType.LAZY)),
            @Result(column = "activityRuleId",property = "activityRule",typeHandler = ActivityRuleTypeHandler.class)
    })
    List<OrderProduct> getOrderProducts(int orderId);

    @Select("select * from orders where customerId=#{customerId} and uid in " +
            "(select A.orderId from orderStatus A, orderCurrentTime B where A.orderId=B.orderId and A.time=B.time and A.status!='complete' and A.status!='refunded')")
    @ResultMap("order")
    List<Order> getCustomerUncompleteOrders(int customerId);

    /**
     * 获取未完成的订单列表
     * @return
     */
    @Select("select * from orders where uid not in (" +
            "select orderId from reservation" +
            ") and uid in " +
            "(select A.orderId from orderStatus A, orderCurrentTime B where A.orderId=B.orderId and A.time=B.time and A.status!='complete' and A.status!='refunded' and A.status!='unpay')" +
            "order by orderTime desc")
    @Results(id="order",
            value = {
                    @Result(id = true,column = "uid",property = "uid"),
                    @Result(column = "customerId",property = "customer",
                            one = @One(select="com.yuyi.tea.mapper.CustomerMapper.getCustomerByUid",
                            fetchType = FetchType.LAZY)),
                    @Result(column = "clerkId",property = "clerk",
                            one = @One(select="com.yuyi.tea.mapper.ClerkMapper.getClerk",
                                    fetchType = FetchType.LAZY)),
                    @Result(column = "placeOrderWay",property = "placeOrderWay",
                            one = @One(select="com.yuyi.tea.mapper.ShopMapper.getShopName",
                                    fetchType = FetchType.LAZY)),
                    @Result(column = "boxOrder",property = "boxOrder",
                            one = @One(select="com.yuyi.tea.mapper.ShopBoxMapper.getShopBoxByUid",
                                    fetchType = FetchType.LAZY)),
                    @Result(column = "placeOrderWay",property = "placeOrderWay",
                            one = @One(select="com.yuyi.tea.mapper.ShopMapper.getShopName",
                                    fetchType = FetchType.LAZY)),
                    @Result(column = "deliverMode",property = "deliverMode"),
                    @Result(column = "uid",property = "products",
                            one = @One(select="com.yuyi.tea.mapper.OrderMapper.getOrderProducts",
                                    fetchType = FetchType.LAZY)),
                    @Result(column = "activityRuleId",property = "activityRule",typeHandler = ActivityRuleTypeHandler.class),
                    @Result(column = "trackingId",property = "trackInfo",
                            one = @One(select="com.yuyi.tea.mapper.OrderMapper.getTrackInfo",
                                    fetchType = FetchType.LAZY)),
                    @Result(column = "uid",property = "status",
                            one = @One(select="com.yuyi.tea.mapper.OrderMapper.getOrderCurrentStatus",
                                    fetchType = FetchType.LAZY)),
                    @Result(column = "uid",property = "orderStatusHistory",
                            one = @One(select="com.yuyi.tea.mapper.OrderMapper.getOrderStatusHistory",
                                    fetchType = FetchType.LAZY)),
                    @Result(column = "uid",property = "reservations",
                            one = @One(select="com.yuyi.tea.mapper.ShopBoxMapper.getReservationByOrderId",
                                    fetchType = FetchType.LAZY)),
                    @Result(column = "addressId",property = "address",
                            one = @One(select="com.yuyi.tea.mapper.CustomerMapper.getAddress",
                                    fetchType = FetchType.LAZY)),
            })
    List<Order> getUncompleteOrders();

    /**
     * 根据条件获取订单列表
     * @param status
     * @param timeRange
     * @return
     */
    @Select("<script>" +
            "select * from orders where uid not in (" +
            "select orderId from reservation" +
            ") and " +
            "<if test='status!=\"all\"'> uid in (select A.orderId from orderStatus A, orderCurrentTime B where A.orderId=B.orderId and A.time=B.time and A.status=#{status}) and</if>" +
            "<if test='timeRange.startDate!=-1'> <![CDATA[orderTime>= #{timeRange.startDate}]]> and </if>" +
            "<![CDATA[orderTime<=#{timeRange.endDate}]]> order by orderTime desc" +
            "</script>")
    @ResultMap(value = "order")
    List<Order> getOrders(String status, TimeRange timeRange);

    /**
     * 根据uid获取订单详细信息
     * @param uid
     * @return
     */
    @Select("select * from orders where uid=${uid}")
    @ResultMap(value = "order")
    Order getOrder(int uid);

    //获取订单信息
    @Select("select * from trackInfo where uid=#{uid}")
    TrackInfo getTrackInfo(int uid);

    //获取订单状态历史信息
    @Select("select * from orderStatus where orderId=#{orderId} order by time desc")
    @Results(id="orderStatuls",
            value = {
                    @Result(id = true,column = "uid",property = "uid"),
                    @Result(column = "processer",property = "processer",
                            one = @One(select="com.yuyi.tea.mapper.ClerkMapper.getBriefClerk",
                                    fetchType = FetchType.LAZY))
            })
    List<OrderStatus> getOrderStatusHistory(int orderId);

    //获取订单当前状态
    @Select("select * from orderStatus where orderId=#{orderId} order by time desc limit 1")
    @ResultMap("orderStatuls")
    OrderStatus getOrderCurrentStatus(int orderId);

    //将订单状态更新为已发货
    @Insert("<script>" +
            "insert into orderStatus(orderId,status,time,processer) values(#{orderId},#{status},#{time},<if test='#{processer}!=null'>#{processer.uid}</if><if test='#{processer}==null'>null</if>)" +
            "</script>")
    @Options(useGeneratedKeys=true, keyProperty="uid")
    void saveOrderStatus(OrderStatus status);

    //保存物流信息
    @Insert("insert into trackInfo(companyName,trackingId,description,phone) values(#{companyName},#{trackingId},#{description},#{phone})")
    @Options(useGeneratedKeys=true, keyProperty="uid")
    void saveTrackInfo(TrackInfo trackInfo);

    //更新orders表的trackingId
    @Update("update orders set trackingId=#{trackInfo.uid} where uid=#{uid}")
    void updateOrderTrackingId(Order order);

    //更新卖家留言
    @Update("update orders set sellerPs=#{sellerPs} where uid=#{uid}")
    void saveSellerPs(Order order);


    /**
     * 保存包厢预约订单
     * @param order
     */
    @Insert("insert orders(orderTime,customerId,clerkId,ingot,credit,placeOrderWay) values(#{orderTime},#{customer.uid},#{clerk.uid},#{ingot},#{credit},#{placeOrderWay.uid})")
    @Options(useGeneratedKeys=true, keyProperty="uid")
    void saveReservationOrder(Order order);

    /**
     * 保存包厢预约记录
     * @param reservation
     * @param orderId
     */
    @Insert("insert into reservation(reservationTime,boxId,orderId) values(#{reservation.reservationTime},#{reservation.box.uid},#{orderId})")
    void saveReservation(Reservation reservation, int orderId);

    /**
     * 查询未付款预约包厢订单
     * @param customerId
     * @return
     */
    @Select("select uid,orderTime,ingot,credit from orders,reservation where customerId=#{customerId} and uid=reservation.orderId and uid in " +
            "(select A.orderId from orderStatus A, orderCurrentTime B where A.orderId=B.orderId and A.time=B.time and A.status='unpay')" +
            "order by orderTime desc")
    @Results(id="reservation",
            value = {
                    @Result(id = true,column = "uid",property = "uid"),
                    @Result(column = "uid",property = "reservations",
                            many = @Many(select="com.yuyi.tea.mapper.ShopBoxMapper.getReservationByOrderId",
                                    fetchType = FetchType.LAZY)),
                    @Result(column = "uid",property = "status",
                            one = @One(select="com.yuyi.tea.mapper.OrderMapper.getOrderCurrentStatus",
                                    fetchType = FetchType.LAZY)),
                    @Result(column = "uid",property = "orderStatusHistory",
                            one = @One(select="com.yuyi.tea.mapper.OrderMapper.getOrderStatusHistory",
                                    fetchType = FetchType.LAZY)),
            })
    List<Order> getUnpayReservationOrder(int customerId);

    /**
     * 保存移动端订单
     * @param order
     */
    @Insert("<script>" +
            "insert into orders(orderTime,customerId,clerkId,ingot,credit,clerkDiscount,placeOrderWay,addressId,deliverMode,buyerPs,boxOrder) values(#{orderTime},#{customer.uid},#{clerk.uid},#{ingot},#{credit},#{clerkDiscount},#{placeOrderWay.uid},<if test='#{address}!=null'>#{address.uid}</if><if test='#{address}==null'>null</if>,#{deliverMode},#{buyerPs},#{boxOrder.uid})" +
            "</script>")
    @Options(useGeneratedKeys=true, keyProperty="uid")
    void saveOrder(Order order);

    /**
     * 保存订单产品
     * @param orderProduct
     */
    @Insert("insert into orderProduct(productId,orderId,number,activityRuleId) values(#{orderProduct.product.uid},#{orderId},#{orderProduct.number},#{orderProduct.activityRule.uid})")
    void saveOrderProduct(OrderProduct orderProduct,int orderId);

    /**
     * 删除订单产品表的内容
     * @param uid
     */
    @Delete("delete from orderProduct where uid=#{uid}")
    void deleteOrderProduct(int uid);

    /**
     * 删除订单
     * @param uid
     */
    @Delete("delete from orders where uid=#{uid}")
    void deleteOrder(int uid);

    /**
     * 获取最近一次未付费订单
     * @param customerId
     * @return
     */
    @Select("select * from orders where customerId=#{customerId} and uid in " +
            "(select A.orderId from orderStatus A, orderCurrentTime B where A.orderId=B.orderId and A.time=B.time and A.status='unpay')" +
            "order by orderTime desc limit 1")
    @ResultMap("order")
    Order getLatestUnpayOrder(int customerId);

    /**
     * 设置收货地址和买家留言
     * @param order
     */
    @Update("update orders set deliverMode=#{deliverMode}, placeOrderWay=#{placeOrderWay.uid},addressId=#{address.uid},buyerPs=#{buyerPs} where uid=#{uid}")
    void updateOrderAddressAndPs(Order order);


    /**
     * 获取客户所有状态的订单（10条，不含预约）
     * @param offset
     * @param customerId
     * @return
     */
    @Select("select * from orders where customerId=#{customerId} and uid not in " +
            "(select orderId from reservation) " +
            " order by orderTime desc limit #{offset},10")
    @ResultMap("order")
    List<Order> getAllOrders(int offset, int customerId);
    
    /**
     * 获取客户所有未付款的订单（10条，不含预约）
     * @param offset
     * @param customerId
     * @return
     */
    @Select("select * from orders where customerId=#{customerId} and uid not in" +
            "(select orderId from reservation)" +
            " and uid in " +
            "(select A.orderId from orderStatus A, orderCurrentTime B where A.orderId=B.orderId and A.time=B.time and A.status='unpay')" +
            " order by orderTime desc limit #{offset},10")
    @ResultMap("order")
    List<Order> getUnapyOrders(int offset, int customerId);

    /**
     * 获取客户所有已付款的订单（10条，不含预约）
     * @param offset
     * @param customerId
     * @return
     */
    @Select("select * from orders where customerId=#{customerId} and uid not in " +
            "(select orderId from reservation)" +
            " and uid in " +
            "(select A.orderId from orderStatus A, orderCurrentTime B where A.orderId=B.orderId and A.time=B.time and A.status='payed')" +
            " order by orderTime desc limit #{offset},10")
    @ResultMap("order")
    List<Order> getPayedOrders(int offset, int customerId);

    /**
     * 获取客户所有已发货的订单（10条，不含预约）
     * @param offset
     * @param customerId
     * @return
     */
    @Select("select * from orders where customerId=#{customerId} and uid not in " +
            "(select orderId from reservation)" +
            " and uid in " +
            "(select A.orderId from orderStatus A, orderCurrentTime B where A.orderId=B.orderId and A.time=B.time and (A.status='shipped' or A.status='rejectRefund'))" +
            " order by orderTime desc limit #{offset},10")
    @ResultMap("order")
    List<Order> getShippedOrders(int offset, int customerId);

    /**
     * 更新买家退款理由
     * @param orderId
     * @param reason
     */
    @Update("update orders set buyerRefundReason=#{reason} where uid=#{orderId}")
    void updateOrderRequestRefundReason(int orderId, String reason);

    /**
     * 获取客户所有申请退款的订单（10条）
     * @param offset
     * @param customerId
     * @return
     */
    @Select("select * from orders where customerId=#{customerId} and uid not in " +
            "(select orderId from reservation)" +
            " and uid in " +
            "(select A.orderId from orderStatus A, orderCurrentTime B where A.orderId=B.orderId and A.time=B.time and (A.status='requestRefund' or A.status='refunded'))" +
            " order by orderTime desc limit #{offset},10")
    @ResultMap("order")
    List<Order> getRefundOrders(int offset, int customerId);

    /**
     * 获取客户所有未付款的预约（10条）
     * @param offset
     * @param customerId
     * @return
     */
    @Select("select * from orders where customerId=#{customerId} and uid  in " +
            "(select orderId from reservation)" +
            " and uid in " +
            "(select A.orderId from orderStatus A, orderCurrentTime B where A.orderId=B.orderId and A.time=B.time and A.status='unpay')" +
            " order by orderTime desc limit #{offset},10")
    @ResultMap("reservation")
    List<Order> getUnpayReservations(int offset, int customerId);


    /**
     * 取客户所有已付款的预约（10条）
     * @param offset
     * @param customerId
     * @return
     */
    @Select("select * from orders where customerId=#{customerId} and uid  in " +
            "(select orderId from reservation)" +
            " and uid in " +
            "(select A.orderId from orderStatus A, orderCurrentTime B where A.orderId=B.orderId and A.time=B.time and A.status='payed')" +
            " order by orderTime desc limit #{offset},10")
    @ResultMap("reservation")
    List<Order> getPayedReservations(int offset, int customerId);

    /**
     * 获取客户所有已完成的预约（10条）
     * @param offset
     * @param customerId
     * @return
     */
    @Select("select * from orders where customerId=#{customerId} and uid  in " +
            "(select orderId from reservation)" +
            " and uid in " +
            "(select A.orderId from orderStatus A, orderCurrentTime B where A.orderId=B.orderId and A.time=B.time and A.status='complete')" +
            " order by orderTime desc limit #{offset},10")
    @ResultMap("reservation")
    List<Order> getCompleteReservations(int offset, int customerId);

    /**
     * 获取客户所有已退款的预约（10条）
     * @param offset
     * @param customerId
     * @return
     */
    @Select("select * from orders where customerId=#{customerId} and deliverMode is null " +
            " and uid in " +
            "(select A.orderId from orderStatus A, orderCurrentTime B where A.orderId=B.orderId and A.time=B.time and A.status='refunded')" +
            " order by orderTime desc limit #{offset},10")
    @ResultMap("reservation")
    List<Order> getRefundReservations(int offset, int customerId);

    /**
     * 删除预约记录
     * @param orderId
     */
    @Delete("delete from reservation where orderId=#{orderId}")
    void deleteReservationsByOrderId(int orderId);

    /**
     * 获取客户预约订单（10条）
     * @param offset
     * @param customerId
     * @return
     */
    @Select("select * from orders where customerId=#{customerId} and uid  in " +
            "(select orderId from reservation)" +
            " order by orderTime desc limit #{offset},10")
    @ResultMap("reservation")
    List<Order> getAllReservationOrders(int offset, int customerId);
}
