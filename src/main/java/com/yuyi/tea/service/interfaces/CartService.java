package com.yuyi.tea.service.interfaces;

import com.yuyi.tea.bean.CartProduct;
import com.yuyi.tea.bean.Order;

import java.util.List;

public interface CartService {

    /**
     * 获取客户购物车详情
     * @param customerId
     * @return
     */
    List<CartProduct> getCartList(int customerId);

    /**
     * 加入购物车
     * @param cartProduct
     */
    void addToCart(CartProduct cartProduct);

    /**
     * 修改购物车商品数量
     * @param cartProduct
     */
    void changeCartProductNumber(CartProduct cartProduct);

    /**
     * 删除购物车商品
     * @param uid
     */
    void deleteCartProduct(int uid);

    /**
     * 根据订单内容删除购物车商品
     * @param order
     */
    void deleteCartProductByOrder(Order order);
}
