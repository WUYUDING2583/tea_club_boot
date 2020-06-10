package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.Product;
import com.yuyi.tea.bean.ProductSale;
import com.yuyi.tea.bean.ProductType;
import com.yuyi.tea.typehandler.ProductTypeHandler;
import com.yuyi.tea.typehandler.ShopTypeHandler;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.math.BigDecimal;
import java.util.List;

public interface ProductMapper {

    /**
     * 获取所有产品类型
     * @return
     */
    @Select("select * from productType")
    List<ProductType> getProductTypes();

    //获取某一产品种类的信息
    @Select("select * from productType where uid=#{uid}")
    ProductType getProductType(int uid);

    /**
     * 获取所有产品的名称和类型
     * @return
     */
    @Select("select uid,name,type from product")
    @Results({
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column="type",property="type",
                    one=@One(select="com.yuyi.tea.mapper.ProductMapper.getProductType",
                            fetchType= FetchType.LAZY))
    })
    List<Product> getProductsNameAndType();

    /**
     * 创建新的产品种类
     * @param productType
     */
    @Insert("insert into productType(name) values(#{name})")
    @Options(useGeneratedKeys=true, keyProperty="uid")
    void saveProductType(ProductType productType);

    //创建商品
    @Insert("insert into product(name,type,description,priceId,storage,showOnHome) " +
            "values(#{name},#{type.uid},#{description},#{price.uid},#{storage},#{showOnHome})")
    @Options(useGeneratedKeys=true, keyProperty="uid")
    void saveProduct(Product product);

    /**
     * 获取产品列表
     * @return
     */
    @Select("select * from product")
    @ResultMap("product")
    List<Product> getProducts();

    /**
     * 下架商品
     * @param uid
     */
    @Update("update product set enforceTerminal=true where uid=#{uid}")
    void terminalProduct(int uid);

    /**
     * 根据uid获取产品信息
     * @param uid
     * @return
     */
    @Select("select * from product where uid=#{uid}")
    @Results(id = "product",
            value = {
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
                            fetchType= FetchType.LAZY)),
            @Result(column="shopId",property="shop",
                    one=@One(select="com.yuyi.tea.mapper.ShopMapper.getShopByUid",
                    fetchType= FetchType.LAZY)),
            @Result(column="uid",property="sales",
                    one=@One(select="com.yuyi.tea.mapper.ProductMapper.getProductMonthSales",
                            fetchType= FetchType.LAZY)),
            @Result(column="uid",property="productDetails",
                    one=@One(select="com.yuyi.tea.mapper.PhotoMapper.getProductDetailPhotos",
                            fetchType= FetchType.LAZY))
    })
    Product getProduct(int uid);

    //更新产品信息
    @Update("update product set name=#{name},type=#{type.uid},description=#{description},storage=#{storage} where uid=#{uid}")
    void updateProduct(Product product);

    /**
     * 获取门店产品列表
     * @param shopId
     * @return
     */
    @Select("select * from product where shopId=#{shopId} and enforceTerminal=false")
    @ResultMap("product")
    List<Product> getShopProducts(int shopId);

    /**
     * 获取小程序走马灯展示的产品
     * @return
     */
    @Select("select * from product where isShowOnHome=true")
    @ResultMap("product")
    List<Product> getSwiperList();

    /**
     * 获取最近一月销量最多的产品
     * @return
     */
    @Select("select * from lastMonthSalesView order by sales desc")
    @Results(id = "productSales",
            value = {
                    @Result(id = true,column = "productId",property = "productId"),
                    @Result(column="productId",property="product",
                            one=@One(select="com.yuyi.tea.mapper.ProductMapper.getProduct",
                                    fetchType= FetchType.LAZY))
            })
    List<ProductSale> getHotProducts();

    /**
     * 获取产品最近30天销量
     * @param productId
     * @return
     */
    @Select("select sales from lastMonthSalesView where productId=#{productId}")
    BigDecimal getProductMonthSales(int productId);

    @Select("select * from product where name like #{value} or description like #{value}")
    @ResultMap("product")
    List<Product> search(String value);
}
