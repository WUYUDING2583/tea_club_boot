package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.CartProduct;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CartMapper {

    /**
     * 获取客户购物车详情
     * @param customerId
     * @return
     */
    @Select("select * from cartDetail where customerId=#{customerId}")
    List<CartProduct> getCartDetail(int customerId);
}
