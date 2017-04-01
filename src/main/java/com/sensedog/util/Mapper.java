package com.sensedog.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class Mapper {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static Map<?, ?> toMap(Object obj) {
        return MAPPER.convertValue(obj, Map.class);
    }
}
