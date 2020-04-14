package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.Clerk;
import com.yuyi.tea.bean.Position;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface ClerkMapper {

    //根据门店id获取该门店所有职员信息
    @Select("select uid,name,positionId from clerk where shopId=#{shopId}")
    @Results({
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column="positionId",property="position",
                    one=@One(select="com.yuyi.tea.mapper.ClerkMapper.getPosition",
                            fetchType= FetchType.LAZY))
    })
    List<Clerk> getClerksByShopId(int shopId);

    //根据员工提供的身份证号获取员工信息
    @Select("select * from clerk where identityId=#{identityId}")
    @ResultMap("clerk")
    Clerk getClerkByIdentityId(String identityId);

    //根据uid获取职位信息
    @Select("select * from position where uid=#{uid}")
    Position getPosition(int uid);

    /**
     * 获取除管理远外的职员列表
     * @return
     */
    @Select("select * from clerk where positionId!=1")
    @ResultMap("clerk")
    List<Clerk> getAllClerks();

    //跟新职员所属门店
    @Update("update clerk set shopId=#{shopId} where uid=#{uid}")
    void updateClerkShopId(int uid, int shopId);

    /**
     * 失效职员
     * @param uid
     */
    @Update("update clerk set enforceTerminal=true where uid=#{uid}")
    void terminalClerk(int uid);

    /**
     * 获取职位列表
     * @return
     */
    @Select("select * from position")
    List<Position> getPositions();

    //新增职员
    @Insert("insert into clerk(name,positionId,contact,identityId,gender,address,shopId,password) values(#{name},#{position.uid},#{contact},#{identityId},#{gender},#{address},#{shop.uid},#{password})")
    @Options(useGeneratedKeys=true, keyProperty="uid")
    void saveClerk(Clerk clerk);

    //获取职员信息
    @Select("select * from clerk where uid=#{uid}")
    @Results(id = "clerk",
            value = {
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column="positionId",property="position",
                    one=@One(select="com.yuyi.tea.mapper.ClerkMapper.getPosition",
                            fetchType= FetchType.LAZY)),
            @Result(column="shopId",property="shop",
                    one=@One(select="com.yuyi.tea.mapper.ShopMapper.getShopByUid",
                            fetchType= FetchType.LAZY)),
            @Result(column="uid",property="avatar",
                    one=@One(select="com.yuyi.tea.mapper.PhotoMapper.getAvatarByClerkId",
                            fetchType= FetchType.LAZY))
    })
    Clerk getClerk(int uid);

    //更新职员信息
    @Update("update clerk set name=#{name},positionId=#{position.uid},contact=#{contact}," +
            "identityId=#{identityId},gender=#{gender},address=#{address},shopId=#{shop.uid} where uid=#{uid}")
    void updateClerk(Clerk clerk);

    //通过手机号获取职员信息
    @Select("select * from clerk where contact=#{contact}")
    @ResultMap("clerk")
    Clerk getClerkByContact(String contact);
}
