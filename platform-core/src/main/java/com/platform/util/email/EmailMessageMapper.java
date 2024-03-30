package com.platform.util.email;

import com.platform.model.EmailMessageDto;
import org.mapstruct.Mapper;
import org.springframework.mail.SimpleMailMessage;

@Mapper(
    componentModel = "spring"
)
interface EmailMessageMapper {

  SimpleMailMessage toSimpleEmailMessage(EmailMessageDto dto);
}
