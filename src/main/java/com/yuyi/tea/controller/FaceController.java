package com.yuyi.tea.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.toolkit.ImageFactory;
import com.arcsoft.face.toolkit.ImageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yuyi.tea.bean.Clerk;
import com.yuyi.tea.bean.Customer;
import com.yuyi.tea.common.CodeMsg;
import com.yuyi.tea.domain.UserFaceInfo;
import com.yuyi.tea.dto.FaceSearchResDto;
import com.yuyi.tea.dto.FaceUserInfo;
import com.yuyi.tea.dto.ProcessInfo;
import com.yuyi.tea.enums.ErrorCodeEnum;
import com.yuyi.tea.exception.GlobalException;
import com.yuyi.tea.service.interfaces.FaceEngineService;
import com.yuyi.tea.service.interfaces.UserFaceInfoService;
import com.yuyi.tea.typeAdapter.CustomerTypeAdapter;
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

import static com.yuyi.tea.common.utils.StringUtil.base64Process;


@RestController
public class FaceController {

    public final static Logger logger = LoggerFactory.getLogger(FaceController.class);


    @Autowired
    FaceEngineService faceEngineService;

    @Autowired
    UserFaceInfoService userFaceInfoService;


    /**
     * 人脸识别
     * @param file
     * @param groupId
     * @return
     * @throws Exception
     */
    @PostMapping("/faceSearch")
    public List<FaceSearchResDto> faceSearch(String file, Integer groupId) throws Exception {

        if (groupId == null) {
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

            if (!CollectionUtil.isNotEmpty(userFaceInfoList)) {
                //无匹配人脸时将其存入数据库
                userFaceInfoList = userFaceInfoService.addFace(file, groupId, null);
            }
            for(FaceUserInfo faceUserInfo:userFaceInfoList){
                FaceSearchResDto faceSearchResDto = new FaceSearchResDto();
                BeanUtil.copyProperties(faceUserInfo, faceSearchResDto);
                List<ProcessInfo> processInfoList = faceEngineService.process(imageInfo);
                faceUserInfo=userFaceInfoService.getFaceUserInfo(faceUserInfo);
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
                    faceSearchResDto.setClerk(faceUserInfo.getClerk());
                    faceSearchResDto.setCustomer(faceUserInfo.getCustomer());
                }
                if(faceSearchResDto.getCustomer()!=null)
                    faceSearchResDtoList.add(faceSearchResDto);
            }
        }
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Customer.class, new CustomerTypeAdapter());
        gsonBuilder.setPrettyPrinting();
        Gson gson=gsonBuilder.create();
        String t=gson.toJson(faceSearchResDtoList);
        WebSocketServer.sendInfo(t,null);
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



}
