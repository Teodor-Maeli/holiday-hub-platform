package com.platform.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "platform-security")
public class PlatformSecurityProperties {

  private String[] allowedPaths;
  private String unlockCompleteUrl;
  private String unlockReplyEmail;
  private Long maxConsecutiveBadCredentials;
  private Long badCredentialsExpiryTime;
  private String corsAllowedHeaders;
  private String corsAllowedOrigins;
  private String corsAllowedMethods;
  private String corsPathMappings;
  private String loginSuccessUrl;
  private String loginFailureUrl;
  private String jwtIssuer;
}
