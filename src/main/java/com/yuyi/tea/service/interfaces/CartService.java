package com.yuyi.tea.service.interfaces;

import com.yuyi.tea.bean.CartProduct;

import java.util.List;

public interface CartService {

    /**
     * 获取客户购物车详情
     * @param customerId
     * @return
     */
    List<CartProduct> getCartDetail(int customerId);
}
