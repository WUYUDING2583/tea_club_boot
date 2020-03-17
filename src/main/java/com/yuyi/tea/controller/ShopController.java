package com.yuyi.tea.controller;

import com.yuyi.tea.bean.Shop;
import com.yuyi.tea.common.CommConstants;
import com.yuyi.tea.exception.ShopNotExistException;
import com.yuyi.tea.exception.UserException;
import com.yuyi.tea.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ShopController {

    @Autowired
    private ShopService shopService;

    @GetMapping("/shops")
    public List<Shop> getShopList(){
        List<Shop> shopList = shopService.getShopList();
        return shopList;
    }

    @GetMapping("/shop/{uid}")
    public Shop getShopByUid(@PathVariable int uid){
        Shop shop = shopService.getShopByUid(uid);
        System.out.println("query shop "+uid+" "+shop);
        if(shop==null){
            throw new ShopNotExistException();
        }
        return new Shop();
    }

    @PostMapping("/shop")
    @Transactional(rollbackFor = Exception.class)
    public Shop saveShop(@RequestBody Shop shop){
        shopService.saveShop(shop);
        System.out.println("add shop"+shop);
        return shop;
    }

    @DeleteMapping("/shop/{uid}")
    @Transactional(rollbackFor = Exception.class)
    public String deleteShop(@PathVariable int uid){
        shopService.deleteShop(uid);
        return "Success";
    }

    @GetMapping("/test")
    public String test(){
        return "success";
    }
}
