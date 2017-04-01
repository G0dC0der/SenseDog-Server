package com.sensedog.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

public class PropertyUtil {

    public static String readSingle(String propertyPath, String key) {
        String value;
        try (InputStream in = PropertyUtil.class.getClassLoader().getResourceAsStream(propertyPath)) {
            Properties properties = new Properties();
            properties.load(in);
            value = properties.getProperty(key);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return value;
    }

    public static Properties readProperties(String propertyPath) {
        try (InputStream in = PropertyUtil.class.getClassLoader().getResourceAsStream(propertyPath)) {
            Properties properties = new Properties();
            properties.load(in);
            return properties;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
