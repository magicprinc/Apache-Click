/*
 * Copyright 2004-2005 Malcolm A. Edgar
 *
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
package net.sf.click.control;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Locale;

import net.sf.click.Context;
import net.sf.click.util.ClickUtils;

import org.apache.commons.lang.StringUtils;

/**
 * Provides the Column table data &lt;td&gt; and table header &lt;th&gt;
 * renderer.
 * <p/>
 * <b>PLEASE NOTE</b>: the Column class is undergoing preliminary development
 * and is subject to significant change
 *
 * @see Decorator
 * @see Table
 *
 * @author Malcolm Edgar
 * @version $Id$
 */
public class Column {

    // ----------------------------------------------------- Instance Variables

    /** The column table data &lt;td&gt; CSS class attribute. */
    protected String dataClass;

    /** The column table data &lt;td&gt; CSS style attribute. */
    protected String dataStyle;

    /** The column row decorator. */
    protected Decorator decorator;

    /** The column message format pattern. */
    protected String format;

    /** The CSS class attribute of the column header. */
    protected String headerClass;

    /** The CSS style attribute of the column header. */
    protected String headerStyle;

    /** The title of the column header. */
    protected String headerTitle;

    protected MessageFormat messageFormat;

    /** The property name of the row object to render. */
    protected String name;

    /** The cached column property method. */
    protected Method propertyMethod;

    // ----------------------------------------------------------- Constructors

    /**
     * Create a table column with the given property name. The table header
     * title will be set as the capitalized property name.
     *
     * @param name the name of the property to render
     */
    public Column(String name) {
        this.name = name;
        this.headerTitle = StringUtils.capitalize(name);
    }

    // ------------------------------------------------------ Public Properties

    /**
     * Return the table data &lt;td&gt; CSS class.
     *
     * @return the table data CSS class
     */
    public String getDataClass() {
        return dataClass;
    }

    /**
     * Set the table data &lt;td&gt; CSS class
     *
     * @param dataClass the table data CSS class
     */
    public void setDataClass(String dataClass) {
        this.dataClass = dataClass;
    }

    /**
     * Return the table data &lt;td&gt; CSS style.
     *
     * @return the table data CSS style
     */
    public String getDataStyle() {
        return dataStyle;
    }

    /**
     * Set the table data &lt;td&gt; CSS style
     *
     * @param style the table data CSS style
     */
    public void setDataStyle(String style) {
        dataStyle = style;
    }

    /**
     * Return the row column &lt;td&gt; decorator.
     *
     * @return the row column &lt;td&gt; decorator
     */
    public Decorator getDecorator() {
        return decorator;
    }

    /**
     * Set the row column &lt;td&gt; decorator.
     *
     * @param decorator the row column &lt;td&gt; decorator
     */
    public void setDecorator(Decorator decorator) {
        this.decorator = decorator;
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
     * Set the row column message format pattern.
     *
     * @param pattern the message format pattern
     */
    public void setFormat(String pattern) {
        this.format = pattern;
    }

    /**
     * Return the MessageFormat instance used to format the row object value.
     *
     * @return the MessageFormat instance used to format the row object value
     */
    public MessageFormat getMessageFormat() {
        return messageFormat;
    }

    /**
     * Set the MessageFormat instance used to format the row object value.
     *
     * @param messageFormat the MessageFormat used to format the row object
     * value
     */
    public void setMessageFormat(MessageFormat messageFormat) {
        this.messageFormat = messageFormat;
    }

    /**
     * Return the property name.
     *
     * @return the name of the property
     */
    public String getName() {
        return name;
    }

    /**
     * Set the property name.
     *
     * @param name the property name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return the table header &lt;th&gt; CSS class.
     *
     * @return the table header CSS class
     */
    public String getHeaderClass() {
        return headerClass;
    }

    /**
     * Set the table header &lt;th&gt; CSS class
     *
     * @param headerClass the table header CSS class
     */
    public void setHeaderClass(String headerClass) {
        this.headerClass = headerClass;
    }

    /**
     * Return the table header &lt;th&gt; CSS style.
     *
     * @return the table header CSS style
     */
    public String getHeaderStyle() {
        return headerStyle;
    }

    /**
     * Set the table header &lt;th&gt; CSS style
     *
     * @param style the table header CSS style
     */
    public void setHeaderStyle(String style) {
        headerStyle = style;
    }

    /**
     * Return the table header &lt;th&gt; title.
     *
     * @return the table header title
     */
    public String getHeaderTitle() {
        return headerTitle;
    }

    /**
     * Set the table header &lt;th&gt; title
     *
     * @param title the table header title
     */
    public void setHeaderTitle(String title) {
        headerTitle = title;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Render the column table data &lt;td&gt; element to the given buffer using
     * the passed row object.
     *
     * @param row the row object to render
     * @param buffer the string buffer to render to
     * @param context the request context
     */
    public void renderTableData(Object row, StringBuffer buffer,
            Context context) {

        if (getMessageFormat() == null && getFormat() != null) {
            Locale locale = context.getRequest().getLocale();
            setMessageFormat(new MessageFormat(getFormat(), locale));
        }

        buffer.append("<td");
        if (getDataClass() != null) {
            buffer.append(" class='");
            buffer.append(getDataClass());
            buffer.append("'");
        }
        if (getDataStyle() != null) {
            buffer.append(" style='");
            buffer.append(getDataStyle());
            buffer.append("'");
        }
        buffer.append(">");

        Object columnValue = getProperty(row);

        if (getDecorator() != null) {
            buffer.append(getDecorator().render(columnValue, context));

        } else if (getMessageFormat() != null) {
            Object[] args = new Object[] { columnValue };
            buffer.append(getMessageFormat().format(args));

        } else {
            buffer.append(columnValue);
        }

        buffer.append("</td>");
    }

    /**
     * Render the column table header &lt;tr&gt; element to the given buffer.
     *
     * @param buffer the string buffer to render to
     * @param context the request context
     */
    public void renderTableHeader(StringBuffer buffer, Context context) {
        buffer.append("<th");
        if (getHeaderClass() != null) {
            buffer.append(" class='");
            buffer.append(getHeaderClass());
            buffer.append("'");
        }
        if (getHeaderStyle() != null) {
            buffer.append(" style='");
            buffer.append(getHeaderStyle());
            buffer.append("'");
        }
        buffer.append(">");
        buffer.append(getHeaderTitle());
        buffer.append("</th>");
    }

    /**
     * Return the named column property value from the given row object.
     *
     * @param row the row object to obtain the property from
     * @return the named row object property value
     * @throws RuntimeException if an error occured obtaining the property
     */
    protected Object getProperty(Object row) {
        try {
            if (propertyMethod == null) {
                String methodName = ClickUtils.toGetterName(name);
                propertyMethod = row.getClass().getMethod(methodName, null);
            }

            return propertyMethod.invoke(row, null);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
