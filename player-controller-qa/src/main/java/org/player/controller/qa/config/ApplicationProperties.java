package org.player.controller.qa.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationProperties {
    protected static final Logger LOGGER = LoggerFactory.getLogger(ApplicationProperties.class);
    private static Properties propertyNames = new Properties();
    private static String propertiesFile = System.getProperty("environment");

    static {
        propertyNames = new Properties();
        try (InputStream input = ApplicationProperties.class.getClassLoader()
                .getResourceAsStream(setDefaultPropertyFile())) {
            propertyNames.load(input);
            LOGGER.info(propertiesFile);
        } catch (IOException e) {
            LOGGER.error("Could not load properties file", e);
        } catch (NullPointerException e) {
            LOGGER.error("Could not load properties", e);
        }
    }

    public static String setDefaultPropertyFile() {
        if (propertiesFile == null || propertiesFile.isEmpty()) {
            propertiesFile = "application.properties";
        }
        return propertiesFile;
    }

    public String getProperty(String key) {
        return propertyNames.getProperty(key);
    }
}
