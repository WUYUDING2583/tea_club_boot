package com.yuyi.tea.controller;

import com.yuyi.tea.bean.Address;
import com.yuyi.tea.service.interfaces.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    /**
     * 保存地址
     * @param address
     * @return
     */
    @PostMapping("/mp/address")
    public List<Address> saveAddress(@RequestBody Address address){
        List<Address> addresses=new ArrayList<>();
        if(address.getUid()==0){
            //新增地址
            addresses = addressService.saveAddress(address);
        }
        else{
            addresses=addressService.updateAddress(address);
        }
        return addresses;
    }

    /**
     * 获取地址
     * @param addressId
     * @return
     */
    @GetMapping("/mp/address/{addressId}")
    public Address getAddress(@PathVariable int addressId){
        Address address=addressService.getAddress(addressId);
        return address;
    }
}
