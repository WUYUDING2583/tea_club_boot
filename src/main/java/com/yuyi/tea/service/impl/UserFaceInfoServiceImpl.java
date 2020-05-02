package com.yuyi.tea.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.RandomUtil;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.toolkit.ImageFactory;
import com.arcsoft.face.toolkit.ImageInfo;
import com.yuyi.tea.common.CodeMsg;
import com.yuyi.tea.domain.UserFaceInfo;
import com.yuyi.tea.dto.FaceUserInfo;
import com.yuyi.tea.enums.ErrorCodeEnum;
import com.yuyi.tea.exception.GlobalException;
import com.yuyi.tea.mapper.MybatisUserFaceInfoMapper;
import com.yuyi.tea.service.interfaces.FaceEngineService;
import com.yuyi.tea.service.interfaces.UserFaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.yuyi.tea.common.utils.StringUtil.base64Process;


@Service
@Slf4j
public class UserFaceInfoServiceImpl implements UserFaceInfoService {


    @Autowired
    private MybatisUserFaceInfoMapper userFaceInfoMapper;

    @Autowired
    private FaceEngineService faceEngineService;

    @Override
    public void insertSelective(UserFaceInfo userFaceInfo) {
        userFaceInfoMapper.insertUserFaceInfo(userFaceInfo);
    }

    /**
     * 人脸添加,并返回人脸数据
     * @param file
     * @param groupId
     */
    @Override
    public List<FaceUserInfo> addFace(String file, Integer groupId,String name) {
        try {
            if (file == null) {
                throw new GlobalException(CodeMsg.FILE_IS_NULL);
            }
            if (groupId == null) {
                throw new GlobalException(CodeMsg.GROUP_ID_IS_NULL);
            }
            if (name == null) {
                name="unkonw";
            }

            byte[] decode = Base64.decode(base64Process(file));
            ImageInfo imageInfo = ImageFactory.getRGBData(decode);

            //人脸特征获取
            List<FaceFeature> faceFeatureList = faceEngineService.extractFaceFeature(imageInfo);
            if (faceFeatureList == null) {
                throw new GlobalException(new CodeMsg(ErrorCodeEnum.NO_FACE_DETECTED));
            }
            List<FaceUserInfo> userFaceInfoList=new ArrayList<>();
            for(FaceFeature faceFeature:faceFeatureList){

                UserFaceInfo userFaceInfo = new UserFaceInfo();
                userFaceInfo.setGroupId(groupId);
                userFaceInfo.setFaceFeature(faceFeature.getFeatureData());
                userFaceInfo.setFaceId(RandomUtil.randomString(10));
                userFaceInfo.setFace(file.getBytes());

                //人脸特征插入到数据库
                insertSelective(userFaceInfo);
                FaceUserInfo faceUserInfo=new FaceUserInfo();
                faceUserInfo.setFaceId(userFaceInfo.getFaceId());
                faceUserInfo.setFaceFeature(userFaceInfo.getFaceFeature());
                userFaceInfoList.add(faceUserInfo);
            }

            log.info("faceAdd:" + name);
            return userFaceInfoList;
        } catch (Exception e) {
            log.error("", e);
            throw new GlobalException(new CodeMsg(ErrorCodeEnum.UNKNOWN));
        }
    }

    /**
     * 获取人脸对应的所有信息
     * @param faceUserInfo
     * @return
     */
    @Override
    public FaceUserInfo getFaceUserInfo(FaceUserInfo faceUserInfo) {
        faceUserInfo=userFaceInfoMapper.getFaceUserInfoByFaceId(faceUserInfo.getFaceId());
        return faceUserInfo;
    }
}
