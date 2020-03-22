package com.yuyi.tea.controller;

import com.yuyi.tea.bean.Clerk;
import com.yuyi.tea.service.ClerkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ClerkController {

    @Autowired
    ClerkService clerkService;

    @GetMapping("/clerks")
    public List<Clerk> getAllClerks(){
        List<Clerk> clerks = clerkService.getAllClerks();
        return clerks;
    }
}
