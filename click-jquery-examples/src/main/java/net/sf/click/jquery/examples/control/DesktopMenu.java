package net.sf.click.jquery.examples.control;

import lombok.val;
import net.sf.click.jquery.behavior.JQBehavior;
import org.apache.click.Context;
import org.apache.click.element.CssImport;
import org.apache.click.element.JsImport;
import org.apache.click.element.JsScript;
import org.apache.click.extras.control.Menu;
import org.apache.click.util.HtmlStringBuffer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class DesktopMenu extends Menu {
  private static final long serialVersionUID = 7125842095383261701L;


  protected String cssImport = "/click-jquery/example/desktopmenu/desktopmenu.css";

  // -------------------------------------------------------- Constructor

  public DesktopMenu() {
  }

  public DesktopMenu(String name) {
    super(name);
  }

  public DesktopMenu(String name, String label) {
    super(name);
    setLabel(label);
  }

  // ------------------------------------------------------ Public Properties

  /**
   * Return the HTML href attribute value.
   *
   * @return the HTML href attribute value
   */
  @Override
  public String getHref() {
    // TODO why was this overridden?
    if (getPath() == null) {
      setPath("#");
    } else if (hasChildren() && "".equals(getPath())) {
      setPath("#");
    }
    return super.getHref();
  }

  // --------------------------------------------------------- Public Methods

  @Override
  public List<org.apache.click.element.Element> getHeadElements() {
    String id = getId();
    if (id == null) {
      throw new IllegalStateException("Menu name is not set");
    }

    if (headElements == null) {
      headElements = new ArrayList<>(3);

      headElements.add(new CssImport(cssImport));

      JsImport jsImport = new JsImport(JQBehavior.jqueryPath);
      headElements.add(jsImport);

      jsImport = new JsImport("/click-jquery/example/desktopmenu/jquery.menu.js");
      headElements.add(jsImport);

      Context context = Context.getThreadLocalContext();
      JsScript jsScript = getJsTemplate(context);
      headElements.add(jsScript);

      jsImport = new JsImport("/click/menu-fix-ie6.js");
      jsImport.setConditionalComment(JsImport.IF_LESS_THAN_IE7);
      headElements.add(jsImport);
    }

    // Note, the addLoadEvent script is recreated and checked if it
    // is contained in the headElement.
    JsScript script = new JsScript();
    script.setId(id + "_js_setup");

    // Internal ID is created by jQuery menu plugin
    String internalId = "root-menu-div";

    if (!headElements.contains(script)) {
      // Script must be executed as soon as browser dom is ready
      HtmlStringBuffer buffer = new HtmlStringBuffer();
      buffer.append("jQuery(document).ready(function() {");
      buffer.append(" if(typeof Click != 'undefined' && typeof Click.menu != 'undefined') {\n");
      buffer.append("   if(typeof Click.menu.fixHiddenMenu != 'undefined') {\n");
      buffer.append("     Click.menu.fixHiddenMenu(\"").append(internalId).append("\");\n");
      buffer.append("     Click.menu.fixHover(\"").append(internalId).append("\");\n");
      buffer.append("   }\n");
      buffer.append(" }\n");
      buffer.append("});");
      script.setContent(buffer.toString());
      headElements.add(script);
    }

    return headElements;
  }

  // ------------------------------------------------------ Protected Methods

  protected JsScript getJsTemplate(Context context) {
    val model = new HashMap<String,Object>();
    model.put("context", context.getRequest().getContextPath());
    model.put("selector", ".desktopmenu");
    return new JsScript("/click-jquery/example/template/desktopmenu.template.js", model);
  }

  @Override
  protected void renderRootMenu(HtmlStringBuffer buffer) {
    int depth = 0;
    renderMenuList(buffer, this, depth);
  }

  @Override
  protected void renderMenuListAttributes(HtmlStringBuffer buffer, Menu menu, int depth) {
    if (depth == 0) {
      buffer.appendAttribute("class", "desktopmenu");
    } else {
      buffer.appendAttribute("class", "submenu");
    }
    buffer.append(" ");
  }

  @Override
  protected void renderMenuListItemAttributes(HtmlStringBuffer buffer, Menu menu, int depth) {
    buffer.append(" class=\"menuitem");
    if (depth == 0) {
      buffer.append(" topitem");
      if (menu.getChildren().size() == 0) {
        buffer.append(" empty");
      }
    }

    buffer.append("\" ");
  }

  @Override
  protected void renderSeparator(HtmlStringBuffer buffer, Menu menu) {
  }
}