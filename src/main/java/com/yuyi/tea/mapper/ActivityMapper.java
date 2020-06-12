package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface ActivityMapper {

    /**
     * 获取所有活动的名称和描述
     * @return
     */
    @Select("select uid,name,description from activity")
    List<Activity> getActivitiesNameDesc();

    /**
     * 获取所有活动规则类型
     * @return
     */
    @Select("select * from activityRuleType")
    List<ActivityRuleType> getActivityRuleTypes();

    /**
     * 存储新增活动
     * @param activity
     */
    @Insert("insert into activity(name,description,startTime,endTime,enforceTerminal,priority,showOnHome) values(#{name},#{description},#{startTime},#{endTime},#{enforceTerminal},#{priority},#{showOnHome})")
    @Options(useGeneratedKeys=true, keyProperty="uid")
    void saveActivity(Activity activity);

    @Insert("insert into mutexActivity(activityId,mutexActivityId) values(#{uid},#{mutexActivityId})")
    void saveMutexActivity(int uid, int mutexActivityId);

    //存储活动规则主表
    @Insert("insert into activityRule(typeId,activityRule1,activityId) values(#{activityRuleType.uid},#{activityRule1},#{activityId})")
    @Options(useGeneratedKeys=true, keyProperty="uid")
    void saveActivityRule(ActivityRule activityRule);

    /**
     * 存储活动使用产品范围
     * @param product
     * @param activityRuleId
     */
    @Insert("insert into activityApplyForProduct(activityRuleId,productId) values(#{activityRuleId},#{product.uid})")
    void saveActivityApplyForProduct(Product product,int activityRuleId);

    /**
     * 存储活动使用客户类型
     * @param customerType
     * @param activityRuleId
     */
    @Insert("insert into activityApplyForCustomerType(activityRuleId,customerTypeId) values(#{activityRuleId},#{customerType.uid})")
    void saveActivityApplyForCustomerType(CustomerType customerType, int activityRuleId);

    /**
     * 存储活动规则优惠规则2
     * @param activityRule2
     * @param activityRuleId
     */
    @Insert("insert into activityRule2(activityRuleId,number,currency,operation) values(#{activityRuleId},#{activityRule2.number},#{activityRule2.currency},#{activityRule2.operation})")
    void saveActivityRule2(ActivityRule2 activityRule2,int activityRuleId);

    /**
     * 获取活动列表
     * @return
     */
    @Select("select * from activity")
    List<Activity> getActivities();

    /**
     * 终止活动
     * @param uid
     */
    @Update("update activity set enforceTerminal=true where uid=#{uid}")
    void terminalActivity(int uid);

    /**
     * 根据uid获取活动详细信息
     * @param uid
     * @return
     */
    @Select("select * from activity where uid=#{uid}")
    @Results(id="activity",
            value = {
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

    /**
     * 更新活动信息
     * @param activity
     */
    @Update("update activity set name=#{name},description=#{description},startTime=#{startTime},endTime=#{endTime},showOnHome=#{showOnHome},priority=#{priority} where uid=#{uid}")
    void updateActivity(Activity activity);

    //根据uid删除该活动所有互斥活动
    @Delete("delete from mutexActivity where activityId=#{uid} or mutexActivityId=#{uid}")
    void deleteMutexActivity(int uid);

    //根据uid删除该活动所有活动规则
    @Delete("delete from activityRule where activityId=#{uid}")
    void deleteActivityRules(int uid);

    //根据产品uid获取该产品适用的活动
    @Select("select * from activity where enforceTerminal=false and uid in (" +
            "select activityId from activityRule where uid in (" +
                "select activityRuleId from activityApplyForProduct where productId=#{productId}" +
                ")" +
            ")")
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
    List<Activity> getActivitiesByProduct(int productId);

    //根据uid获取活动规则
    @Select("select * from activityRule where uid=#{uid}")
    @ResultMap("activityRule")
    ActivityRule getActivityRule(int uid);

    //根据productId获取产品适用活动规则
    @Select("select * from activityRule where uid in (" +
            "select activityRuleId from activityApplyForProduct where productId=#{productId}" +
            ") and activityId in (" +
            "select uid from activity where enforceTerminal=false" +
            ") or typeId=2")
    @Results(
            id = "activityRule",
            value = {
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
            @Result(column = "activityId",property = "activity",
                    one = @One(select="com.yuyi.tea.mapper.ActivityMapper.getActivity",
                            fetchType = FetchType.LAZY))
    })
    List<ActivityRule> getActivityRulesByProduct(int productId);

    /**
     * 获取小程序走马灯展示的活动
     * @return
     */
    @Select("select * from activity where showOnHome=true")
    @ResultMap("activity")
    List<Activity> getSwiperList();

    /**
     * 获取阅读活动详情
     * @return
     */
    @Select("select * from activity where enforceTerminal=false and" +
            " UNIX_TIMESTAMP(NOW())*1000<endTime and UNIX_TIMESTAMP(NOW())*1000>startTime and " +
            " uid in (" +
            "select activityId from activityRule where typeId=1" +
            ") limit 1")
    @ResultMap("activity")
    Activity getReadingActivity();

    /**
     * 获取购物满减/赠活动，此活动适用于所有产品
     * @return
     */
    @Select("select * from activity where enforceTerminal=false and " +
            "UNIX_TIMESTAMP(NOW())*1000<endTime and UNIX_TIMESTAMP(NOW())*1000>startTime and " +
            "uid in (" +
            "select activityId from activityRule where typeId=2" +
            ")")
    @ResultMap("activity")
    List<Activity> getShoppingActivity();
}
