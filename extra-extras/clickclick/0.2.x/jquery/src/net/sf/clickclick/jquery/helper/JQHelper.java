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
package net.sf.clickclick.jquery.helper;

import net.sf.clickclick.jquery.util.JQBinding;
import net.sf.clickclick.jquery.util.JQEvent;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import org.apache.click.ActionListener;
import net.sf.clickclick.AjaxControlRegistry;
import net.sf.clickclick.util.AjaxUtils;
import org.apache.click.Context;
import org.apache.click.Control;
import org.apache.click.element.JsImport;
import org.apache.click.element.JsScript;
import org.apache.click.service.ConfigService;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.commons.lang.StringUtils;

/**
 * Provide a JQuery helper that Ajax enables a target Control/s.
 * <p/>
 * This helper has an associated JavaScript template that can be modified
 * according to your needs. Click <a href="../../../../../js/template/jquery.template.js.txt">here</a>
 * to view the JQHelper template. You can set your own custom template using the
 * method {@link #setTemplate(java.lang.String)}.
 * <p/>
 * The default template is suited for Link based controls and most Field such
 * as Buttons, TextFields, Selects, Checkboxes and Radio buttons. Sophisticated
 * controls such as Forms, Tables and Trees would require customized templates
 * eg: {@link JQFormHelper}.
 * <p/>
 * JQHelper has two primary use cases:
 * <ul>
 * <li>To Embed JQHelper inside custom Controls. The custom Control then delegates
 * to JQHelper to add the necessary JavaScript and Ajax behavior.</li>
 * <li>To Ajax enable non-ajax Controls in a Page. Here JQHelper is used to "decorate"
 * target Controls with the necessary JavaScript and Ajax behavior.</li>
 * </ul>
 *
 * Here are two examples of using embedding as well as decorating JQHelper.
 *
 * <h3>Embedded example</h3>
 *
 * Below is an example of a custom control with an embedded JQHelper that
 * provides Ajax behavior:
 *
 * <pre class="prettyprint">
 * public class JQSubmit extends Submit {
 *
 *     // The embedded JQuery helper object.
 *     protected JQHelper jqHelper;
 *
 *     // Constructor
 *     public JQSubmit(String name) {
 *         super(name);
 *     }
 *
 *     public JQHelper getJQueryHelper() {
 *         if (jqHelper == null) {
 *             jqHelper = new JQHelper(this);
 *         }
 *         return jqHelper;
 *     }
 *
 *     // During onInit event the control is registered as an Ajax control
 *     &#64;Override
 *     public void onInit() {
 *         super.onInit();
 *
 *         // 1. Register the control with Click so it can handle Ajax requests.
 *         // Registration should occur within the "onInit" event. This ensures
 *         // the Ajax Control can be used within stateful Pages
 *         jqHelper.registerAjaxControl(this);
 *     }
 *
 *     &#64;Override
 *     public List getHeadElements() {
 *         if (headElements == null) {
 *             headElements = super.getHeadElements();
 *
 *             // 2. Bind the submit button ID attribute for "click" events.
 *             // From this binding the JQHelper template will render the
 *             // following JavaScript code: "jQuery.live('#form_submit', 'onclick', someFunction)
 *             // where "someFunction" is defined in the JQHelper template
 *             getJQueryHelper().bind(getId(), JQEvent.CLICK);
 *
 *             // 3. Add the JavaScript template and other supporting libraries
 *             // to the control HEAD elements
 *             getJQueryHelper().addHeadElements(headElements);
 *         }
 *         return headElements;
 *     }
 * } </pre>
 *
 * To use JQSubmit to receive Ajax requests you need to set an {@link net.sf.clickclick.AjaxListener}
 * on JQSubmit:
 *
 * <pre class="prettyprint">
 * public class AjaxDemo extends BorderPage {
 *
 *     public AjaxDemo() {
 *         JQSubmit submit = new JQSubmit("mysubmit");
 *
 *         // Register an Ajax listener on the submit (JQAjaxAdapter is an AjaxListener subclass)
 *         submit.setActionListener(new JQAjaxAdapter() {
 *
 *             &#64;Override
 *             public Partial onAjaxAction(Control source, JQEvent event) {
 *                 Taconite partial = new Taconite();
 *                 ...
 *                 return partial;
 *             }
 *         }
 *     }
 * } </pre>
 *
 * <h3>Decorate example</h3>
 *
 * Below is an example how to decorate a non-ajax TextField to update a span
 * element when the user types into the textfield:
 *
 * <pre class="prettyprint">
 * public class FieldDemo extends BorderPage {
 *
 *     private Field field = new TextField("field");
 *     private Span label = new Span("label", "label");
 *
 *     public FieldDemo() {
 *
 *         // Register an Ajax listener on the field which is invoked on every
 *         // "keyup" event.
 *         field.setActionListener(new AjaxAdapter() {
 *
 *             &#64;Override
 *             public Partial onAjaxAction(Control source) {
 *                 Taconite partial = new Taconite();
 *
 *                 // Set the label content to the latest field value
 *                 label.add(new Text(field.getValue()));
 *
 *                 // Replace the label in the browser with the new one
 *                 partial.replace(label);
 *                 return partial;
 *             }
 *         });
 *
 *         JQHelper helper = new JQHelper(field);
 *
 *         // Switch off the Ajax busy indicator
 *         helper.setShowIndicator(false);
 *
 *         // Delay Ajax invoke for 350 millis, otherwise too many Ajax requests
 *         // are made to the server
 *         helper.setThreshold(350);
 *
 *         // Set Ajax to fire on keyup events
 *         helper.setEvent(JQEvent.KEYUP);
 *
 *         // Ajaxify the Field
 *         helper.ajaxify();
 *
 *         addControl(field);
 *         addControl(label);
 *     }
 * } </pre>
 */
