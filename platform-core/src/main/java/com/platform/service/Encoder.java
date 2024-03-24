package com.platform.service;

/**
 * Encodes a content, encoding technique may differ in the implementations of this interface.
 */
public interface Encoder {

  String encode(String raw);

  boolean matches(String raw, String hashed);

}
