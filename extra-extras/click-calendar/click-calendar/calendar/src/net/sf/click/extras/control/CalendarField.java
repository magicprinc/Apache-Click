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
package net.sf.click.extras.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.apache.click.element.CssImport;
import org.apache.click.element.Element;
import org.apache.click.element.JsImport;
import org.apache.click.element.JsScript;
import org.apache.click.extras.control.DateField;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.HtmlStringBuffer;

/**
 * Provides a Calendar Field control: &nbsp; &lt;input type='text'&gt;&lt;img&gt;.
 *
 * <table class='htmlHeader' cellspacing='6'>
 * <tr>
 * <td style="vertical-align:baseline">Calendar Field</td>
 * <td style="vertical-align:baseline"><input type='text' size='20' title='CalendarField Control' value='15 Mar 2006'/><img align='middle' hspace='2' style='cursor:hand' src='calendar.gif' title='Calendar'/></td>
 * </tr>
 * </table>
 *
 * The CalendarField control provides a Date entry field and a popup DHTML Calendar
 * &lt;div&gt;. Users can either key in a Date value or select a Date using the
 * Calendar.
 * <p/>
 * Example:
 * <pre class="prettyprint">
 * public MyPage extends Page {
 *
 *     public void onInit() {
 *         Form form = new Form("form");
 *
 *         // Create new CalendarField with default date format: 'dd MMM yyyy'
 *         CalendarField calendarField = new CalendarField("calendar");
 *
 *         // You can change the format to: 'yyyy-MM-dd'
 *         calendarField.setFormatPattern("yyyy-MM-dd");
 *
 *         // Finally add calendarField to form
 *         form.add(calendarField);
 *
 *         addControl(form);
 *     }
 * } </pre>
 *
 * <h3>CSS and JavaScript resources</h3>
 *
 * The Calendar popup is provided by the JSCalendar library from
 * <a href="http://www.dynarch.com/">Dynarch.com</a>.
 * <p/>
 * If you want a DateField without a Calendar popup you can switch it off
 * by setting the {@link #setShowCalendar(boolean)} to false. No JavaScript or
 * CSS will be included when this option is false.
 * <p/>
 * The CalendarField control makes use of the following resources
 * (which Click automatically deploys to the application directories,
 * <tt>/click/jscalendar</tt>):
 *
 * <ul>
 * <li><tt>click/jscalendar/calendar-{style}.css</tt> - where {style} is a specific Calendar style e.g. <tt>blue</tt>, <tt>brown</tt>, <tt>green</tt> etc.</li>
 * <li><tt>click/jscalendar/calendar.js</tt></li>
 * <li><tt>click/jscalendar/calendar-{lang}.js</tt> - where {lang} is the language specified by the browser e.g. <tt>fr</tt> (French), <tt>de</tt> (German) etc.</li>
 * </ul>
 *
 * The Calendar popup is
 * created as a &lt;div&gt; element using JavaScript. To enable the Calendar
 * popup, reference <span class="blue">$headElements</span> and <span class="blue">$jsElements</span>
 * in the page template. For example:
 *
 * <pre class="codeHtml">
 * &lt;html&gt;
 * &lt;head&gt;
 * <span class="blue">$headElements</span>
 * &lt;/head&gt;
 * &lt;body&gt;
 * <span class="red">$form</span>
 * &lt;/body&gt;
 * &lt;/html&gt;
 * <span class="blue">$jsElements</span> </pre>
 *
 * The default Calendar style is 'system' which has a similar appearance
 * to the Windows Calendar control. The JSCalendar styles include:
 * <ul style="margin-top: 0.5em;">
 * <li>blue</li>
 * <li>blue2</li>
 * <li>brown</li>
 * <li>green</li>
 * <li>system</li>
 * <li>tas</li>
 * <li>win2k-1</li>
 * <li>win2k-2</li>
 * <li>win2k-cold-1</li>
 * <li>win2k-cold-2</li>
 * </ul>
 *
 * The DateField JavaScript, CSS and image resources are automatically deployed
 * to the <tt>click/jscalendar</tt> web directory on application startup.
 */
public class CalendarField extends DateField {

    // Constants --------------------------------------------------------------

    private static final long serialVersionUID = 1L;

    /** The default date format: <tt>dd MMM yyyy</tt>. */
    private static final String DEFAULT_JS_DATE_FORMAT_PATTERN = "%d %b %Y";

    /** The default minimum year: <tt>1900</tt>. */
    private static final int DEFAULT_MINIMUM_YEAR = 1900;

    /** The default minimum year: <tt>2999</tt>. */
    private static final int DEFAULT_MAXIMUM_YEAR = 2999;