public class JQHelper implements Serializable {

    // -------------------------------------------------------------- Constants

    private static final long serialVersionUID = 1L;

    /**
     * The JQuery library (http://jquery.com/):
     * "<tt>/clickclick/jquery/jquery-1.3.2.js</tt>".
     */
    public static String jqueryImport = "/clickclick/jquery/jquery-1.3.2.js";

    /**
     * The JQuery Click library:
     * "<tt>/clickclick/jquery/jquery.click.js</tt>"
     * <p/>
     * This library includes JQuery Taconite plugin
     * (http://www.malsup.com/jquery/taconite/), JQuery LiveQuery plugin
     * (http://docs.jquery.com/Plugins/livequery)
     * and utility JavaScript functions.
     */
    public static String jqueryClickImport = "/clickclick/jquery/jquery.click.js";

    /**
     * The JQuery blockUI plugin (http://malsup.com/jquery/block/):
     * "<tt>/clickclick/jquery/blockui/jquery.blockUI.js</tt>".
     */
    public static String blockUIImport = "/clickclick/jquery/blockui/jquery.blockUI.js";

    // -------------------------------------------------------------- Variables

    /**
     * List of JQuery bindings. Each binding consists of a CSS Selector and Event.
     */
    protected List<JQBinding> bindings;

    /**
     * The path of the default template to render:
     * "<tt>/clickclick/jquery/template/jquery.template.js</tt>".
     */
    protected String template = "/clickclick/jquery/template/jquery.template.js";

    /**
     * The event which initiates an Ajax request, default value:
     * {@link net.sf.clickclick.util.JQEvent#CLICK}.
     */
    protected JQEvent event = JQEvent.CLICK;

    /** The data model for the JavaScript {@link #template}. */
    protected Map model;

    /** The Ajax request parameters. */
    protected Map parameters;

    /** The target control. */
    protected Control control;

    /** The type request (POST / GET), default value is GET. */
    protected String type = "GET";

    /** The Ajax request url. */
    protected String url;

    /** The CSS selector for selecting the target element to Ajaxify. */
    protected String selector;

    /**
     * The threshold within which multiple Ajax requests are merged into a
     * single request.
     */
    protected int threshold = 0;

    /**
     * The message to display if an Ajax error occurs.
     *
     * @see #getErrorMessage()
     */
    protected String errorMessage;

    /**
     * Flag indicating whether an Ajax indicator (busy indicator) must be shown,
     * default value is true.
     */
    protected boolean showIndicator = true;

    /**
     * The message to display when an Ajax indicator (busy indicator) is shown.
     *
     * @see #getIndicatorMessage()
     */
    protected String indicatorMessage;

    /**
     * The Ajax indicator (busy indicator) target.
     */
    protected String indicatorTarget;

    /**
     * The Ajax indicator (busy indicator) options. See the JQuery
     * <a href="http://malsup.com/jquery/block/">BlockUI</a> plugin for
     * available options.
     */
    protected String indicatorOptions;

    /**
     * The JavaScript template ID attribute
     * (&lt;script <span class="blue">id</span>="someid" &gt;).
     * If no ID attribute is specified (which is normally the case) the associated
     * {@link #control}'s ID will be used as the script element ID attribute.
     */
    protected String id;

    /**
     * Indicates whether the HEAD elements for this helper has been added to
     * or not.
     */
    protected boolean headElementsAdded = false;

    /**
     * Flag indicating whether bindings can be added to this helper. This allows
     * you to dissalow Ajax aware controls from adding their bindings to a
     * helper instance.
     */
    protected boolean bindingDisabled = false;

    // ----------------------------------------------------------- Constructors

    /**
     * Create a new JQHelper for the given ID.
     *
     * @param the id attribute of the script element
     */
    public JQHelper(String id) {
        setId(id);
    }

    /**
     * Create a new JQHelper for the given target control.
     *
     * @param control the helper target control
     */
    public JQHelper(String id, String selector) {
        setId(id);
        setSelector(selector);
    }

