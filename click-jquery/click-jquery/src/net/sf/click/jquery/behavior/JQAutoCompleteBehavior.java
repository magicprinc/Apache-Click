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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import net.sf.click.jquery.JQEvent;
import net.sf.click.jquery.util.JSONWriter;
import net.sf.click.jquery.util.Options;
import org.apache.click.Context;
import org.apache.click.Control;
import org.apache.click.Page;
import org.apache.click.ActionResult;
import org.apache.click.control.Field;
import org.apache.click.element.CssImport;
import org.apache.click.element.Element;
import org.apache.click.element.JsImport;
import org.apache.click.element.JsScript;
import org.apache.click.service.ConfigService;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.HtmlStringBuffer;

/**
 * Provide a specialized JQuery helper that adds auto complete functionality
 * to a target Field control.
 * <p/>
 * This helper has an associated JavaScript template that can be modified
 * according to your needs. Click <a href="../../../../../js/template/jquery.autocomplete.template.js.txt">here</a>
 * to view the template.
 * <p/>
 * When text is entered in a field an Ajax request is made back to the server
 * with the entered text as a request parameter. On the server a list of
 * suggested completions is compiled, based on the entered text, and send back
 * to the browser.
 * <p/>
 * <b>Please note: </b> suggestions must be separated by newline ('\n')
 * character.
 * <p/>
 * JQAutoCompleteHelper can either be embedded inside a custom Field, or used
 * to decorate the Field.
 *
 * <h3>Embedded example</h3>
 *
 * Below is an example of a custom Field with an embedded JQAutoCompleteHelper
 * that enables Ajax behavior:
 *
 * <pre class="prettyprint">
 * public abstract class JQAutoCompleteTextField extends TextField {
 *
 *     // The embedded JQuery AutoComplete helper object.
 *     private JQHelper jqHelper;
 *
 *     // Constructor
 *     public JQAutoCompleteTextField(String name) {
 *         super(name);
 *     }
 *
 *     &#64;Override
 *     public void onInit() {
 *         super.onInit();
 *         AjaxControlRegistry.registerAjaxControl(this);
 *
 *         // Register an Ajax listener which returns the list of suggestions
 *         setActionListener(new AjaxAdapter() {
 *
 *             &#64;Override
 *             public ActionResult onAjaxAction(Control source) {
 *                 ActionResult actionResult = new ActionResult();
 *                 List autocompleteList = getAutoCompleteList(getValue());
 *                 if (autocompleteList != null) {
 *                     HtmlStringBuffer buffer = new HtmlStringBuffer(autocompleteList.size() * 5);
 *                     for (Iterator it = autocompleteList.iterator(); it.hasNext(); ) {
 *                         buffer.append(it.next());
 *                         if (it.hasNext()) {
 *                             buffer.append("\n");
 *                         }
 *                     }
 *                     actionResult.setContent(buffer.toString());
 *                 }
 *                 return actionResult;
 *             }
 *         });
 *     }
 *
 *     &#64;Override
 *     public List getHeadElements() {
 *         if (headElements == null) {
 *             headElements = super.getHeadElements();
 *
 *             getJQueryHelper().addHeadElements(headElements);
 *         }
 *         return headElements;
 *     }
 *
 *     public JQAutoCompleteHelper getJQueryHelper() {
 *         if(jqHelper == null) {
 *             jqHelper = new JQAutoCompleteHelper(this);
 *         }
 *         return jqHelper;
 *     }
 *
 *     protected abstract List getAutoCompleteList(String criteria);
 * } </pre>
 *
 * Below is an example how to decorate a TextField to retrieve suggestions every
 * time the user enters text:
 *
 * <pre class="prettyprint">
 * public class AutoCompleteDemo extends BorderPage {
 *
 *     private Form form = new Form("form");
 *
 *     public AutoCompleteDemo() {
 *         addControl(form);
 *
 *         final TextField countryField = new TextField("country");
 *         form.add(countryField);
 *
 *         // Decorate the countryField with Ajax functionality
 *         JQAutoCompleteBehavior behavior = new JQAutoCompleteBehavior(countryField);
 *
 *         // Register an Ajax listener on the form which is invoked when the
 *         // form is submitted.
 *         countryField.addBehavior(new AjaxBehavior() {
 *
 *             &#64;Override
 *             public ActionResult onAction(Control source) {
 *
 *                 // Create an action result that contains the auto complete suggestions
 *                 ActionResult actionResult = new ActionResult();
 *
 *                 String criteria = countryField.getValue();
 *
 *                 // Retrieve suggestions for the given criteria. Each suggestion
 *                 // should be separated by a newline char: '\n'.
 *                 String suggestions = getSuggestions(criteria);
 *
 *                 actionResult.setContent(suggestions);
 *
 *                 return actionResult;
 *             }
 *         });
 *     }
 * } </pre>
 */
