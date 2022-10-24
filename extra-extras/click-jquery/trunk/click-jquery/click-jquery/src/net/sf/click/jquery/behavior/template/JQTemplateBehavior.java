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
package net.sf.click.jquery.behavior.template;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import net.sf.click.jquery.behavior.AbstractJQBehavior;
import org.apache.click.Context;
import org.apache.click.Control;
import org.apache.click.Page;
import org.apache.click.ActionResult;
import org.apache.click.element.Element;
import org.apache.click.element.JsImport;
import org.apache.click.element.JsScript;
import org.apache.click.service.ConfigService;
import org.apache.click.service.LogService;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.Format;
import org.apache.commons.lang.StringUtils;

/**
 *
 */
public class JQTemplateBehavior extends AbstractJQBehavior implements Serializable {

    // Constants --------------------------------------------------------------

    private static final long serialVersionUID = 1L;

    // Variables --------------------------------------------------------------

    /**
     * The path of the default jQuery templates.
     */
    protected String template;

    protected String templateId;

    /** The data model for the JavaScript {@link #template}. */
    protected Map<String, Object> model;

    /** The Ajax request parameters. */
    //protected Map<String, Object> parameters;

    /** The type request (POST / GET), default value is GET. */
    protected String type = "GET";

    /** The Ajax request url. */
    protected String url;

    /**
     * The CSS Selector for the behavior, defaults to the Controls CSS selector
     * returned by {@link org.apache.click.util.ClickUtils#getCssSelector(org.apache.click.Control)}.
     */
    protected String cssSelector;

    // Constructors -----------------------------------------------------------

    public JQTemplateBehavior() {
    }

    // Public Properties ------------------------------------------------------

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
     * Return the template to render for this behavior.
     *
     * @return the template to render for this behavior
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Set the template to render for this behavior.
     *
     * @param template the template to render for this behavior
     */
    public void setTemplate(String template) {
        this.template = template;
    }

    public String getTemplateId() {
        if (templateId == null) {
            // TODO move to another method and get rid of incoming source argument
            templateId = getTemplate().substring(1).replace('/', '-').replace('.', '-');
        }
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
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
     * Create a default data model for the Ajax {@link #template}.
     * <p/>
     * The following values are automatically added to the model:
     * <ul>
     * <li>"context" - the request context path e.g: '/myapp'</li>
     * <li>"{@link #control}" - the target control</li>
     * <li>"{@link #selector}" - the CSS selector</li>
     * <li>"{@link #event}" - the event that initiates the Ajax request</li>
     * </ul>
     *
     * @return the default data model for the Ajax template
     */
    protected Map<String, Object> createTemplateModel(Page page, Control source) {
        Context context = getContext();

        Map<String, Object> templateModel = new HashMap<String, Object>(getModel());

        if (page != null) {
            addModel(templateModel, "path", page.getPath(), page, context);
        }

        addModel(templateModel, "url", getUrl(), page, context);
        addModel(templateModel, "request", context.getRequest(), page, context);
        addModel(templateModel, "context", context.getRequest().getContextPath(), page, context);

        addModel(templateModel, "type", getType(), page, context);

        Format format = page.getFormat();
        if (format != null) {
            addModel(templateModel, "format", format, page, context);
        }

        Map localMessages = getMessages();
        if (page != null) {
            localMessages.putAll(page.getMessages());
        }
        addModel(templateModel, "messages", localMessages, page, context);

        String localCssSelector = getCssSelector();
        if (localCssSelector == null) {

            localCssSelector = ClickUtils.getCssSelector(source);
            if (localCssSelector == null) {
                throw new IllegalStateException("Control {"
                    + source.getClass().getSimpleName() + ":"
                    + source.getName()
                    + "} has no css selector defined. Please define the control Name"
                    + " or ID or set cssSelector of the behavior: JQTemplateBehavior.setCssSelector(String)");
            }
        }

        addModel(templateModel, "cssSelector", cssSelector, page, context);
        addModel(templateModel, "control", source, page, context);

        return templateModel;
    }

    // Behavior Methods -------------------------------------------------------

    @Override
    public ActionResult onAction(Control source) {
        return null;
    }

    // Callback Methods -------------------------------------------------------

    /**
     * Add the necessary JavaScript imports and scripts to the given
     * headElements list to enable Ajax requests.
     *
     * @param source Control to add to it's headElements all JavaScript imports and
     * scripts to enable Ajax requests
     */
    @Override
    protected void addHeadElementsOnce(Control source) {

        List<Element> headElements = source.getHeadElements();

        JsImport jsImport = new JsImport(jqueryPath);
        if (!headElements.contains(jsImport)) {
            headElements.add(0, jsImport);
        }

        jsImport = new JsImport(jqueryClickPath);
        if (!headElements.contains(jsImport)) {
            headElements.add(1, jsImport);
        }

        // TODO Add production modes to context to quicken this check
        ServletContext servletContext = getContext().getServletContext();
        ConfigService configService = ClickUtils.getConfigService(servletContext);

        // If Click is running in development modes, enable JavaScript debugging
        if (!configService.isProductionMode() && !configService.isProfileMode()) {
            addJSDebugScript(headElements);
        }

        addTemplate(source);
    }

    // Protected Methods ------------------------------------------------------

    /**
     * Add the {@link #template} content to the given headElements list.
     *
     * @param source Control which to add the Ajax template to
     */
    protected void addTemplate(Control source) {
        if (StringUtils.isNotBlank(getTemplate())) {

            JsScript jsScript = new JsScript();
            jsScript.setId(getTemplateId());

            List<Element> headElements = source.getHeadElements();

            // Guard against adding duplicate templates in case of stateful pages
            if (!headElements.contains(jsScript)) {
                jsScript.setTemplate(getTemplate());

                Map<String, Object> templateModel = createTemplateModel(page, source);
                jsScript.setModel(templateModel);

                headElements.add(jsScript);
            }
        }
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
}
