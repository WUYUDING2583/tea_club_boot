package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.Notification;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface NoticeMapper {


    /**
     * 获取客户未读通知
     * @param customerId
     * @return
     */
    @Select("select * from notification where customerId=#{customerId} and isRead=false")
    @Results(id = "notification",
            value = {
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column="customerId",property="customer",
                    many=@Many(select="com.yuyi.tea.mapper.CustomerMapper.getCustomerByUid",
                            fetchType= FetchType.LAZY))
    })
    List<Notification> getUnreadNotifications(int customerId);

    /**
     * 分页获取客户通知（20条）
     * @param customerId
     * @param offset
     * @return
     */
    @Select("select * from notification where customerId=#{customerId} order by time desc limit #{offset},20")
    @ResultMap("notification")
    List<Notification> getNotifications(int customerId, int offset);

    /**
     * 保存通知
     * @param notification
     */
    @Insert("insert into notification(isRead,title,detail,time,type,customerId) values(#{isRead},#{title},#{detail},#{time},#{type},#{customer.uid})")
    @Options(useGeneratedKeys=true, keyProperty="uid")
    void saveNotification(Notification notification);

    /**
     * 设置通知为已读
     * @param noticeId
     */
    @Update("update notification set isRead=true where uid=#{noticeId}")
    void setNotificationRead(int noticeId);

    /**
     * 清除客户所有未读通知
     * @param customerId
     */
    @Update("update notification set isRead=true where customerId=#{customerId}")
    void clearUnread(int customerId);
}
