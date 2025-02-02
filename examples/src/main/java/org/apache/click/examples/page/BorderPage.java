package org.apache.click.examples.page;

import org.apache.click.Page;
import org.apache.click.extras.control.Menu;
import org.apache.click.extras.control.MenuFactory;
import org.apache.click.util.ClickUtils;

/**
 * Provides a page border template. This Page returns the template
 * <tt>"border-template.htm"</tt>, and sets the Page model values <tt>$title</tt> and
 * <tt>$srcPath</tt>.
 * <p/>
 * Please note this page is designed for extending by Page subclasses and will
 * not be auto mapped as the template name <tt>"border-template.htm"</tt> does
 * not match the Pages class name <tt>BorderPage</tt>.
 */
public class BorderPage extends Page {
  private static final long serialVersionUID = 1L;

  /**
     * The root menu. Note this transient variable is reinitialized in onInit()
     * to support serialized stateful pages.
   */
  private transient Menu rootMenu;

  static {
    System.out.println("\n\n\n****************************** BORDER PAGE INIT: "+System.getProperty("java.version")
      +" "+Runtime.class.getPackage().getImplementationVersion()  +"\n\n\n");

  }

  /**
     * Create a BorderedPage and set the model attributes <tt>$title</tt> and
     * <tt>$srcPath</tt>.
     * <ul>
     * <li><tt>$title</tt> &nbsp; - &nbsp; the Page title from classname</li>
     * <li><tt>$srcPath</tt> &nbsp; - &nbsp; the Page Java source path</li>
     * </ul>
   */
  public BorderPage () {
    String className = getClass().getName();

    String shortName = className.substring(className.lastIndexOf('.') + 1);
    String title = ClickUtils.toLabel(shortName);
    addModel("title", title);

    String srcPath = className.replace('.', '/') + ".java";
    addModel("srcPath", srcPath);
  }



  /**
     * @see org.apache.click.Page#onInit()
   */
  @Override
  public void onInit () {
    super.onInit();

    MenuFactory menuFactory = new MenuFactory();
    rootMenu = menuFactory.getRootMenu();

    // Add rootMenu to Page control list. Note: rootMenu is removed in Page
    // onDestroy() to ensure rootMenu is not serialized
    addControl(rootMenu);
  }


  /**
     * @see org.apache.click.Page#onDestroy()
   */
  @Override
  public void onDestroy () {
    // Remove menu for when BorderPage is serialized
    if (rootMenu != null){
      removeControl(rootMenu);
    }
  }



  /**
     * Returns the name of the border template: &nbsp; <tt>"/border-template.htm"</tt>
     * <p/>
     * Please note this page is designed for extending by Page subclasses and will
     * not be auto mapped as the template name <tt>"border-template.htm"</tt> does
     * not match the Pages class name <tt>BorderPage</tt>.
     *
     * @see org.apache.click.Page#getTemplate()
   */
  @Override
  public String getTemplate () {
    return "/border-template.htm";
  }
}