public abstract class JQAutoCompleteBehavior extends JQBehavior {

    // -------------------------------------------------------------- Constants

    private static final long serialVersionUID = 1L;

    /**
     * The JQuery Autocomplete plugin JavaScript import (http://bassistance.de/jquery-plugins/jquery-plugin-autocomplete/):
     * "<tt>/click-jquery/autocomplete/jquery.autocomplete.js</tt>".
     */
    public static String jqueryAutoCompleteJsPath
        = "/click-jquery/autocomplete/jquery.autocomplete.js";

    /**
     * The JQuery Autocomplete plugin CSS import (http://bassistance.de/jquery-plugins/jquery-plugin-autocomplete/):
     * "<tt>/click-jquery/autocomplete/jquery.autocomplete.css</tt>".
     */
    public static String jqueryAutocompleteCssPath
        = "/click-jquery/autocomplete/jquery.autocomplete.css";

    // -------------------------------------------------------------- Variables

    /**
     * The Autocomplete options. Autocomplete options are outlined
     * <a href="http://docs.jquery.com/Plugins/Autocomplete/autocomplete#url_or_dataoptions">here</a>.
     */
    protected Options autoCompleteOptions;

    // ----------------------------------------------------------- Constructors

    /**
     * Create a new JQAutoCompleteHelper for the given target control and CSS
     * selector.
     */
    public JQAutoCompleteBehavior(String eventType) {
        super(eventType);
        setShowBusyIndicator(false);
        //setTemplates(template);
    }

    /**
     * Create a new JQAutoCompleteHelper for the given target control and CSS
     * selector.
     */
    public JQAutoCompleteBehavior() {
        super(JQEvent.KEYUP);
        setShowBusyIndicator(false);
    }

    // Behavior Methods -------------------------------------------------------

    @Override
    public ActionResult onAction(Control source, JQEvent eventType) {
        if (!(source instanceof Field)) {
            throw new IllegalStateException("JQAutoCompleteBehavior source must be a Field, not "
                + source.getClass().getSimpleName());
        }

        Field field = (Field) source;

        ActionResult actionResult = new ActionResult();
        List autocompleteList = getAutoCompleteList(field.getValue());
        if (autocompleteList != null) {
            HtmlStringBuffer buffer = new HtmlStringBuffer(autocompleteList.size() * 5);
            for (Iterator it = autocompleteList.iterator(); it.hasNext();) {
                buffer.append(it.next());
                if (it.hasNext()) {
                    buffer.append("\n");
                }
            }
            actionResult.setContent(buffer.toString());
        }

        return actionResult;
    }

    @Override
    public boolean isAjaxTarget(Context context) {
        // TODO send event from JS to check if this is ajax target
        return true;
    }

    // ------------------------------------------------------ Public Properties

   /**
     * Return true if the behavior has autoCompleteOptions, false otherwise.
     *
     * @see #setOptions(java.lang.String)
     *
     * @return true if any autocomplete options are defined, false otherwise
     */
    public boolean hasAutoCompleteOptions() {
        return autoCompleteOptions != null && !autoCompleteOptions.isEmpty();
    }

    /**
     * Return the Autocomplete options.
     *
     * @see #setOptions(java.lang.String)
     *
     * @return the autocomplete options
     */
    public Options getAutoCompleteOptions() {
        return autoCompleteOptions;
    }

