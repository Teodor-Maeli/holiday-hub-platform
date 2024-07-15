package com.platform.service;

import com.platform.exception.PlatformBackendException;
import com.platform.model.EmailMessageDetails;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender emailSender;

  public void send(EmailMessageDetails details) {

    try {
      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(emailSender.createMimeMessage());
      populateMessage(details, mimeMessageHelper);

      emailSender.send(mimeMessageHelper.getMimeMessage());
    } catch (MailException | MessagingException e) {
      throw new PlatformBackendException()
          .setMessage("General error while sending message")
          .setHttpStatus(INTERNAL_SERVER_ERROR).setCause(e);
    }
  }

  private void populateMessage(EmailMessageDetails details, MimeMessageHelper mimeMessageHelper) throws MessagingException {

    mimeMessageHelper.setSubject(details.getSubject());
    mimeMessageHelper.setFrom(details.getFrom());
    mimeMessageHelper.setReplyTo(details.getReplyTo());
    mimeMessageHelper.setText(details.getText(), true);
    mimeMessageHelper.setSentDate(details.getSentDate());

    mimeMessageHelper.setTo(details.getTo() == null
                            ? new String[]{}
                            : details.getTo());

    mimeMessageHelper.setCc(details.getCc() == null
                            ? new String[]{}
                            : details.getCc());

    mimeMessageHelper.setBcc(details.getBcc() == null
                             ? new String[]{}
                             : details.getBcc());
  }

}
