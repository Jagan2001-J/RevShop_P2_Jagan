package com.rev.app.service.Impl;

import com.rev.app.entity.Notification;
import com.rev.app.entity.User;
import com.rev.app.repository.INotificationRepository;
import com.rev.app.repository.IUserRepository;
import com.rev.app.service.Interface.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements INotificationService {

    @Autowired
    private INotificationRepository notificationRepo;
    @Autowired
    private IUserRepository userRepo;

    @Override
    public Notification sendNotification(Long userId, String message) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setIsRead(false);
        return notificationRepo.save(notification);
    }

    @Override
    public List<Notification> getNotificationsForUser(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return notificationRepo.findByUserOrderByCreatedAtDesc(user);
    }

    @Override
    public List<Notification> getUnreadNotificationsForUser(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return notificationRepo.findByUserAndIsReadFalse(user);
    }

    @Override
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepo.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setIsRead(true);
        notificationRepo.save(notification);
    }
}
