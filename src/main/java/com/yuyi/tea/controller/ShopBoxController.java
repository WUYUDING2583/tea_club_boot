package com.yuyi.tea.controller;

import com.yuyi.tea.bean.ShopBox;
import com.yuyi.tea.service.ShopBoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ShopBoxController {

    @Autowired
    ShopBoxService shopBoxService;

    @PostMapping("/shopBox")
    @Transactional(rollbackFor = Exception.class)
    ShopBox saveShopBox(@RequestBody ShopBox shopBox){
        shopBoxService.saveShopBox(shopBox);
        return shopBox;
    }

    @GetMapping("/shopBoxes")
    List<ShopBox> getShopBoxes(){
        List<ShopBox> shopBoxes = shopBoxService.getShopBoxes();
        return shopBoxes;
    }

    @DeleteMapping("/shopBox/{uid}")
    @Transactional(rollbackFor = Exception.class)
    public String deleteShopBox(@PathVariable int uid){
        shopBoxService.deleteShopBoxByUid(uid);
        return "Success";
    }

    @GetMapping("/shopBox/{uid}")
    public ShopBox getShopBoxByUid(@PathVariable int uid){
        ShopBox shopBoxByUid = shopBoxService.getShopBoxByUid(uid);
        return shopBoxByUid;
    }

    @PutMapping("/shopBox")
    @Transactional(rollbackFor = Exception.class)
    public ShopBox updateShopBox(@RequestBody ShopBox shopBox){
        shopBoxService.updateShopBox(shopBox);
        return shopBox;
    }
}
