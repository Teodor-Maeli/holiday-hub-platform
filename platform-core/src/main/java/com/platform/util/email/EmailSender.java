package com.platform.util.email;

import com.platform.exception.PlatformBackendException;
import com.platform.model.EmailMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {

  private static final Logger LOGGER = LoggerFactory.getLogger(EmailSender.class);

  private final JavaMailSender emailSender;
  private final EmailMessageMapper emailMessageMapper;

  public EmailSender(JavaMailSender emailSender, EmailMessageMapper emailMessageMapper) {
    this.emailSender = emailSender;
    this.emailMessageMapper = emailMessageMapper;
  }

  public void sent(EmailMessageDto dto) {

    try {
      SimpleMailMessage message = emailMessageMapper.toSimpleEmailMessage(dto);
      emailSender.send(message);
    } catch (MailException e) {
      LOGGER.error("General error while sending message", e);
      throw PlatformBackendException.builder()
          .message("General error while sending message")
          .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
          .cause(e)
          .build();

    }
  }

}