    /**
     * Create a new JQHelper for the given target control.
     *
     * @param control the helper target control
     */
    public JQHelper(String id, String selector, JQEvent event) {
        setId(id);
        setSelector(selector);
        setEvent(event);
    }

    /**
     * Create a new JQHelper for the given target control.
     *
     * @param control the helper target control
     */
    public JQHelper(Control control) {
        setControl(control);
    }

    /**
     * Create a new JQHelper for the given target control and CSS selector.
     * <p/>
     * Although any valid CSS selector can be specified, the CSS selector
     * usually specifies the HTML ID attribute of a target element on the page
     * eg: "<tt>#form-id</tt>".
     *
     * @param control the helper target control
     * @param selector the CSS selector
     */
    public JQHelper(Control control, String selector) {
        setControl(control);
        setSelector(selector);
    }

    /**
     * Create a new JQHelper for the given target control, CSS selector and event.
     * <p/>
     * Although any valid CSS selector can be specified, the CSS selector
     * usually specifies the HTML ID attribute of a target element on the page
     * eg: "<tt>#form-id</tt>".
     *
     * @param control the helper target control
     * @param selector the CSS selector
     * @param event the HTML event
     */
    public JQHelper(Control control, String selector, JQEvent event) {
        setControl(control);
        setSelector(selector);
        setEvent(event);
    }

    // ------------------------------------------------------ Public Properties

    /**
     * Return the list of JQuery bindings.
     *
     * @see #bind(java.lang.String, net.sf.clickclick.jquery.util.JQEvent)
     *
     * @return the list of JQuery bindings
     */
    public List<JQBinding> getBindings() {
        if (bindings == null) {
            bindings = new ArrayList<JQBinding>();
        }
        return bindings;
    }

    /**
     * Return the message to display when an error occurs during an Ajax request.
     * If no value is set, this method will try and lookup a localized message
     * using the target control for the key "<tt>ajax-error-message</tt>".
     * If a message cannot be found a default value is set:
     * "<tt>&;lt;h1&gt;Error occurred!&lt;/h1&gt;</tt>".
     *
     * @return the message to display wnen an error occurs during an Ajax request
     */
    public String getErrorMessage() {
        if (errorMessage == null) {
            errorMessage = getMessage("ajax-error-message");

            if (errorMessage == null) {
                // Set a default message
                errorMessage = "Error occurred!";
            }
        }
        return errorMessage;
    }

    /**
     * Set the error message to display when an error occurs during an Ajax
     * request.
     *
     * @param errorMessage the error message to display when an error occurs
     * during an Ajax request
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Return true if the Ajax indicator (busy indicator) should be shown,
     * false otherwise.
     *
     * @return true if the Ajax indicator (busy indicator) should be shown,
     * false otherwise
     */
    public boolean isShowIndicator() {
        return showIndicator;
    }

    /**
     * Set whether an Ajax indicator (busy indicator) should be shown during
     * Ajax requests.
     *
     * @param showIndicator indicates whether an Ajax indicator should be shown
     * during Ajax requests
     */
    public void setShowIndicator(boolean showIndicator) {
        this.showIndicator = showIndicator;
    }

    /**
     * Return the number of milliseconds to wait before the Ajax request is
     * invoked.
     *
     * @see #setThreshold(int)
     *
     * @return the number of milliseconds to wait before the Ajax request is
     * invoked
     */
    public int getThreshold() {
        return threshold;
    }

    /**
     * Set the number of milliseconds to wait before the Ajax request is
     * invoked, default value is 0, meaning requests are invoked immediately.
     * <p/>
     * <b>Please note:</b> all further Ajax requests invoked within the
     * threshold period are merged into a single request.
     *
     * @param threshold the threshold
     */
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    /**
     * Return the message to display when an Ajax indicator (busy indicator) is
     * shown. If no value is set, this method will try and lookup a
     * localized message using the target control for the key
     * "<tt>ajax-indicator-message</tt>". If a message cannot be found a
     * default value is set: "<tt>&;lt;h1&gt;Please wait...&lt;/h1&gt;</tt>".
     *
     * @return the message to display wnen an Ajax indicator is shown
     */
    public String getIndicatorMessage() {
        if (indicatorMessage == null) {
            indicatorMessage = getMessage("ajax-indicator-message");

            if (indicatorMessage == null) {
                // Set a default message
                indicatorMessage = "<h1>Please wait...</h1>";
            }
        }
        return indicatorMessage;
    }

    /**
     * Set the message to display when an Ajax indicator (busy indicator) is
     * shown. If no value is specified the the default value:
     * "<tt>&;lt;h1&gt;Please wait...&lt;/h1&gt;</tt>" is used.
     * <p/>
     * <b>Please note:</b> the indicator message will be enclosed inside single
     * quotes ('). If the message itself contains single quotes, they must be
     * escaped using two backslash (\\) characters e.g:
     * "<tt>Please enter your \\'name\\'.</tt>".
     *
     * @param indicatorMessage the message to display wnen an Ajax indicator is
     * shown
     */
    public void setIndicatorMessage(String indicatorMessage) {
        this.indicatorMessage = indicatorMessage;
    }

