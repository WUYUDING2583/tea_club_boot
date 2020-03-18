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
}
