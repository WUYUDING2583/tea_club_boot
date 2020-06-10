package com.yuyi.tea.controller;

import com.yuyi.tea.bean.AuthorityFront;
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

    /**
     * 获取职员列表
     * @return
     */
    @GetMapping("/admin/clerks")
    public List<Clerk> getAllClerks(){
        List<Clerk> clerks = clerkService.getAllClerks();
        return clerks;
    }

    /**
     * 获取职位列表
     * @return
     */
    @GetMapping("/positions")
    public List<Position> getPositions(){
        List<Position> positions = clerkService.getPositions();
        return positions;
    }

    /**
     * 新增职员
     * @param clerk
     * @return
     */
    @PostMapping("/admin/clerk")
    @Transactional(rollbackFor = Exception.class)
    public Clerk saveClerk(@RequestBody Clerk clerk){
        clerkService.saveClerk(clerk);
        return clerk;
    }

    /**
     * 失效职员
     * @param uid
     * @return
     */
    @DeleteMapping("/admin/clerk/{uid}")
    @Transactional(rollbackFor = Exception.class)
    public String terminalClerk(@PathVariable int uid){
        clerkService.terminalClerk(uid);
        return "success";
    }

    /**
     * 获取职员详情
     * @param uid
     * @return
     */
    @GetMapping("/admin/clerk/{uid}")
    public Clerk getClerk(@PathVariable int uid){
        Clerk clerk = clerkService.getClerk(uid);
        return clerk;
    }

    /**
     * 更新职员信息
     * @param clerk
     * @return
     */
    @PutMapping("/admin/clerk")
    @Transactional(rollbackFor = Exception.class)
    public Clerk updateClerk(@RequestBody Clerk clerk){
        clerk = clerkService.updateClerk(clerk);
        return clerk;
    }

}
