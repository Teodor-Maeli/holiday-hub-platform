package com.platform.model;

import java.util.Date;

public class EmailMessageDto {

  private String from;
  private String replyTo;
  private String[] to;
  private String[] cc;
  private String[] bcc;
  private Date sentDate;
  private String subject;
  private String text;

  private EmailMessageDto() {
  }

  public static EmailMessageDto create() {
    return new EmailMessageDto();
  }

  public EmailMessageDto withFrom(String from) {
    this.from = from;
    return this;
  }

  public EmailMessageDto withReplyTo(String replyTo) {
    this.replyTo = replyTo;
    return this;
  }

  public EmailMessageDto withTo(String to) {
    this.to = new String[]{to};
    return this;
  }

  public EmailMessageDto withTo(String... to) {
    this.to = to;
    return this;
  }

  public EmailMessageDto withCc(String cc) {
    this.cc = new String[]{cc};
    return this;
  }

  public EmailMessageDto withCc(String... cc) {
    this.cc = cc;
    return this;
  }

  public EmailMessageDto withBcc(String bcc) {
    this.bcc = new String[]{bcc};
    return this;
  }

  public EmailMessageDto withBcc(String... bcc) {
    this.bcc = bcc;
    return this;
  }

  public EmailMessageDto withSentDate(Date sentDate) {
    this.sentDate = sentDate;
    return this;
  }

  public EmailMessageDto withSubject(String subject) {
    this.subject = subject;
    return this;
  }

  public EmailMessageDto withText(String text) {
    this.text = text;
    return this;
  }

  public String getFrom() {
    return from;
  }

  public String getReplyTo() {
    return replyTo;
  }

  public String[] getTo() {
    return to;
  }

  public String[] getCc() {
    return cc;
  }

  public String[] getBcc() {
    return bcc;
  }

  public Date getSentDate() {
    return sentDate;
  }

  public String getSubject() {
    return subject;
  }

  public String getText() {
    return text;
  }
}
