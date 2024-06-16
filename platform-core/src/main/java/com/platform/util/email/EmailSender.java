package com.platform.util.email;

import com.platform.exception.PlatformBackendException;
import com.platform.model.EmailMessageDetails;
import jakarta.mail.MessagingException;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

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
      throw new PlatformBackendException().setMessage("General error while sending message").setHttpStatus(INTERNAL_SERVER_ERROR).setCause(e);
    }
  }

  private void enrichMessage(EmailMessageDetails details, MimeMessageHelper mimeMessageHelper) throws MessagingException {

    mimeMessageHelper.setSubject(details.getSubject());
    mimeMessageHelper.setFrom(details.getFrom());
    mimeMessageHelper.setReplyTo(details.getReplyTo());
    mimeMessageHelper.setText(details.getText(), true);
    mimeMessageHelper.setSentDate(details.getSentDate());

    mimeMessageHelper.setTo(details.getTo() == null
                            ? new String[]{}
                            : details.getCc());

    mimeMessageHelper.setCc(details.getCc() == null
                            ? new String[]{}
                            : details.getCc());

    mimeMessageHelper.setBcc(details.getBcc() == null
                             ? new String[]{}
                             : details.getBcc());
  }

}
