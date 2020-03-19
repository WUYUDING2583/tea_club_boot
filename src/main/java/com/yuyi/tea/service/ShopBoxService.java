package com.yuyi.tea.service;

import com.yuyi.tea.bean.Photo;
import com.yuyi.tea.bean.ShopBox;
import com.yuyi.tea.mapper.PhotoMapper;
import com.yuyi.tea.mapper.ShopBoxMapper;
import com.yuyi.tea.mapper.ShopMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopBoxService {

    @Autowired
    ShopBoxMapper shopBoxMapper;

    @Autowired
    PhotoMapper photoMapper;

    public void saveShopBox(ShopBox shopBox){
        shopBoxMapper.saveShopBox(shopBox);
        for(Photo photo:shopBox.getPhotos()){
            photo.setShopBoxId(shopBox.getUid());
            photoMapper.saveShopBoxPhotos(photo);
        }
    }

    public List<ShopBox> getShopBoxes(){
        List<ShopBox> shopBoxes = shopBoxMapper.getShopBoxes();
        return shopBoxes;
    }

    public void deleteShopBoxByUid(int uid){
        shopBoxMapper.deleteShopBoxByUid(uid);
    }
}
