package com.automation.engineer.utils;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * 
 * @author Rasana_R
 *
 */

public class LoggingHelper {
private static Logger LOG = Logger.getLogger(LoggingHelper.class);

public static void log(String message) {
    LOG.log(LoggingHelper.class.getCanonicalName(), Level.INFO, message, null);
}

public static void logNotWorking(String message) {
    LOG.info(message);
} }