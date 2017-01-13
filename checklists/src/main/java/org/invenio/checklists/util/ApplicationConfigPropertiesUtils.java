/*
 * Copyright (c) 2014-2017 Eleven Systems, LLC. All Rights Reserved.
 */
package org.invenio.checklists.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Reads the application-config.properties file.
 * 
 * @author avillalobos
 */
@Component
public class ApplicationConfigPropertiesUtils {

    /** Logger instance. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationConfigPropertiesUtils.class);

    private Properties properties;
    private static ApplicationConfigPropertiesUtils instance;
    
    {
        if (properties == null) {
            try (InputStream inputStream = this.getClass().getResourceAsStream("/application-config.properties")) {
                properties = new Properties();
                properties.load(inputStream);
            } catch (IOException e) {
                LOGGER.error("Unable to load application-config.properties", e);

                // Nothing works if we can't load the configuration properties.
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        // Making sure we can call the class outside of Spring
        instance = this;
    }
    
    public ApplicationConfigPropertiesUtils() {
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }
    
    public int getIntProperty(String key) {
        return Integer.parseInt(getProperty(key));
    }
    
    public static ApplicationConfigPropertiesUtils getInstance() {
        return instance;
    }

    public Float getFloatProperty(String key) {
        return Float.valueOf(getProperty(key));
    }
}
