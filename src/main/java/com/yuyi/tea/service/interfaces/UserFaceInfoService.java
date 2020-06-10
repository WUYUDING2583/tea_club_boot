package com.yuyi.tea.service.interfaces;


import com.yuyi.tea.bean.Clerk;
import com.yuyi.tea.domain.UserFaceInfo;
import com.yuyi.tea.dto.FaceUserInfo;

import java.util.List;

public interface UserFaceInfoService {

    void insertSelective(UserFaceInfo userFaceInfo);

    List<FaceUserInfo> addFace(String file, Integer groupId, String name);

    List<FaceUserInfo> addFace(byte[] file, Integer groupId, Clerk clerk);

    FaceUserInfo getFaceUserInfo(FaceUserInfo faceUserInfo);

    FaceUserInfo getFaceUserInfo(int uid);

    void matchCustomer(int faceId, int uid);
}
