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
package net.sf.click.jquery.behavior;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletContext;
import org.apache.click.Context;
import org.apache.click.Control;
import org.apache.click.Page;
import org.apache.click.ActionResult;
import org.apache.click.element.Element;
import org.apache.click.element.JsImport;
import org.apache.click.element.JsScript;
import net.sf.click.jquery.JQEvent;
import net.sf.click.jquery.util.JSONWriter;
import net.sf.click.jquery.util.Options;
import org.apache.click.service.ConfigService;
import org.apache.click.service.LogService;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.commons.lang.StringUtils;

/**
 * TODO: Describe how to set a custom setupScript.
 * <p/>
 * This behavior has an associated JavaScript template that can be modified
 * according to your needs.
 * <p/>
 The jQuery-Click templates are provided in the JavaScript file
 * <a href="../../../../../js/template/jquery.templates.js.txt">jquery.templates.js</a>.
 * This behavior's template is <tt>Template 1</tt> and the JavaScript function is
 * called <tt>Click.jq.ajaxTemplate</tt>.
 * <p/>
 * If you rename the JavaScript function or change the function's arguments, you
 * will also have to customize the {@link #setSetupScript(org.apache.click.element.JsScript) setupScript},
 * otherwise the Behavior will still generate the old JavaScript function signature.
 *
 * * @see JQTemplateBehavior if you want to create a new custom template instead.
 */
public class JQBehavior extends AbstractJQBehavior implements Serializable {

    // Constants --------------------------------------------------------------

    private static final long serialVersionUID = 1L;

    /**
     * The path of the default jQuery templates:
     * "<tt>/click-jquery/template/jquery.templates.js</tt>".
     * <p/>
     * These templates can be customized to suite your needs. You can even
     * replace these templates with your own version.
     */
    public static String defaultTemplatesPath = "/click-jquery/template/jquery.templates.js";

    /**
     * The path to the {@value #defaultTemplatesPath} language packs, default
     * value is <tt>"/click-jquery/template/lang/"</tt>.
     */
    public static String defaultTemplatesLangFolder = "/click-jquery/template/lang/";

    // Variables --------------------------------------------------------------

    /** Supported locales. */
    static String[] SUPPORTED_LANGUAGES = {"af", "en"};

    /**
     * The path of the default jQuery templates: {@value #jqueryTemplatesPath}.
     * <p/>
     * These templates can be customized to suite your needs. You can even
     * replace these templates with your own version.
     */
    protected String templatesPath = defaultTemplatesPath;

    /**
     * The folder where the template language packs can be found, the default
     * value is {@value #defaultTemplatesLangFolder}.
     */
    protected String templatesLangFolder = defaultTemplatesLangFolder;

    protected String eventType = JQEvent.CLICK;

    /** The data model for the JavaScript {@link #template}. */
    protected Map<String, Object> model;

    /** The Ajax request parameters. */
    protected Map<String, Object> data;

    /** The type request (POST / GET), default value is GET. */
    protected String type = "GET";

    /** The Ajax request url. */
    protected String url;

    protected String setupScriptId;

    /**
     * The delay within which multiple Ajax requests are merged into a
     * single request. Useful for keyboard and mouse based events.
     */
    protected int delay = 0;

    protected boolean skipSetupScript = false;

    protected boolean skipHeadElements = false;

    protected JsScript setupScript;

    /**
     * Flag indicating whether an Ajax indicator (busy indicator) must be shown,
     * default value is true.
     */
    protected boolean showBusyIndicator = true;

    protected Options busyIndicatorOptions;

    protected String busyIndicatorMessage;

    protected String busyIndicatorTarget;

    protected int timeout = 20000;

    protected int timeoutRetryLimit = 3;

    /**
     * By default JQBehavior uses the cssSelector of each control it is
     * added to. You can override this default behavior by setting the
     * CSS Selector property to use.
     */
    protected String cssSelector;

    // ----------------------------------------------------------- Constructors

    public JQBehavior(String eventType) {
        // EventType is immutable and cannot be changed at a later stage
        this.eventType = eventType;
    }

