package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.CartProduct;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface CartMapper {

    /**
     * 获取客户购物车详情
     * @param customerId
     * @return
     */
    @Select("select * from cartDetail where customerId=#{customerId}")
    @Results(id = "cartDetail",
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
}
