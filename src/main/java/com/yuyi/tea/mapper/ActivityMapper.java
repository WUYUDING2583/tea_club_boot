package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.Activity;
import com.yuyi.tea.bean.ActivityRuleType;
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

    @Insert("insert into activity(name,description,startTime,endTime,enforceTerminal) values(#{name},#{description},#{startTimer},#{endTime},#{enforceTerminal})")
    @Options(useGeneratedKeys=true, keyProperty="uid")
    void saveActivity(Activity activity);

    @Insert("insert into mutexActivity(activityId,mutexActivityId) values(#{uid},#{mutexActivityId})")
    void saveMutexActivity(int uid, int mutexActivityUid);
}
