package com.yuyi.tea.service;

import com.yuyi.tea.bean.Clerk;
import com.yuyi.tea.bean.Photo;
import com.yuyi.tea.bean.Position;
import com.yuyi.tea.bean.Shop;
import com.yuyi.tea.mapper.ClerkMapper;
import com.yuyi.tea.mapper.PhotoMapper;
import com.yuyi.tea.mapper.ShopMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClerkService {

    @Autowired
    private ClerkMapper clerkMapper;

    @Autowired
    private PhotoMapper photoMapper;

    @Autowired
    private ShopMapper shopMapper;


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
}