    /** Supported locales. */
    static final String[] SUPPORTED_LANGUAGES =
        {"al", "bg", "cs", "da", "de", "el", "en", "es", "fi", "fr", "he", "it",
         "ja", "ko", "lt", "lv", "nl", "no", "pl", "pt", "ro", "ru", "sk", "sv",
         "tr", "zh"};

    // ----------------------------------------------------------- Constructors

    /**
     * Construct the Calendar Field with the given name.
     * <p/>
     * The date format pattern will be set to <tt>dd MMM yyyy</tt>.
     *
     * @param name the name of the field
     */
    public CalendarField(String name) {
        super(name);
        init();
    }

    /**
     * Construct the Calendar Field with the given name and label.
     * <p/>
     * The date format pattern will be set to <tt>dd MMM yyyy</tt>.
     *
     * @param name the name of the field
     * @param label the label of the field
     */
    public CalendarField(String name, String label) {
        super(name, label);
        init();
    }

    /**
     * Construct the Calendar Field with the given name and required status.
     * <p/>
     * The date format pattern will be set to <tt>dd MMM yyyy</tt>.
     *
     * @param name the name of the field
     * @param required the field required status
     */
    public CalendarField(String name, boolean required) {
        super(name, required);
        init();
    }

    /**
     * Construct the Calendar Field with the given name, label and required status.
     * <p/>
     * The date format pattern will be set to <tt>dd MMM yyyy</tt>.
     *
     * @param name the name of the field
     * @param label the label of the field
     * @param required the field required status
     */
    public CalendarField(String name, String label, boolean required) {
        super(name, label, required);
        init();
    }

    /**
     * Construct the Calendar Field with the given name, label and size.
     *
     * @param name the name of the field
     * @param label the label of the field
     * @param size the size of the field
     */
    public CalendarField(String name, String label, int size) {
        super(name, label, size);
        init();
    }

    /**
     * Construct the Calendar Field with the given name, label, size and
     * required status.
     *
     * @param name the name of the field
     * @param label the label of the field
     * @param size the size of the field
     * @param required the field required status
     */
    public CalendarField(String name, String label, int size, boolean required) {
        super(name, label, size, required);
        init();
    }

    /**
     * Create a Calendar Field with no name defined.
     * <p/>
     * <b>Please note</b> the control's name must be defined before it is valid.
     */
    public CalendarField() {
        super();
        init();
    }

    // ------------------------------------------------------ Public Attributes

    /**
     * Return the CalendarField HTML HEAD elements for the following resources:
     *
     * <ul>
     * <li><tt>click/jscalendar/calendar-{style}.css</tt> - where {style} is a specific
     * Calendar style e.g. <tt>system</tt>, <tt>blue</tt>, <tt>green</tt> etc.</li>
     * <li><tt>click/control.js</tt></li>
     * <li><tt>click/jscalendar/calendar.js</tt></li>
     * <li><tt>click/jscalendar/calendar-{lang}.js</tt> - where {lang} is the language
     * specified by the browser e.g. <tt>en</tt> (English), <tt>fr</tt> (French),
     * <tt>de</tt> (German) etc.</li>
     * </ul>
     *
     * This method delegates to {@link #addCalendarOptions(java.util.List)} to
     * include the Calendar Options script.
     *
     * @see org.apache.click.Control#getHeadElements()
     *
     * @return the HTML HEAD elements for the control
     */
    @Override
    public List getHeadElements() {
        // CLK-309. Skip imports if dateField is disabled, readonly or calendar
        // should not be displayed.
        if (isReadonly() || isDisabled() || !isShowCalendar()) {
            return new ArrayList(0);
        }

        // Check that the field id has been set
        String fieldName = getName();
        if (fieldName == null) {
            throw new IllegalStateException("CalendarField name"
                + " is not defined. Set the name before calling"
                + " getHeadElements().");
        }

        if (headElements == null) {
            // Ensure we do not import DateField resources (prototype.js) as it
            // clashes with other JS libraries
            headElements = new ArrayList<Element>(5);

            String versionIndicator = ClickUtils.getResourceVersionIndicator(getContext());

            headElements.add(new CssImport("/click/jscalendar/calendar-" + getStyle()
                + ".css", versionIndicator));

            headElements.add(new JsImport("/click/control.js",
                versionIndicator));

            headElements.add(new JsImport("/click/jscalendar/calendar.js",
                versionIndicator));

            String language = getLocale().getLanguage();
            JsImport jsImport = new JsImport("/click/jscalendar/calendar-"
                + language + ".js", versionIndicator);
            jsImport.setAttribute("charset", "UTF-8");
            headElements.add(jsImport);
        }

        addCalendarOptions(headElements);

        return headElements;
    }

