package com.yuyi.tea.service.interfaces;

import com.yuyi.tea.bean.Notification;

import java.util.List;

public interface NoticeService {


    /**
     * 获取客户未读通知
     * @param customerId
     * @return
     */
    List<Notification> getUnreadNotifications(int customerId);

    /**
     * 分页获取客户通知（20条）
     * @param customerId
     * @param page
     * @return
     */
    List<Notification> getNotifications(int customerId, int page);

    void clearNotifications(List<Notification> notifications);

    /**
     * 保存通知
     * @param notification
     */
    void saveNotification(Notification notification);
}
