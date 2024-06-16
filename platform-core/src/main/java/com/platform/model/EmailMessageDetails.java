package com.platform.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Getter
@Setter
@Accessors(fluent = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailMessageDetails {

  private String from;
  private String replyTo;
  private String[] to;
  private String[] cc;
  private String[] bcc;
  private Date sentDate;
  private String subject;
  private String text;

  public static EmailMessageDetails create() {
    return new EmailMessageDetails();
  }
}
