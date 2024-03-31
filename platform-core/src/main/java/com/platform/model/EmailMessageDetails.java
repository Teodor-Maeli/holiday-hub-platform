package com.platform.model;

import java.util.Date;

public class EmailMessageDetails {

  private String from;
  private String replyTo;
  private String[] to;
  private String[] cc;
  private String[] bcc;
  private Date sentDate;
  private String subject;
  private String text;

  private EmailMessageDetails() {
  }

  public static EmailMessageDetails create() {
    return new EmailMessageDetails();
  }

  public EmailMessageDetails withFrom(String from) {
    this.from = from;
    return this;
  }

  public EmailMessageDetails withReplyTo(String replyTo) {
    this.replyTo = replyTo;
    return this;
  }

  public EmailMessageDetails withTo(String to) {
    this.to = new String[]{to};
    return this;
  }

  public EmailMessageDetails withTo(String... to) {
    this.to = to;
    return this;
  }

  public EmailMessageDetails withCc(String cc) {
    this.cc = new String[]{cc};
    return this;
  }

  public EmailMessageDetails withCc(String... cc) {
    this.cc = cc;
    return this;
  }

  public EmailMessageDetails withBcc(String bcc) {
    this.bcc = new String[]{bcc};
    return this;
  }

  public EmailMessageDetails withBcc(String... bcc) {
    this.bcc = bcc;
    return this;
  }

  public EmailMessageDetails withSentDate(Date sentDate) {
    this.sentDate = sentDate;
    return this;
  }

  public EmailMessageDetails withSubject(String subject) {
    this.subject = subject;
    return this;
  }

  public EmailMessageDetails withText(String text) {
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
