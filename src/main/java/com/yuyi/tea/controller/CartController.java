package com.yuyi.tea.controller;

import com.yuyi.tea.bean.CartProduct;
import com.yuyi.tea.service.interfaces.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/mp/cart")
    public List<CartProduct> addToCart(@RequestBody CartProduct cartProduct){
        try {
            //加入购物车
            cartService.addToCart(cartProduct);
            //查询该客户购物车列表
            List<CartProduct> cartList = cartService.getCartList(cartProduct.getCustomer().getUid());
            for(CartProduct product:cartList){
                product.setCustomer(null);
                product.setProduct(null);
            }
            return cartList;
        }catch (Exception e){
            throw e;
        }
    }


}
