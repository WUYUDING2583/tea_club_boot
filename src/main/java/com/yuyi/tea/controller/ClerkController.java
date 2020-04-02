package com.yuyi.tea.controller;

import com.yuyi.tea.bean.Clerk;
import com.yuyi.tea.bean.Position;
import com.yuyi.tea.service.ClerkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/positions")
//    @Cacheable(cacheNames = "position")
    public List<Position> getPositions(){
        List<Position> positions = clerkService.getPositions();
        return positions;
    }

    @PostMapping("/clerk")
    @Transactional(rollbackFor = Exception.class)
    public Clerk saveClerk(@RequestBody Clerk clerk){
        clerkService.saveClerk(clerk);
        return clerk;
    }

    @DeleteMapping("/clerk/{uid}")
    @Transactional(rollbackFor = Exception.class)
    public String deleteClerk(@PathVariable int uid){
        clerkService.deleteClerk(uid);
        return "success";
    }

    @GetMapping("/clerk/{uid}")
    public Clerk getClerk(@PathVariable int uid){
        Clerk clerk = clerkService.getClerk(uid);
        return clerk;
    }

    @PutMapping("/clerk")
    @Transactional(rollbackFor = Exception.class)
    public Clerk updateClerk(@RequestBody Clerk clerk){
        clerkService.updateClerk(clerk);
        return clerk;
    }

}
