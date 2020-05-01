package com.yuyi.tea.mapper;

import com.yuyi.tea.domain.UserFaceInfo;
import com.yuyi.tea.dto.FaceUserInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;


@Mapper
public interface MybatisUserFaceInfoMapper {

    @Select("SELECT name,create_time from user_face_info")
    @Results(id = "userFace",
            value = {
            @Result(id = true,column = "id",property = "id"),
            @Result(column = "group_id",property = "groupId"),
            @Result(column = "face_id",property = "faceId"),
            @Result(column = "phone_number",property = "phoneNumber"),
            @Result(column = "create_time",property = "createTime"),
            @Result(column = "face_feature",property = "faceFeature")
    })
    List<UserFaceInfo> findUserFaceInfoList();

    @Insert("INSERT INTO user_face_info (group_id,face_id,name,face_feature) VALUES (#{groupId},#{faceId},#{name},#{faceFeature})")
    void insertUserFaceInfo(UserFaceInfo userFaceInfo);

    @Select("select face_id,name,face_feature from user_face_info")
    @Results(id = "userFace2",
            value = {
                    @Result(id = true,column = "id",property = "id"),
                    @Result(column = "group_id",property = "groupId"),
                    @Result(column = "face_id",property = "faceId"),
                    @Result(column = "phone_number",property = "phoneNumber"),
                    @Result(column = "create_time",property = "createTime"),
                    @Result(column = "face_feature",property = "faceFeature")
            })
    List<FaceUserInfo> getUserFaceInfoByGroupId(Integer groupId);

}
