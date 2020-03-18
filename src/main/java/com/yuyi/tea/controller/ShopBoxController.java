package com.yuyi.tea.controller;

import com.yuyi.tea.bean.ShopBox;
import com.yuyi.tea.service.ShopBoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
