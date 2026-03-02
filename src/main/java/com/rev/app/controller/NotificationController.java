package com.rev.app.controller;

import com.rev.app.entity.Notification;
import com.rev.app.entity.User;
import com.rev.app.service.Interface.INotificationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/notifications")
@Slf4j
public class NotificationController {

    @Autowired
    private INotificationService notificationService;

    @GetMapping
    public String viewNotifications(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            log.warn("Unauthenticated user attempted to view notifications.");
            return "redirect:/login";
        }

        log.debug("Fetching notifications for user ID: {}", user.getId());
        List<Notification> notifications = notificationService.getNotificationsForUser(user.getId());
        model.addAttribute("notifications", notifications);

        return "buyer/notifications";
    }

    @GetMapping("/read/{id}")
    public String markAsRead(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            log.warn("Unauthenticated user attempted to mark notification {} as read.", id);
            return "redirect:/login";
        }

        try {
            log.info("User ID {} marking notification {} as read.", user.getId(), id);
            notificationService.markAsRead(id);
        } catch (Exception e) {
            log.error("Error marking notification {} as read for user {}: {}", id, user.getId(), e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error marking notification as read.");
        }

        return "redirect:/notifications";
    }
}
