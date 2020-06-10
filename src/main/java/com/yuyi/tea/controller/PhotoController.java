package com.yuyi.tea.controller;

import com.yuyi.tea.bean.Photo;
import com.yuyi.tea.exception.PhotoTooBigException;
import com.yuyi.tea.exception.UserException;
import com.yuyi.tea.service.PhotoService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    /**
     * 保存图片
     * @param request
     * @return
     */
    @PostMapping("/savephoto")
    @Transactional(rollbackFor = Exception.class)
    public Photo savePhoto( HttpServletRequest request){
        // 得到文件
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
        if(file.getSize()>1024*1024*1024){
            log.info("图片太大");
            throw new PhotoTooBigException();
        }
        Photo photo = new Photo();
        try {
            byte[] data;
            data = file.getBytes();
            photo.setPhoto(data);
            photoService.insertPhoto(photo);
        }catch (Exception e){
            e.printStackTrace();
            throw new UserException(e.getMessage(),e.getMessage());
        }
        return photo;
    }
}
