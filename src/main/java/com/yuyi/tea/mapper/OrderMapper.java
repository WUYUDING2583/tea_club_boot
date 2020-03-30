package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.ActivityRule;
import com.yuyi.tea.bean.Order;
import com.yuyi.tea.bean.OrderProduct;
import com.yuyi.tea.component.TimeRange;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface OrderMapper {

    //获取客户订单列表
    @Select("<script>" +
            "select * from orders where customerId=#{customerId} and " +
            "<if test='timeRange.startDate!=-1'> <![CDATA[orderTime>= #{timeRange.startDate}]]> and </if>" +
            "<![CDATA[orderTime<=#{timeRange.endDate}]]> order by orderTime desc" +
            "</script>")
    @Results({
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column = "customerId",property = "customer",
                    one = @One(select="com.yuyi.tea.mapper.CustomerMapper.getCustomerByUid",
                            fetchType = FetchType.LAZY)),
            @Result(column = "clerkId",property = "clerk",
                    one = @One(select="com.yuyi.tea.mapper.ClerkMapper.getClerk",
                            fetchType = FetchType.LAZY)),
            @Result(column = "uid",property = "products",
                    one = @One(select="com.yuyi.tea.mapper.OrderMapper.getOrderProducts",
                            fetchType = FetchType.LAZY)),
            @Result(column = "uid",property = "activityRule",
                    one = @One(select="com.yuyi.tea.mapper.ActivityMapper.getActivityRule",
                            fetchType = FetchType.LAZY))
    })
    List<Order> getOrdersByCustomer(int customerId, TimeRange timeRange);

    //根据订单id获取该订单所有产品信息
    @Select("select * from orderProduct where orderId=#{orderId}")
    @Results({
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column = "productId",property = "product",
                    one = @One(select="com.yuyi.tea.mapper.ProductMapper.getProduct",
                            fetchType = FetchType.LAZY)),
            @Result(column = "activityRuleId",property = "activityRule",
                    one = @One(select="com.yuyi.tea.mapper.ActivityMapper.getActivityRule",
                            fetchType = FetchType.LAZY)),
    })
    List<OrderProduct> getOrderProducts(int orderId);


    //获取未完成的订单列表
    @Select("select * from orders where status!='complete' order by orderTime desc")
    @Results({
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column = "customerId",property = "customer",
                    one = @One(select="com.yuyi.tea.mapper.CustomerMapper.getCustomerByUid",
                            fetchType = FetchType.LAZY)),
            @Result(column = "clerkId",property = "clerk",
                    one = @One(select="com.yuyi.tea.mapper.ClerkMapper.getClerk",
                            fetchType = FetchType.LAZY)),
            @Result(column = "uid",property = "products",
                    one = @One(select="com.yuyi.tea.mapper.OrderMapper.getOrderProducts",
                            fetchType = FetchType.LAZY)),
            @Result(column = "uid",property = "activityRule",
                    one = @One(select="com.yuyi.tea.mapper.ActivityMapper.getActivityRule",
                            fetchType = FetchType.LAZY))
    })
    List<Order> getUncompleteOrders();

    //根据条件获取订单列表
    @Select("<script>" +
            "select * from orders where " +
            "<if test='status!=\"all\"'>status=#{status} and</if>" +
            "<if test='timeRange.startDate!=-1'> <![CDATA[orderTime>= #{timeRange.startDate}]]> and </if>" +
            "<![CDATA[orderTime<=#{timeRange.endDate}]]> order by orderTime desc" +
            "</script>")
    @Results({
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column = "customerId",property = "customer",
                    one = @One(select="com.yuyi.tea.mapper.CustomerMapper.getCustomerByUid",
                            fetchType = FetchType.LAZY)),
            @Result(column = "clerkId",property = "clerk",
                    one = @One(select="com.yuyi.tea.mapper.ClerkMapper.getClerk",
                            fetchType = FetchType.LAZY)),
            @Result(column = "uid",property = "products",
                    one = @One(select="com.yuyi.tea.mapper.OrderMapper.getOrderProducts",
                            fetchType = FetchType.LAZY)),
            @Result(column = "uid",property = "activityRule",
                    one = @One(select="com.yuyi.tea.mapper.ActivityMapper.getActivityRule",
                            fetchType = FetchType.LAZY))
    })
    List<Order> getOrders(String status, TimeRange timeRange);
}
