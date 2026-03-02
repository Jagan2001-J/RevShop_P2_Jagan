package com.rev.app.service.Impl;

import com.rev.app.entity.Notification;
import com.rev.app.entity.User;
import com.rev.app.repository.INotificationRepository;
import com.rev.app.repository.IUserRepository;
import com.rev.app.service.Interface.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationServiceImpl implements INotificationService {

    @Autowired
    private INotificationRepository notificationRepo;
    @Autowired
    private IUserRepository userRepo;

    @Override
    public Notification sendNotification(Long userId, String message) {
        log.info("Sending notification to user ID {}: {}", userId, message);
        User user = userRepo.findById(userId).orElseThrow(() -> {
            log.warn("Failed to send notification. User ID {} not found.", userId);
            return new RuntimeException("User not found");
        });
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setIsRead(false);
        return notificationRepo.save(notification);
    }

    @Override
    public List<Notification> getNotificationsForUser(Long userId) {
        log.debug("Fetching all notifications for user ID: {}", userId);
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return notificationRepo.findByUserOrderByCreatedAtDesc(user);
    }

    @Override
    public List<Notification> getUnreadNotificationsForUser(Long userId) {
        log.debug("Fetching unread notifications for user ID: {}", userId);
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return notificationRepo.findByUserAndIsReadFalse(user);
    }

    @Override
    public void markAsRead(Long notificationId) {
        log.info("Marking notification ID {} as read.", notificationId);
        Notification notification = notificationRepo.findById(notificationId)
                .orElseThrow(() -> {
                    log.error("Failed to mark as read. Notification ID {} not found.", notificationId);
                    return new RuntimeException("Notification not found");
                });
        notification.setIsRead(true);
        notificationRepo.save(notification);
    }
}
