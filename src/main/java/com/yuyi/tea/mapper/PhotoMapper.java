package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.Photo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PhotoMapper {

    @Insert("insert into photo(photo) values(#{photo})")
    @Options(useGeneratedKeys=true, keyProperty="uid")
    void insertPhoto(Photo photo);


    @Select("select * from photo where shopId=#{shopId}")
    List<Photo> getPhotosByShopId(int shopId);


    @Update("update photo set shopId=#{shopId} where uid=#{uid}")
    void saveShopPhotos(Photo photo);

    @Update("update photo set shopBoxId=#{shopBoxId} where uid=#{uid}")
    void saveShopBoxPhotos(Photo photo);

}
