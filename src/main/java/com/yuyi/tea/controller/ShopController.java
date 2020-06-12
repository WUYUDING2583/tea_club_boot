package com.yuyi.tea.controller;

import com.yuyi.tea.bean.Photo;
import com.yuyi.tea.bean.Shop;
import com.yuyi.tea.common.CommConstants;
import com.yuyi.tea.exception.ShopNotExistException;
import com.yuyi.tea.exception.UserException;
import com.yuyi.tea.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ShopController {

    @Autowired
    private ShopService shopService;

    /**
     * 获取门店列表
     * @return
     */
    @GetMapping("/admin/shops")
    public List<Shop> getShopList(){
        List<Shop> shopList = shopService.getShopList();
        return shopList;
    }

    /**
     * 根据uid获取门店详细信息
     * @param uid
     * @return
     */
    @GetMapping("/admin/shop/{uid}")
    public Shop getShopByUid(@PathVariable int uid){
        Shop shop = shopService.getShopByUid(uid);
        if(shop==null){
            throw new ShopNotExistException();
        }
        return shop;
    }

    /**
     * 新增门店
     * @param shop
     * @return
     */
    @PostMapping("/admin/shop")
    @Transactional(rollbackFor = Exception.class)
    public Shop saveShop(@RequestBody Shop shop){
        shopService.saveShop(shop);
        return shop;
    }

    /**
     * 门店失效，不再在商城展示
     * @param uid
     * @return
     */
    @DeleteMapping("/admin/shop/{uid}")
    @Transactional(rollbackFor = Exception.class)
    public String terminalShop(@PathVariable int uid){
        shopService.terminalShop(uid);
        return "Success";
    }

    /**
     * 更改门店信息
     * @param shop
     * @return
     */
    @PutMapping("/admin/shop")
    @Transactional(rollbackFor = Exception.class)
    public Shop updateShop(@RequestBody Shop shop){
        shopService.updateShop(shop);
        return shop;
    }

    /**
     * 获取门店名字列表
     * @return
     */
    @GetMapping("/mp/shopName")
    public List<Shop> getShopNameList(){
        List<Shop> shopList = shopService.getShopList();
        for(Shop shop:shopList){
            shop.setOpenHours(null);
            shop.setPhotos(null);
            shop.setClerks(null);
            shop.setShopBoxes(null);
        }
        return shopList;
    }

    /**
     * 获取门店列表
     * @return
     */
    @GetMapping("/mp/shop")
    public List<Shop> getMpShopList(){
        List<Shop> shopList = shopService.getShopList();
        for(Shop shop:shopList){
            shop.setClerks(null);
            shop.setShopBoxes(null);
            List<Photo> photos=new ArrayList<>();
            photos.add(shop.getPhotos().get(0));
            shop.setPhotos(photos);
        }

        return shopList;
    }

    @GetMapping("/mp/shop/{shopId}")
    public Shop getMpShop(@PathVariable int shopId){
        Shop shop = shopService.getShopByUid(shopId);
        shop.setClerks(null);
        shop.setPhotos(new ArrayList<>());
        shop.setShopBoxes(new ArrayList<>());
        return shop;
    }


}
