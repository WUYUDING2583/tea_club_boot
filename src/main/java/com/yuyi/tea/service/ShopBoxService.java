package com.yuyi.tea.service;

import com.yuyi.tea.bean.Photo;
import com.yuyi.tea.bean.Shop;
import com.yuyi.tea.bean.ShopBox;
import com.yuyi.tea.mapper.PhotoMapper;
import com.yuyi.tea.mapper.PriceMapper;
import com.yuyi.tea.mapper.ShopBoxMapper;
import com.yuyi.tea.mapper.ShopMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopBoxService {

    @Autowired
    ShopBoxMapper shopBoxMapper;

    @Autowired
    PhotoMapper photoMapper;

    @Autowired
    PriceMapper priceMapper;

    @Autowired
    ShopMapper shopMapper;

    public void saveShopBox(ShopBox shopBox){
        priceMapper.savePrice(shopBox.getPrice());
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

    public ShopBox getShopBoxByUid(int uid){
        ShopBox shopBoxByUid = shopBoxMapper.getShopBoxByUid(uid);
        return shopBoxByUid;
    }

    public void updateShopBox(ShopBox shopBox){
        priceMapper.updatePrice(shopBox.getPrice());
        shopBoxMapper.updateShopBox(shopBox);
        ArrayList<Integer> photosUid=new ArrayList<Integer>();
        //先更新包厢照片数据
        for(Photo photo:shopBox.getPhotos()){
            photosUid.add(photo.getUid());
            photo.setShopBoxId(shopBox.getUid());
            photoMapper.saveShopBoxPhotos(photo);
        }
        //获取目前所有该包厢照片再过滤出过滤后要删除的照片
        List<Photo> originPhotos = photoMapper.getPhotosByShopBoxId(shopBox.getUid());
        List<Photo> needDeletePhotos=originPhotos.stream()
                .filter((Photo photo)->!photosUid.contains(photo.getUid()))
                .collect(Collectors.toList());
        //删除这些照片
        for(Photo photo:needDeletePhotos){
            photoMapper.deletePhoto(photo.getUid());
        }
        List<Photo> currentPhotos = originPhotos.stream()
                .filter((Photo photo)->photosUid.contains(photo.getUid()))
                .collect(Collectors.toList());
        Shop shop = shopMapper.getShopOfShopBox(shopBox.getShop().getUid());
        shopBox.setShop(shop);
        shopBox.setPhotos(currentPhotos);
    }
}
