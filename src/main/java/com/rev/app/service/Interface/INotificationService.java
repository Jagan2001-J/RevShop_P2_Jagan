package com.rev.app.service.Interface;

import com.rev.app.entity.Notification;
import java.util.List;

public interface INotificationService {
    Notification sendNotification(Long userId, String message);

    List<Notification> getNotificationsForUser(Long userId);

    List<Notification> getUnreadNotificationsForUser(Long userId);

    void markAsRead(Long notificationId);
}
