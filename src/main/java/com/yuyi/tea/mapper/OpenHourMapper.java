package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.OpenHour;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface OpenHourMapper {

    @Select("select * from openHour where shopId=#{shopId}")
    @Results({
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column="uid",property="date",
                    many=@Many(select="com.yuyi.tea.mapper.OpenHourMapper.getRepeatDateByOpenHourId",
                            fetchType= FetchType.LAZY))
    })
    List<OpenHour> getOpenHoursByShopId(int shopId);

    @Insert({
            "<script>",
            "insert into openHour(startTime,endTime,shopId) values ",
            "<foreach collection='openHours' item='openHour' index='index' separator=','>",
            "(#{openHour.startTime}, #{openHour.endTime}, #{openHour.shopId})",
            "</foreach>",
            "</script>"
    })
    @Options(useGeneratedKeys = true,keyProperty = "uid")
    void insertOpenHours(@Param(value = "openHours") List<OpenHour> openHours);

    @Insert("insert into openRepeatDate(date,openHourId) values(#{date},#{openHourId})")
    void insertOpenHourRepeat(String date,int openHourId);

    @Insert("insert into openHour(startTime,endTime,shopId) values(#{startTime},#{endTime},#{shopId})")
    @Options(useGeneratedKeys = true,keyProperty = "uid")
    void insertOpenHour(OpenHour openHour);

    @Select("select date from openRepeatDate where openHourId=#{openHourId}")
    List<String> getRepeatDateByOpenHourId(int openHourId);

    @Delete("delete from openHour where shopId=#{shopId}")
    void deleteOpenHourByShop(int shopId);
}
