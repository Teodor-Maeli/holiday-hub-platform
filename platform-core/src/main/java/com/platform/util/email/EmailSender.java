package com.platform.util.email;

import com.platform.exception.PlatformBackendException;
import com.platform.model.EmailMessageDetails;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {

  private final JavaMailSender emailSender;

  public EmailSender(JavaMailSender emailSender) {
    this.emailSender = emailSender;
  }

  public void send(EmailMessageDetails details) {

    try {
      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(emailSender.createMimeMessage());
      enrichMessage(details, mimeMessageHelper);

      emailSender.send(mimeMessageHelper.getMimeMessage());
    } catch (MailException | MessagingException e) {
      throw PlatformBackendException.builder()
          .message("General error while sending message")
          .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
          .cause(e)
          .build();

    }
  }

  private void enrichMessage(EmailMessageDetails details, MimeMessageHelper mimeMessageHelper) throws MessagingException {
    mimeMessageHelper.setSubject(details.getSubject());
    mimeMessageHelper.setTo(details.getTo());
    mimeMessageHelper.setFrom(details.getFrom());
    mimeMessageHelper.setReplyTo(details.getReplyTo());
    mimeMessageHelper.setCc(details.getCc());
    mimeMessageHelper.setBcc(details.getBcc());
    mimeMessageHelper.setText(details.getText(), true);
    mimeMessageHelper.setSentDate(details.getSentDate());
  }

}
