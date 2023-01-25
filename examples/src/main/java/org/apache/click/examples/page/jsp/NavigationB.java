package org.apache.click.examples.page.jsp;

import java.io.Serial;

/**
 * Provides a navigation example Page demonstrating forward and redirect
 * page navigation. See NavigationA page for details.
 */
public class NavigationB extends NavigationA {
  @Serial private static final long serialVersionUID = 1L;

  /**
   * Target template to forward to.
   * <p/>
   * In order to forward to a Page with a JSP template, we specify the target
   * with an htm extension so that ClickServlet will process the Page.
   * After the Page NavigationA.java is processed, Click will forward to the
   * underlying template /jsp/navigation-a.jsp.
   */
  @Override public String getTarget() {
    return "/jsp/navigation-a.htm";
  }

  /**
   * Returns the name of the border template: &nbsp; <tt>"/border-template.jsp"</tt>
   *
   * @see org.apache.click.Page#getTemplate()
   */
  @Override public String getTemplate() {
    return "/border-template.jsp";
  }

}