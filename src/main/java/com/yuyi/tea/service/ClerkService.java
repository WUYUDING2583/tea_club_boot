package com.yuyi.tea.service;

import com.yuyi.tea.bean.Clerk;
import com.yuyi.tea.mapper.ClerkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClerkService {

    @Autowired
    ClerkMapper clerkMapper;

    public List<Clerk> getAllClerks(){
        List<Clerk> clerks = clerkMapper.getAllClerks();
        return clerks;
    }
}
