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
package net.sf.click.ajax4click.jquery;

import org.apache.click.ActionResult;
import org.apache.click.Context;
import org.apache.click.Control;
import org.apache.click.ajax.DefaultAjaxBehavior;
import org.apache.click.element.Element;
import org.apache.click.element.JsImport;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Provides a jQuery specific behavior with the following features:
 * <ul>
 * <li>you can specify the event (click, blur etc.) JQBehavior should handle.</li>
 * <li>JQBehavior provides it's own <tt>onAction</tt> handler:
 * {@link #onAction(org.apache.click.Control, net.sf.click.ajax4click.jquery.JQEvent)}
 * which passes in an event to inspect which event fired on the browser.
 * </li>
 * <li>adds <tt>jquery-1.4.4.js</tt> to the Page HEAD</li>
 * <li>adds <tt>click-jquery-utils.js</tt> to the Page HEAD</li>
 * </ul>
 *
 * Here is an example usinh JQBehavior:
 * * <pre class="prettyprint">
 * public void onInit() {
 *     ActionLink link = new ActionLink("link");
 *     link.setId("myLink");
 *     link.addBehavior(new JQBehavior() {
 *         public ActionResult onAction(Control source, JQEvent event) {
 *             MultiActionResult result = new MultiActionResult();
 *
                String time = getFormat().date(new Date(), "HH:mm:ss");
 *
                // Add the time under the name 'time'
                result.add("time", "The time is now: " + time);
                return result;
 *         }
 *     });
 * } </pre>
 */
public class JQBehavior extends DefaultAjaxBehavior {

    // Variables --------------------------------------------------------------

    /**
     * The JQuery library (http://jquery.com/):
     * "<tt>/ajax4click/jquery-1.4.4.min.js</tt>".
     */
    public static String jqueryPath = "/ajax4click/jquery/jquery-1.4.4.min.js";

        /**
     * The JQuery Utils script:
     * "<tt>/ajax4click/click-jquery-utils.js</tt>".
     */
      public static String jqueryUtilsPath = "/ajax4click/jquery/click-jquery-utils.js";

      /**
     * The folder where the click-jquery-utils language packs can be found, the default
     * value is {@link #defaultUtilsLangFolder}.
     */
    protected String utilsLangFolder = defaultUtilsLangFolder;

   /**
     * The default path to the click-jquery-utils language packs, default
     * value is <tt>"/ajax4click/lang/"</tt>.
     */
    public static String defaultUtilsLangFolder = "/ajax4click/jquery/lang/";

    protected String eventType = JQEvent.CLICK;

    /** Supported locales. */
    static String[] SUPPORTED_LANGUAGES = { "af", "en" };

    // ----------------------------------------------------------- Constructors
    public JQBehavior(String eventType) {
        // EventType is immutable and cannot be changed at a later stage
        this.eventType = eventType;
    }

    /**
     * Create a new behavior instance.
     */
    public JQBehavior() {
    }

    // Behavior Methods -------------------------------------------------------
    /**
     * If JQBehavior specifies an event, the incoming request must
     * have an event parameter that matches the behavior's event before
     * this method is invoked.
     *
     * If no event is specified, the onAction is always called if this
     * behavior is the Ajax target.
     */
    public ActionResult onAction(Control source, JQEvent eventType) {
        return null;
    }

    // Public methods ---------------------------------------------------------
    public String getEventType() {
        return eventType;
    }

    /**
     * Return the Context of the current request.
     *
     * @return the Context of the current request
     */
    protected Context getContext() {
        return Context.getThreadLocalContext();
    }

    /**
     * Return the folder containing the utils language packs. The default
     * value is {@link #defaultUtilsLangFolder}.
     *
     * @see #setUtilsLangPath(java.lang.String)
     *
     * @return the utils to render for this helper
     */
    public String getUtilsLangFolder() {
        return utilsLangFolder;
    }

    /**
     * Set the path of the utils language pack to render. Specific language
     * packs will be looked up from this folder based on the request's
     * {@link org.apache.click.Context#getLocale() Context locale}.
     * <p/>
     * For example if utilsLangPath is <tt>"/ajax4click/utils/lang/"</tt>
     * and the Context locale is German (de), the behavior language pack will
     * be loaded from <tt>"/ajax4click/utils/de.js"</tt>.
     *
     * @param utilsLangPath the path of the utils language pack to render
     */
    public void setUtilsLangPath(String utilsLangPath) {
        this.utilsLangFolder = utilsLangPath;
    }

    // Callback Methods -------------------------------------------------------

    @Override
    public final ActionResult onAction(Control source) {
        Context context = getContext();
        String eventTypeParam = context.getRequestParameter("event");
        String whichParam = context.getRequestParameter("which");

        JQEvent event = new JQEvent();
        event.setType(eventTypeParam);
        event.setWhich(whichParam);
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

    // Protected methods ------------------------------------------------------

    /**
     * Add the necessary JavaScript imports and scripts to the given
     * headElements list to enable Ajax requests.
     *
     * @param source the control the behavior is registered with
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
        jsImport = new JsImport(jqueryUtilsPath);
        if (!headElements.contains(jsImport)) {
            headElements.add(index, jsImport);
        }

        String language = getLocale().getLanguage();

        // English is default language, only include language pack if other
        // than English
        if (!"en".equals(language)) {
            jsImport = new JsImport(getUtilsLangFolder() + language + ".js");
            jsImport.setAttribute("charset", "UTF-8");
            headElements.add(jsImport);
        }
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