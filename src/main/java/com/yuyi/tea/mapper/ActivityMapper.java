package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

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
    @Insert("insert into activityRule(typeId,activityRule1,activityId) values(#{activityRuleType.uid},#{activityRule1},#{activityId})")
    @Options(useGeneratedKeys=true, keyProperty="uid")
    void saveActivityRule(ActivityRule activityRule);

    @Insert("insert into activityApplyForProduct(activityRuleId,productId) values(#{activityRuleId},#{product.uid})")
//    @Options(useGeneratedKeys=true, keyProperty="uid")
    void saveActivityApplyForProduct(Product product,int activityRuleId);

    @Insert("insert into activityApplyForCustomerType(activityRuleId,customerTypeId) values(#{activityRuleId},#{customerType.uid})")
//    @Options(useGeneratedKeys=true, keyProperty="uid")
    void saveActivityApplyForCustomerType(CustomerType customerType, int activityRuleId);

    @Insert("insert into activityRule2(activityRuleId,number,currency,operation) values(#{activityRuleId},#{activityRule2.number},#{activityRule2.currency},#{activityRule2.operation})")
//    @Options(useGeneratedKeys=true, keyProperty="uid")
    void saveActivityRule2(ActivityRule2 activityRule2,int activityRuleId);

    //获取活动列表
    @Select("select * from activity")
    List<Activity> getActivities();

    //终止活动
    @Update("update activity set enforceTerminal=true where uid=#{uid}")
    void terminalActivity(int uid);

    //根据uid获取活动详细信息
    @Select("select * from activity where uid=#{uid}")
    @Results({
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column="uid",property="photos",
                    many=@Many(select="com.yuyi.tea.mapper.PhotoMapper.getPhotosByActivityId",
                            fetchType= FetchType.LAZY)),
            @Result(column="uid",property="mutexActivities",
                    many=@Many(select="com.yuyi.tea.mapper.ActivityMapper.getMutexActivities",
                            fetchType= FetchType.LAZY)),
            @Result(column="uid",property="activityRules",
                    many=@Many(select="com.yuyi.tea.mapper.ActivityMapper.getActivityRules",
                            fetchType= FetchType.LAZY))
    })
    Activity getActivity(int uid);

    //根据uid获取互斥活动
    @Select("select uid,name from activity where uid in (" +
            "select mutexActivityId from mutexActivity where activityId=#{activityId}" +
    ")")
    List<Activity> getMutexActivities(int activityId);


    //根据uid获得活动规则
    @Select("select * from activityRule where activityId=#{activityId}")
    @Results({
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column = "typeId",property = "activityRuleType",
                    one = @One(select="com.yuyi.tea.mapper.ActivityMapper.getActivityRuleType",
                            fetchType = FetchType.LAZY)),
            @Result(column = "uid",property = "activityApplyForProduct",
                    many = @Many(select="com.yuyi.tea.mapper.ActivityMapper.getActivityApplyForProduct",
                            fetchType = FetchType.LAZY)),
            @Result(column = "uid",property = "activityApplyForCustomerTypes",
                    many = @Many(select="com.yuyi.tea.mapper.ActivityMapper.getActivityApplyForCustomerTypes",
                            fetchType = FetchType.LAZY)),
            @Result(column = "uid",property = "activityRule2",
                    one = @One(select="com.yuyi.tea.mapper.ActivityMapper.getActivityRule2",
                            fetchType = FetchType.LAZY)),
    })
    List<ActivityRule> getActivityRules(int activityId);

    //根据uid获取活动类型
    @Select("select * from activityRuleType where uid=#{uid}")
    ActivityRuleType getActivityRuleType(int uid);

    //根据活动规则uid获取活动规则使用产品范围
    @Select("select uid,name from product where uid in (" +
            "select productId from activityApplyForProduct where activityRuleId=#{activityRuleId})")
    List<Product> getActivityApplyForProduct(int activityRuleId);

    //根据活动规则uid获取活动规则使用的客户类型
    @Select("select uid,name from customerType where uid in (" +
            "select customerTypeId from activityApplyForCustomerType where activityRuleId=#{activityRuleId})")
    List<CustomerType> getActivityApplyForCustomerTypes(int activityRuleId);

    //根据活动规则uid获取第二规则
    @Select("select uid,number,currency,operation from activityRule2 where activityRuleId=#{activityRuleId}")
    ActivityRule2 getActivityRule2(int activityRuleId);
}
