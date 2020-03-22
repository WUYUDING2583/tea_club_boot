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

    @Select("select * from photo where shopBoxId=#{shopBoxId}")
    List<Photo> getPhotosByShopBoxId(int shopBoxId);


    @Select("select * from photo where clerkId=#{clerkId} limit 1")
    Photo getAvatarByClerkId(int clerkId);


    @Update("update photo set shopId=#{shopId} where uid=#{uid}")
    void saveShopPhotos(Photo photo);

    @Update("update photo set shopBoxId=#{shopBoxId} where uid=#{uid}")
    void saveShopBoxPhotos(Photo photo);

    @Delete("delete from photo where uid=#{uid}")
    void deletePhoto(int uid);

    @Update("update photo set clerkId=#{clerkId} where uid=#{uid}")
    void saveClerkAvatar(Photo avatar);
}
