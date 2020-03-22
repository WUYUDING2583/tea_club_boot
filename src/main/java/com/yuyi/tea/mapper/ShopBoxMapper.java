package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.ShopBox;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface ShopBoxMapper {

    @Select("select * from shopBox where shopId=#{shopId}")
    List<ShopBox> getShopBoxByShopId(int shopId);

    @Insert("insert into shopBox(uid,name,description,shopId,boxNum,priceId,duration) " +
            "values(#{uid},#{name},#{description},#{shop.uid},#{boxNum},#{price.uid},#{duration})")
    @Options(useGeneratedKeys=true, keyProperty="uid")
    void saveShopBox(ShopBox shopBox);

    @Select("select uid,name,description,shopId from shopBox")
    @Results({
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column="shopId",property="shop",
                    many=@Many(select="com.yuyi.tea.mapper.ShopMapper.getShopOfShopBox",
                            fetchType= FetchType.LAZY))
    })
    List<ShopBox> getShopBoxes();

    @Delete("delete from shopBox where uid=#{uid}")
    void deleteShopBoxByUid(int uid);

    @Select("select * from shopBox where uid=#{uid}")
    @Results({
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column="shopId",property="shop",
                    many=@Many(select="com.yuyi.tea.mapper.ShopMapper.getShopOfShopBox",
                            fetchType= FetchType.LAZY)),
            @Result(column="priceId",property="price",
                    many=@Many(select="com.yuyi.tea.mapper.PriceMapper.getPrice",
                            fetchType= FetchType.LAZY)),
            @Result(column="uid",property="photos",
                    many=@Many(select="com.yuyi.tea.mapper.PhotoMapper.getPhotosByShopBoxId",
                            fetchType= FetchType.LAZY))
    })
    ShopBox getShopBoxByUid(int uid);

    @Update("update shopBox set name=#{name},description=#{description},priceId=#{price.uid},duration=#{duration},boxNum=#{boxNum},shopId=#{shop.uid} where uid=#{uid}")
    void updateShopBox(ShopBox shopBox);

    @Update("update shopBox set shopId=#{shopId} where uid=#{uid}")
    void updateShopBoxShopId(int uid, int shopId);

    @Update("update shopBox set shopId=null where uid=#{uid}")
    void setShopIdNull(int uid);
}


