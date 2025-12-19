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
            setLocalExecutionSystemProperty();
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

    /*  Function that is setting local.execution System property if you running tests NOT with 'mvn clean install' command
     *   Possible local.execution property options:
     *   - 'true' : executing tests locally
     *   - 'false' : executing tests remotely using Selenoid
     */
    public static void setLocalExecutionSystemProperty() {
        if (System.getProperty("local.execution") == null) {
            System.setProperty("local.execution", "true");
        }
    }

    public String getProperty(String key) {
        return propertyNames.getProperty(key);
    }
}
