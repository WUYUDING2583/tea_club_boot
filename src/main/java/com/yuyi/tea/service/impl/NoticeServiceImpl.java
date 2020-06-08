package com.yuyi.tea.service.impl;

import com.yuyi.tea.bean.Notification;
import com.yuyi.tea.mapper.NoticeMapper;
import com.yuyi.tea.service.interfaces.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public List<Notification> getUnreadNotifications(int customerId) {
        List<Notification> notifications=noticeMapper.getUnreadNotifications(customerId);

        return notifications;
    }

    @Override
    public List<Notification> getNotifications(int customerId, int page) {
        List<Notification> notifications=noticeMapper.getNotifications(customerId,page*20);
        return notifications;
    }

    @Override
    public void clearNotifications(List<Notification> notifications) {
        for(Notification notification:notifications){
            notification.getCustomer().setPassword(null);
            notification.getCustomer().setAvatar(null);
            notification.getCustomer().setIdentityId(null);
            notification.getCustomer().setOrders(null);
            notification.getCustomer().setAddresses(null);
            notification.getCustomer().setIngot(0);
            notification.getCustomer().setCredit(0);
            notification.getCustomer().setEnterpriseCustomerApplications(null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveNotification(Notification notification) {
        noticeMapper.saveNotification(notification);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setNotificationRead(int noticeId) {
        noticeMapper.setNotificationRead(noticeId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearUnread(int customerId) {
        noticeMapper.clearUnread(customerId);
    }
}
