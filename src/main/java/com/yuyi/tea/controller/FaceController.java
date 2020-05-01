package com.yuyi.tea.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.toolkit.ImageFactory;
import com.arcsoft.face.toolkit.ImageInfo;
import com.yuyi.tea.common.CodeMsg;
import com.yuyi.tea.domain.UserFaceInfo;
import com.yuyi.tea.dto.FaceSearchResDto;
import com.yuyi.tea.dto.FaceUserInfo;
import com.yuyi.tea.dto.ProcessInfo;
import com.yuyi.tea.enums.ErrorCodeEnum;
import com.yuyi.tea.exception.GlobalException;
import com.yuyi.tea.service.interfaces.FaceEngineService;
import com.yuyi.tea.service.interfaces.UserFaceInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


@RestController
public class FaceController {

    public final static Logger logger = LoggerFactory.getLogger(FaceController.class);


    @Autowired
    FaceEngineService faceEngineService;

    @Autowired
    UserFaceInfoService userFaceInfoService;


//    /*
//    人脸添加
//     */
//    @RequestMapping(value = "/faceAdd", method = RequestMethod.POST)
//    @ResponseBody
//    public String faceAdd(@RequestParam("file") String file, @RequestParam("groupId") Integer groupId, @RequestParam("name") String name) {
//
//        try {
//            if (file == null) {
//                throw new GlobalException(CodeMsg.FILE_IS_NULL);
//            }
//            if (groupId == null) {
//                throw new GlobalException(CodeMsg.GROUP_ID_IS_NULL);
//            }
//            if (name == null) {
//                throw new GlobalException(CodeMsg.NAME_IS_NULL);
//            }
//
//            byte[] decode = Base64.decode(base64Process(file));
//            ImageInfo imageInfo = ImageFactory.getRGBData(decode);
//
//            //人脸特征获取
//            List<FaceFeature> faceFeatureList = faceEngineService.extractFaceFeature(imageInfo);
//            if (faceFeatureList == null) {
//                throw new GlobalException(new CodeMsg(ErrorCodeEnum.NO_FACE_DETECTED));
//            }
//
//            for(FaceFeature faceFeature:faceFeatureList){
//
//                UserFaceInfo userFaceInfo = new UserFaceInfo();
//                userFaceInfo.setName(name);
//                userFaceInfo.setGroupId(groupId);
//                userFaceInfo.setFaceFeature(faceFeature.getFeatureData());
//                userFaceInfo.setFaceId(RandomUtil.randomString(10));
//
//                //人脸特征插入到数据库
//                userFaceInfoService.insertSelective(userFaceInfo);
//            }
//
//            logger.info("faceAdd:" + name);
//            return "success";
//        } catch (Exception e) {
//            logger.error("", e);
//            throw new GlobalException(new CodeMsg(ErrorCodeEnum.UNKNOWN));
//        }
//    }

    /*
    人脸识别
     */
    @PostMapping("/faceSearch")
//    @RequestMapping(value = "/faceSearch", method = RequestMethod.POST)
//    @ResponseBody
    public List<FaceSearchResDto> faceSearch(String file, Integer groupId) throws Exception {

        if (groupId == null) {;
            throw new GlobalException(CodeMsg.GROUP_ID_IS_NULL);
        }
        byte[] decode = Base64.decode(base64Process(file));
        BufferedImage bufImage = ImageIO.read(new ByteArrayInputStream(decode));
        ImageInfo imageInfo = ImageFactory.bufferedImage2ImageInfo(bufImage);


        //人脸特征获取
        List<FaceFeature> faceFeatureList = faceEngineService.extractFaceFeature(imageInfo);
        if (faceFeatureList == null) {
            throw new GlobalException(new CodeMsg(ErrorCodeEnum.NO_FACE_DETECTED));
        }
        List<FaceSearchResDto> faceSearchResDtoList=new ArrayList<>();
        for(FaceFeature faceFeature:faceFeatureList){

            //人脸比对，获取比对结果
            List<FaceUserInfo> userFaceInfoList = faceEngineService.compareFaceFeature(faceFeature.getFeatureData(), groupId);

            if (CollectionUtil.isNotEmpty(userFaceInfoList)) {
                FaceUserInfo faceUserInfo = userFaceInfoList.get(0);
                FaceSearchResDto faceSearchResDto = new FaceSearchResDto();
                BeanUtil.copyProperties(faceUserInfo, faceSearchResDto);
                List<ProcessInfo> processInfoList = faceEngineService.process(imageInfo);
                if (CollectionUtil.isNotEmpty(processInfoList)) {
                    //人脸检测
                    List<FaceInfo> faceInfoList = faceEngineService.detectFaces(imageInfo);
                    int left = faceInfoList.get(0).getRect().getLeft();
                    int top = faceInfoList.get(0).getRect().getTop();
                    int width = faceInfoList.get(0).getRect().getRight() - left;
                    int height = faceInfoList.get(0).getRect().getBottom() - top;

                    Graphics2D graphics2D = bufImage.createGraphics();
                    graphics2D.setColor(Color.RED);//红色
                    BasicStroke stroke = new BasicStroke(5f);
                    graphics2D.setStroke(stroke);
                    graphics2D.drawRect(left, top, width, height);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    ImageIO.write(bufImage, "jpg", outputStream);
                    byte[] bytes1 = outputStream.toByteArray();
                    faceSearchResDto.setImage("data:image/jpeg;base64," + Base64Utils.encodeToString(bytes1));
                    faceSearchResDto.setAge(processInfoList.get(0).getAge());
                    faceSearchResDto.setGender(processInfoList.get(0).getGender().equals(1) ? "女" : "男");

                }
                faceSearchResDtoList.add(faceSearchResDto);
            }else{
                throw new GlobalException(new CodeMsg(ErrorCodeEnum.FACE_DOES_NOT_MATCH));
            }


        }
        return faceSearchResDtoList;
    }


    @RequestMapping(value = "/detectFaces", method = RequestMethod.POST)
    @ResponseBody
    public List<FaceInfo> detectFaces(String image) throws IOException {
        byte[] decode = Base64.decode(image);
        InputStream inputStream = new ByteArrayInputStream(decode);
        ImageInfo imageInfo = ImageFactory.getRGBData(inputStream);

        if (inputStream != null) {
            inputStream.close();
        }
        List<FaceInfo> faceInfoList = faceEngineService.detectFaces(imageInfo);

        return faceInfoList;
    }


    private String base64Process(String base64Str) {
        if (!StringUtils.isEmpty(base64Str)) {
            String photoBase64 = base64Str.substring(0, 30).toLowerCase();
            int indexOf = photoBase64.indexOf("base64,");
            if (indexOf > 0) {
                base64Str = base64Str.substring(indexOf + 7);
            }

            return base64Str;
        } else {
            return "";
        }
    }
}
