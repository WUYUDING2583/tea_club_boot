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

    /**
     * 新增包厢
     * @param shopBox
     * @return
     */
    @PostMapping("/admin/shopBox")
    @Transactional(rollbackFor = Exception.class)
    ShopBox saveShopBox(@RequestBody ShopBox shopBox){
        shopBoxService.saveShopBox(shopBox);
        return shopBox;
    }

    /**
     * 获取包厢列表
     * @return
     */
    @GetMapping("/admin/shopBoxes")
    List<ShopBox> getShopBoxes(){
        List<ShopBox> shopBoxes = shopBoxService.getShopBoxes();
        return shopBoxes;
    }

    /**
     * 失效包厢
     * @param uid
     * @return
     */
    @DeleteMapping("/admin/shopBox/{uid}")
    @Transactional(rollbackFor = Exception.class)
    public String terminalShopBox(@PathVariable int uid){
        shopBoxService.terminalShopBoxByUid(uid);
        return "Success";
    }

    /**
     * 查看包厢详情
     * @param uid
     * @return
     */
    @GetMapping("/admin/shopBox/{uid}")
    public ShopBox getShopBoxByUid(@PathVariable int uid){
        ShopBox shopBoxByUid = shopBoxService.getShopBoxByUid(uid);
        return shopBoxByUid;
    }

    /**
     * 修改包厢信息
     * @param shopBox
     * @return
     */
    @PutMapping("/admin/shopBox")
    @Transactional(rollbackFor = Exception.class)
    public ShopBox updateShopBox(@RequestBody ShopBox shopBox){
        shopBoxService.updateShopBox(shopBox);
        return shopBox;
    }
}
