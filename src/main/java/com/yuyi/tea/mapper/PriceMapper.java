package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.Price;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

public interface PriceMapper {

    @Insert("insert into price(ingot,credit) values(#{ingot},#{credit})")
    @Options(useGeneratedKeys = true,keyProperty = "uid")
    void savePrice(Price price);

    @Select("select * from price where uid=#{uid}")
    Price getPrice(int uid);
}