    public JQBehavior() {
    }

    // Public Method ----------------------------------------------------------

    /**
     * Return the CSS Selector for the behavior, defaults to the Controls CSS selector
     * returned by {@link org.apache.click.util.ClickUtils#getCssSelector(org.apache.click.Control)}.
     *
     * @return the behavior CSS Selector
     */
    public String getCssSelector() {
        return cssSelector;
    }

    /**
     * By default JQBehavior uses the cssSelector of each control it is added to.
     * You can override this default behavior by setting the CSS Selector property
     * to use.
     *
     * @param cssSelector the behavior CSS Selector
     */
    public void setCssSelector(String cssSelector) {
        this.cssSelector = cssSelector;
    }

    public String getEventType() {
        return eventType;
    }

    /**
     * Return the template to render for this behavior.
     *
     * @return the template to render for this behavior
     */
    public String getTemplatesPath() {
        return templatesPath;
    }

    /**
     * Set the template to render for this behavior.
     *
     * @param templatesPath the template to render for this behavior
     */
    public void setTemplatesPath(String templatesPath) {
        this.templatesPath = templatesPath;
    }

    /**
     * Return the folder containing the template language packs. The default
     * value is {@value #templatesLangFolder}.
     *
     * @see #setTemplatesLangPath(java.lang.String)
     *
     * @return the template to render for this helper
     */
    public String getTemplatesLangFolder() {
        return templatesLangFolder;
    }

    /**
     * Set the path of the template language pack to render. Specific language
     * packs will be looked up from this folder based on the request's
     * {@link org.apache.click.Context#getLocale() Context locale}.
     * <p/>
     * For example if templatesLangPath is <tt>"/click-jquery/templates/"</tt>
     * and the Context locale is German (de), the behavior language pack will
     * be loaded from <tt>"/click-jquery/templates/de.js"</tt>.
     *
     * @param templatesLangPath the path of the template language pack to render
     */
    public void setTemplatesLangPath(String templatesLangPath) {
        this.templatesLangFolder = templatesLangPath;
    }

    /**
     * Return the number of milliseconds to wait before the Ajax request is
     * invoked.
     *
     * @see #setDelay(int)
     *
     * @return the number of milliseconds to wait before the Ajax request is
     * invoked
     */
    public int getDelay() {
        return delay;
    }

    /**
     * Set the number of milliseconds to wait before the Ajax request is
     * invoked, default value is 0, meaning requests are invoked immediately.
     * <p/>
     * <b>Please note:</b> all other Ajax requests invoked within the
     * delay period, is merged into a single request.
     *
     * @param delay the delay to wait before the Ajax request is invoked
     */
    public void setDelay(int delay) {
        this.delay = delay;
    }

    /**
     * Return the data model for the JavaScript {@link #template}.
     *
     * @return the data model for the JavaScript template
     */
    public Map<String, Object> getModel() {
        if (model == null) {
            model = new HashMap<String, Object>();
        }
        return model;
    }

    /**
     * Return the Ajax request parameter Map.
     *
     * @return the Ajax request parameter Map
     */
    public Map<String, Object> getData() {
        if (data == null) {
            data = new HashMap<String, Object>(2);
        }
        return data;
    }

    /**
     * Return true if the Ajax request has parameters, false otherwise.
     *
     * @return true if the Ajax request has parameters, false otherwise
     */
    public boolean hasData() {
        return data == null || data.isEmpty() ? false : true;
    }

    /**
     * Set the Ajax request parameter.
     *
     * @param data the Ajax request parameters
     */
    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    /**
     * Set an Ajax request parameter with the given name and value.
     *
     * @param name the name of the parameter
     * @param value the value of the parameter
     */
    public void setData(String name, Object value) {
        if (name == null) {
            throw new IllegalArgumentException("Null name data parameter");
        }

        if (value != null) {
            getData().put(name, value);
        } else {
            getData().remove(name);
        }
    }

    /**
     * Return true if the Ajax indicator (busy indicator) should be shown,
     * false otherwise.
     *
     * @return true if the Ajax indicator (busy indicator) should be shown,
     * false otherwise
     */
    public boolean isShowBusyIndicator() {
        return showBusyIndicator;
    }

