package net.sf.clickclick.examples.page;

import net.sf.clickclick.control.breadcrumb.Breadcrumb;
import net.sf.clickclick.examples.nav.MenuBuilder;
import net.sf.clickclick.examples.services.ApplicationRegistry;
import net.sf.clickclick.examples.services.CustomerService;
import org.apache.click.Page;
import org.apache.click.extras.control.Menu;
import org.apache.click.util.ClickUtils;

import java.io.Serial;

/**
 *
 */
public class BorderPage extends Page {
  @Serial private static final long serialVersionUID = -440182563202305511L;

  public final Menu rootMenu = MenuBuilder.getMenus();

  private final Breadcrumb breadcrumb;

  public BorderPage() {
    String className = getClass().getName();

    String shortName = className.substring(className.lastIndexOf('.') + 1);
    String title = ClickUtils.toLabel(shortName);
    addModel("title", title);

    breadcrumb = new Breadcrumb("breadcrumb", 4);
    breadcrumb.setSeparator(" | ");
    breadcrumb.getExcludedPaths().add("login");
    addControl(breadcrumb);
  }

  @Override public String getTemplate(){ return "/border-template.htm"; }


  public CustomerService getCustomerService() {
    return ApplicationRegistry.getInstance().getCustomerService();
  }
}