package com.yuyi.tea.service;

import com.yuyi.tea.bean.Activity;
import com.yuyi.tea.bean.ActivityRuleType;
import com.yuyi.tea.bean.Photo;
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
    }
}
