package com.yuyi.tea.controller;

import com.yuyi.tea.domain.UserFaceInfo;
import com.yuyi.tea.mapper.MybatisUserFaceInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
public class UserListController {

    @Autowired
    MybatisUserFaceInfoMapper userFaceInfoMapper;

    @GetMapping("/userInfo")
    public List<UserFaceInfo> getUserInfo()
    {
        List<UserFaceInfo> list = new ArrayList<UserFaceInfo>();
        list = userFaceInfoMapper.findUserFaceInfoList();
        return list;
    }
}
