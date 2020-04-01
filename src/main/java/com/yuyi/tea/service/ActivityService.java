package com.yuyi.tea.service;

import com.yuyi.tea.bean.*;
import com.yuyi.tea.mapper.ActivityMapper;
import com.yuyi.tea.mapper.PhotoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CacheConfig(cacheNames = "activity")
@Service
public class ActivityService {

    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private PhotoMapper photoMapper;

    @Autowired
    private RedisService redisService;

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
        saveMutexActivity(activity);
        // 存储优惠规则
        for(ActivityRule activityRule:activity.getActivityRules()){
            activityRule.setActivityId(activity.getUid());
            saveActivityRule(activityRule);
        }
    }

    //存储并返回活动规则
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

    public Activity updateActivity(Activity activity) {
        activityMapper.updateActivity(activity);
        //更新照片
        ArrayList<Integer> photosUid=new ArrayList<>();
        for(Photo photo:activity.getPhotos()){
            photosUid.add(photo.getUid());
            photo.setActivityId(activity.getUid());
            photoMapper.saveActivityPhoto(photo);
        }
        List<Photo> originPhotos=photoMapper.getPhotosByActivityId(activity.getUid());
        List<Photo> needDeletePhotos=originPhotos.stream()
                .filter((Photo photo)->!photosUid.contains(photo.getUid()))
                .collect(Collectors.toList());
        //删除这些照片
        for(Photo photo:needDeletePhotos){
            photoMapper.deletePhoto(photo.getUid());
        }
        List<Photo> updatePhotos=originPhotos.stream()
                .filter((Photo photo)->photosUid.contains(photo.getUid()))
                .collect(Collectors.toList());
        activity.setPhotos(updatePhotos);
        //更新互斥活动
        activityMapper.deleteMutexActivity(activity.getUid());
        List<Activity> mutexActivities = saveMutexActivity(activity);
        activity.setMutexActivities(mutexActivities);
        //更新活动规则
        //删除该活动当前所有规则
        activityMapper.deleteActivityRules(activity.getUid());
        //保存所有规则
        for(ActivityRule activityRule:activity.getActivityRules()) {
            activityRule.setActivityId(activity.getUid());
            saveActivityRule(activityRule);
        }
        List<ActivityRule> activityRules = activityMapper.getActivityRules(activity.getUid());
        activity.setActivityRules(activityRules);
        return activity;
    }

    //插入并返回互斥活动
    public List<Activity> saveMutexActivity(Activity activity){
        for(Activity mutexActivity:activity.getMutexActivities()){
            activityMapper.saveMutexActivity(activity.getUid(),mutexActivity.getUid());
            activityMapper.saveMutexActivity(mutexActivity.getUid(),activity.getUid());
        }
        List<Activity> mutexActivities = activityMapper.getMutexActivities(activity.getUid());
        return mutexActivities;
    }

    //获取redis中存储的activityRule
    public ActivityRule getRedisActivityRule(int uid){
        ActivityRule activityRule=null;
        if(uid==0){
            return null;
        }
         boolean hasKey = redisService.exists("activityRules:activityRule:"+uid);
        if(hasKey){
            //获取缓存
            activityRule= (ActivityRule) redisService.get("activityRules:activityRule:"+uid);
            log.info("从缓存获取的数据"+ activityRule);
        }else{
            //从数据库中获取信息
            log.info("从数据库中获取数据");
            activityRule = activityMapper.getActivityRule(uid);
            //数据插入缓存（set中的参数含义：key值，user对象，缓存存在时间10（long类型），时间单位）
            redisService.set("activityRules:activityRule:"+uid,activityRule);
            log.info("数据插入缓存" + activityRule);
        }
        return activityRule;
    }


    //清除前端不需要的activityRule的内容
    public static void clearActivityRule(ActivityRule activityRule){
        if(activityRule!=null) {
            activityRule.setActivityApplyForProduct(null);
            activityRule.getActivity().setPhotos(null);
            activityRule.getActivity().setActivityRules(null);
            activityRule.getActivity().setMutexActivities(null);
        }
    }

    //清除前端不需要的activityRules中的内容
    public static void clearActivityRules(List<ActivityRule> activityRules){
        for(ActivityRule activityRule:activityRules){
            ActivityService.clearActivityRule(activityRule);
        }
    }

    //清除前端不需要的activities中的内容
    public static void clearActivities(List<Activity> activities) {
        for(Activity activity:activities){
            clearActivity(activity);
        }
    }

    //清除前端不需要的activity中的内容
    public static void clearActivity(Activity activity){
        activity.setPhotos(null);
        activity.setMutexActivities(null);
        activity.setActivityRules(null);
    }
}