    /**
     * Return the target element that displays the Ajax indicator
     * (busy indicator).
     *
     * @return the target element that displays the Ajax indicator
     */
    public String getIndicatorTarget() {
        return indicatorTarget;
    }

    /**
     * Set the target element that displays the Ajax indicator (busy indicator).
     *
     * @param indicatorTarget the target element the displays the Ajax indicator
     */
    public void setIndicatorTarget(String indicatorTarget) {
        this.indicatorTarget = indicatorTarget;
    }

    /**
     * Set the target element that displays the Ajax indicator (busy indicator).
     *
     * @param indicatorTarget the target element the displays the Ajax indicator
     * @throws IllegalArgumentException if the control is null or the control ID
     * is not set
     */
    public void setIndicatorTarget(Control indicatorTarget) {
        if (indicatorTarget == null) {
            throw new IllegalArgumentException("control cannot be null");
        }
        String id = indicatorTarget.getId();
        if (id == null) {
            throw new IllegalArgumentException("control ID not set");
        }
        this.indicatorTarget = '#' + indicatorTarget.getId();
    }

    /**
     * Return the Ajax indicator (busy indicator) options.
     *
     * @see #setIndicatorOptions(java.lang.String)
     *
     * @return the target element that displays the Ajax indicator
     */
    public String getIndicatorOptions() {
        return indicatorOptions;
    }

    /**
     * Set the Ajax indicator (busy indicator) options.
     * <p/>
     * The Ajax indicator is based on the JQuery
     * <a href="http://malsup.com/jquery/block/">BlockUI</a> plugin so you can
     * use any of the options outlined
     * <a href="http://malsup.com/jquery/block/#options">here</a>.
     * <p/>
     * For example:
     *
     * <pre class="prettyprint">
     * public String getIndicatorOptions() {
     *     String options =
     *     "css : {"
     *         + "  textAlign: 'right',"
     *         + "  color: 'blue'"
     *         + "},"
     *         + "centerX: false,"
     *         + "centerY: false";
     *
     *     return options;
     * } </pre>
     *
     * <b>Please note</b> that the "<tt>message</tt>" option must be specified
     * through {@link #setIndicatorMessage(java.lang.String)} instead.
     *
     * @param indicatorOptions the Ajax indicator optiosn
     */
    public void setIndicatorOptions(String indicatorOptions) {
        this.indicatorOptions = indicatorOptions;
    }

    /**
     * Return the template to render for this helper.
     *
     * @return the template to render for this helper
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Set the template to render for this helper.
     *
     * @param template the template to render for this helper
     */
    public void setTemplate(String template) {
        this.template = template;
    }

    /**
     * Return the JavaScript event that fire the Ajax request.
     *
     * @return the JavaScript event that fire the Ajax request.
     */
    public JQEvent getEvent() {
        return event;
    }

    /**
     * Set the JavaScript event that fire the Ajax request.
     *
     * @param event the JavaScript event that fire the Ajax request.
     */
    public void setEvent(JQEvent event) {
        this.event = event;
    }

    /**
     * Return the data model for the JavaScript {@link #template}.
     *
     * @return the data model for the JavaScript template
     */
    public Map getModel() {
        if (model == null) {
            model = createDefaultModel();
        }
        return model;
    }

    /**
     * Set the data model for the JavaScript {@link #template}.
     *
     * @param model the data model for the JavaScript template
     */
    public void setModel(Map model) {
        this.model = model;
    }

    /**
     * Return the JavaScript template ID attribute
     * (&lt;script <span class="blue">id</span>="someid" &gt;).
     * If no ID attribute is specified (which is normally the case) the associated
     * {@link #control}'s ID will be used as the script element ID attribute.
     *
     * @return the ID attribute of the JavaScript template
     */
    public String getId() {
        if (id == null) {
            Control control = getControl();
            if (control != null) {
                id = control.getId();
                if (StringUtils.isBlank(id)) {
                    id = control.getName();
                }
            }
        }
        return id;
    }

    /**
     * Set the JavaScript template ID attribute
     * (&lt;script <span class="blue">id</span>="someid" &gt;).
     *
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Return the target control that initiates the Ajax request.
     *
     * @return the target control that initiates the Ajax request
     */
    public Control getControl() {
        return control;
    }

    /**
     * Set the target control that initiates the Ajax request.
     *
     * @param control the target control that initiates the Ajax request
     */
    public void setControl(Control control) {
        this.control = control;
    }

    /**
     * Return the Ajax request parameter Map.
     *
     * @return the Ajax request parameter Map
     */
    public Map getParameters() {
        if (parameters == null) {
            parameters = new HashMap(2);
        }
        return parameters;
    }

