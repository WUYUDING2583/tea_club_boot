package com.yuyi.tea.service;

import com.yuyi.tea.bean.*;
import com.yuyi.tea.mapper.ActivityMapper;
import com.yuyi.tea.mapper.PhotoMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ActivityService {

    public static final String REDIS_ACTIVITIES_NAME="activities";
    public static final String REDIS_ACTIVITY_RULE_TYPES_NAME=REDIS_ACTIVITIES_NAME+":activityRuleTypes";
    public static final String REDIS_ACTIVITY_NAME=REDIS_ACTIVITIES_NAME+":activity";
    public static final String REDIS_ACTIVITY_RULES_NAME=REDIS_ACTIVITIES_NAME+":activityRules";
    public static final String REDIS_ACTIVITY_RULE_NAME=REDIS_ACTIVITY_RULES_NAME+":activityRule";
    public static final String REDIS_PRODUCT_ACTIVITY_RULES_NAME=REDIS_ACTIVITY_RULES_NAME+":product";

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private PhotoMapper photoMapper;

    @Autowired
    private RedisService redisService;

    /**
     * 获取所有活动的名称和描述
     * @return
     */
    public List<Activity> getActivitiesNameDesc(){
        List<Activity> activitiesNameDesc = activityMapper.getActivitiesNameDesc();
        return activitiesNameDesc;
    }

    /**
     * 获取所有活动规则类型
     * @return
     */
    public List<ActivityRuleType> getActivityRuleTypes() {
        boolean hasKey=redisService.exists(REDIS_ACTIVITY_RULE_TYPES_NAME);
        List<ActivityRuleType> activityRuleTypes;
        if(hasKey){
            activityRuleTypes= (List<ActivityRuleType>) redisService.get(REDIS_ACTIVITY_RULE_TYPES_NAME);
            log.info("从redis中获取活动规则类型列表"+activityRuleTypes);
        }else{
            log.info("从数据库中获取活动规则类型列表");
            activityRuleTypes= activityMapper.getActivityRuleTypes();
            redisService.set(REDIS_ACTIVITY_RULE_TYPES_NAME,activityRuleTypes);
            log.info("将活动规则类型列表存入redis"+activityRuleTypes);
        }
        return activityRuleTypes;
    }

    /**
     * 存储新增活动
     * @param activity
     */
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

    /**
     * 存储并返回活动规则
     * @param activityRule
     */
    public void saveActivityRule(ActivityRule activityRule){
        activityMapper.saveActivityRule(activityRule);
        for(Product product:activityRule.getActivityApplyForProduct()){
            //product.getActivityRules().add(activityRule);
            activityMapper.saveActivityApplyForProduct(product,activityRule.getUid());
        }
        for(CustomerType customerType:activityRule.getActivityApplyForCustomerTypes()){
            //customerType.getActivityRules().add(activityRule);
            activityMapper.saveActivityApplyForCustomerType(customerType,activityRule.getUid());
        }
        activityMapper.saveActivityRule2(activityRule.getActivityRule2(),activityRule.getUid());
    }

    /**
     * 获取活动列表
     * @return
     */
    public List<Activity> getActivities() {
        List<Activity> activities = activityMapper.getActivities();
        return activities;
    }

    /**
     * 终止活动
     * @param uid
     */
    public void terminalActivity(int uid) {
        activityMapper.terminalActivity(uid);
        boolean hasKey=redisService.exists(REDIS_ACTIVITY_NAME+":"+uid);
        if(hasKey){
            log.info("更新redis中活动状态");
            Activity activity= (Activity) redisService.get(REDIS_ACTIVITY_NAME+":"+uid);
            activity.setEnforceTerminal(true);
            redisService.set(REDIS_ACTIVITY_NAME+":"+uid,activity);
        }
    }

    /**
     * 根据uid获取活动详细信息
     * @param uid
     * @return
     */
    public Activity getActivity(int uid) {
        boolean hasKey=redisService.exists(REDIS_ACTIVITY_NAME+":"+uid);
        Activity activity;
        if(hasKey){
            activity= (Activity) redisService.get(REDIS_ACTIVITY_NAME+":"+uid);
            log.info("从redis中获取活动信息"+activity);
        }else{
            log.info("从数据库中获取活动信息");
            activity= activityMapper.getActivity(uid);
            log.info("将获得信息存储到redis中"+activity);
            redisService.set(REDIS_ACTIVITY_NAME+":"+uid,activity);
        }
        //获取互斥活动
        List<Activity> mutexActivities = activityMapper.getMutexActivities(uid);
        activity.setMutexActivities(mutexActivities);
        return activity;
    }

    /**
     * 更新活动信息
     * @param activity
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
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
//        boolean hasKey=redisService.exists(REDIS_ACTIVITY_NAME+":"+activity.getUid());
        activity=activityMapper.getActivity(activity.getUid());
//        if(hasKey){
//            log.info("更新redis中活动信息");
//            redisService.set(REDIS_ACTIVITY_NAME+":"+activity.getUid(),activity);
//        }
        return activity;
    }

    /**
     * 存储并返回互斥活动
     * @param activity
     * @return
     */
    public List<Activity> saveMutexActivity(Activity activity){
        for(Activity mutexActivity:activity.getMutexActivities()){
            activityMapper.saveMutexActivity(activity.getUid(),mutexActivity.getUid());
            activityMapper.saveMutexActivity(mutexActivity.getUid(),activity.getUid());
        }
        List<Activity> mutexActivities = activityMapper.getMutexActivities(activity.getUid());
        return mutexActivities;
    }

    /**
     * 获取redis中存储的activityRule
     * @param uid
     * @return
     */
    public ActivityRule getRedisActivityRule(int uid){
        ActivityRule activityRule=null;
        if(uid==0){
            return null;
        }
         boolean hasKey = redisService.exists(REDIS_ACTIVITY_RULE_NAME+":"+uid);
        if(hasKey){
            activityRule= (ActivityRule) redisService.get(REDIS_ACTIVITY_RULE_NAME+":"+uid);
            log.info("从缓存获取的数据"+ activityRule);
        }else{
            log.info("从数据库中获取数据");
            activityRule = activityMapper.getActivityRule(uid);
            redisService.set(REDIS_ACTIVITY_RULE_NAME+":"+uid,activityRule);
            log.info("数据插入缓存" + activityRule);
        }
        return activityRule;
    }


    /**
     * 清除前端不需要的activityRule的内容
     * @param activityRule
     */
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

    /**
     * 获取小程序走马灯展示的文章，活动，产品，包厢
     * @return
     */
    public List<Activity> getSwiperList() {
        List<Activity> activities=activityMapper.getSwiperList();
        return activities;
    }

    /**
     * 获取阅读活动详情
     *
     * @return
     */
    public Activity getReadingActivity() {
        Activity activity=activityMapper.getReadingActivity();
        return activity;
    }


    /**
     * 获取产品参与的活动
     * @param uid
     * @return
     */
    public List<Activity> getProductActivity(int uid) {
        List<Activity> activitiesByProduct = activityMapper.getActivitiesByProduct(uid);
        List<Activity> shoppingActivity = activityMapper.getShoppingActivity();
        for (Activity activity : shoppingActivity) {
            activitiesByProduct.add(activity);
        }
        return activitiesByProduct;
    }

    public List<ActivityRule> getActivityRuleByProduct(int productId) {
        List<ActivityRule> activityRulesByProduct = activityMapper.getActivityRulesByProduct(productId);
        return activityRulesByProduct;
    }
}
