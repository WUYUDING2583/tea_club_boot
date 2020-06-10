package com.yuyi.tea.mapper;

import com.yuyi.tea.domain.UserFaceInfo;
import com.yuyi.tea.dto.FaceUserInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;


@Mapper
public interface MybatisUserFaceInfoMapper {

    @Insert("INSERT INTO user_face_info (group_id,face_id,customerId,clerkId,face_feature,face) VALUES (#{groupId},#{faceId},#{customer.uid},#{clerk.uid},#{faceFeature},#{face})")
    void insertUserFaceInfo(UserFaceInfo userFaceInfo);

    @Select("select uid,face_id,customerId,clerkId,face_feature,face from user_face_info")
    @Results(id = "userFace",
            value = {
                    @Result(id = true,column = "uid",property = "uid"),
                    @Result(column = "group_id",property = "groupId"),
                    @Result(column = "face_id",property = "faceId"),
                    @Result(column = "customerId",property = "customer",
                            one = @One(select="com.yuyi.tea.mapper.CustomerMapper.getCustomerByUid",
                            fetchType = FetchType.LAZY)),
                    @Result(column = "clerkId",property = "clerk",
                            one = @One(select="com.yuyi.tea.mapper.ClerkMapper.getClerk",
                                    fetchType = FetchType.LAZY)),
                    @Result(column = "create_time",property = "createTime"),
                    @Result(column = "update_time",property = "updateTime"),
                    @Result(column = "face_feature",property = "faceFeature"),
                    @Result(column = "face",property = "face"),
                    @Result(column = "fpath",property = "fPath")
            })
    List<FaceUserInfo> getUserFaceInfoByGroupId(Integer groupId);

    @Select("select uid,face_id,customerId,clerkId,face_feature,face from user_face_info where face_Id=#{faceId}")
    @ResultMap("userFace")
    FaceUserInfo getFaceUserInfoByFaceId(String faceId);

    @Select("select uid,face_id,customerId,clerkId,face_feature,face from user_face_info where uid=#{uid}")
    @ResultMap("userFace")
    FaceUserInfo getFaceUserInfo(int uid);

    /**
     * 将人脸信息和客户信息匹配
     * @param faceId
     * @param customerId
     */
    @Update("update user_face_info set customerId=#{customerId} where uid=#{faceId}")
    void matchCustomer(int faceId, int customerId);

    @Select("select uid,face_id,customerId,clerkId,face_feature,face from user_face_info where clerkId=#{clerkId}")
    List<FaceUserInfo> getUserFaceInfoByClerkId(int clerkId);
}
