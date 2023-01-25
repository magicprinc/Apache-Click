package net.sf.click.jquery.behavior;

import lombok.NonNull;
import org.apache.click.Context;
import org.apache.click.Control;
import org.apache.click.Page;
import org.apache.click.ajax.DefaultAjaxBehavior;
import org.apache.click.element.Element;
import org.apache.click.element.JsScript;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.click.util.MessagesMap;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Base Abstract jQuery Behavior class.
 */
public abstract class AbstractJQBehavior extends DefaultAjaxBehavior {


  public static final String BEHAVIOR_MESSAGES = "jq-behavior";

  /**
   * The JQuery library (http://jquery.com/):
   * "<tt>/click-jquery/jquery-1.4.2.js</tt>".
   */
  public static String jqueryPath = "/click-jquery/jquery-1.4.2.js";

  /**
   * The JQuery Click library:
   * "<tt>/click-jquery/jquery.click.js</tt>".
   * <p/>
   * This library includes JQuery Taconite plugin
   * (http://www.malsup.com/jquery/taconite/), JQuery LiveQuery plugin
   * (http://docs.jquery.com/Plugins/livequery)
   * and utility JavaScript functions.
   */
  public static String jqueryClickPath = "/click-jquery/jquery.click.js";

  /**
   * The JQuery blockUI plugin (http://malsup.com/jquery/block/):
   * "<tt>/click-jquery/blockui/jquery.blockUI.2.31.js</tt>".
   */
  public static String blockUIPath = "/click-jquery/blockui/jquery.blockUI.2.31.js";



  /** Page provides access to its localized messages. */
  protected Page page;

  /** The Behavior localized messages Map. */
  protected transient Map<String, String> messages;

  // Public Methods ---------------------------------------------------------

  /**
   * Return a Map of localized messages for this behavior.
   *
   * @return a Map of localized messages for the behavior
   */
  public Map<String, String> getMessages() {
    if (messages == null){
      messages = new MessagesMap(getClass(), BEHAVIOR_MESSAGES);
    }
    return messages;
  }

  public String getMessage (@NonNull String name) {

    String message = null;

    if (page != null) {
      message = getPageMessage(page, name);
    }

    if (message == null && getMessages().containsKey(name)){
      message = getMessages().get(name);
    }

    return message;
  }

  public String getMessage(String name, Object... args) {
    String value = getMessage(name);
    if (value == null) {
      return null;
    }
    return MessageFormat.format(value, args);
  }

  // Callback Methods -------------------------------------------------------

  @Override
  public void preRenderHeadElements(Control source) {
    if (page == null) {
      // Try and retrieve page from source. If source parent page is not
      // set, page will be null
      page = ClickUtils.getParentPage(source);
    }

    super.preRenderHeadElements(source);
  }

  // Protected Methods ------------------------------------------------------

  protected String getPageMessage(Page page, String name) {
    String message = null;
    if (page.getMessages().containsKey(name)) {
      message = page.getMessages().get(name);
    }
    return message;
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
   * Add a special {@link org.apache.click.element.JsScript} which enables
   * detailed JavaScript log output to the given headElements list.
   * <p/>
   * <b>Please note:</b> use the Firefox browser and Firebug plugin to view
   * the logged output. However other browsers might also support logging
   * output.
   *
   * @param headElements list which to add the debug script to
   */
  protected void addJSDebugScript(List<Element> headElements) {
    JsScript jsScript = new JsScript();
    jsScript.setId("enable_js_debugging");
    if (headElements.contains(jsScript)) {
      return;
    }

    HtmlStringBuffer buffer = new HtmlStringBuffer(100);
    buffer.append("if (typeof jQuery !== 'undefined') {\n");
    buffer.append("  if (typeof jQuery.taconite !== 'undefined') {jQuery.taconite.debug=true}\n");
    buffer.append("  if (typeof Click !== 'undefined' && typeof Click.jq !== 'undefined') {Click.jq.debug = true}\n");
    buffer.append("}");
    jsScript.setContent(buffer.toString());
    headElements.add(jsScript);
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
  protected String serialize(Map<String, Object> parameters) {
    HtmlStringBuffer buffer = new HtmlStringBuffer();
    Set<Entry<String, Object>> entries = parameters.entrySet();
    for (Iterator<Entry<String, Object>> it = entries.iterator(); it.hasNext(); ) {
      Entry<String, Object> entry = it.next();
      String key = entry.getKey();
      if (key == null) {
        continue;
      }
      Object value = entry.getValue();
      String encodedValue = ClickUtils.encodeURL(value);
      buffer.append(key);
      buffer.append("=");
      buffer.append(encodedValue);
      if (it.hasNext()) {
        buffer.append("&");
      }
    }
    return buffer.toString();
  }
}