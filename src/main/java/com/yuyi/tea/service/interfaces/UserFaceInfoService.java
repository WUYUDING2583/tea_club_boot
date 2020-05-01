package com.yuyi.tea.service.interfaces;


import com.yuyi.tea.domain.UserFaceInfo;
import com.yuyi.tea.dto.FaceUserInfo;

import java.util.List;

public interface UserFaceInfoService {

    void insertSelective(UserFaceInfo userFaceInfo);

    List<FaceUserInfo> addFace(String file, Integer groupId, String name);

}
