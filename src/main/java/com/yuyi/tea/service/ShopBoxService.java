package com.yuyi.tea.service;

import com.yuyi.tea.bean.*;
import com.yuyi.tea.common.utils.TimeUtil;
import com.yuyi.tea.mapper.PhotoMapper;
import com.yuyi.tea.mapper.PriceMapper;
import com.yuyi.tea.mapper.ShopBoxMapper;
import com.yuyi.tea.mapper.ShopMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ShopBoxService {

    @Autowired
    ShopBoxMapper shopBoxMapper;

    @Autowired
    PhotoMapper photoMapper;

    @Autowired
    PriceMapper priceMapper;

    @Autowired
    ShopMapper shopMapper;

    @Autowired
    private RedisService redisService;

    public static String REDIS_SHOP_BOXES_NAME="shopBoxes";
    public static String REDIS_SHOP_BOX_NAME=REDIS_SHOP_BOXES_NAME+":shopBox";

    /**
     * 新增包厢
     * @param shopBox
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveShopBox(ShopBox shopBox){
        priceMapper.savePrice(shopBox.getPrice());
        shopBoxMapper.saveShopBox(shopBox);
        for(Photo photo:shopBox.getPhotos()){
            photo.setShopBoxId(shopBox.getUid());
            photoMapper.saveShopBoxPhotos(photo);
        }
        for(ShopBoxInfo info:shopBox.getInfos()){
            info.setBoxId(shopBox.getUid());
            shopBoxMapper.saveBoxInfo(info);
        }
    }

    /**
     * 获取包厢列表
     * @return
     */
    public List<ShopBox> getShopBoxes(){
        List<ShopBox> shopBoxes = shopBoxMapper.getShopBoxes();
        return shopBoxes;
    }

    /**
     * 失效包厢
     * @param uid
     */
    @Transactional(rollbackFor = Exception.class)
    public void terminalShopBoxByUid(int uid){
        shopBoxMapper.terminalShopBoxByUid(uid);
        log.info("将redis中对应"+uid+"包厢删除");
        boolean hasKey=redisService.exists(REDIS_SHOP_BOX_NAME+":"+uid);
        if(hasKey){
            redisService.remove(REDIS_SHOP_BOX_NAME+":"+uid);
        }
    }

    /**
     * 查看包厢详情
     * @param uid
     * @return
     */
    public ShopBox getShopBoxByUid(int uid){
        boolean hasKey=redisService.exists(REDIS_SHOP_BOX_NAME+":"+uid);
        ShopBox shopBox;
        if(hasKey){
            shopBox= (ShopBox) redisService.get(REDIS_SHOP_BOX_NAME+":"+uid);
            log.info("从redis获取包厢详情"+shopBox);
        }else{
            log.info("从数据库获取包厢详情");
            shopBox = shopBoxMapper.getShopBoxByUid(uid);
            log.info("将包厢信息存入redis"+shopBox);
            redisService.set(REDIS_SHOP_BOX_NAME+":"+uid,shopBox);
        }
        return shopBox;
    }

    /**
     * 修改包厢信息
     * @param shopBox
     */
    @Transactional(rollbackFor = Exception.class)
    public ShopBox updateShopBox(ShopBox shopBox){
        priceMapper.updatePrice(shopBox.getPrice());
        shopBoxMapper.updateShopBox(shopBox);
        ArrayList<Integer> photosUid=new ArrayList<Integer>();
        //先更新包厢照片数据
        for(Photo photo:shopBox.getPhotos()){
            photosUid.add(photo.getUid());
            photo.setShopBoxId(shopBox.getUid());
            photoMapper.saveShopBoxPhotos(photo);
        }
        //获取目前所有该包厢照片再过滤出过滤后要删除的照片
        List<Photo> originPhotos = photoMapper.getPhotosByShopBoxId(shopBox.getUid());
        List<Photo> needDeletePhotos=originPhotos.stream()
                .filter((Photo photo)->!photosUid.contains(photo.getUid()))
                .collect(Collectors.toList());
        //删除这些照片
        for(Photo photo:needDeletePhotos){
            photoMapper.deletePhoto(photo.getUid());
        }
        List<Photo> currentPhotos = originPhotos.stream()
                .filter((Photo photo)->photosUid.contains(photo.getUid()))
                .collect(Collectors.toList());
        //更新包厢须知
        //首先删除包厢当前所有须知
        shopMapper.deleteBoxInfos(shopBox.getUid());
        for(ShopBoxInfo info:shopBox.getInfos()){
            info.setBoxId(shopBox.getUid());
            shopBoxMapper.saveBoxInfo(info);
        }
        shopBox=shopBoxMapper.getShopBoxByUid(shopBox.getUid());
        log.info("更新redis中包厢信息"+shopBox);
        redisService.set(REDIS_SHOP_BOX_NAME+":"+shopBox.getUid(),shopBox);
        return shopBox;
    }

    /**
     * 根据shopId获取门店包厢列表及其当天预约信息
     * @param shopId
     * @return
     */
    public List<ShopBox> getShopBoxes(int shopId) {
        long startTime= TimeUtil.getNDayAgoStartTime(0);
        long endTime = TimeUtil.getNDayAgoStartTime(-1);
        List<ShopBox> shopBoxes=shopBoxMapper.getShopBoxByShopId(shopId);
        for(ShopBox shopBox:shopBoxes){
            List<Reservation> reservations = shopBoxMapper.getReservationByBoxId(shopBox.getUid(), startTime, endTime);
            shopBox.setReservations(reservations);
        }
        return shopBoxes;
    }

    /**
     * 根据boxId,开始时间，结束时间获取包厢预约列表
     * @param boxId
     * @param startTime
     * @param endTime
     * @return
     */
    public List<Reservation> getReservations(int boxId, long startTime, long endTime) {
        List<Reservation> reservationByBoxId = shopBoxMapper.getReservationByBoxId(boxId, startTime, endTime);
        return reservationByBoxId;
    }

    /**
     * 获取小程序走马灯展示的包厢
     * @return
     */
    public List<ShopBox> getSwiperList() {
        List<ShopBox> shopBoxes=shopBoxMapper.getSwiperList();
        return shopBoxes;
    }

    /**
     * 获取近期热门包厢
     * @return
     */
    public List<BoxReservation> getHotBoxes() {
        List<BoxReservation> boxes=shopBoxMapper.getHotBoxes();
        for(BoxReservation boxReservation:boxes){
            boxReservation.getBox().setShop(new Shop(boxReservation.getBox().getShop().getUid()));
            boxReservation.getBox().setReservations(null);
        }
        return boxes;
    }

    public List<ShopBox> search(String value) {
        List<ShopBox> boxes=shopBoxMapper.search("%"+value+"%");
        return boxes;
    }
}
