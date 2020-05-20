package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.CartProduct;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface CartMapper {

    /**
     * 获取客户购物车详情
     * @param customerId
     * @return
     */
    @Select("select * from cartDetail where customerId=#{customerId}")
    @Results(id = "cartItem",
            value = {
                    @Result(id = true,column = "uid",property = "uid"),
                    @Result(column="customerId",property="customer",
                            one=@One(select="com.yuyi.tea.mapper.CustomerMapper.getCustomerByUid",
                                    fetchType= FetchType.LAZY)),
                    @Result(column="productId",property="product",
                            one=@One(select="com.yuyi.tea.mapper.ProductMapper.getProduct",
                                    fetchType= FetchType.LAZY))
            })
    List<CartProduct> getCartDetail(int customerId);

    /**
     * 加入购物车
     * @param cartProduct
     */
    @Insert("insert into cartDetail(customerId,productId,number) values(#{customer.uid},#{product.uid},#{number})")
    @Options(useGeneratedKeys=true, keyProperty="uid")
    void addToCart(CartProduct cartProduct);

    /**
     * 查找购物车内是否有此类产品记录
     * @param customerId
     * @param productId
     * @return
     */
    @Select("select * from cartDetail where customerId=#{customerId} and productId=#{productId} limit 1")
    @ResultMap("cartItem")
    CartProduct findCartItem(int customerId, int productId);

    /**
     * 更新购物车数量
     * @param cartItem
     */
    @Update("update cartDetail set number=#{number} where customerId=#{customer.uid} and productId=#{product.uid}")
    void updateCartItem(CartProduct cartItem);
}
