package com.ticketbooking.notification.service.mapper;

import com.ticketbooking.notification.domain.NotificationInboxEvent;
import com.ticketbooking.notification.service.dto.NotificationInboxEventDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link NotificationInboxEvent} and its DTO {@link NotificationInboxEventDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationInboxEventMapper extends EntityMapper<NotificationInboxEventDTO, NotificationInboxEvent> {}
