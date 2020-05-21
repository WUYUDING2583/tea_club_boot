package com.yuyi.tea.service.impl;

import com.yuyi.tea.bean.Address;
import com.yuyi.tea.mapper.AddressMapper;
import com.yuyi.tea.service.interfaces.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public List<Address> saveAddress(Address address) {
        if(address.getIsDefaultAddress()){
            //判断当前客户是否有默认地址
            Address defaultAddress=addressMapper.getDefaultAddress(address.getCustomer().getUid());
            if(defaultAddress!=null){
                //将其设置未非默认地址
                addressMapper.setUnDefaultAddress(defaultAddress.getUid());
            }
        }
        addressMapper.saveAddress(address);
        List<Address> addresses=addressMapper.getAddresses(address.getCustomer().getUid());
        return addresses;
    }
}
