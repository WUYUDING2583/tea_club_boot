package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.ShopBox;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface ShopBoxMapper {

    @Select("select * from shopBox where shopId=#{shopId}")
    List<ShopBox> getShopBoxByShopId(int shopId);

    @Insert("insert into shopBox(uid,name,description,shopId,boxNum,price,duration) " +
            "values(#{uid},#{name},#{description},#{shop.uid},#{boxNum},#{price},#{duration})")
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
}
