package com.yuyi.tea.service.interfaces;

import com.yuyi.tea.bean.Address;

import java.util.List;

public interface AddressService {

    /**
     * 保存收货地址
     * @param address
     */
    List<Address> saveAddress(Address address);
}
