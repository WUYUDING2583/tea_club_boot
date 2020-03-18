package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.Photo;
import com.yuyi.tea.bean.Shop;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface ShopMapper {


    @Select("select * from shop")
    List<Shop> getShopList();

    @Select("select * from shop where uid=#{uid}")
    @Results({
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column="uid",property="openHours",
                    many=@Many(select="com.yuyi.tea.mapper.OpenHourMapper.getOpenHoursByShopId",
                            fetchType= FetchType.LAZY)),
            @Result(column="uid",property="photos",
                    many=@Many(select="com.yuyi.tea.mapper.PhotoMapper.getPhotosByShopId",
                            fetchType= FetchType.LAZY)),
            @Result(column="uid",property="clerks",
                    many=@Many(select="com.yuyi.tea.mapper.ClerkMapper.getClerksByShopId",
                            fetchType= FetchType.LAZY)),
            @Result(column="uid",property="shopBoxes",
                    many=@Many(select="com.yuyi.tea.mapper.ShopBoxMapper.getShopBoxByShopId",
                            fetchType= FetchType.LAZY))
    })
    Shop getShopByUid(int uid);

    @Insert("insert into shop(name,address,description,contact) values(#{name},#{address},#{description},#{contact}) ")
    @Options(useGeneratedKeys=true, keyProperty="uid")
     void saveShop(Shop shop);

    @Update("update photo set shopId=#{shopId} where uid=#{uid}")
    void saveShopPhotos(Photo photo);

    @Delete("delete from shop where uid=#{uid}")
    void deleteShop(int uid);

}
