/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.clickclick.control.data;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import org.apache.click.Context;
import org.apache.click.control.AbstractControl;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.click.util.PropertyUtils;

/**
 *
 */
public class DataControl extends AbstractControl {

    // -------------------------------------------------------------- Variables

    protected Object dataSource;

    protected String expr;

    /** The data control format pattern. */
    protected String format;

    /**
     * The optional MessageFormat used to render the column table cell value.
     */
    protected MessageFormat messageFormat;

    protected DataDecorator decorator;

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

    /** The escape HTML characters flag. The default value is true. */
    protected boolean escapeHtml = true;

    // ----------------------------------------------------------- Constructors

    public DataControl(Object dataSource, String expr, String format) {
        this.dataSource = dataSource;
        this.expr = expr;
        this.format = format;
    }

    public DataControl(String name, Object dataSource, String expr, String format) {
        super(name);
        this.dataSource = dataSource;
        this.expr = expr;
        this.format = format;
    }

    public DataControl(String name, Object dataSource, String expr) {
        super(name);
        this.dataSource = dataSource;
        this.expr = expr;
    }

    public DataControl(Object dataSource, String expr) {
        this.dataSource = dataSource;
        this.expr = expr;
    }

    public DataControl() {
    }

    // ------------------------------------------------------ Public Properties

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

    public void setFormat(String pattern) {
        this.format = pattern;
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

    public DataDecorator getDecorator() {
        return decorator;
    }

    public void setDecorator(DataDecorator decorator) {
        this.decorator = decorator;
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

    /**
     * Set the escape HTML characters when rendering column data flag.
     *
     * @param escape the flag to escape HTML characters
     */
    public void setEscapeHtml(boolean escape) {
        this.escapeHtml = escape;
    }

    @Override
    public void onDestroy() {
        setDataSource(null);
    }

    // --------------------------------------------------------- Public Methods

    @Override
    public void render(HtmlStringBuffer buffer) {
        initFormatter(getContext());

        render(buffer, getDataSource(), getExpr(), getContext());
    }

    @Override
    public String toString() {
        HtmlStringBuffer buffer = new HtmlStringBuffer();
        render(buffer);
        return buffer.toString();
    }

    // ------------------------------------------------------ Protected Methods

    protected void initFormatter(Context context) {
        if (getMessageFormat() == null && getFormat() != null) {
            Locale locale = context.getLocale();
            setMessageFormat(new MessageFormat(getFormat(), locale));
        }
    }

    protected void render(HtmlStringBuffer buffer, Object dataSource, String expr,
        Context context) {

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
            if (object != null) {
                return object;
            }

            return null;

        } else {
            return PropertyUtils.getValue(dataSource, expr);
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
