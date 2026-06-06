package com.ticketbooking.notification.service.mapper;

import com.ticketbooking.notification.domain.EmailNotification;
import com.ticketbooking.notification.service.dto.EmailNotificationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EmailNotification} and its DTO {@link EmailNotificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmailNotificationMapper extends EntityMapper<EmailNotificationDTO, EmailNotification> {}
