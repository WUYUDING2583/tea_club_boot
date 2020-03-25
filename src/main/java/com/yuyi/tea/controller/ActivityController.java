package com.yuyi.tea.controller;

import com.yuyi.tea.bean.Activity;
import com.yuyi.tea.bean.ActivityRuleType;
import com.yuyi.tea.exception.ShopNotExistException;
import com.yuyi.tea.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    //获取所有活动的名称和描述
    @GetMapping("/activitiesNameDesc")
    public List<Activity> getActivitiesNameDesc(){
        List<Activity> activitiesNameDesc = activityService.getActivitiesNameDesc();
        return activitiesNameDesc;
    }

    //获取所有活动规则类型
    @GetMapping("/activityRuleTypes")
    public List<ActivityRuleType> getActivityRuleTypes(){
        List<ActivityRuleType> activityRuleTypes = activityService.getActivityRuleTypes();
        return activityRuleTypes;
    }

    @PostMapping("/activity")
    @Transactional(rollbackFor = Exception.class)
    public String saveActivity(@RequestBody Activity activity){
        System.out.println("save activity "+activity);
        activityService.saveActivity(activity);
        throw new ShopNotExistException();
//        return "success";
    }
}
