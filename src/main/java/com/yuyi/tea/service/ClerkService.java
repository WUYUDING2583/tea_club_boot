package com.yuyi.tea.service;

import com.yuyi.tea.bean.Clerk;
import com.yuyi.tea.bean.Photo;
import com.yuyi.tea.bean.Position;
import com.yuyi.tea.bean.Shop;
import com.yuyi.tea.mapper.ClerkMapper;
import com.yuyi.tea.mapper.PhotoMapper;
import com.yuyi.tea.mapper.ShopMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClerkService {

    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private ClerkMapper clerkMapper;

    @Autowired
    private PhotoMapper photoMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private RedisService redisService;


    public List<Clerk> getAllClerks(){
        List<Clerk> clerks = clerkMapper.getAllClerks();
        return clerks;
    }

    public List<Position> getPositions() {
        List<Position> positions = clerkMapper.getPositions();
        return positions;
    }

    public void saveClerk(Clerk clerk) {
        clerkMapper.saveClerk(clerk);
        clerk.getAvatar().setClerkId(clerk.getUid());
        photoMapper.saveClerkAvatar(clerk.getAvatar());
    }

    public void deleteClerk(int uid) {
        clerkMapper.deleteClerk(uid);
    }

    public Clerk getClerk(int uid) {
        Clerk clerk = clerkMapper.getClerk(uid);
        return clerk;
    }

    public void updateClerk(Clerk clerk) {
        clerkMapper.updateClerk(clerk);
        Photo avatar = clerk.getAvatar();
        Photo originAvatar = photoMapper.getAvatarByClerkId(clerk.getUid());
        if(originAvatar!=null&&originAvatar.getUid()!=avatar.getUid()){
            photoMapper.deletePhoto(originAvatar.getUid());
        }
        avatar.setClerkId(clerk.getUid());
        photoMapper.saveClerkAvatar(avatar);
        Photo updateAvatar = photoMapper.getAvatarByClerkId(clerk.getUid());
        Shop updateClerkShop = shopMapper.getShopOfClerk(clerk.getShop().getUid());
        Position updatePosition = clerkMapper.getPosition(clerk.getPosition().getUid());
        clerk.setPosition(updatePosition);
        clerk.setShop(updateClerkShop);
        clerk.setAvatar(updateAvatar);
    }

    //从缓存中获取职员信息
    public Clerk getRedisClerk(int uid){
        Clerk clerk=null;
        boolean hasKey = redisService.exists("clerks:clerk:"+uid);
        if(hasKey){
            //获取缓存
            clerk= (Clerk) redisService.get("clerks:clerk:"+uid);
            log.info("从缓存获取的数据"+ clerk);
        }else{
            //从数据库中获取信息
            log.info("从数据库中获取数据");
            clerk = clerkMapper.getClerk(uid);
            //数据插入缓存（set中的参数含义：key值，user对象，缓存存在时间10（long类型），时间单位）
            redisService.set("clerks:clerk:"+uid,clerk);
            log.info("数据插入缓存" + clerk);
        }
        return clerk;
    }
}
