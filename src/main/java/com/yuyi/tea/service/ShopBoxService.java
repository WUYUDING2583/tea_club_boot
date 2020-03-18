package com.yuyi.tea.service;

import com.yuyi.tea.bean.ShopBox;
import com.yuyi.tea.mapper.ShopBoxMapper;
import com.yuyi.tea.mapper.ShopMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShopBoxService {

    @Autowired
    ShopBoxMapper shopBoxMapper;

    public void saveShopBox(ShopBox shopBox){
        shopBoxMapper.saveShopBox(shopBox);
    }
}