    /**
     * Return true if the Ajax request has parameters, false otherwise.
     *
     * @return true if the Ajax request has parameters, false otherwise
     */
    public boolean hasParameters() {
        if (parameters != null && !parameters.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Set the Ajax request parameter.
     *
     * @param parameters the Ajax request parameters
     */
    public void setParameters(Map parameters) {
        this.parameters = parameters;
    }

    /**
     * Set an Ajax request parameter with the given name and value.
     *
     * @param name the name of the parameter
     * @param value the value of the parameter
     */
    public void setParameter(String name, String value) {
        if (name == null) {
            throw new IllegalArgumentException("Null name parameter");
        }

        if (value != null) {
            getParameters().put(name, value);
        } else {
            getParameters().remove(name);
        }
    }

    /**
     * Return the type of Ajax request eg GET or POST.
     *
     * @return the type of Ajax request
     */
    public String getType() {
        return type;
    }

    /**
     * Set the type of the Ajax reques eg GET or POST.
     *
     * @param type the type of the Ajax request
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Return the URL for the Ajax request, defaults to the URL of the
     * current Page.
     *
     * @return the URL for the Ajax request
     */
    public String getUrl() {
        if (url == null) {
            Context context = getContext();
            url = ClickUtils.getRequestURI(context.getRequest());
            url = context.getResponse().encodeURL(url);
        }
        return url;
    }

    /**
     * Set the URL for the Ajax request. If no URL is set it will default to
     * the URL of the current Page.
     *
     * @param url the URL for the Ajax request
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Return the CSS selector for selecting the target element to Ajaxify.
     *
     * @return the CSS selector for selecting the target element to Ajaxify
     */
    public String getSelector() {
        return selector;
    }

    /**
     * Set the CSS selector for selecting the target element to Ajaxify.
     * <p/>
     * Usually the selector will be the ID attribute of the target {@link #control}
     * eg "<tt>#my-field</tt>".
     *
     * @param selector the CSS selector for selecting the target element to
     * Ajaxify
     */
    public void setSelector(String selector) {
        this.selector = selector;
    }

    /**
     * Return true if {@link #bind(java.lang.String, net.sf.clickclick.jquery.util.JQEvent) bindings}
     * can be added to this instance.
     *
     * @return true if bindings can added, false otherwide
     */
    public boolean isBindingDisabled() {
        return bindingDisabled;
    }

    /**
     * Set whether {@link #bind(java.lang.String, net.sf.clickclick.jquery.util.JQEvent) bindings}
     * can be added to this instance.
     * <p/>
     * This property allows you to disable Ajax aware controls from adding their
     * bindings to a helper in case you need to explicitly control the bindings.
     *
     * @param bindingDisabled true if bindings should be disabled, false otherwise
     */
    public void setBindingDisabled(boolean bindingDisabled) {
        this.bindingDisabled = bindingDisabled;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Return the localized message for the given key, or null if not found.
     * <p/>
     * This method will attempt to lookup the localized message in the
     * {@link #getControl()} parent's messages, which resolves to the Page's
     * resource bundle.
     * <p/>
     * If the message was not found, this method will attempt to look up the
     * value in the <tt>/click-control.properties</tt> message properties file,
     * through the method {@link #getMessages()}.
     * <p/>
     * If still not found, this method will return null.
     *
     * @param name the name of the message resource
     * @return the named localized message, or null if not found
     */
    public String getMessage(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Null name parameter");
        }

        String message = null;

        Control control = getControl();

        if (control != null) {
            message = ClickUtils.getParentMessage(control, name);

            if (message == null && getMessages().containsKey(name)) {
                message = (String) getMessages().get(name);
            }
        }

        return message;
    }

    /**
     * Return the formatted message for the given resource name,
     * message format argument and the context request locale, or null
     * if no message was found.
     * <p/>
     * {@link #getMessage(java.lang.String, java.lang.Object[])} is invoked to
     * retrieve the message for the specified name.
     *
     * @param name resource name of the message
     * @param arg the message argument to format
     * @return the named localized message for the control
     */
    public String getMessage(String name, Object arg) {
        Object[] args = new Object[] { arg };
        return getMessage(name, args);
    }

    /**
     * Return the formatted message for the given resource name,
     * message format arguments and the context request locale, or null if
     * no message was found.
     * <p/>
     * {@link #getMessage(java.lang.String)} is invoked to retrieve the message
     * for the specified name.
     *
     * @param name resource name of the message
     * @param args the message arguments to format
     * @return the named localized message for the package or null if no message
     * was found
     */
    public String getMessage(String name, Object[] args) {
        if (args == null) {
            throw new IllegalArgumentException("Null args parameter");
        }
        String value = getMessage(name);
        if (value == null) {
            return null;
        }
        return MessageFormat.format(value, args);
    }

    /**
     * Return a Map of localized messages for the specified {@link #getControl()}.
     *
     * @return a Map of localized messages for the control
     * @throws IllegalStateException if the context for the control has not be set
     */
    public Map getMessages() {
        Control control = getControl();
        if (control == null) {
            return Collections.EMPTY_MAP;
        } else {
            return getControl().getMessages();
        }
    }

    /**
     * Create a default data model for the Ajax {@link #template}.
     * <p/>
     * The following values are added:
     * <ul>
     * <li>"context" - the request context path e.g: '/myapp'</li>
     * <li>"{@link #bindings}" - the JavaScript bindings for events</li>
     * <li>"{@link #control}" - the target control</li>
     * <li>"{@link #selector}" - the CSS selector</li>
     * <li>"{@link #event}" - the event that initiates the Ajax request</li>
     * <li>"<span color="blue">productionMode</span>" - true if Click is running
     * in a production mode (production or profile), false otherwise</li>
     * <li>"{@link #url url}" - the Ajax request URL</li>
     * <li>"{@link #type}" - the type of the Ajax request, eg POST or GET</li>
     * <li>"{@link #threshold}" - the threshold within which multiple Ajax
     * requests are merged into a single request.</li>
     * <li>"{@link #showIndicator}" - the showIndicator flag</li>
     * <li>"{@link #indicatorOptions}"</span> - the Ajax indicator options. Note
     * that {@link #indicatorMessage} is rendered as part of the options</li>
     * <li>"{@link #indicatorTarget}" - the target element of the Ajax indicator</li>
     * <li>"{@link #errorMessage}" - the message to display if an Ajax error occurs</li>
     * <li>"{@link #parameters}" - the Ajax request parameters</li>
     * <li><span color="blue">"selector"</span> - the CSS {@link #selector}</li>
     * </ul>
     *
     * @return the default data model for the Ajax template
     */
    public Map createDefaultModel() {
        Context context = getContext();
        ConfigService configService = ClickUtils.getConfigService(context.getServletContext());
        boolean productionMode = configService.isProductionMode()
            || configService.isProfileMode();

        HtmlStringBuffer buffer = new HtmlStringBuffer();
        String message = getIndicatorMessage();
        String options = getIndicatorOptions();

        if (message != null) {
            buffer.append("message:'").append(message).append("'");
        }

        if (StringUtils.isNotBlank(options)) {
            if (buffer.length() > 0) {
                buffer.append(",");
            }
            buffer.append(options);
        }

        Map model = new HashMap();
        model.put("bindings", getBindings());
        model.put("context", context.getRequest().getContextPath());
        model.put("control", getControl());

        model.put("selector", getSelectorOrControlId());

        model.put("event", getEvent());
        model.put("productionMode", productionMode ? "true" : "false");
        model.put("url", getUrl());
        model.put("type", getType());
        model.put("threshold", getThreshold());
        model.put("showIndicator", isShowIndicator() ? "true" : "false");
        model.put("indicatorOptions", buffer.toString());
        model.put("indicatorTarget", getIndicatorTarget());
        model.put("errorMessage", getErrorMessage());
        model.put("parameters", serializeParameters());
        return model;
    }

    /**
     * Add the necessary JavaScript imports and scripts to the given
     * headElements list to enable Ajax requests.
     *
     * @param headElements the list which to add all JavaScript imports and
     * scripts to enable Ajax requests
     */
    public void addHeadElements(List headElements) {
        // Indicate head elements have been added for this helper
        headElementsAdded = true;

        JsImport jsImport = new JsImport(jqueryImport);
        if (!headElements.contains(jsImport)) {
            headElements.add(0, jsImport);
        }

        jsImport = new JsImport(jqueryClickImport);
        if (!headElements.contains(jsImport)) {
            headElements.add(1, jsImport);
        }

        if (isShowIndicator()) {
            jsImport = new JsImport(blockUIImport);
            if (!headElements.contains(jsImport)) {
                headElements.add(2, jsImport);
            }
        }

        ServletContext servletContext = getContext().getServletContext();
        ConfigService configService = ClickUtils.getConfigService(servletContext);

        // If Click is running in development modes, enable JavaScript debugging
        if (!configService.isProductionMode() && !configService.isProfileMode()) {
            addJSDebugScript(headElements);
        }
        addTemplate(headElements);
    }

    /**
     * Ajaxifies the the given control so that its registered
     * {@link net.sf.clickclick.AjaxListener} can be invoked for Ajax requests.
     * <p/>
     * If the helper's default {@link #control} is not set, the given control
     * will be set as the new default control.
     * <p/>
     * The given control cannot be null, however if the given CSS selector is null
     * it will default to the value returned by
     * {@link net.sf.clickclick.util.AjaxUtils#getSelector(org.apache.click.Control)}.
     * <p/>
     * This method does the following:
     * <ul>
     * <li>Regsiters the target {@link #control} on the {@link net.sf.clickclick.AjaxControlRegistry}
     * by invoking {@link net.sf.clickclick.AjaxControlRegistry#registerAjaxControl(Control)}</li>
     * <li>invokes {@link #addHeadElements(java.util.List)} which adds the necessary
     * JavaScript imports and scripts to enable Ajax requests</li>
     * </ul>
     * <b>Please note</b>: when invoking this method before or from the
     * onInit event, it will only be executed after the onInit event.
     * If this method is called after the onInit method, it will be executed
     * immediately.
     *
     * @param control the control to ajaxify
     * @param selector the CSS selector
     * @param event the JavaScript event
     * @throws IllegalArgumentException if control is null
     */
    public void ajaxify(final Control control, final String selector, final JQEvent event) {
        if (control == null) {
            throw new IllegalArgumentException("Control cannot be null");
        }

        // Set the given control as the default control if no control has been set
        if (getControl() == null) {
            setControl(control);
        }

        // Reason we register a callback below is that the control ID might only
        // be fully set after #ajaxify is invoked. By using the special
        // ON_AJAX_EVENT callback which is triggered before onProcess, we
        // ensure that the control ID will be available.
        AjaxControlRegistry.dispatchActionEvent(control, new ActionListener() {
            public boolean onAction(Control source) {
                if (selector != null) {
                    bind(selector, event);
                } else {
                    bind(AjaxUtils.getSelector(source), event);
                }

                AjaxControlRegistry.registerAjaxControl(source);
                return true;
            }
        }, AjaxControlRegistry.ON_AJAX_EVENT);

        // Register a POST_ON_RENDER_EVENT callback to add the helper head
        // elements to the control. By adding HEAD elements in this event,
        // Controls inside containers such as Repeaters can still be Ajax
        // targets because their names, at this stage, should have the Repeater
        // index applied. Repeaters also apply indexes to their child names during
        // the POST_ON_RENDER_EVENT, but normally the Repeater will have registered
        // its POST_ON_RENDER_EVENT before any child control have been created,
        // thus the Repeater event would have fired by the time the control head
        // elements is applied.
        // PLEASE NOTE: Ajax enabled controls shouldn't have this issue, as their
        // HEAD elements should be applied inside the getHeadElements method which
        // is invoked after the POST_ON_RENDER_EVENT
        if (!headElementsAdded) {
            headElementsAdded = true;
            AjaxControlRegistry.dispatchActionEvent(control, new ActionListener() {

                public boolean onAction(Control source) {
                    addHeadElements(source.getHeadElements());

                    // Stateful Page note: reset headElements flag
                    headElementsAdded = false;
                    return true;
                }
            }, AjaxControlRegistry.POST_ON_RENDER_EVENT);
        }
    }

    /**
     * Ajaxifies the the given control so that its registered
     * {@link net.sf.clickclick.AjaxListener} can be invoked for Ajax requests.
     *
     * @see #ajaxify(org.apache.click.Control, java.lang.String, net.sf.clickclick.jquery.util.JQEvent)
     *
     * @param control the control to ajaxify
     * @param event the JavaScript event
     */
    public void ajaxify(Control control, JQEvent event) {
        ajaxify(control, getSelector(), event);
    }

    /**
     * Ajaxifies the the given control so that its registered
     * {@link net.sf.clickclick.AjaxListener} can be invoked for Ajax requests.
     *
     * @see #ajaxify(org.apache.click.Control, java.lang.String, net.sf.clickclick.jquery.util.JQEvent)
     *
     * @param control the control to ajaxify
     */
    public void ajaxify(Control control) {
        ajaxify(control, getSelector(), getEvent());
    }

    /**
     * Ajaxifies the the target {@link #control} so that its registered
     * {@link net.sf.clickclick.AjaxListener} can be invoked for Ajax requests.
     *
     * @see #ajaxify(org.apache.click.Control, java.lang.String, net.sf.clickclick.jquery.util.JQEvent)
     */
    public void ajaxify() {
        ajaxify(getControl(), getSelector(), getEvent());
    }

    /**
     * Register the given control to be processed by the ClickServlet for Ajax
     * requests.
     * <p/>
     * This method delegates to
     * {@link net.sf.clickclick.AjaxControlRegistry#registerAjaxControl(org.apache.click.Control)}.
     *
     * @param control the control to register as an Ajax target
     * @throws IllegalArgumentException if control is null
     */
    public void registerAjaxControl(Control control) {
        if (control == null) {
            throw new IllegalArgumentException("Control cannot be null");
        }

        AjaxControlRegistry.registerAjaxControl(control);
    }

    /**
     * Synonymous to JQuery <a href="http://docs.jquery.com/Events/bind">bind</a>
     * / <a href="http://docs.jquery.com/Events/live">live</a> functionality.
     *
     * @see #bind(java.lang.String, net.sf.clickclick.jquery.util.JQEvent)
     *
     * @param event the JavaScript event to bind
     */
    public void bind(JQEvent event) {
        bind(getSelectorOrControlId(), event);
    }

    /**
     * Synonymous to JQuery <a href="http://docs.jquery.com/Events/bind">bind</a>
     * / <a href="http://docs.jquery.com/Events/live">live</a> functionality.
     *
     * @see #bind(java.lang.String, net.sf.clickclick.jquery.util.JQEvent)
     *
     * @param selector the CSS selector to bind
     */
    public void bind(String selector) {
        bind(selector, getEvent());
    }

    /**
     * Synonymous to JQuery <a href="http://docs.jquery.com/Events/bind">bind</a>
     * / <a href="http://docs.jquery.com/Events/live">live</a> functionality.
     * <p/>
     * This method provides an easy way to bind a CSS selector and JavaScript
     * {@link net.sf.clickclick.jquery.util.JQEvent}.
     * <p/>
     * For example the following snippet:
     *
     * <pre class="prettyprint">
     * Form form = new Form("form");
     * Submit submit = new Submit("ok");
     *
     * // NOTE: Add button to parent form before invoking #bind (see below for details)
     * form.add(submit);
     *
     * jqHelper.bind('#' + submit.getId(), JQEvent.CLICK);
     * </pre>
     *
     * will render as follows:
     *
     * <pre class="prettyprint">
     * jQuery('#form_ok').live('click', template);
     * </pre>
     *
     * <b>Note:</b> if the CSS selector passed to the #bind method is a Control's
     * ID, ensure the Control is attached to its parent Container so that the
     * full ID is passed in.
     *
     * @param selector the CSS selector to bind
     * @param event the JavaScript event to bind
     */
    public void bind(String selector, JQEvent event) {
        if (isBindingDisabled()) {
            return;
        }

        JQBinding binding = new JQBinding(selector, event);

        List<JQBinding> bindingList = getBindings();
        if (bindingList.contains(binding)) {
            return;
        }

        bindingList.add(binding);
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Return the Context of the current request
     *
     * @return the Context of the current request
     */
    protected Context getContext() {
        return Context.getThreadLocalContext();
    }

    /**
     * Return the Ajax request {@link #parameters} as a serialized URL string.
     * <p/>
     * The serialized string will consist of name/value pairs delimited by
     * the '&' char. An example URL string could be:
     * "<tt>firstname=John&lastname=Smith&age=12</tt>".
     *
     * @return the Ajax request parameters as a serialized URL string
     */
    protected String serializeParameters() {
        HtmlStringBuffer buffer = new HtmlStringBuffer();
        Iterator i = getParameters().keySet().iterator();
        while (i.hasNext()) {
            Object key = i.next();
            if (key == null) {
                continue;
            }
            String name = key.toString();
            Object paramValue = getParameters().get(name);
            String encodedValue =
                ClickUtils.encodeURL(paramValue);
            buffer.append(name);
            buffer.append("=");
            buffer.append(encodedValue);
            if (i.hasNext()) {
                buffer.append("&");
            }
        }
        return buffer.toString();
    }

    /**
     * Add a special {@link org.apache.click.element.JsScript} which enables
     * detailed JavaScript log output to the given headElements list.
     * <p/>
     * <b>Please note:</b> use the Firefox browser and Firebug plugin to view
     * the logged output. However other browsers might also support logging
     * output.
     *
     * @param headElements list which to add the debug script to
     */
    protected void addJSDebugScript(List headElements) {
        JsScript jsScript = new JsScript();
        jsScript.setId("enable_js_debugging");
        if (headElements.contains(jsScript)) {
            return;
        }

        HtmlStringBuffer buffer = new HtmlStringBuffer(100);
        buffer.append("if (typeof jQuery != 'undefined') {\n");
        buffer.append("  if (typeof jQuery.taconite != 'undefined') {\n");
        buffer.append("    jQuery.taconite.debug = true;\n");
        buffer.append("  }\n");
        buffer.append("  if (typeof Click != 'undefined') {\n");
        buffer.append("    Click.debug = true;\n");
        buffer.append("  }\n");
        buffer.append("}");
        jsScript.setContent(buffer.toString());
        headElements.add(jsScript);
    }

    /**
     * Add the {@link #template} content to the given headElements list.
     *
     * @param headElements list which to add the Ajax template to
     */
    protected void addTemplate(List headElements) {

        if (StringUtils.isNotBlank(getTemplate())) {
            JsScript jsScript = new JsScript(getTemplate(), getModel());

            String id = getId();
            if (id == null) {
                throw new IllegalArgumentException("JQHelper id is null");
            }

            jsScript.setId(id + "_jquery_template");

            // remove previous script in case of stateful pages
            headElements.remove(jsScript);
            // add script
            headElements.add(jsScript);
        }
    }

    /**
     * Return the {@link #selector} or the Control ID/name attribute if the
     * selector is not set.
     *
     * @return return selector or Control ID/name attribute
     */
    protected String getSelectorOrControlId() {
        String cssSelector = getSelector();
        if (cssSelector == null) {
            Control control = getControl();
            if (control != null) {
                cssSelector = AjaxUtils.getSelector(control);
            }
        }
        return cssSelector;
    }
}
