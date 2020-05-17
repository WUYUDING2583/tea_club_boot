package com.yuyi.tea.service.impl;

import com.yuyi.tea.bean.CartProduct;
import com.yuyi.tea.mapper.CartMapper;
import com.yuyi.tea.service.interfaces.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Override
    public List<CartProduct> getCartDetail(int customerId) {
        List<CartProduct> cartProducts= cartMapper.getCartDetail(customerId);
        return cartProducts;
    }
}
