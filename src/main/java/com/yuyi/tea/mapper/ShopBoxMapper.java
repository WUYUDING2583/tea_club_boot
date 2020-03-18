package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.ShopBox;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ShopBoxMapper {

    @Select("select * from shopBox where shopId=#{shopId}")
    List<ShopBox> getShopBoxByShopId(int shopId);

    @Insert("insert into shopBox(uid,name,description,shopId,boxNum,price,duration) " +
            "values(#{uid},#{name},#{description},#{shopId},#{boxNum},#{price},#{duration})")
    @Options(useGeneratedKeys=true, keyProperty="uid")
    void saveShopBox(ShopBox shopBox);
}
