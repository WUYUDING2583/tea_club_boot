package com.yuyi.tea.controller;

import com.yuyi.tea.bean.CartProduct;
import com.yuyi.tea.bean.Product;
import com.yuyi.tea.common.CodeMsg;
import com.yuyi.tea.exception.GlobalException;
import com.yuyi.tea.service.ProductService;
import com.yuyi.tea.service.interfaces.CartService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

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

    /**
     * 获取客户购物车内容
     * @param customerId
     * @return
     */
    @GetMapping("/mp/cart/{customerId}")
    public List<CartProduct> getCart(@PathVariable int customerId){
        List<CartProduct> cartList = cartService.getCartList(customerId);
        return cartList;
    }

    @PostMapping("/mp/cart/changeNumber")
    public String changeCartProductNumber(@RequestBody CartProduct cartProduct){
        //查询商品库存
        Product product = productService.getProduct(cartProduct.getProduct().getUid());
        if(cartProduct.getNumber()>product.getStorage()){
            throw new GlobalException(CodeMsg.OUT_OF_STORATE);
        }
        cartService.changeCartProductNumber(cartProduct);
        return "success";
    }

    @DeleteMapping("/mp/cart")
    public List<CartProduct> deleteCartItem(@RequestBody List<CartProduct> cartProducts){
        for(CartProduct cartProduct:cartProducts){
            cartService.deleteCartProduct(cartProduct.getUid());
        }
        List<CartProduct> cartList = cartService.getCartList(cartProducts.get(0).getCustomer().getUid());
        return cartList;
    }


}
