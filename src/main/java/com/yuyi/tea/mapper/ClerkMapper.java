package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.Clerk;
import com.yuyi.tea.bean.Position;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface ClerkMapper {

    @Select("select uid,name,positionId from clerk where shopId=#{shopId}")
    @Results({
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column="positionId",property="position",
                    one=@One(select="com.yuyi.tea.mapper.ClerkMapper.getPosition",
                            fetchType= FetchType.LAZY))
    })
    List<Clerk> getClerksByShopId(int shopId);


    @Select("select * from position where uid=#{uid}")
    Position getPosition(int uid);

    @Select("select * from clerk")
    @Results({
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column="positionId",property="position",
                    one=@One(select="com.yuyi.tea.mapper.ClerkMapper.getPosition",
                            fetchType= FetchType.LAZY)),
            @Result(column="shopId",property="shop",
                    one=@One(select="com.yuyi.tea.mapper.ShopMapper.getShopOfClerk",
                            fetchType= FetchType.LAZY))
    })
    List<Clerk> getAllClerks();

    @Update("update clerk set shopId=#{shopId} where uid=#{uid}")
    void updateClerkShopId(int uid, int shopId);

    @Delete("delete from clerk where uid=#{uid}")
    void deleteClerk(int uid);

    @Select("select * from position")
    List<Position> getPositions();

    @Insert("insert into clerk(name,positionId,contact,identityId,sex,address,shopId) values(#{name},#{position.uid},#{contact},#{identityId},#{sex},#{address},#{shop.uid})")
    @Options(useGeneratedKeys=true, keyProperty="uid")
    void saveClerk(Clerk clerk);

    @Select("select * from clerk where uid=#{uid}")
    @Results({
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column="positionId",property="position",
                    one=@One(select="com.yuyi.tea.mapper.ClerkMapper.getPosition",
                            fetchType= FetchType.LAZY)),
            @Result(column="shopId",property="shop",
                    one=@One(select="com.yuyi.tea.mapper.ShopMapper.getShopOfClerk",
                            fetchType= FetchType.LAZY)),
            @Result(column="uid",property="avatar",
                    one=@One(select="com.yuyi.tea.mapper.PhotoMapper.getAvatarByClerkId",
                            fetchType= FetchType.LAZY))
    })
    Clerk getClerk(int uid);

    @Update("update clerk set name=#{name},positionId=#{position.uid},contact=#{contact}," +
            "identityId=#{identityId},sex=#{sex},address=#{address},shopId=#{shop.uid} where uid=#{uid}")
    void updateClerk(Clerk clerk);
}