    /**
     * Set the Autocomplete options.
     * <p/>
     * The Autocomplete options are outlined
     * <a href="http://docs.jquery.com/Plugins/Autocomplete/autocomplete#url_or_dataoptions">here</a>.
     * <p/>
     * For example:
     *
     * <pre class="prettyprint">
     * public MyPage extends Page {
     *     public void onInit() {
     *
     *         JQAutoCompleteTextField field = new JQAutoCompleteTextField("field");
     *
     *         // Specify the 'max', 'width' and 'formatResult' options
     *         String options = "max: 6, width: 400, formatResult: function(row, data){return data.split(',')[0];}";
     *
     *         field.getJQBehavior().setAutoCompleteOptions(options);
     *     }
     * } </pre>
     *
     * @param autoCompleteOptions the Autocomplete options
     */
    public void setAutocCompleteOptions(Options autoCompleteOptions) {
        this.autoCompleteOptions = autoCompleteOptions;
    }

    /**
     * Create a default data model for the Autocomplete {@link #template}.
     *
     * @return the default data model for the Ajax template
     */
    @Override
    protected Map<String, Object> createTemplateModel(Page page, Control source,
        Context context) {

        Map<String, Object> templateModel = new HashMap<String, Object>();

        String localCssSelector = getCssSelector();
        if (localCssSelector == null) {
            localCssSelector = ClickUtils.getCssSelector(source);
            if (localCssSelector == null) {
                throw new IllegalStateException("Control {"
                    + source.getClass().getSimpleName() + ":"
                    + source.getName()
                    + "} has no css selector defined. Either set a proper CSS"
                    + " selector or set JQAutoCompleteBehavior.setSkipSetup(true).");
            }
        }

        addModel(templateModel, "cssSelector", localCssSelector, page, context);

        String localUrl = getUrl();
        if (localUrl != null) {
            addModel(templateModel, "url", localUrl, page, context);
        }

        if (hasData()) {
            addModel(templateModel, "data", serialize(getData()), page, context);
        }

        if (hasAutoCompleteOptions()) {
            templateModel.put("options", getAutoCompleteOptions());
        }
        return templateModel;
    }

    /**
     * Add the necessary JavaScript imports and scripts to the given
     * headElements list to enable AutoComplete functionality.
     *
     * @param headElements the list which to add all JavaScript imports and
     * scripts to enable AutoComplete functionality
     */
    @Override
    protected void addHeadElementsOnce(Control source) {
        List<Element> headElements = source.getHeadElements();

        JsImport jsImport = new JsImport(jqueryPath);
        if (!headElements.contains(jsImport)) {
            headElements.add(0, jsImport);
        }

        jsImport = new JsImport(jqueryAutoCompleteJsPath);
        if (!headElements.contains(jsImport)) {
            headElements.add(jsImport);
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

        CssImport cssImport = new CssImport(jqueryAutocompleteCssPath);
        if (!headElements.contains(cssImport)) {
            headElements.add(cssImport);
        }

        // TODO Add production modes to context to quicken this check
        ServletContext servletContext = getContext().getServletContext();
        ConfigService configService = ClickUtils.getConfigService(servletContext);

        // If Click is running in development modes, enable JavaScript debugging
        if (!configService.isProductionMode() && !configService.isProfileMode()) {
            addJSDebugScript(headElements);
        }
    }

    /**
     * Return the list of auto complete suggestions for the given criteria.
     * <p/>
     * When the user enters text into the text field, this method is invoked,
     * passing in the current value of the text field.
     * <p/>
     * This method must be implemented by users to return the list of suggestions
     * based on the current text field value, the criteria.
     *
     * @param criteria the search criteria
     * @return the list of auto complete suggestions
     */
    protected abstract List getAutoCompleteList(String criteria);

    @Override
    protected void setupScript(JsScript script, Control source) {
        Map templateModel = createTemplateModel(page, source, getContext());
        String json = new JSONWriter().write(templateModel);

        HtmlStringBuffer buffer = new HtmlStringBuffer();
        buffer.append("jQuery(document).ready(function(){");
        buffer.append("Click.jq.autoCompleteTemplate(");
        buffer.append(json);
        buffer.append(");");

        buffer.append("});");

        script.setContent(buffer.toString());
    }
}
