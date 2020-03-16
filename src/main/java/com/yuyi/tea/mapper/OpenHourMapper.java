package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.OpenHour;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OpenHourMapper {

    @Select("select * from openHour where shopId=#{shopId}")
    List<OpenHour> getOpenHoursByShopId(int shopId);

    @Insert("insert openHour(startTime,endTime,shopId) values(#{startTime},#{endTime},#{shopId})")
    @Options(useGeneratedKeys = true,keyProperty = "uid")
    void insertOpenHour(OpenHour openHour);

    @Insert("insert openRepeatDate(date,openHourId) values(#{date},#{openHourId})")
    void insertOpenHourRepeat(int date,int openHourId);
}
