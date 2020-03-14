package com.yuyi.tea.controller;

import com.yuyi.tea.bean.Shop;
import com.yuyi.tea.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return shop;
    }

    @PostMapping("/shop")
    public Shop addShop(@RequestBody Shop shop){
        System.out.println("add shop"+shop);
        return shop;
    }
}
