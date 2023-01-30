package net.sf.click.ajax4click;

import org.apache.click.ActionResult;
import org.apache.click.Context;
import org.apache.click.Control;
import org.apache.click.element.CssStyle;
import org.apache.click.element.Element;
import org.apache.click.element.JsScript;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.click.util.PageImports;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Provides an ActionResult that can return multiple results or Controls to the
 * browser. The results could be any combination of Controls and HTML snippets.
 * <p/>
 * The MultiActionResult will render each result in a block consisting of a
 * <tt>name</tt> and content. This allows the browser to easily extract each
 * piece of content and provide access to it through it's name.
 * <p/>
 * Here is an example:
 * <pre class="prettyprint">
 * public void onInit() {
 *     ActionLink link = new ActionLink("link");
 *     link.setId("myLink");
 *     link.addBehavior(new JQBehavior() {
 *         public ActionResult onAction(Control source, JQEvent event) {
 *             MultiActionResult result = new MultiActionResult();
 *
 *              // Add a table to the result
 *              result(table);
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
public class MultiActionResult extends ActionResult {


  private static final String HEAD_ELEMENTS = "headElements";

  private static final String SCRIPTS = "scripts";

  // Variables --------------------------------------------------------------

  protected Map<String, Object> sections = null;

  protected boolean renderHeadElements = true;

  protected boolean renderJsScripts = true;

  // Constructors -----------------------------------------------------------

  /**
   * Create a new default MultiActionResult with it's {@link #setContentType(java.lang.String) contentType}
   * set to {@link #HTML}.
   */
  public MultiActionResult() {
    setContentType(HTML);
  }

  /**
   * Create a new MultiActionResult containing the given control.
   * The {@link #setContentType(java.lang.String) contentType} will be set to
   * {@link #HTML}.
   */
  public MultiActionResult(Control control) {
    add(control);
    setContentType(HTML);
  }

  // Public methods ---------------------------------------------------------

  /**
   * @param control the control to add
   */
  public void add(Control control) {
    if (control.getName() == null) {
      throw new IllegalThreadStateException("control name cannot be null");
    }
    add(control.getName(), control);
  }

  /**
   * Remove the given control from the list of controls.
   *
   * @param control the control to remove
   */
  public void remove(Control control) {
    if (control.getName() == null) {
      throw new IllegalThreadStateException("control name cannot be null");
    }
    remove(control.getName());
  }

  /**
   * @return the list of content
   */
  public Map<String, Object> getSections() {
    if (sections == null) {
      sections = new HashMap<>();
    }
    return sections;
  }

  /**
   * @param section the section to add
   */
  public void add(String sectionName, Object section) {
    getSections().put(sectionName, section);
  }

  /**
   * Remove the given section from the map of sections.
   *
   * @param sectionName the name of the section to remove
   * @return the section that was removed
   */
  public Object remove(String sectionName) {
    return getSections().remove(sectionName);
  }

  /**
   * @return the {@link #renderHeadElements} flag.
   */
  public boolean isRenderHeadElements() {
    return renderHeadElements;
  }

  /**
   * @param renderHeadElements the renderHeadElements flag to set
   */
  public void setRenderHeadElements(boolean renderHeadElements) {
    this.renderHeadElements = renderHeadElements;
  }

  /**
   * @return the {@link #renderJsScripts} flag.
   */
  public boolean isRenderJsScripts() {
    return renderJsScripts;
  }

  /**
   * @param renderJsScripts the renderJsScripts flag to set
   */
  public void setRenderJsScripts(boolean renderJsScripts) {
    this.renderJsScripts = renderJsScripts;
  }

  /**
   * Render the XML representation of JQTaconite to the given buffer.
   *
   * @param buffer the buffer to render output to
   */
  public void render(HtmlStringBuffer buffer) {
    renderHeadElements(buffer);
    renderSections(buffer);
  }

  /**
   * Return a HTML representation of HtmlResult.
   *
   * @return the HTML representation of HtmlResult
   */
  @Override
  public String toString() {
    HtmlStringBuffer buffer = new HtmlStringBuffer(150);
    render(buffer);
    return buffer.toString();
  }

  // Protected methods ------------------------------------------------------

  /**
   * Render HtmlResult to the specified response.
   *
   * @param context the request context
   */
  @Override
  protected void renderActionResult(Context context) {
    setReader(new StringReader(toString()));
    super.renderActionResult(context);
  }

  /**
   * Render the Controls content to the given buffer.
   *
   * @param buffer the buffer to render to
   */
  protected void renderSections(HtmlStringBuffer buffer) {
    for (Entry<String, Object> entry : getSections().entrySet()) {
      renderSeparator(buffer, entry.getKey());
      Object section = entry.getValue();
      if (section instanceof Control) {
        ((Control) section).render(buffer);
      } else {
        if (section != null) {
          buffer.append(section);
        }
      }
    }
  }

  /**
   * Render a separator between each Control and also the list of HEAD elements.
   * The separator can be used by JavaScript to identify where each Control's
   * content starts and ends.
   *
   * @param buffer the buffer to render to
   * @param separatorName the name of the separator to render
   */
  protected void renderSeparator(HtmlStringBuffer buffer, String separatorName) {
    buffer.append("<!--* ");
    buffer.append("Clk:'");
    buffer.append(separatorName);
    buffer.append("' *-->\n");
  }

  /**
   * Render the Control HEAD elements to the given buffer.
   *
   * @param buffer the buffer to render to
   */
  protected void renderHeadElements(HtmlStringBuffer buffer) {
    if (!isRenderHeadElements() && !isRenderJsScripts()) {
      return;
    }

    HeadElementsHolder holder = getHeadElements();

    if (isRenderHeadElements()) {
      List<Element> headElements = holder.headElements;
      if (!headElements.isEmpty()) {
        renderSeparator(buffer, HEAD_ELEMENTS);

        prepareHeadElementsForRendering(headElements);
        for (Element element : headElements) {
          element.render(buffer);
          buffer.append("\n");
        }
      }
    }

    if (isRenderJsScripts()) {
      List<JsScript> jsScriptElements = holder.jsScriptElements;
      if (!jsScriptElements.isEmpty()) {
        renderSeparator(buffer, SCRIPTS);
        prepareJsScriptForRendering(jsScriptElements);
        for (JsScript jsScript : jsScriptElements) {
          jsScript.render(buffer);
          buffer.append("\n");
        }
      }
    }
  }

  // private methods --------------------------------------------------------

  /**
   * Return all the head elements for this ActionResult {@link #getControls()}
   * in two separate lists, with CSS and JsImport elements in one list and
   * JsScript elements in the other.
   *
   * @return all the head elements for the controls of this ActionResult
   */
  private HeadElementsHolder getHeadElements() {
    PageImports pageImports = new PageImports(null);
    for (Object section : getSections().values()) {
      if (section instanceof Control) {
        processHeadElements((Control) section, pageImports);
      }
    }

    List<Element> headElements = pageImports.getHeadElements();
    List<JsScript> jsScriptElements = new ArrayList<>();
    List<Element> allJsElements = pageImports.getJsElements();

    // Place all JsScripts at the bottom of the command list which ensures
    // that new HTML elements added through JQTaconite are present in the DOM
    // when scripts are executed. Otherwise scripts which target elements
    // will fail as the elements are only loaded after the script executes
    orderJavaScriptElements(allJsElements, jsScriptElements, headElements);

    HeadElementsHolder holder = new HeadElementsHolder();
    holder.headElements = headElements;
    holder.jsScriptElements = jsScriptElements;

    return holder;
  }

  /**
   * Prepare the JsScript to be rendered back to the browser. Preparation
   * include:
   * - ensure jsScripts are XML safe by wrapping content in CDATA tags.
   * - since this is an Ajax request and there is no DOM ready event, ensure
   * the DOM ready function, addLoadEvent, is not rendered
   *
   * @param jsScripts the JsScripts to prepare for rendering
   */
  private void prepareJsScriptForRendering(List<JsScript> jsScripts) {
    for (JsScript jsScript : jsScripts) {
      jsScript.setCharacterData(true);

      // If any JavaScript relies on a DOM ready function, we need to
      // ensure this function is *not* rendered because Ajax requests
      // does not trigger DOM ready functions.
      if (jsScript.isExecuteOnDomReady()) {
        jsScript.setExecuteOnDomReady(false);

        // Nullify the ID attribute otherwise the script won't be
        // included in the Page by the jquery.click.js script
        jsScript.setId(null);
      }
    }
  }

  /**
   * Prepare the elements to be rendered back to the browser. Preparation
   * include:
   * - ensure elements are XML safe by wrapping content in CDATA tags.
   *
   * @param elements the HEAD elements to prepare for rendering
   */
  private void prepareHeadElementsForRendering(List<Element> elements) {
    for (Element element : elements) {
      if (element instanceof CssStyle) {
        // Ensure CssStyle content is wrapped in CDATA tags because the
        // content must be valid XML
        ((CssStyle) element).setCharacterData(true);
      }
    }
  }

  /**
   * Add the JavaScript elements (JsImport and JsScript) to either one of the
   * two given lists. JsScript elements must be added to jsScriptsCommand
   * and JsImport elements must be added to headElementsCommand.
   *
   * @param headElementsCommand the command added as the first taconite command
   * @param jsScriptsCommand the command added as the last taconite command
   * @param jsElements list of JsScript elements
   */
  private void orderJavaScriptElements(List<Element> allJsElements,
      List<JsScript> jsScriptElements, List<Element> headElements) {

    for (Element element : allJsElements){
      if (element instanceof JsScript jsScript){
        jsScriptElements.add(jsScript);
      } else {
        headElements.add(element);
      }
    }
  }

  /**
   * Process the HEAD elements of the given control and add the elements to
   * pageImports.
   *
   * @param command the command to process
   * @param pageImports the pageImports to add HEAD elements to
   */
  private void processHeadElements(Control control, PageImports pageImports) {
    // Add all controls to the PageImports instance

    // Ensure the head elements are included
    pageImports.processControl(control);
  }

  /**
   * Holds the HTML HEAD Elements in two lists, one containing all JsImport,
   * CssImport and CssStyles and the other containing JsScripts. By separating
   * the scripts allows the client to process JsScripts separated from the other
   * elements.
   */
  private static class HeadElementsHolder {
    public List<Element> headElements = new ArrayList<>();

    public List<JsScript> jsScriptElements = new ArrayList<>();
  }
}