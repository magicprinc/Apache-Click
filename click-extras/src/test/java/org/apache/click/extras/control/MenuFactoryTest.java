package org.apache.click.extras.control;

import junit.framework.TestCase;
import org.apache.click.MockContext;

import java.util.Map;

/**
 * Provide tests for MenuFactory.
 */
public class MenuFactoryTest extends TestCase {

  /**
   * Check that Menus are loaded from default config: menu.xml.
   */
  public void testLoadMenuFromConfig() {
    MockContext.initContext();

    MenuFactory menuFactory = new MenuFactory();

    Menu rootMenu = menuFactory.getRootMenu();
    Menu menu1 = rootMenu.getChildren().get(0);

    assertEquals(2, rootMenu.getChildren().size());
    assertEquals("Home", menu1.getLabel());
  }

  /**
   * Check that Menus are loaded from custom config: menu_custom.xml.
   */
  public void testLoadMenuFromCustomConfig() {
    MockContext.initContext();

    String customMenuConfig = "menu_custom.xml";

    MenuFactory menuFactory = new MenuFactory();
    Menu rootMenu = menuFactory.getRootMenu("customRootMenu", customMenuConfig);
    Menu menu1 = rootMenu.getChildren().get(0);

    assertEquals(1, rootMenu.getChildren().size());
    assertEquals("CustomMenu", menu1.getLabel());
  }

  /**
   * Test that Menus are loaded and cached.
   */
  public void testMenuCache() {
    MockContext.initContext(Map.of("mode", "profile"));

    MenuFactory menuFactory = new MenuFactory();

    Menu rootMenu = menuFactory.getRootMenu();

    // Check that the same menu instance is returned on consecutive getRootMenu calls
    assertSame(rootMenu, menuFactory.getRootMenu());
  }

  /**
   * Check that Menus can be loaded and not cached.
   */
  public void testMenuNoCache() {
    MockContext.initContext();

    MenuFactory menuFactory = new MenuFactory();

    Menu rootMenu = menuFactory.getRootMenu(false);

    // Check that the different menu instances are returned on consecutive
    // getRootMenu calls
    assertNotSame(rootMenu, menuFactory.getRootMenu(false));
  }
}