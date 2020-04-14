package com.yuyi.tea.controller;

import com.yuyi.tea.bean.Activity;
import com.yuyi.tea.bean.ActivityRuleType;
import com.yuyi.tea.exception.ShopNotExistException;
import com.yuyi.tea.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    /**
     * 获取所有活动的名称和描述
     * @return
     */
    @GetMapping("/admin/activitiesNameDesc")
    public List<Activity> getActivitiesNameDesc(){
        List<Activity> activitiesNameDesc = activityService.getActivitiesNameDesc();
        return activitiesNameDesc;
    }

    /**
     * 获取所有活动规则类型
     * @return
     */
    @GetMapping("/admin/activityRuleTypes")
    public List<ActivityRuleType> getActivityRuleTypes(){
        List<ActivityRuleType> activityRuleTypes = activityService.getActivityRuleTypes();
        return activityRuleTypes;
    }

    /**
     * 添加活动
     * @param activity
     * @return
     */
    @PostMapping("/admin/activity")
    @Transactional(rollbackFor = Exception.class)
    public String saveActivity(@RequestBody Activity activity){
        activityService.saveActivity(activity);
        return "success";
    }

    /**
     * 获取活动列表
     * @return
     */
    @GetMapping("/admin/activities")
    public List<Activity> getActivities(){
        List<Activity> activities = activityService.getActivities();
        return activities;
    }

    /**
     * 终止活动
     * @param uid
     * @return
     */
    @DeleteMapping("/admin/activity/{uid}")
    @Transactional(rollbackFor = Exception.class)
    public String terminalActivity(@PathVariable int uid){
        activityService.terminalActivity(uid);
        return "success";
    }

    /**
     * 根据uid获取活动详情信息
     * @param uid
     * @return
     */
    @GetMapping("/admin/activity/{uid}")
    public Activity getActivity(@PathVariable int uid){
        Activity activity = activityService.getActivity(uid);
        return activity;
    }

    /**
     * 更新活动信息
     * @param activity
     * @return
     */
    @PutMapping("/admin/activity")
    @Transactional(rollbackFor = Exception.class)
    public Activity updateActivity(@RequestBody Activity activity){
        return activityService.updateActivity(activity);
    }
}
