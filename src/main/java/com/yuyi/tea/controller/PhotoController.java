package com.yuyi.tea.controller;

import com.yuyi.tea.bean.Photo;
import com.yuyi.tea.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.management.relation.Relation;
import javax.servlet.http.HttpServletRequest;

@RestController
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @PostMapping(value = "savephoto")
    @Transactional(rollbackFor = Exception.class)
    public Photo searchMember( HttpServletRequest request){
        // 得到文件
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
        Photo photo = new Photo();
        try {
            byte[] data;
            data = file.getBytes();
            photo.setPhoto(data);
            photoService.insertPhoto(photo);
        }catch (Exception e){
            e.printStackTrace();
        }
        return photo;
    }
}
