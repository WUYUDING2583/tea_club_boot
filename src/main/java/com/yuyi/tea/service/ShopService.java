package com.yuyi.tea.service;

import com.yuyi.tea.bean.*;
import com.yuyi.tea.mapper.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CacheConfig(cacheNames = "shop")//抽取缓存的公共配置
@Service
public class ShopService {

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private OpenHourMapper openHourMapper;

    @Autowired
    private PhotoMapper photoMapper;

    @Autowired
    private ClerkMapper clerkMapper;

    @Autowired
    private ShopBoxMapper shopBoxMapper;

    public List<Shop> getShopList(){
        List<Shop> shopList = shopMapper.getShopList();
        System.out.println("query shop list"+shopList.toString());
        return shopList;
    }

//    @Cacheable
    public Shop getShopByUid(int uid){
        Shop shop = shopMapper.getShopByUid(uid);
        return shop;
    }

    public void saveShop(Shop shop){
        shopMapper.saveShop(shop);
        for(OpenHour openHour:shop.getOpenHours()){
            openHour.setShopId(shop.getUid());
            openHourMapper.insertOpenHour(openHour);
            for(String date:openHour.getDate()){
                openHourMapper.insertOpenHourRepeat(date,openHour.getUid());
            }
        }
        for(Photo photo:shop.getPhotos()){
            photo.setShopId(shop.getUid());
            photoMapper.saveShopPhotos(photo);
        }
    }

    public void deleteShop(int uid){
        shopMapper.deleteShop(uid);
    }

    public Shop updateShop(Shop shop) {
        shopMapper.updateShop(shop);
        ArrayList<Integer> clerksUid=new ArrayList<Integer>();
        //更新职员数据
        for(Clerk clerk:shop.getClerks()){
            clerksUid.add(clerk.getUid());
            clerkMapper.updateClerkShopId(clerk.getUid(),shop.getUid());
        }
        //获取目前该门店所有职员再过滤出过滤后要删除的职员
        List<Clerk> originClerkss = clerkMapper.getClerksByShopId(shop.getUid());
        List<Clerk> needDeleteClerks=originClerkss.stream()
                .filter((Clerk clerk)->!clerksUid.contains(clerk.getUid()))
                .collect(Collectors.toList());
        //删除这些职员
        for(Clerk clerk:needDeleteClerks){
            clerkMapper.deleteClerk(clerk.getUid());
        }
        //将更新后的职员返回
        List<Clerk> updateClerks=originClerkss.stream()
                .filter((Clerk clerk)->clerksUid.contains(clerk.getUid()))
                .collect(Collectors.toList());
        shop.setClerks(updateClerks);
        //更新包厢信息
        ArrayList<Integer> shopBoxesUid=new ArrayList<>();
        for(ShopBox shopBox:shop.getShopBoxes()){
            shopBoxesUid.add(shopBox.getUid());
            shopBoxMapper.updateShopBoxShopId(shopBox.getUid(),shop.getUid());
        }
        List<ShopBox> originShopBoxes=shopBoxMapper.getShopBoxes();
        List<ShopBox> needDeleteShopBoxes=originShopBoxes.stream()
                .filter((ShopBox shopBox)->!shopBoxesUid.contains(shopBox.getUid()))
                .collect(Collectors.toList());
        //将这些包厢的shopId设为null
        for(ShopBox shopBox:needDeleteShopBoxes){
            shopBoxMapper.setShopIdNull(shopBox.getUid());
        }
        List<ShopBox> updateShopBoxes=originShopBoxes.stream()
                .filter((ShopBox shopBox)->shopBoxesUid.contains(shopBox.getUid()))
                .collect(Collectors.toList());
        shop.setShopBoxes(updateShopBoxes);
        //更新门店照片
        ArrayList<Integer> photosUid=new ArrayList<>();
        for(Photo photo:shop.getPhotos()){
            photosUid.add(photo.getUid());
            photo.setShopId(shop.getUid());
            photoMapper.saveShopPhotos(photo);
        }
        List<Photo> originPhotos=photoMapper.getPhotosByShopId(shop.getUid());
        List<Photo> needDeletePhotos=originPhotos.stream()
                .filter((Photo photo)->!photosUid.contains(photo.getUid()))
                .collect(Collectors.toList());
        //删除这些照片
        for(Photo photo:needDeletePhotos){
            photoMapper.deletePhoto(photo.getUid());
        }
        List<Photo> updatePhotos=originPhotos.stream()
                .filter((Photo photo)->photosUid.contains(photo.getUid()))
                .collect(Collectors.toList());
        shop.setPhotos(updatePhotos);
        //更新营业时间
        //删除门店所有营业时间
        openHourMapper.deleteOpenHourByShop(shop.getUid());
        for(OpenHour openHour:shop.getOpenHours()){
            openHour.setShopId(shop.getUid());
            openHourMapper.insertOpenHour(openHour);
            for(String date:openHour.getDate()){
                openHourMapper.insertOpenHourRepeat(date,openHour.getUid());
            }
        }
//        openHourMapper.insertOpenHours(shop.getOpenHours());
        return shop;
    }
}