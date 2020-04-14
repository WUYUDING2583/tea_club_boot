package com.yuyi.tea.service;

import com.yuyi.tea.bean.*;
import com.yuyi.tea.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//@CacheConfig(cacheNames = "shop")//抽取缓存的公共配置
@Service
@Slf4j
public class ShopService {

    public static final String REDIS_SHOPS_NAME="shops";
    public static final String REDIS_SHOP_NAME=REDIS_SHOPS_NAME+":shop";

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

    @Autowired
    private RedisService redisService;

    /**
     * 获取门店列表
     * @return
     */
    public List<Shop> getShopList(){
        log.info("从数据库中获取门店列表");
        List<Shop> shopList = shopMapper.getShopList();
        for (Shop shop : shopList) {
            shop.setOpenHours(null);
            shop.setClerks(null);
            shop.setShopBoxes(null);
            shop.setPhotos(null);
        }
        return shopList;
    }

    /**
     * 根据uid获取门店详细信息
     * @param uid
     * @return
     */
    public Shop getShopByUid(int uid){
        boolean hasKey=redisService.exists(REDIS_SHOP_NAME+":"+uid);
        Shop shop=null;
        if(hasKey){
            shop= (Shop) redisService.get(REDIS_SHOP_NAME+":"+uid);
            log.info("从redis中获取门店信息"+shop);
        }else {
            shop = shopMapper.getShopByUid(uid);
            log.info("从数据库获取门店数据");
            log.info("将门店信息存入redis"+shop);
            redisService.set(REDIS_SHOP_NAME+":"+uid,shop);
        }
        return shop;
    }


    /**
     * 新增门店
     * @param shop
     */
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
        log.info("将新增门店存入redis"+shop);
        redisService.set(REDIS_SHOP_NAME+":"+shop.getUid(),shop);
    }

    /**
     * 门店失效，不再在商城展示
     * @param uid
     */
    public void terminalShop(int uid){
        boolean hasKey=redisService.exists(REDIS_SHOP_NAME+":"+uid);
        if(hasKey){
            //若缓存中存在此商店，更新缓存中信息
            log.info("更新缓存商店状态信息");
            Shop shop= (Shop) redisService.get(REDIS_SHOP_NAME+":"+uid);
            shop.setEnforceTerminal(true);
            redisService.set(REDIS_SHOP_NAME+":"+uid,shop);
        }
        shopMapper.terminalShop(uid);
    }

    /**
     * 更改门店信息
     * @param shop
     */
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
            clerkMapper.terminalClerk(clerk.getUid());
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
        log.info("更新redis中门店信息"+shop);
        redisService.set(REDIS_SHOP_NAME+":"+shop.getUid(),shop);
        return shop;
    }
}