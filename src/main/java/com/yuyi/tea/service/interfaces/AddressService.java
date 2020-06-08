package com.yuyi.tea.service.interfaces;

import com.yuyi.tea.bean.Address;

import java.util.List;

public interface AddressService {

    /**
     * 保存收货地址
     * @param address
     */
    List<Address> saveAddress(Address address);

    /**
     * 获取地址
     * @param addressId
     * @return
     */
    Address getAddress(int addressId);

    /**
     * 更新地址
     * @param address
     * @return
     */
    List<Address> updateAddress(Address address);

    /**
     * 删除地址
     * @param addressId
     */
    void deleteAddress(int addressId);
}
