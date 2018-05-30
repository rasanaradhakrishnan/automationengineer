package com.automation.engineer.utils;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;

import org.apache.log4j.Logger;


/**
 * 
 * @author Rasana_R
 *
 */
public class LogFacade {

	String log4jConfPath = "libs/log4j.properties";
	private String tagName = null;
	 static final String LOG_PROPERTIES_FILE = "libs/log4j.properties";
	
	final static Logger logger = Logger.getRootLogger();
	
	/**
	 * 
	 */
	private LogLevel logLevel;
	
	
	
	
	/**
	 * 
	 * @param name
	 */
	public LogFacade(String name) {
		this.tagName = name;
		logLevel = LogFacadeFactory.getInstance().getLogLevel();
		//initializeLogger();
	}
	
	/**
	 * debug 
	 * 
	 * @param message
	 * @param arguments
	 */
	public void debug(String message, Object... arguments) {
	
		if (logLevel.ordinal() <= LogLevel.DEBUG.ordinal()) {
			log(LogLevel.DEBUG, message, arguments);
		}
	}
	
	/**
	 * Log info 
	 * 
	 * @param message the message
	 * @param arguments
	 */
	public void info(String message, Object... arguments) {
		if (logLevel.ordinal() <= LogLevel.INFO.ordinal()) {
			log(LogLevel.DEBUG, message, arguments);
		}
		
	}

	/**
	 *  Warning 
	 *  
	 * @param message the message
	 * @param arguments and arguments 
	 */
	public void warning(String message, Object... arguments) {
		if (logLevel.ordinal() <= LogLevel.WARN.ordinal()) {
			log(LogLevel.DEBUG, message, arguments);
		}
		
	}
	
	
	/**
	 * Error 
	 * 
	 * @param message
	 * @param arguments
	 */
	public void error(String message, Object... arguments) {
		if (logLevel.ordinal() <= LogLevel.ERROR.ordinal()) {
			log(LogLevel.DEBUG, message, arguments);
		}
	}
	
	public void error(Throwable e) {
		if (logLevel.ordinal() <= LogLevel.ERROR.ordinal()) {
			
			StackTraceElement[] stackTrace = e.getStackTrace();
			
			StringBuilder builder = new StringBuilder();
			builder
				.append(e.getMessage())
				.append("\n").append(e.getClass().getName()).append(":").append(e.getLocalizedMessage());
			
			for (StackTraceElement traceElement : stackTrace) {
				builder.append("\n at ")
					.append(traceElement.getClassName())
					.append(".")
					.append(traceElement.getMethodName())
					.append("(")
					.append(traceElement.getFileName())
					.append(":")
					.append(traceElement.getLineNumber())
					.append(")");
			}
			
			log(LogLevel.CUSTOM, builder.toString(), new Object[] {});
		}
	}
	
	
	/**
	 * 
	 * @param debug
	 * @param message
	 * @param arguments
	 */
	private void log(LogLevel logLevel, String message, Object[] arguments) {
		
		String formattedMessage = "";
		
		if (arguments != null) {
			formattedMessage = MessageFormat.format(message, arguments);
		} else {
			formattedMessage = MessageFormat.format(message, new Object[] {});
		}
		
		
		
		if(LogLevel.CUSTOM.equals(logLevel)){
	
		}else if (LogLevel.DEBUG.equals(logLevel)) {
			System.out.println(tagName+" :: " + formattedMessage);
			//logger.debug(tagName+" :: " + formattedMessage);
		} else if (LogLevel.INFO.equals(logLevel)) {
			//logger.info(tagName+" :: " + formattedMessage);
			System.out.println(tagName+" :: " + formattedMessage);
		} else if (LogLevel.WARN.equals(logLevel)) {
			System.out.println(tagName+" :: " + formattedMessage);
			//logger.warn(tagName+" :: " + formattedMessage);
		} else if (LogLevel.ERROR.equals(logLevel)) {
			System.out.println(tagName+" :: " + formattedMessage);
			//logger.error(tagName+" :: " + formattedMessage);
		}
	}
	
