package com.platform.util.email;

import com.platform.model.EmailMessageDetails;
import org.mapstruct.Mapper;
import org.springframework.mail.SimpleMailMessage;

@Mapper(
    componentModel = "spring"
)
interface EmailMessageMapper {

  SimpleMailMessage toSimpleEmailMessage(EmailMessageDetails dto);
}
