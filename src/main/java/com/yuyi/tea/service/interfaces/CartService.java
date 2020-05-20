package com.yuyi.tea.service.interfaces;

import com.yuyi.tea.bean.CartProduct;

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
}