    /**
     * Set whether an Ajax indicator (busy indicator) should be shown during
     * Ajax requests.
     *
     * @param showBusyIndicator indicates whether an Ajax indicator should be shown
     * during Ajax requests
     */
    public void setShowBusyIndicator(boolean showBusyIndicator) {
        this.showBusyIndicator = showBusyIndicator;
    }

    /**
     * @return the skipHeadElements
     */
    public boolean isSkipHeadElements() {
        return skipHeadElements;
    }

    /**
     * @param skipHeadElements true if headElements should not be rendered,
     * false otherwise
     */
    public void setSkipHeadElements(boolean skipHeadElements) {
        this.skipHeadElements = skipHeadElements;
    }

    /**
     * @return the skipSetup
     */
    public boolean isSkipSetupScript() {
        return skipSetupScript;
    }

    /**
     * @param skipSetupScript the skipSetup to set
     */
    public void setSkipSetupScript(boolean skipSetupScript) {
        this.skipSetupScript = skipSetupScript;
    }

    public JsScript getSetupScript() {
        return setupScript;
    }

    /**
     * Set a custom setup script which override the default rendered script.
     * <p/>
     * The Behavior {@link #createTemplateModel(org.apache.click.Page, org.apache.click.Control, org.apache.click.Context) model}
     * values will be passed to the JsScript if the script {@link org.apache.click.element.JsScript#setTemplate(java.lang.String) template}
     * is set.
     * <p/>
     * <b>Please note:</b> specifying your own setup script implies using your
     * own JavaScript template, meaning the Behavior {@link #templatesPath}
     * should not be rendered and thus the {@link #templatesPath} will be set
     * to null by this method.
     *
     * @param setupScript the behavior setup script to render
     */
    public void setSetupScript(JsScript setupScript) {
        this.setupScript = setupScript;
        setTemplatesPath(null);
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
     * Set the type of the Ajax request, e.g. GET or POST.
     *
     * @param type the type of the Ajax request
     */
    public void setType(String type) {
        this.type = type;
    }

    public String getSetupScriptId() {
        if (setupScriptId == null) {
            // TODO move to another method and get rid of incoming source argument
            setupScriptId = getTemplatesPath().substring(1).replace('/', '-').replace('.', '-');
        }
        return setupScriptId;
    }

    public void setSetupScriptId(String setupScriptId) {
        this.setupScriptId = setupScriptId;
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
     * @return the busyIndicatorOptions
     */
    public Options getBusyIndicatorOptions() {
        return busyIndicatorOptions;
    }

    /**
     * @return the busyIndicatorOptions
     */
    public boolean hasBusyIndicatorOptions() {
        return busyIndicatorOptions == null || busyIndicatorOptions.isEmpty() ? false : true;
    }

    /**
     * @param busyIndicatorOptions the busyIndicatorOptions to set
     */
    public void setBusyIndicatorOptions(Options busyIndicatorOptions) {
        this.busyIndicatorOptions = busyIndicatorOptions;
    }

    /**
     * @return the busyIndicatorMessage
     */
    public String getBusyIndicatorMessage() {
        // TODO lookup message from bundle first
        return busyIndicatorMessage;
    }

    /**
     * @param busyIndicatorMessage the busyIndicatorMessage to set
     */
    public void setBusyIndicatorMessage(String busyIndicatorMessage) {
        this.busyIndicatorMessage = busyIndicatorMessage;
    }

    /**
     * @return the busyIndicatorTarget
     */
    public String getBusyIndicatorTarget() {
        return busyIndicatorTarget;
    }

    /**
     * @param busyIndicatorTarget the busyIndicatorTarget to set
     */
    public void setBusyIndicatorTarget(String busyIndicatorTarget) {
        this.busyIndicatorTarget = busyIndicatorTarget;
    }

    /**
     * @return the timeout
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * @param timeout the timeout to set
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * @return the timeoutRetryLimit
     */
    public int getTimeoutRetryLimit() {
        return timeoutRetryLimit;
    }

    /**
     * @param timeoutRetryLimit the timeoutRetryLimit to set
     */
    public void setTimeoutRetryLimit(int timeoutRetryLimit) {
        this.timeoutRetryLimit = timeoutRetryLimit;
    }

    public static void addSupportedLanguages(String... languages) {
        if (languages == null) {
            throw new IllegalArgumentException("Languages cannot be null");
        }

        String[] newLanguages = new String[SUPPORTED_LANGUAGES.length + languages.length];
        System.arraycopy(SUPPORTED_LANGUAGES, 0, newLanguages, 0, SUPPORTED_LANGUAGES.length);
        SUPPORTED_LANGUAGES = newLanguages;
    }

    // Behavior Methods -------------------------------------------------------

    /**
     * If the behavior specifies an eventType, the incoming request must
     * have a eventType parameter that matches the behavior eventType before
     * this method is invoked.
     *
     * If no eventType is specified, the onAction is always called if the
     * behavior is the Ajax target.
     */
    public ActionResult onAction(Control source, JQEvent eventType) {
        return null;
    }

    // Callback Methods -------------------------------------------------------

    @Override
    public final ActionResult onAction(Control source) {
        Context context = getContext();
        String whichTypeParam = context.getRequestParameter("which");

        JQEvent event = new JQEvent();
        event.setType(getEventType());
        event.setWhich(whichTypeParam);
        return onAction(source, event);
    }

    @Override
    public boolean isAjaxTarget(Context context) {
        String eventTypeParam = context.getRequestParameter("event");
        if (StringUtils.isBlank(eventTypeParam) && StringUtils.isBlank(getEventType())) {
            return true;
        }
        return StringUtils.equalsIgnoreCase(eventTypeParam, getEventType());
    }

    @Override
    public void preRenderHeadElements(Control source) {
        // If headElements should be skipped, exit early
        if (isSkipHeadElements()) {
            return;
        }

        super.preRenderHeadElements(source);

        // If setup script should be skipped, exit early
        if (!isSkipSetupScript()) {
            addSetupScript(source);
        }
    }

    /**
     * Two JQBehaviors are equal if their {@link #eventType} is equal. This
     * ensures that a control can only have one behavior of a certain type. It
     * also prevents memory leaks in stateful pages.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     *
     * @param o the object with which to compare this instance with
     * @return true if the specified object is the same as this object
     */
    @Override
    public boolean equals(Object o) {

        //1. Use the == operator to check if the argument is a reference to this object.
        if (o == this) {
            return true;
        }

        //2. Use the instanceof operator to check if the argument is of the correct type.
        if (!(o instanceof JQBehavior)) {
            return false;
        }

        //3. Cast the argument to the correct type.
        JQBehavior that = (JQBehavior) o;

        String localEventType = getEventType();
        String thatEventType = that.getEventType();
        return localEventType == null ? thatEventType == null : localEventType.equals(thatEventType);
    }

    /**
     * A JQBehavior hash code value is based on its {@link #eventType}. This
     * ensures that a control can only have one behavior of a certain type. It
     * also prevents memory leaks in stateful pages.
     *
     * @see java.lang.Object#hashCode()
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + (getEventType() == null ? 0 : getEventType().hashCode());
        return result;
    }

    // Protected methods ------------------------------------------------------

    /**
     * Create a default data model for the Ajax {@link #template}.
     * <p/>
     * The following values are automatically added to the model:
     * <ul>
     * <li>"<span color="blue">context</span>" - the request context path e.g: '/myapp'</li>
     * <li>"<span color="blue">control</span>" - the behavior's source control</li>
     * <li>"{@link #cssSelector}" - the CSS selector</li>
     * <li>"{@link #eventType event}" - the event that initiates the Ajax request</li>
     * <li>"<span color="blue">productionMode</span>" - true if Click is running
     * in a production mode (production or profile), false otherwise</li>
     * <li>"{@link #url url}" - the Ajax request URL</li>
     * <li>"{@link #type}" - the type of the Ajax request, eg POST or GET</li>
     * <li>"{@link #delay}" - the time to wait before making the Ajax request.
     * If more Ajax requests are fired within this period, they are merged into
     * a single request.</li>
     * <li>"{@link #timeout}" - how long to wait before wait before the Ajax
     * request should be canceled.</li>
     * <li>"{@link #timeoutRetryLimit}" - the number of times a {@link #timeout timed out}
     * Ajax request should be retried before giving up.</li>
     * <li>"{@link #showBusyIndicator}" - flag indicating whether a busy
     * indicator is shown or not</li>
     * <li>"{@link #busyIndicatorOptions}"</span> - the Ajax indicator options. Note
     * that {@link #busyIndicatorMessage} is rendered as part of the options</li>
     * <li>"{@link #busyIndicatorTarget}" - the target element of the Ajax indicator</li>
     * <li>"{@link #data}" - the Ajax request parameters</li>
     * </ul>
     *
     * @return the default data model for the Ajax template
     */
    protected Map<String, Object> createTemplateModel(Page page, Control source, Context context) {

        Map<String, Object> templateModel = new HashMap<String, Object>(getModel());

        String localEventType = getEventType();
        boolean bindableEvent = JQEvent.isBindableEvent(localEventType);

        if (bindableEvent) {
            addModel(templateModel, "event", localEventType, page, context);
        }

        String localCssSelector = getCssSelector();
        if (localCssSelector == null) {

            localCssSelector = ClickUtils.getCssSelector(source);
            if (localCssSelector == null) {
                throw new IllegalStateException("Control {"
                    + source.getClass().getSimpleName() + ":"
                    + source.getName()
                    + "} has no css selector defined. Either set a proper CSS"
                    + " selector or set JQBehavior.setSkipSetup(true).");
            }
        }

        addModel(templateModel, "context", context.getRequest().getContextPath(), page, context);
        addModel(templateModel, "cssSelector", localCssSelector, page, context);

        String localBusyIndicatorMessage = getBusyIndicatorMessage();

        // If set, add message to options
        if (localBusyIndicatorMessage != null) {
            getBusyIndicatorOptions().put("message", localBusyIndicatorMessage);
        }

        if (!isShowBusyIndicator()) {
            addModel(templateModel, "showBusyIndicator", false, page, context);
        }
        if (hasBusyIndicatorOptions()) {
            addModel(templateModel, "busyIndicatorOptions", getBusyIndicatorOptions(), page, context);
        }
        if (getBusyIndicatorTarget() != null) {
            addModel(templateModel, "busyIndicatorTarget", getBusyIndicatorTarget(), page, context);
        }

        String localUrl = getUrl();
        if (localUrl != null) {
            addModel(templateModel, "url", localUrl, page, context);
        }
        if (!"GET".equals(getType())) {
            addModel(templateModel, "type", getType(), page, context);
        }
        if (getDelay() > 0) {
            addModel(templateModel, "delay", getDelay(), page, context);
        }

        if (getTimeout() != 20000) {
            addModel(templateModel, "timeout", getTimeout(), page, context);
        }

        if (getTimeoutRetryLimit() != 3) {
            addModel(templateModel, "timeoutRetryLimit", getTimeoutRetryLimit(), page, context);
        }

        if (hasData()) {
            addModel(templateModel, "data", serialize(getData()), page, context);
        }

        return templateModel;
    }

    /**
     * Add the necessary JavaScript imports and scripts to the given
     * headElements list to enable Ajax requests.
     *
     * @param headElements the list which to add all JavaScript imports and
     * scripts to enable Ajax requests
     */
    @Override
    protected void addHeadElementsOnce(Control source) {

        List<Element> headElements = source.getHeadElements();

        int index = 0;
        JsImport jsImport = new JsImport(jqueryPath);
        if (!headElements.contains(jsImport)) {
            headElements.add(index, jsImport);
        }

        index++;
        jsImport = new JsImport(jqueryClickPath);
        if (!headElements.contains(jsImport)) {
            headElements.add(index, jsImport);
        }

        if (isShowBusyIndicator()) {
            index++;
            jsImport = new JsImport(blockUIPath);
            if (!headElements.contains(jsImport)) {
                headElements.add(index, jsImport);
            }
        }

        jsImport = new JsImport(getTemplatesPath());
        if (!headElements.contains(jsImport)) {
            headElements.add(jsImport);
        }

        String language = getLocale().getLanguage();

        // English is default language, only include language pack if other
        // than English
        if (!"en".equals(language)) {
            jsImport = new JsImport(getTemplatesLangFolder() + language + ".js");
            jsImport.setAttribute("charset", "UTF-8");
            headElements.add(jsImport);
        }

        // TODO Add production modes to context to quicken this check
        ServletContext servletContext = getContext().getServletContext();
        ConfigService configService = ClickUtils.getConfigService(servletContext);

        // If Click is running in development modes, enable JavaScript debugging
        if (!configService.isProductionMode() && !configService.isProfileMode()) {
            addJSDebugScript(headElements);
        }
    }

    protected void addSetupScript(Control source) {
        List<Element> headElements = source.getHeadElements();

        // Check if user set a custom setup script
        JsScript script = getSetupScript();

        if (script != null) {
            // Set the user defined setup script

            if (script.getTemplate() != null) {
                // Get template model
                Map<String, Object> templateModel = createTemplateModel(page, source, getContext());

                // Copy script model over templateModel
                Map<String, Object> scriptModel = script.getModel();
                if (scriptModel != null) {
                    templateModel.putAll(scriptModel);
                }

                // Set templateModel as script model
                script.setModel(templateModel);
            }

            // First remove the script to cater for stateful pages (Seems strange
            // to remove and add the same instance, but JsScript is compared
            // against it's ID attribute
            headElements.remove(script);
            headElements.add(script);

        } else {
            // Create a default setup script
            script = createSetupScript();

            // EventType is immutable so should be ok for subsequent requests
            script.setId(getSetupScriptId(source, getEventType()));

            headElements.remove(script);

            setupScript(script, source);

            headElements.add(script);
        }
    }

    protected void setupScript(JsScript script, Control source) {
        Map templateModel = createTemplateModel(page, source, getContext());
        templateModel.remove("context");
        String json = new JSONWriter().write(templateModel);

        HtmlStringBuffer buffer = new HtmlStringBuffer();
        buffer.append("jQuery(document).ready(function(){");
        buffer.append("Click.jq.ajaxTemplate(");
        buffer.append(json);
        buffer.append(");");
        buffer.append("});");

        script.setContent(buffer.toString());
    }

    protected JsScript createSetupScript() {
        JsScript script = new JsScript();
        // Script should execute each time in case properties changed.
        script.setRenderId(false);
        return script;
    }

    protected void addModel(Map<String, Object> model, String key, Object value, Page page, Context context) {
        Object pop = model.put(key, value);
        if (pop != null && page != null && !page.isStateful()) {
            ConfigService configService = ClickUtils.getConfigService(context.getServletContext());
            LogService logger = configService.getLogService();

            String msg = page.getClass().getName() + " on " + page.getPath()
                         + " model contains an object keyed with reserved "
                         + "name \"" + key + "\". The behavior model object "
                         + pop + " has been replaced with the " + key + "object";
            logger.warn(msg);
        }
    }

    protected String getSetupScriptId(Control source, String event) {
        StringBuilder builder = new StringBuilder();
        builder.append(getSetupScriptId());

        String postfix = source.getId();
        if (postfix == null) {
            postfix = source.getName();
        }

        if (postfix != null) {
            builder.append('_');
            builder.append(postfix);
        }

        builder.append('_');
        builder.append(event);
        return builder.toString();
    }

    /**
     * Returns the <tt>Locale</tt> that should be used in this behavior. The
     * returned locale must be present in the list of {@link #SUPPORTED_LANGUAGES}.
     * <p/>
     * If a locale is not currently supported you can set the
     * {@link #SUPPORTED_LANGUAGES} manually.
     *
     * @return the locale that should be used in this behavior
     */
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

}
