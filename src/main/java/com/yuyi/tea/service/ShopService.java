package com.yuyi.tea.service;

import com.yuyi.tea.bean.Shop;
import com.yuyi.tea.mapper.ShopMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;

@CacheConfig(cacheNames = "shop")//抽取缓存的公共配置
@Service
public class ShopService {

    @Autowired
    private ShopMapper shopMapper;

    public List<Shop> getShopList(){
        List<Shop> shopList = shopMapper.getShopList();
        System.out.println("query shop list"+shopList.toString());
        return shopList;
    }

    public Shop getShopByUid(int uid){
        Shop shop = shopMapper.getShopByUid(uid);
        System.out.println("query shop"+shop);
        return shop;
    }
}
