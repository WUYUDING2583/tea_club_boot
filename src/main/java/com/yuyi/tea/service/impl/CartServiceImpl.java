package com.yuyi.tea.service.impl;

import com.yuyi.tea.bean.ActivityRule;
import com.yuyi.tea.bean.CartProduct;
import com.yuyi.tea.bean.Order;
import com.yuyi.tea.bean.OrderProduct;
import com.yuyi.tea.common.CodeMsg;
import com.yuyi.tea.exception.GlobalException;
import com.yuyi.tea.mapper.CartMapper;
import com.yuyi.tea.service.interfaces.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Override
    public List<CartProduct> getCartList(int customerId) {
        List<CartProduct> cartProducts= cartMapper.getCartDetail(customerId);
        for(CartProduct cartProduct:cartProducts){
            cartProduct.getCustomer().setIdentityId(null);
            cartProduct.getCustomer().setAvatar(null);
            cartProduct.getCustomer().setPassword(null);
            for(ActivityRule rule:cartProduct.getProduct().getActivityRules()){
                rule.setActivityApplyForProduct(null);
                rule.getActivity().setPhotos(null);
                rule.getActivity().setActivityRules(null);
            }
            cartProduct.getProduct().setShop(null);
            cartProduct.getProduct().setProductDetails(null);
        }
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeCartProductNumber(CartProduct cartProduct) {
        try{
            cartMapper.updateCartItem(cartProduct);
        }catch (Exception e){
            e.printStackTrace();
            throw new GlobalException(CodeMsg.FAIL_CHANGE_CART_PRODUCT_NUMBER);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCartProduct(int uid) {
        try{
            cartMapper.deleteCartProduct(uid);
        }catch (Exception e){
            e.printStackTrace();
            throw new GlobalException(CodeMsg.FAIL_DELETE_CART_ITEM);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCartProductByOrder(Order order) {
        //获取客户购物车内容
        List<CartProduct> cartList = getCartList(order.getCustomer().getUid());
        List<Integer> deleteCartItem=new ArrayList<>();
        for(CartProduct cartProduct:cartList){
            for(OrderProduct orderProduct:order.getProducts()){
                if(orderProduct.getProduct().getUid()==cartProduct.getProduct().getUid()){
                    deleteCartItem.add(cartProduct.getUid());
                    break;
                }
            }
        }
        for(Integer uid:deleteCartItem){
            cartMapper.deleteCartProduct(uid);
        }
    }
}
