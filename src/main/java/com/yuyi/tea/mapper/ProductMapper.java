package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.Product;
import com.yuyi.tea.bean.ProductType;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface ProductMapper {

    @Select("select * from productType")
    List<ProductType> getProductTypes();

    //获取某一产品种类的信息
    @Select("select * from productType where uid=#{uid}")
    ProductType getProductType(int uid);

    //获取所有产品的名称
    @Select("select uid,name,type from product")
    @Results({
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column="type",property="type",
                    one=@One(select="com.yuyi.tea.mapper.ProductMapper.getProductType",
                            fetchType= FetchType.LAZY))
    })
    List<Product> getProductsName();
}
