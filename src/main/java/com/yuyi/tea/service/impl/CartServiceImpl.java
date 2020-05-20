package com.yuyi.tea.service.impl;

import com.yuyi.tea.bean.CartProduct;
import com.yuyi.tea.common.CodeMsg;
import com.yuyi.tea.exception.GlobalException;
import com.yuyi.tea.mapper.CartMapper;
import com.yuyi.tea.service.interfaces.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Override
    public List<CartProduct> getCartList(int customerId) {
        List<CartProduct> cartProducts= cartMapper.getCartDetail(customerId);
        return cartProducts;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addToCart(CartProduct cartProduct) {
        try {
            //先查找购物车内是否已经有此类产品记录
            CartProduct originCartItem=cartMapper.findCartItem(cartProduct.getCustomer().getUid(),cartProduct.getProduct().getUid());
            if(originCartItem==null){
                //没有此类记录,save
                cartMapper.addToCart(cartProduct);
            }else{
                //update
                originCartItem.setNumber(originCartItem.getNumber()+cartProduct.getNumber());
                cartMapper.updateCartItem(originCartItem);
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new GlobalException(CodeMsg.ADD_TO_CART_FAIL);
        }
    }
}
