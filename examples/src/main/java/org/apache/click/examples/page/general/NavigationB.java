package org.apache.click.examples.page.general;



/**
 * Provides a navigation example Page demonstrating forward and redirect
 * page navigation. See NavigationA page for details.
 */
public class NavigationB extends NavigationA {
  private static final long serialVersionUID = 1L;

  @Override public String getTarget() {
    return "/general/navigation-a.htm";
  }

}