package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.Shop;
import org.apache.ibatis.annotations.*;

import java.util.List;

//@Mapper
public interface ShopMapper {

    @Select("select uid,name from shop")
    List<Shop> getShopList();

    @Select("select * from shop where uid=#{uid}")
    Shop getShopByUid(int uid);

    @Update("update Shop set lastName=#{lastName},email=#{email},gender=#{gender},d_uid=#{duid} where uid=#{uid}")
     void updateEmp(Shop Shop);

    @Delete("delete from Shop where uid=#{uid}")
     void deleteEmp(int uid);

    @Insert("insert into shop(name,address,description,contact) values(#{lastName},#{email},#{gender},#{duid}) ")
     void insertShop(Shop shop);

    @Select("select * from Shop where lastName=#{lastName}")
     Shop getEmpByLastName(String lastName);
}
