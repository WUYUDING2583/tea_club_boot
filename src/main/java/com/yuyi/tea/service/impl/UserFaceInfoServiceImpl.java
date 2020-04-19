package com.yuyi.tea.service.impl;

import com.yuyi.tea.domain.UserFaceInfo;
import com.yuyi.tea.mapper.MybatisUserFaceInfoMapper;
import com.yuyi.tea.service.interfaces.UserFaceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserFaceInfoServiceImpl implements UserFaceInfoService {


    @Autowired
    private MybatisUserFaceInfoMapper userFaceInfoMapper;

    @Override
    public void insertSelective(UserFaceInfo userFaceInfo) {
        userFaceInfoMapper.insertUserFaceInfo(userFaceInfo);
    }
}
