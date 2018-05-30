package com.automation.engineer.utils;

/**
 * Facade for loggers 
 * 
 * @author Rasana_R
 *
 */
public class LogFacadeFactory {

	/**
	 * Singleton instance 
	 */
	private static LogFacadeFactory instance;

	
	/**
	 * Default log level
	 */
	private LogLevel logLevel = LogLevel.DEBUG;

	
	/**
	 * The private constrcutor 
	 */
	private LogFacadeFactory() {

		
	}
	
	/**
	 * Default log level 
	 * @return
	 */
	public LogLevel getLogLevel() {
		return this.logLevel;
	}

	
	/**
	 * Creates a logger facade 
	 * @return logger facade 
	 */
	public LogFacade createLogFacade() {

		StackTraceElement[] trace = Thread.currentThread().getStackTrace();

		for (int i = trace.length - 1; i > -1; --i) {

			String className = trace[i].getClassName();
			if (!className.equals(LogFacadeFactory.class.getName())) {
				return createLogFacade(className);
			}
		}

		return createLogFacade(LogFacadeFactory.class.getName());

	}

	/**
	 * Creates the logger facade for the give name
	 * 
	 * @param name the name 
	 * @return the logger facade 
	 */
	private LogFacade createLogFacade(String name) {
		return new LogFacade(name);
	}

	
	/**
	 * Creates the logger facade for the given class
	 * 
	 * @param clazz the given class
	 * @return logger facade for the given class
	 */
	public LogFacade createLogFacade(Class<?> clazz) {
		return createLogFacade(clazz.getName());
	}

	
	/**
	 * Returns the instance of the factory
	 * @return
	 */
	public static LogFacadeFactory getInstance() {
		if (instance == null) {
			instance = new LogFacadeFactory();
		}

		return instance;

	}

}

//End of class Logger Facade
