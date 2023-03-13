package net.sf.click.jquery.examples.page;

import net.sf.click.jquery.examples.control.DesktopMenu;
import net.sf.click.jquery.examples.services.ApplicationRegistry;
import net.sf.click.jquery.examples.services.CustomerService;
import net.sf.click.jquery.examples.services.PostCodeService;
import net.sf.click.jquery.examples.util.JQUILibrary;
import org.apache.click.Context;
import org.apache.click.Page;
import org.apache.click.extras.control.Menu;
import org.apache.click.extras.control.MenuFactory;
import org.apache.click.util.ClickUtils;



public class BorderPage extends Page {
  private static final long serialVersionUID = -4223387575202278283L;

  private final Menu rootMenu = new MenuFactory().getRootMenu(DesktopMenu.class);

  public BorderPage() {
    // Set the default JQuery UI style
    JQUILibrary.style = "ui-lightness";

    String className = getClass().getName();

    String shortName = className.substring(className.lastIndexOf('.') + 1);
    String title = ClickUtils.toLabel(shortName);
    addModel("title", title);

    final String srcPath = className.replace('.', '/') + ".java";
    addModel("srcPath", srcPath);

    addControl(rootMenu);
    addSourceCodeMenus(rootMenu, srcPath);
  }


  /**
   * @see #getTemplate()
   */
  @Override public String getTemplate() {
    return "border-template.htm";
  }

  public CustomerService getCustomerService() {
    return ApplicationRegistry.getInstance().getCustomerService();
  }

  public PostCodeService getPostCodeService() {
    return ApplicationRegistry.getInstance().getPostCodeService();
  }


  private void addSourceCodeMenus(final Menu rootMenu, final String srcPath) {

    Context context = getContext();

    // Add menu for Java Source code
    DesktopMenu pageJavaMenu = new DesktopMenu("pageJava", " Page Java");
    pageJavaMenu.setPath("source-viewer.htm?filename=WEB-INF/classes/" + srcPath);
    pageJavaMenu.setAccessController(rootMenu.getAccessController());

    if (!rootMenu.contains(pageJavaMenu)) {
      pageJavaMenu.setImageSrc("/assets/images/lightbulb1.png");
      pageJavaMenu.setTitle("Page Java source");
      pageJavaMenu.setTarget("_blank");
      rootMenu.add(pageJavaMenu);
    }

    // Add menu for Html Source code
    DesktopMenu pageHtmlMenu = new DesktopMenu("pageHtml", " Page HTML");
    pageHtmlMenu.setPath("source-viewer.htm?filename=" + context.getPagePath(getClass()));
    pageHtmlMenu.setAccessController(rootMenu.getAccessController());

    if (!rootMenu.contains(pageHtmlMenu)) {
      pageHtmlMenu.setTitle("Page Content source");
      pageHtmlMenu.setTarget("_blank");
      pageHtmlMenu.setImageSrc("/assets/images/lightbulb2.png");
      rootMenu.add(pageHtmlMenu);
    }
  }
}