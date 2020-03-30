package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.Product;
import com.yuyi.tea.bean.ProductType;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface ProductMapper {

    //获取产品类型列表
    @Select("select * from productType")
    List<ProductType> getProductTypes();

    //获取某一产品种类的信息
    @Select("select * from productType where uid=#{uid}")
    ProductType getProductType(int uid);

    //获取所有产品的名称和类型
    @Select("select uid,name,type from product")
    @Results({
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column="type",property="type",
                    one=@One(select="com.yuyi.tea.mapper.ProductMapper.getProductType",
                            fetchType= FetchType.LAZY))
    })
    List<Product> getProductsNameAndType();

    //创建新的产品种类
    @Insert("insert into productType(name) values(#{name})")
    @Options(useGeneratedKeys=true, keyProperty="uid")
    void saveProductType(ProductType productType);

    //创建商品
    @Insert("insert into product(name,type,description,priceId,storage) " +
            "values(#{name},#{type.uid},#{description},#{price.uid},#{storage})")
    @Options(useGeneratedKeys=true, keyProperty="uid")
    void saveProduct(Product product);

    //获取产品列表
    @Select("select * from product")
    @Results({
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column="type",property="type",
                    one=@One(select="com.yuyi.tea.mapper.ProductMapper.getProductType",
                            fetchType= FetchType.LAZY)),
            @Result(column="priceId",property="price",
                    one=@One(select="com.yuyi.tea.mapper.PriceMapper.getPrice",
                            fetchType= FetchType.LAZY))
    })
    List<Product> getProducts();

    //下架商品
    @Update("update product set enforceTerminal=true where uid=#{uid}")
    void terminalProduct(int uid);

    @Select("select * from product where uid=#{uid}")
    @Results({
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column="type",property="type",
                    one=@One(select="com.yuyi.tea.mapper.ProductMapper.getProductType",
                            fetchType= FetchType.LAZY)),
            @Result(column="priceId",property="price",
                    one=@One(select="com.yuyi.tea.mapper.PriceMapper.getPrice",
                            fetchType= FetchType.LAZY)),
            @Result(column="uid",property="photos",
                    one=@One(select="com.yuyi.tea.mapper.PhotoMapper.getPhotosByProductId",
                            fetchType= FetchType.LAZY)),
            @Result(column="uid",property="activities",
                    one=@One(select="com.yuyi.tea.mapper.ActivityMapper.getActivitiesByProduct",
                            fetchType= FetchType.LAZY)),
            @Result(column="uid",property="activityRules",
                    one=@One(select="com.yuyi.tea.mapper.ActivityMapper.getActivityRulesByProduct",
                            fetchType= FetchType.LAZY))
    })
    Product getProduct(int uid);

    //更新产品信息
    @Update("update product set name=#{name},type=#{type.uid},description=#{description},storage=#{storage} where uid=#{uid}")
    void updateProduct(Product product);
}
