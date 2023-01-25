package net.sf.clickclick.control.data;

import lombok.Getter;
import lombok.Setter;
import org.apache.click.Context;
import org.apache.click.control.AbstractControl;
import org.apache.click.service.ConfigService;
import org.apache.click.service.PropertyService;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.HtmlStringBuffer;

import java.io.Serial;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;

/**
 *
 */
public class DataControl extends AbstractControl {
  @Serial private static final long serialVersionUID = 4755193707349840170L;

  protected Object dataSource;

  protected String expr;

  /** The data control format pattern. */
  @Setter protected String format;

  /**
   * The optional MessageFormat used to render the column table cell value.
   */
  protected MessageFormat messageFormat;

  @Getter @Setter protected DataDecorator decorator;

  /**
   * The maximum column length. If maxLength is greater than 0 and the column
   * data string length is greater than maxLength, the rendered value will be
   * truncated with an eclipse(...).
   * <p/>
   * Autolinked email or URL values will not be constrained.
   * <p/>
   * The default value is 0.
   */
  protected int maxLength;

  /** The escape HTML characters flag. The default value is true.
   Set the escape HTML characters when rendering column data flag */
  @Setter protected boolean escapeHtml = true;

  private final PropertyService propertyService;

  public DataControl(Object dataSource, String expr, String format) {
    this(null, dataSource, expr, format);
  }

  public DataControl(String name, Object dataSource, String expr, String format) {
    super(name);
    this.dataSource = dataSource;
    this.expr = expr;
    this.format = format;

    ConfigService configService = ClickUtils.getConfigService();
    propertyService = configService.getPropertyService();
  }

  public DataControl(String name, Object dataSource, String expr) {
    this(name, dataSource, expr, null);
  }

  public DataControl(Object dataSource, String expr) {
    this(null, dataSource, expr, null);
  }


  public void setDataSource(Object dataSource) {
    this.dataSource = dataSource;
  }

  public Object getDataSource() {
    return dataSource;
  }

  public String getExpr() {
    return expr;
  }

  public void setExpr(String expr) {
    this.expr = expr;
  }

  /**
   * The maximum column length. If maxLength is greater than 0 and the column
   * data string length is greater than maxLength, the rendered value will be
   * truncated with an eclipse(...).
   *
   * @return the maximum column length, or 0 if not defined
   */
  public int getMaxLength() {
    return maxLength;
  }

  /**
   * Set the maximum column length. If maxLength is greater than 0 and the
   * column data string length is greater than maxLength, the rendered value
   * will be truncated with an eclipse(...).
   *
   * @param value the maximum column length
   */
  public void setMaxLength(int value) {
    maxLength = value;
  }

  /**
   * Return the row column message format pattern.
   *
   * @return the message row column message format pattern
   */
  public String getFormat() {
    return format;
  }


  /**
   * Return the MessageFormat instance used to format the table cell value.
   *
   * @return the MessageFormat instance used to format the table cell value
   */
  public MessageFormat getMessageFormat() {
    return messageFormat;
  }

  /**
   * Set the MessageFormat instance used to format the table cell value.
   *
   * @param messageFormat the MessageFormat used to format the table cell
   *  value
   */
  public void setMessageFormat(MessageFormat messageFormat) {
    this.messageFormat = messageFormat;
  }



  /**
   * Return true if the HTML characters will be escaped when rendering the
   * column data. By default this method returns true.
   *
   * @return true if the HTML characters will be escaped when rendered
   */
  public boolean getEscapeHtml() {
    return escapeHtml;
  }


  @Override public void onDestroy() {
    setDataSource(null);
  }



  @Override public void render(HtmlStringBuffer buffer) {
    var context = Context.getThreadLocalContext();

    if (getMessageFormat() == null && getFormat() != null){
      Locale locale = context.getLocale();
      setMessageFormat(new MessageFormat(getFormat(), locale));
    }

    render(buffer, getDataSource(), getExpr(), context);
  }

  @Override public String toString() {
    HtmlStringBuffer buffer = new HtmlStringBuffer();
    render(buffer);
    return buffer.toString();
  }


  protected void render(HtmlStringBuffer buffer, Object dataSource, String expr,  Context context) {

    if (getDecorator() != null) {
      getDecorator().render(buffer, dataSource, context);

    } else {
      Object property = getProperty(dataSource, expr);
      String value = format(property);

      renderValue(buffer, value);
    }
  }

  protected void renderValue(HtmlStringBuffer buffer, String value) {

    if (value != null) {
      if (getMaxLength() > 0) {
        value = ClickUtils.limitLength(value, getMaxLength());
      }
      if (getEscapeHtml()) {
        buffer.appendEscaped(value);
      } else {
        buffer.append(value);
      }
    }
  }

  protected Object getProperty(Object dataSource, String expr) {
    if (dataSource instanceof Map) {
      Map map = (Map) dataSource;

      Object object = map.get(expr);
      if (object != null) {
        return object;
      }

      String upperCaseName = expr.toUpperCase();
      object = map.get(upperCaseName);
      if (object != null) {
        return object;
      }

      String lowerCaseName = expr.toLowerCase();
      object = map.get(lowerCaseName);
      return object;
    } else {
      return propertyService.getValue(dataSource, expr);
    }
  }

  protected String format(Object object) {
    if (object == null) {
      return null;
    }

    if (getMessageFormat() != null) {
      Object[] args = new Object[] { object };
      return getMessageFormat().format(args);
    } else {
      return object.toString();
    }
  }
}