	/**
	 * Print  
	 * 
	 * @param message
	 * @param arguments
	 */
	public void customLog(String message, boolean isPostResponse , Object... arguments) {
		if ((logLevel.ordinal() <= LogLevel.CUSTOM.ordinal()) && isPostResponse ) {
			boolean print=true;
			if (arguments != null) {
				String msg = MessageFormat.format("", arguments);
				String temp[]=msg.split(",");
				if(temp.length!=0){
					if(temp[0].equals("{\"type\":\"data\"")){
						print=false;
					}
				}
			} 
			
			if(print){
				log(LogLevel.CUSTOM, message, arguments);
			}
			
		}
	}

	/**
	 * This will save the log messages to a log file
	 * 
	 * @param logMessage
	 *            - Text to save
	 */
/*	public static void writeLog(Context appContext, String logMessage) {
		String logFilePath = createAndGetLogFilePath(appContext);
        
		logMessage = CommonUtils.getTimeStamp()+logMessage;
		
		if (null != logFilePath && !logFilePath.isEmpty()) {
			File fLog = new File(logFilePath);
			long fileSizeInBytes = fLog.length();
             
			if (fileSizeInBytes == 0) {
				appendMsgToLogFile(logFilePath, logMessage);
			} else {
				byte[] msgSizeByteArray = logMessage.getBytes();
				long msgSizeInBytes = msgSizeByteArray.length;
				int totalSizeInBytes = (int) (fileSizeInBytes + msgSizeInBytes);

				if (totalSizeInBytes <= ((1024 * 1024 ) * 10)) {
					appendMsgToLogFile(logFilePath, logMessage);
				} else {
					int deleteByteSize = totalSizeInBytes - ((1024 * 1024 ) * 10);
					if (deleteByteSize > 0) {
						deleteDataFromLogfile(deleteByteSize, totalSizeInBytes,
								logFilePath);
					}
					appendMsgToLogFile(logFilePath, logMessage);
				}
			}
		}
	}*/

	/**
	 * Creates a folder for saving the log file and returns its path
	 * 
	 * @param environmentType
	 * @return
	 */
/*	public static String createAndGetLogFilePath(Context appContext) {

		String logFilePath = null;

		final File fileOutput = CommonUtils.getExternalFilePath(appContext,
				LOG_PATH);

		// get the output file
		String fileName = "Logger";

		File logFile = new File(fileOutput.getPath() + "/" + fileName + ".txt");

		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (null != logFile && logFile.exists()) {
			logFilePath = logFile.getPath();
		}

		return logFilePath;
	}
*/
	public static void appendMsgToLogFile(String logFilePath, String logMessage) {
		// Write the log message to the log file
		BufferedWriter bufLogWriter;
		try {
			bufLogWriter = new BufferedWriter(new FileWriter(logFilePath, true));
			bufLogWriter.write(logMessage + "\r\n");
			bufLogWriter.newLine();
			bufLogWriter.flush();
			bufLogWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void deleteDataFromLogfile(int deleteByteSize,
			int totalByteSize, String logFilePath) {

		try {
			FileInputStream fisLog = new FileInputStream(logFilePath);
			ByteArrayOutputStream bosLog = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			fisLog.skip(deleteByteSize);
			for (int readNum; (readNum = fisLog.read(buffer)) != -1;) {
				bosLog.write(buffer, 0, readNum);
			}
			fisLog.close();
			byte[] logBytes = null;
			if (null != bosLog) {
				logBytes = bosLog.toByteArray();
			}

			if (null != logBytes) {
				// Clear the log file
				FileOutputStream fosLog = new FileOutputStream(logFilePath);
				fosLog.close();

				final File logFile = new File(logFilePath);
				final OutputStream logFileOS = new FileOutputStream(logFile);
				logFileOS.write(logBytes);
				logFileOS.flush();
				logFileOS.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*private void initializeLogger()
	  {
	    Properties logProperties = new Properties();
	 
	    try
	    {
	      // load our log4j properties / configuration file
	      logProperties.load(new FileInputStream(LOG_PROPERTIES_FILE));
	      PropertyConfigurator.configure(log4jConfPath);
	    }
	    catch(IOException e)
	    {
	      throw new RuntimeException("Unable to load logging property " + LOG_PROPERTIES_FILE);
	    }
	  }*/

}