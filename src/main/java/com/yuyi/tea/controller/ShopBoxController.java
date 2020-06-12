package com.yuyi.tea.controller;

import com.yuyi.tea.bean.*;
import com.yuyi.tea.service.RedisService;
import com.yuyi.tea.service.ShopBoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
        shopBox = shopBoxService.updateShopBox(shopBox);
        shopBox.setShop(new Shop(shopBox.getShop().getUid(),shopBox.getShop().getName()));
        return shopBox;
    }

    /**
     * 获取门店包厢列表及其当天预约信息
     * @param shopId
     * @return
     */
    @GetMapping("/mobile/boxes/{shopId}")
    public List<ShopBox> getMobileShopBoxes(@PathVariable int shopId){
        List<ShopBox> shopBoxes = shopBoxService.getShopBoxes(shopId);
        return shopBoxes;
    }

    /**
     * 根据boxId,开始时间，结束时间获取包厢预约列表
     * @param boxId
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/mobile/reservations/{boxId}/{startTime}/{endTime}")
    public List<Reservation> getReservations(@PathVariable int boxId,@PathVariable long startTime,@PathVariable long endTime){
        List<Reservation> reservations=shopBoxService.getReservations(boxId,startTime,endTime);
        return reservations;
    }

    /**
     * 获取近期热门包厢
     * @return
     */
    @GetMapping("/mp/box/hot")
    public List<BoxReservation> getHotBoxes(){
        List<BoxReservation> boxes=shopBoxService.getHotBoxes();
        List<BoxReservation> list=new ArrayList<>();
        for(int i=0;i<3;i++){
            Photo photo = boxes.get(i).getBox().getPhotos().get(0);
            List<Photo> photos=new ArrayList<>();
            photos.add(photo);
            boxes.get(i).getBox().setPhotos(photos);
            list.add(boxes.get(i));
        }
        return list;
    }

    /**
     * 小程序获取门店包厢列表及其当天预约信息
     * @param shopId
     * @return
     */
    @GetMapping("/mp/shop/box/{shopId}")
    public List<ShopBox> getMpShopBoxList(@PathVariable int shopId){
        List<ShopBox> shopBoxes = shopBoxService.getShopBoxes(shopId);
        for (ShopBox box : shopBoxes) {
            List<Photo> photos=new ArrayList<>();
            photos.add(box.getPhotos().get(0));
            box.setPhotos(photos);
            box.setShop(null);
        }

        return shopBoxes;
    }

    /**
     * 小程序根据boxId,开始时间，结束时间获取包厢预约列表
     * @param boxId
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/mp/reservations/{boxId}/{startTime}/{endTime}")
    public List<Reservation> getMpReservations(@PathVariable int boxId,@PathVariable long startTime,@PathVariable long endTime){
        List<Reservation> reservations=shopBoxService.getReservations(boxId,startTime,endTime);
        return reservations;
    }

    @GetMapping("/mp/box/{boxId}")
    public ShopBox getMpBox(@PathVariable int boxId){
        ShopBox shopBoxByUid = shopBoxService.getShopBoxByUid(boxId);
        return shopBoxByUid;
    }
}
