package com.yuyi.tea.service;

import com.yuyi.tea.bean.*;
import com.yuyi.tea.mapper.ClerkMapper;
import com.yuyi.tea.mapper.PhotoMapper;
import com.yuyi.tea.mapper.ShopMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ClerkService {

    public static String REDIS_POSITIONS_NAME="positions";
    public static String REDIS_CLERKS_NAME="clerks";
    public static String REDIS_CLERK_NAME=REDIS_CLERKS_NAME+":clerk";

    @Autowired
    private ClerkMapper clerkMapper;

    @Autowired
    private PhotoMapper photoMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private RedisService redisService;

    //获取除管理员外的职员列表
    public List<Clerk> getAllClerks(){
        List<Clerk> clerks = clerkMapper.getAllClerks();
        for(Clerk clerk:clerks){
            clerk.getShop().setOpenHours(null);
            clerk.getShop().setPhotos(null);
            clerk.getShop().setClerks(null);
            clerk.getShop().setShopBoxes(null);
            clerk.setAvatar(null);
            clerk.setPassword(null);
            clerk.setPositionAutorityFrontDetails(null);
        }
        return clerks;
    }

    /**
     * 获取职位列表
     * @return
     */
    public List<Position> getPositions() {
        boolean hasKey=redisService.exists(REDIS_POSITIONS_NAME);
        List<Position> positions=null;
        if(hasKey){
            positions= (List<Position>) redisService.get(REDIS_POSITIONS_NAME);
            log.info("从redis中获取职位列表"+positions);
        }else{
            log.info("从数据库获取职位列表");
            positions = clerkMapper.getPositions();
            log.info("将职位列表存入redis"+positions);
            redisService.set(REDIS_POSITIONS_NAME,positions);
        }
        return positions;
    }

    /**
     * 新增职员
     * @param clerk
     */
    public void saveClerk(Clerk clerk) {
        clerk.setPassword(clerk.getIdentityId().substring(clerk.getIdentityId().length()-8));
        clerkMapper.saveClerk(clerk);
        clerk.getAvatar().setClerkId(clerk.getUid());
        photoMapper.saveClerkAvatar(clerk.getAvatar());
    }

    /**
     * 失效职员
     * @param uid
     */
    public void terminalClerk(int uid) {
        clerkMapper.terminalClerk(uid);
        boolean hasKey=redisService.exists(REDIS_CLERK_NAME+":"+uid);
        if(hasKey){
            log.info("职员"+uid+"离职");
            Clerk  clerk= (Clerk) redisService.get(REDIS_CLERK_NAME+":"+uid);
            clerk.setEnforceTerminal(true);
            redisService.set(REDIS_CLERK_NAME+":"+uid,clerk);
        }
    }

    /**
     * 获取职员详情
     * @param uid
     * @return
     */
    public Clerk getClerk(int uid) {
        boolean hasKey=redisService.exists(REDIS_CLERK_NAME+":"+uid);
        Clerk clerk;
        if(hasKey){
            clerk= (Clerk) redisService.get(REDIS_CLERK_NAME+":"+uid);
            log.info("从redis获取职员信息"+clerk);
        }else{
            log.info("从数据库获取职员详情");
            clerk= clerkMapper.getClerk(uid);
            clearClerk(clerk);
            log.info("将职员详情存入redis"+clerk);
            redisService.set(REDIS_CLERK_NAME+":"+uid,clerk);
        }
        return clerk;
    }

    /**
     * 将登陆返回的职员信息内不需要的去除
     * @param clerk
     */
    public static void clearClerk(Clerk clerk){
        clerk.setPassword(null);
        if(clerk.getShop()!=null) {
            clerk.getShop().setPhotos(null);
            clerk.getShop().setShopBoxes(null);
            clerk.getShop().setClerks(null);
            clerk.getShop().setOpenHours(null);
        }
    }

    /**
     * 更新职员信息
     * @param clerk
     */
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
        boolean hasKey=redisService.exists(REDIS_CLERK_NAME+":"+clerk.getUid());
        if(hasKey){
            log.info("更新redis中职员信息");
            redisService.set(REDIS_CLERK_NAME+":"+clerk.getUid(),clerk);
        }
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
