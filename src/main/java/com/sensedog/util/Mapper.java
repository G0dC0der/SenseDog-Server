package com.sensedog.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class Mapper {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    }

    public static Map<?, ?> toMap(final Object obj) {
        return MAPPER.convertValue(obj, Map.class);
    }

    public static String stringify(final Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
