package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.Article;
import com.yuyi.tea.bean.Photo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PhotoMapper {

    @Insert("insert into photo(photo) values(#{photo})")
    @Options(useGeneratedKeys=true, keyProperty="uid")
    void insertPhoto(Photo photo);

    //获取文章照片
    @Select("select * from photo where articleId=#{articleId} limit 1")
    Photo getPhotoByArticleId(int articleId);

    @Select("select * from photo where shopId=#{shopId}")
    List<Photo> getPhotosByShopId(int shopId);

    @Select("select * from photo where shopBoxId=#{shopBoxId}")
    List<Photo> getPhotosByShopBoxId(int shopBoxId);

    @Select("select * from photo where activityId=#{activityId}")
    List<Photo> getPhotosByActivityId(int activityId);


    @Select("select * from photo where clerkId=#{clerkId} limit 1")
    Photo getAvatarByClerkId(int clerkId);

    @Select("select * from photo where enterpriseId=#{enterpriseId} limit 1")
    Photo getBusinessLicenseByEnterpriseId(int enterpriseId);


    @Select("select * from photo where customerId=#{customerId} limit 1")
    Photo getAvatarByCustomerId(int customerId);

    @Select("select * from photo where productId=#{productId}")
    List<Photo> getPhotosByProductId(int productId);

    @Update("update photo set shopId=#{shopId} where uid=#{uid}")
    void saveShopPhotos(Photo photo);

    @Update("update photo set shopBoxId=#{shopBoxId} where uid=#{uid}")
    void saveShopBoxPhotos(Photo photo);

    @Delete("delete from photo where uid=#{uid}")
    void deletePhoto(int uid);

    @Update("update photo set clerkId=#{clerkId} where uid=#{uid}")
    void saveClerkAvatar(Photo avatar);

    @Update("update photo set activityId=#{activityId} where uid=#{uid}")
    void saveActivityPhoto(Photo photo);

    @Update("update photo set productId=#{productId} where uid=#{uid}")
    void saveProductPhoto(Photo photo);

    //保存文章图片
    @Update("update photo set articleId=#{articleId} where uid=#{uid}")
    void saveArticlePhoto(Photo photo);
}
