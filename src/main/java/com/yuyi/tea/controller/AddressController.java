package com.yuyi.tea.controller;

import com.yuyi.tea.bean.Address;
import com.yuyi.tea.service.interfaces.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/mp/address")
    public List<Address> saveAddress(@RequestBody Address address){
        List<Address> addresses = addressService.saveAddress(address);
        return addresses;
    }
}
