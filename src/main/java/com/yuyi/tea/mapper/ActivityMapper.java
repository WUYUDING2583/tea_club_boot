package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ActivityMapper {

    //获取所有活动的名称和描述
    @Select("select uid,name,description from activity")
    List<Activity> getActivitiesNameDesc();

    @Select("select * from activityRuleType")
    List<ActivityRuleType> getActivityRuleTypes();

    @Insert("insert into activity(name,description,startTime,endTime,enforceTerminal) values(#{name},#{description},#{startTime},#{endTime},#{enforceTerminal})")
    @Options(useGeneratedKeys=true, keyProperty="uid")
    void saveActivity(Activity activity);

    @Insert("insert into mutexActivity(activityId,mutexActivityId) values(#{uid},#{mutexActivityId})")
    void saveMutexActivity(int uid, int mutexActivityId);

    //存储活动规则主表
    @Insert("insert into activityRule(typeId,activityRule1) values(#{activityRuleType.uid},#{activityRule1})")
    @Options(useGeneratedKeys=true, keyProperty="uid")
    void saveActivityRule(ActivityRule activityRule);

    @Insert("insert into activityApplyForProduct(activityRuleId,productId) values(#{activityRuleId},#{product.uid})")
//    @Options(useGeneratedKeys=true, keyProperty="uid")
    void saveActivityApplyForProduct(Product product,int activityRuleId);

    @Insert("insert into activityApplyForCustomerTypes(activityRuleId,customerTypeId) values(#{activityRuleId},#{customerType.uid})")
//    @Options(useGeneratedKeys=true, keyProperty="uid")
    void saveActivityApplyForCustomerTypes(CustomerType customerType, int activityRuleId);

    @Insert("insert into activityRule2(activityRuleId,number,currency,operation) values(#{activityRuleId},#{activityRule2.number},#{activityRule2.currency},#{activityRule2.operation})")
//    @Options(useGeneratedKeys=true, keyProperty="uid")
    void saveActivityRule2(ActivityRule2 activityRule2,int activityRuleId);
}
