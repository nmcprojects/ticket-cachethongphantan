package com.ticketbooking.notification.service.mapper;

import com.ticketbooking.notification.domain.EmailNotification;
import com.ticketbooking.notification.domain.EmailNotificationItem;
import com.ticketbooking.notification.service.dto.EmailNotificationDTO;
import com.ticketbooking.notification.service.dto.EmailNotificationItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EmailNotificationItem} and its DTO {@link EmailNotificationItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmailNotificationItemMapper extends EntityMapper<EmailNotificationItemDTO, EmailNotificationItem> {
    @Mapping(target = "notification", source = "notification", qualifiedByName = "emailNotificationId")
    EmailNotificationItemDTO toDto(EmailNotificationItem s);

    @Named("emailNotificationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmailNotificationDTO toDtoEmailNotificationId(EmailNotification emailNotification);
}