    /**
     * Add the calendar options as a script to the list of head elements.
     * <p/>
     * The default option script will render as (depending on the values):
     *
     * <pre class="prettyprint">
     * Click.addLoadEvent(function() {
     *   Calendar.setup({
     *     inputField : 'my-calendar',
     *     ifFormat : 'dd MMM yyyy',
     *     showsTime : true,
     *     button : 'my-calendar-button',
     *     align : 'cr',
     *     singleClick : true,
     *     firstDay : 0,
     *     range : [1900, 2999],
     *     onClose : function onClose(cal) { cal.hide(); cal.destroy(); }
     *   });
     * }); </pre>
     *
     * You can override this method to set your own options using a
     * {@link org.apache.click.element.JsScript}.
     *
     * @param headElements the list of head elements to include for this control
     */
    @Override
    protected void addCalendarOptions(List headElements) {
        String fieldId = getId();

        JsScript script = new JsScript();
        script.setId(fieldId + "_js_setup");

        // Note the Calendar options script is recreated and checked if it
        // is contained in the headElement. This caters for when the field is
        // used in a fly-weight pattern such as FormTable.
        if (!headElements.contains(script)) {

            // Script must be executed as soon as browser dom is ready
            script.setExecuteOnDomReady(true);

            HtmlStringBuffer buffer = new HtmlStringBuffer(150);

            buffer.append("Calendar.setup({");
            buffer.append(" inputField : '").append(fieldId).append("'");
            if (!DEFAULT_JS_DATE_FORMAT_PATTERN.equals(getCalendarPattern())) {
                buffer.append(",ifFormat : '").append(getCalendarPattern()).append("'");
            }
            // Moving defaults to calendar.js
            if (isShowTime()) {
                buffer.append(",showsTime : true");
            }
            buffer.append(",button : '").append(fieldId).append("-button'");
            buffer.append(",firstDay : ").append(getFirstDayOfWeek() - 1);
            if (getMinimumYear() != DEFAULT_MINIMUM_YEAR || getMaximumYear() != DEFAULT_MAXIMUM_YEAR) {
                buffer.append(",range : [").append(getMinimumYear()).append(",").append(getMaximumYear()).append("]");
            }
            buffer.append("});\n");

            script.setContent(buffer.toString());
            headElements.add(script);
        }
    }

    /**
     * Return true if the DHTML Calendar popup will show the time display bar.
     *
     * @deprecated use {@link #isShowTime()} instead
     *
     * @return true if the DHTML Calendar popup will show the time display bar
     */
    public boolean getShowTime() {
        return showTime;
    }

    /**
     * Set the JSCalendar CSS style.
     * <p/>
     * Available styles include: <tt>[blue, blue2, brown, green, system, tas,
     * win2k-1, win2k-2, win2k-cold-1, win2k-cold-2]</tt>
     *
     * @param style the JSCalendar CSS style
     */
    @Override
    public void setStyle(String style) {
        super.setStyle(style);
    }

    // Public Methods ---------------------------------------------------------

    /**
     * Render the HTML representation of the CalendarField.
     *
     * @see #toString()
     *
     * @param buffer the specified buffer to render the control's output to
     */
    @Override
    public void render(HtmlStringBuffer buffer) {
        // Set default title
        if (getTitle() == null) {
            setTitle(getMessage("date-title", formatPattern));
        }

        buffer.elementStart(getTag());

        buffer.appendAttribute("type", getType());
        buffer.appendAttribute("name", getName());
        buffer.appendAttribute("id", getId());
        buffer.appendAttributeEscaped("value", getValue());
        buffer.appendAttribute("size", getSize());
        buffer.appendAttribute("title", getTitle());
        if (getTabIndex() > 0) {
            buffer.appendAttribute("tabindex", getTabIndex());
        }
        if (getMaxLength() > 0) {
            buffer.appendAttribute("maxlength", getMaxLength());
        }

        if (isValid()) {
            removeStyleClass("error");
            if (isDisabled()) {
                addStyleClass("disabled");
            } else {
                removeStyleClass("disabled");
            }
        } else {
            addStyleClass("error");
        }

        appendAttributes(buffer);

        if (isDisabled()) {
            buffer.appendAttributeDisabled();
        }
        if (isReadonly()) {
            buffer.appendAttributeReadonly();
        }

        buffer.elementEnd();

        if (isShowCalendar()) {
            renderCalendarButton(buffer);
        }

        if (getHelp() != null) {
            buffer.append(getHelp());
        }
    }

    // Protected Methods ------------------------------------------------------

