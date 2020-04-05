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

    /**
     * 获取门店列表
     * @return
     */
    @GetMapping("/admin/shops")
    public List<Shop> getShopList(){
        List<Shop> shopList = shopService.getShopList();
        return shopList;
    }

    @GetMapping("/shop/{uid}")
    public Shop getShopByUid(@PathVariable int uid){
//        throw new ShopNotExistException();
        Shop shop = shopService.getShopByUid(uid);
        System.out.println("query shop "+uid+" "+shop);
        if(shop==null){
            throw new ShopNotExistException();
        }
        return shop;
    }

    @PostMapping("/admin/shop")
    @Transactional(rollbackFor = Exception.class)
    public Shop saveShop(@RequestBody Shop shop){
        shopService.saveShop(shop);
        System.out.println("add shop"+shop);
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

    @PutMapping("/shop")
    @Transactional(rollbackFor = Exception.class)
    public Shop updateShop(@RequestBody Shop shop){
        System.out.println("update shop "+shop);
        shopService.updateShop(shop);
        return shop;
    }

}
