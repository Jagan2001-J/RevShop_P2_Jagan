package com.rev.app.mapper;

import com.rev.app.dto.NotificationResponseDTO;
import com.rev.app.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationResponseDTO toDto(Notification notification) {
        if (notification == null) {
            return null;
        }

        return NotificationResponseDTO.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
