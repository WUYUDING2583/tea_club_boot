package com.yuyi.tea.service;

import com.yuyi.tea.bean.Photo;
import com.yuyi.tea.mapper.PhotoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoService {

    @Autowired
    private PhotoMapper photoMapper;

    public void insertPhoto(Photo photo){
        photoMapper.insertPhoto(photo);
    }
}
