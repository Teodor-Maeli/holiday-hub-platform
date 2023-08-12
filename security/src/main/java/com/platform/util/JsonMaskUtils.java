package com.platform.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.Optional;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonMaskUtils {

    private static final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private static final Logger log = LoggerFactory.getLogger(JsonMaskUtils.class);
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("(?<=password\" : \")(.*?)(?=\")");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("(?<=emailAddress\" : \")(.*?)(?=\")");
    private static final Pattern PHONE_PATTERN = Pattern.compile("(?<=password\" : \")(.*?)(?=\")");
    private static final Pattern FAMILY_NAME_PATTERN = Pattern.compile("(?<=familyName\" : \")(.*?)(?=\")");
    private static final Pattern GIVEN_NAME_PATTERN = Pattern.compile("(?<=givenName\" : \")(.*?)(?=\")");
    private static final Pattern MIDDLE_NAME_PATTERN = Pattern.compile("(?<=middleName\" : \")(.*?)(?=\")");
    private static final String MASK = "*******";

    private JsonMaskUtils() {
    }

    public static String mask(Object o) {
        return Optional
            .ofNullable(toJson(o))
            .map(value -> PASSWORD_PATTERN.matcher(value).replaceAll(MASK))
            .map(value -> EMAIL_PATTERN.matcher(value).replaceAll(MASK))
            .map(value -> PHONE_PATTERN.matcher(value).replaceAll(MASK))
            .map(value -> FAMILY_NAME_PATTERN.matcher(value).replaceAll(MASK))
            .map(value -> GIVEN_NAME_PATTERN.matcher(value).replaceAll(MASK))
            .map(value -> MIDDLE_NAME_PATTERN.matcher(value).replaceAll(MASK))
            .orElse(null);
    }

    private static String toJson(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error("JsonMaskUtil failed to write as string!", e);
            return null;
        }
    }
}