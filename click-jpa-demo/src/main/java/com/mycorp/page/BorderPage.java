package com.mycorp.page;

import com.mycorp.service.CustomerService;
import org.apache.click.Page;
import org.apache.click.extras.control.Menu;
import org.apache.click.extras.control.MenuFactory;
import org.apache.click.util.ClickUtils;

public class BorderPage extends Page {

  private static final long serialVersionUID = 1L;

  private final Menu rootMenu = new MenuFactory().getRootMenu();

  public BorderPage() {
    String className = getClass().getName();

    String shortName = className.substring(className.lastIndexOf('.') + 1);
    String title = ClickUtils.toLabel(shortName);
    addModel("title", title);

    String srcPath = className.replace('.', '/') + ".java";
    addModel("srcPath", srcPath);

    addControl(rootMenu);
  }

  /**
   * @see #getTemplate()
   */
  @Override public String getTemplate() {
    return "/page/border-template.htm";
  }

  public CustomerService getCustomerService() {
    return new CustomerService();
  }

}