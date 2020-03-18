package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.Photo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PhotoMapper {

    @Insert("insert into photo(photo) values(#{photo})")
    @Options(useGeneratedKeys=true, keyProperty="uid")
    void insertPhoto(Photo photo);


    @Select("select * from photo where shopId=#{shopId}")
    List<Photo> getPhotosByShopId(int shopId);
//    @Select(" select * from BASE_RELATIONSHIP_PHOTO where relationship_id = #{e} ")
//    @Result(property = "photo",column = "photo", jdbcType = JdbcType.BLOB)
//    List<BaseRelationShipPhoto> fiterPhoto(@Param("e") Long id);

}
