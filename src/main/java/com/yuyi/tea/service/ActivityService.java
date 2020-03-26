package com.yuyi.tea.service;

import com.yuyi.tea.bean.*;
import com.yuyi.tea.mapper.ActivityMapper;
import com.yuyi.tea.mapper.PhotoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@CacheConfig(cacheNames = "activity")
@Service
public class ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private PhotoMapper photoMapper;

    //获取所有活动的名称和描述
//    @Cacheable(key = "'acitvitiesNameDesc'")
    public List<Activity> getActivitiesNameDesc(){
        List<Activity> activitiesNameDesc = activityMapper.getActivitiesNameDesc();
        return activitiesNameDesc;
    }

    @Cacheable(key = "'activityRuleTypes'")
    public List<ActivityRuleType> getActivityRuleTypes() {
        List<ActivityRuleType> activityRuleTypes = activityMapper.getActivityRuleTypes();
        return activityRuleTypes;
    }

    //存储新增活动
    public void saveActivity(Activity activity) {
        activityMapper.saveActivity(activity);
        for(Photo photo:activity.getPhotos()){
            photo.setActivityId(activity.getUid());
            photoMapper.saveActivityPhoto(photo);
        }
        for(Activity mutexActivity:activity.getMutexActivities()){
            activityMapper.saveMutexActivity(activity.getUid(),mutexActivity.getUid());
            activityMapper.saveMutexActivity(mutexActivity.getUid(),activity.getUid());
        }
        //TODO 存储优惠规则
        for(ActivityRule activityRule:activity.getActivityRules()){
            activityRule.setActivityId(activity.getUid());
            saveActivityRule(activityRule);
        }
    }

    //存储活动规则
    public void saveActivityRule(ActivityRule activityRule){
        activityMapper.saveActivityRule(activityRule);
        for(Product product:activityRule.getActivityApplyForProduct()){
            product.getActivityRules().add(activityRule);
            activityMapper.saveActivityApplyForProduct(product,activityRule.getUid());
        }
        for(CustomerType customerType:activityRule.getActivityApplyForCustomerTypes()){
            customerType.getActivityRules().add(activityRule);
            activityMapper.saveActivityApplyForCustomerType(customerType,activityRule.getUid());
        }
        activityMapper.saveActivityRule2(activityRule.getActivityRule2(),activityRule.getUid());
    }

    //获取活动列表
    public List<Activity> getActivities() {
        List<Activity> activities = activityMapper.getActivities();
        return activities;
    }

    //终止活动
    public void terminalActivity(int uid) {
        activityMapper.terminalActivity(uid);
    }

    //根据uid获取活动详细信息
    public Activity getActivity(int uid) {
        Activity activity = activityMapper.getActivity(uid);
        return activity;
    }
}
