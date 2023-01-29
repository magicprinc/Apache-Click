package org.apache.click.service;


import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.util.Objects;

/**
 * Provides a
 * <a target="_blank" href="http://www.slf4j.org/">SLF4J</a> LogService adapter
 * class with a logger name of "<tt>Click</tt>".
 *
 * <h3>Configuration</h3>
 * To configure the JDK LoggingService add the following element to your
 * <tt>click.xml</tt> configuration file.
 *
 * <pre class="codeConfig">
 * &lt;?xml version="1.0" encoding="UTF-8" standalone="yes"?&gt;
 * &lt;click-app charset="UTF-8"&gt;
 *
 *     &lt;pages package="org.apache.click.examples.page"/&gt;
 *
 *     &lt;<span class="red">log-service</span> classname="<span class="blue">org.apache.click.extras.service.Slf4jLogService</span>"/&gt;
 *
 * &lt;/click-app&gt; </pre>
 */
public class Slf4jLogService implements LogService {

  /** The wrapped JDK logger instance. */
  protected Logger logger = LoggerFactory.getLogger("Web.Click");

  /** The logger category name.
   * The default value is <code>Click</code> (Web.Click).
   * Setting the name after the {@link #onInit} method has been invoked will have no effect on the Slf4j loggers name.
   */
  @Getter @Setter
  protected String name = "Click";

  /** @see ServletContext#getServletContextName*/
  @Getter
  protected String servletContextName;

  /** ServletContext.contextPath {@link ServletContext#getContextPath */
  @Getter
  protected String contextPath;

  /**
   * @see LogService#onInit(javax.servlet.ServletContext)
   *
   * @param servletContext the application servlet context
   * @throws Exception if an error occurs initializing the LogService
   */
  @Override public void onInit (@NonNull ServletContext servletContext) throws Exception {
    String loggerName = getName();

    servletContextName = Objects.requireNonNullElse(servletContext.getServletContextName(), "").trim();
    if (servletContextName.length()>0) {
      loggerName = loggerName+"."+servletContextName;
    }

    contextPath = Objects.requireNonNullElse(servletContext.getContextPath(), "").trim();
    if (contextPath.length()>0) {
      loggerName = loggerName+"@"+contextPath;
    }

    logger = LoggerFactory.getLogger(loggerName);
  }

  /**
   * @see LogService#onDestroy()
   */
  @Override public void onDestroy() {}


  @Override public Logger log () {
    return logger;
  }


  /**
   * @see LogService#debug(Object)
   *
   * @param message the message to log
   */
  @Override public void debug(Object message) {
    logger.debug(String.valueOf(message));
  }


  @Override public void debug (Object message, Object... args) {
    logger.debug(String.valueOf(message), args);
  }


  /**
   * @see LogService#debug(Object, Throwable)
   *
   * @param message the message to log
   * @param error the error to log
   */
  @Override public void debug(Object message, Throwable error) {
    logger.debug(String.valueOf(message), error);
  }

  /**
   * @see LogService#error(Object)
   *
   * @param message the message to log
   */
  @Override public void error(Object message) {
    logger.error(String.valueOf(message));
  }

  /**
   * @see LogService#error(Object, Throwable)
   *
   * @param message the message to log
   * @param error the error to log
   */
  @Override public void error(Object message, Throwable error) {
    logger.error(String.valueOf(message), error);
  }

  /**
   * @see LogService#info(Object)
   *
   * @param message the message to log
   */
  @Override public void info(Object message) {
    logger.info(String.valueOf(message));
  }

  /**
   * @see LogService#info(Object, Throwable)
   *
   * @param message the message to log
   * @param error the error to log
   */
  @Override public void info(Object message, Throwable error) {
    logger.info(String.valueOf(message), error);
  }

  /**
   * @see LogService#isDebugEnabled()
   *
   * @return true if [debug] level logging is enabled
   */
  @Override public boolean isDebugEnabled() {
    return logger.isDebugEnabled();
  }

  /**
   * @see LogService#isInfoEnabled()
   *
   * @return true if [info] level logging is enabled
   */
  @Override public boolean isInfoEnabled() {
    return logger.isInfoEnabled();
  }

  /**
   * @see LogService#isTraceEnabled()
   *
   * @return true if [trace] level logging is enabled
   */
  @Override public boolean isTraceEnabled() {
    return logger.isTraceEnabled();
  }

  /**
   * @see LogService#trace(Object)
   *
   * @param message the message to log
   */
  @Override public void trace(Object message) {
    logger.trace(String.valueOf(message));
  }


  @Override public void trace (Object message, Object... args) {
    logger.trace(String.valueOf(message), args);
  }

  /**
   * @see LogService#trace(Object, Throwable)
   *
   * @param message the message to log
   * @param error the error to log
   */
  @Override public void trace(Object message, Throwable error) {
    logger.trace(String.valueOf(message), error);
  }

  /**
   * @see LogService#warn(Object)
   *
   * @param message the message to log
   */
  @Override public void warn(Object message) {
    logger.warn(String.valueOf(message));
  }

  /**
   * @see LogService#warn(Object, Throwable)
   *
   * @param message the message to log
   * @param error the error to log
   */
  @Override public void warn(Object message, Throwable error) {
    logger.warn(String.valueOf(message), error);
  }

}