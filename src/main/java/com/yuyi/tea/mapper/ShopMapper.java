package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.Photo;
import com.yuyi.tea.bean.Shop;
import com.yuyi.tea.bean.ShopBox;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface ShopMapper {


    /**
     * 获取门店列表
     * @return
     */
    @Select("select * from shop where enforceTerminal=false")
    @ResultMap("shop")
    List<Shop> getShopList();

    /**
     * 根据uid获取门店详细信息
     * @param uid
     * @return
     */
    @Select("select * from shop where uid=#{uid}")
    @Results(id="shop",
            value = {
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

    @Select("select uid,name from shop where uid=#{uid}")
    Shop getShopOfShopBox(int uid);

    @Select("select uid,name from shop where uid=#{uid}")
    Shop getShopName(int uid);

    /**
     * 新增门店
     * @param shop
     */
    @Insert("insert into shop(name,address,description,contact) values(#{name},#{address},#{description},#{contact}) ")
    @Options(useGeneratedKeys=true, keyProperty="uid")
     void saveShop(Shop shop);


    /**
     * 门店失效，不再在商城展示
     * @param uid
     */
    @Update("update shop set enforceTerminal=true where uid=#{uid}")
    void terminalShop(int uid);

    @Update("update shop set name=#{name},address=#{address},description=#{description},contact=#{contact} where uid=#{uid}")
    void updateShop(Shop shop);

    @Select("select uid,name from shop where uid=#{uid}")
    Shop getShopOfClerk(int uid);

    /**
     * 删除包厢须知
     * @param boxId
     */
    @Delete("delete from shopBoxInfo where boxId=#{boxId}")
    void deleteBoxInfos(int boxId);
}