    /**
     * Return the JavaScript Calendar pattern for the given Java DateFormat
     * pattern.
     *
     * @param pattern the Java DateFormat pattern
     * @return JavaScript Calendar pattern
     */
    @Override
    protected String parseDateFormatPattern(String pattern) {
        HtmlStringBuffer jsPattern = new HtmlStringBuffer(20);
        int tokenStart = -1;
        int tokenEnd = -1;
        boolean debug = false;

        for (int i = 0; i < pattern.length(); i++) {
            char aChar = pattern.charAt(i);
            if (debug) {
                System.err.print("[" + i + "," + tokenStart + "," + tokenEnd
                                 + "]=" + aChar);
            }

            // If character is in SimpleDateFormat pattern character set
            if ("GyMwWDdFEaHkKhmsSzZ".indexOf(aChar) == - 1) {
                if (debug) {
                    System.err.println(" N");
                }
                if (tokenStart > - 1) {
                    tokenEnd = i;
                }
            } else {
                if (debug) {
                    System.err.println(" Y");
                }
                if (tokenStart == - 1) {
                    tokenStart = i;
                }
            }

            if (tokenStart > -1) {

                if (tokenEnd == -1 && i == pattern.length() - 1) {
                    tokenEnd = pattern.length();
                }

                if (tokenEnd > -1) {
                    String token = pattern.substring(tokenStart, tokenEnd);

                    if ("yyyy".equals(token)) {
                        jsPattern.append("%Y");
                    } else if ("yy".equals(token)) {
                        jsPattern.append("%y");
                    } else if ("MMMM".equals(token)) {
                        jsPattern.append("%B");
                    } else if ("MMM".equals(token)) {
                        jsPattern.append("%b");
                    } else if ("MM".equals(token)) {
                        jsPattern.append("%m");
                    } else if ("M".equals(token)) {
                        jsPattern.append("%m");
                    } else if ("dd".equals(token)) {
                        jsPattern.append("%d");
                    } else if ("d".equals(token)) {
                        jsPattern.append("%e");
                    } else if ("EEEE".equals(token)) {
                        jsPattern.append("%A");
                    } else if ("EEE".equals(token)) {
                        jsPattern.append("%a");
                    } else if ("EE".equals(token)) {
                        jsPattern.append("%a");
                    } else if ("E".equals(token)) {
                        jsPattern.append("%a");
                    } else if ("aaa".equals(token)) {
                        jsPattern.append("%p");
                    } else if ("aa".equals(token)) {
                        jsPattern.append("%p");
                    } else if ("a".equals(token)) {
                        jsPattern.append("%p");
                    } else if ("HH".equals(token)) {
                        jsPattern.append("%H");
                        setShowTime(true);
                    } else if ("H".equals(token)) {
                        jsPattern.append("%H");
                        setShowTime(true);
                    } else if ("hh".equals(token)) {
                        jsPattern.append("%l");
                        setShowTime(true);
                    } else if ("h".equals(token)) {
                        jsPattern.append("%l");
                        setShowTime(true);
                    } else if ("mm".equals(token)) {
                        jsPattern.append("%M");
                        setShowTime(true);
                    } else if ("m".equals(token)) {
                        jsPattern.append("%M");
                        setShowTime(true);
                    } else if ("ss".equals(token)) {
                        jsPattern.append("%S");
                        setShowTime(true);
                    } else if ("s".equals(token)) {
                        jsPattern.append("%S");
                        setShowTime(true);
                    } else {
                        if (debug) {
                            System.err.println("Not mapped:" + token);
                        }
                    }

                    if (debug) {
                        System.err.println("token[" + tokenStart + ","
                                           + tokenEnd + "]='" + token + "'");
                    }
                    tokenStart = -1;
                    tokenEnd = -1;
                }
            }

            if (tokenStart == -1 && tokenEnd == -1) {
                if ("GyMwWDdFEaHkKhmsSzZ".indexOf(aChar) == -1) {
                    jsPattern.append(aChar);
                }
            }
        }

        return jsPattern.toString();
    }

    /**
     * Returns the <tt>Locale</tt> that should be used in this control.
     *
     * @return the locale that should be used in this control
     */
    @Override
    protected Locale getLocale() {
        Locale locale = null;

        locale = getContext().getLocale();
        String lang = locale.getLanguage();
        if (Arrays.binarySearch(SUPPORTED_LANGUAGES, lang) >= 0) {
            return locale;
        }

        locale = Locale.getDefault();
        lang = locale.getLanguage();
        if (Arrays.binarySearch(SUPPORTED_LANGUAGES, lang) >= 0) {
            return locale;
        }

        return Locale.ENGLISH;
    }

    // Private Methods --------------------------------------------------------

    /**
     * Initialize the calendar field default properties.
     */
    private void init() {
        setStyle("system");
        setMinimumYear(DEFAULT_MINIMUM_YEAR);
        setMaximumYear(DEFAULT_MAXIMUM_YEAR);
    }
}
