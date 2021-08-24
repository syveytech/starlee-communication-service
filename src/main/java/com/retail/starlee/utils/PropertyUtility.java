package com.retail.starlee.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertyUtility {
    private static Logger logger = LoggerFactory.getLogger(PropertyUtility.class.getName());

    public static final String propertyPath = "/appserver/starlee/config/configure.properties";
    private static Properties properties;

	static {
		try {
			load();
		} catch (Exception e) {
			logger.error("Exception : ",e);
		}
	}

    public static void load() {
        try {
            properties = new Properties();
            properties.load(new FileInputStream(propertyPath));
        } catch (Exception e) {
			logger.error("Exception : ",e);
            return;
        }
        return;
    }

    public static Properties getProperties() {
        try {
            if (properties == null) {
                load();
            }
        } catch (Exception e) {
			logger.error("Exception : ",e);
        }
        return properties;
    }
}
