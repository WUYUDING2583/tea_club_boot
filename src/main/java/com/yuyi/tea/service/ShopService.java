package com.yuyi.tea.service;

import com.yuyi.tea.bean.OpenHour;
import com.yuyi.tea.bean.Photo;
import com.yuyi.tea.bean.Shop;
import com.yuyi.tea.mapper.OpenHourMapper;
import com.yuyi.tea.mapper.ShopMapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@CacheConfig(cacheNames = "shop")//抽取缓存的公共配置
@Service
public class ShopService {

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private OpenHourMapper openHourMapper;


    public List<Shop> getShopList(){
        List<Shop> shopList = shopMapper.getShopList();
        System.out.println("query shop list"+shopList.toString());
        return shopList;
    }

    public Shop getShopByUid(int uid){
        Shop shop = shopMapper.getShopByUid(uid);
        return shop;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveShop(Shop shop){
        shopMapper.saveShop(shop);
        for(OpenHour openHour:shop.getOpenHours()){
            openHour.setShopId(shop.getUid());
            openHourMapper.insertOpenHour(openHour);
            for(int date:openHour.getDate()){
                openHourMapper.insertOpenHourRepeat(date,openHour.getUid());
            }
        }
        for(Photo photo:shop.getPhotos()){
            photo.setShopId(shop.getUid());
            shopMapper.saveShopPhotos(photo);
        }
    }
}