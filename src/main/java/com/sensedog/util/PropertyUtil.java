package com.sensedog.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

public class PropertyUtil {

    public static String readSingle(final String propertyPath, final String key) {
        final String value;
        try (final InputStream in = PropertyUtil.class.getClassLoader().getResourceAsStream(propertyPath)) {
            final Properties properties = new Properties();
            properties.load(in);
            value = properties.getProperty(key);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }

        return value;
    }

    public static Properties readProperties(final String propertyPath) {
        try (final InputStream in = PropertyUtil.class.getClassLoader().getResourceAsStream(propertyPath)) {
            final Properties properties = new Properties();
            properties.load(in);
            return properties;
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
