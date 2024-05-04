package org.apache.click.service;


import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serial;


/**
 * Provides a template exception class for use by the template service. This is
 * used to wrap TemplateService exceptions and provide error diagnostics
 * such as line and column where error occurred.
 */
@Getter
public class TemplateException extends Exception {
  @Serial private static final long serialVersionUID = 7843729461356696583L;

  /** The template column number where this exception occurred.
	 * -- GETTER --
	 *  Return the template column number where this exception occurred.
	 *@return the template column number where this exception occurred
	 */
  private int columnNumber;

  /** The template the line number where this exception occurred.
	 * -- GETTER --
	 *  Return the template the line number where this exception occurred.
	 *@return the template the line number where this exception occurred
	 */
  private int lineNumber;

  /** The Template name where this exception occurred.
	 * -- GETTER --
	 *  Return the Template name where this exception occurred.
	 *@return the Template name where this exception occurred
	 */
  private String templateName;


  /**
   * Create a template service exception with the given cause.
   *
   * @param cause the underlying cause of the template service error
   */
  public TemplateException(Exception cause) {
    super(cause);
  }

  /**
   * Create a template service exception with the given cause, template name,
   * line number and column number.
   *
   * @param cause the underlying cause of the template service error
   * @param templateName the name of the template which cause the error
   * @param lineNumber the template error line number
   * @param columnNumber the template error column number
   */
  public TemplateException(Exception cause, String templateName, int lineNumber, int columnNumber) {
    super(cause);
    this.templateName = templateName;
    this.lineNumber = lineNumber;
    this.columnNumber = columnNumber;
  }

  // Public Methods ------------------------------------------------------

	/**
   * Return true if a template parsing error.
   *
   * @return true if a template parsing error
   */
  public boolean isParseError() {
    return StringUtils.isNotBlank(getTemplateName());
  }

  /**
   * Prints the cause and its backtrace to the specified print stream.
   *
   * @param printStream the printStream to print the stack trace to
   */
  @Override
  public void printStackTrace(PrintStream printStream) {
    if (getCause() == this){
      super.printStackTrace(printStream);
    } else {
      getCause().printStackTrace(printStream);
    }
  }

  /**
   * Prints the cause and its backtrace to the specified print writer.
   *
   * @param printWriter the printWriter to writer the stack trace to
   */
  @Override
  public void printStackTrace(PrintWriter printWriter) {
    if (getCause() == this) {
      super.printStackTrace(printWriter);
    } else {
      getCause().printStackTrace(printWriter);
    }
  }

  /**
   * Fills in the execution stack trace. This method records within this
   * <code>Throwable</code> object information about the current state of
   * the stack frames for the current thread.
   *
   * @return a reference to this Throwable instance
   */
  @Override
  public synchronized Throwable fillInStackTrace() {
    if (getCause() == this) {
      return super.fillInStackTrace();
    }
    return this;
  }

  /**
   * @see Object#toString()
   *
   * @return the string representation of this error
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder(255);
    builder.append(getClass().getName());
    builder.append(": ");
    if (getLocalizedMessage() != null) {
      builder.append(getLocalizedMessage());
    }

    if (getTemplateName() != null) {
      builder.append(" [templateName=");
      builder.append(getTemplateName());
      builder.append(",lineNumber=");
      builder.append(getLineNumber());
      builder.append(",columnNumber=");
      builder.append(getColumnNumber());
      builder.append("]");
    }

    return builder.toString();
  }
}