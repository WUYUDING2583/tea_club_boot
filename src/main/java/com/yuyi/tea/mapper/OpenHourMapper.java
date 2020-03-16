package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.OpenHour;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OpenHourMapper {

    @Select("select * from openHour where shopId=#{shopId}")
    List<OpenHour> getOpenHoursByShopId(int shopId);

//    @Insert("insert into openHour(startTime,endTime,shopId) values(#{startTime},#{endTime},#{shopId})")
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
    void insertOpenHourRepeat(int date,int openHourId);

    @Insert("insert into openHour(startTime,endTime,shopId) values(#{startTime},#{endTime},#{shopId})")
    @Options(useGeneratedKeys = true,keyProperty = "uid")
    void insertOpenHour(OpenHour openHour);
}
