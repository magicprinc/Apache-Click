package net.sf.clickclick.examples.nav;

import net.sf.clickclick.control.menu.FlexiMenu;
import org.apache.click.Context;
import org.apache.click.Page;
import org.apache.click.extras.control.Menu;

public class MenuBuilder {

  private MenuBuilder() {
  }

  private static class MenuHolder {
    private static Menu INSTANCE = createMenus();
  }

  public static Menu getMenus() {
    return MenuHolder.INSTANCE;
  }

  private static Menu createMenus() {
    FlexiMenu rootMenu = new FlexiMenu("rootMenu");

    FlexiMenu menu = createMenu("Home", "home.htm");
    rootMenu.add(menu);

    menu = createMenu("Controls");
    rootMenu.add(menu);

    FlexiMenu subMenu = createMenu("Basics", "control/html-basics.htm");
    menu.add(subMenu);

    FlexiMenu booleanSelectMenu = createMenu("Boolean Select");
    menu.add(booleanSelectMenu);

    subMenu = createMenu("Boolean Select Demo", "control/boolean-select.htm");
    booleanSelectMenu.add(subMenu);

    subMenu = createMenu("Styled Option Demo", "control/styled-option.htm");
    booleanSelectMenu.add(subMenu);

    subMenu = createMenu("Menu", "control/menu.htm");
    menu.add(subMenu);

    subMenu = createMenu("Basic Rich Editor", "control/basic-email-form.htm");
    menu.add(subMenu);

    subMenu = createMenu("Advanced Rich Editor", "control/advanced-email-form.htm");
    menu.add(subMenu);

    subMenu = createMenu("Image", "control/image.htm");
    menu.add(subMenu);

    subMenu = createMenu("Dynamic Image", "control/dynamic-image.htm");
    menu.add(subMenu);

    menu = createMenu("Layout");
    rootMenu.getChildren().add(menu);

    subMenu = createMenu("Vertical Panel", "layout/vertical-panel-demo.htm");
    menu.getChildren().add(subMenu);

    subMenu = createMenu("Horizontal Panel", "layout/horizontal-panel-demo.htm");
    menu.getChildren().add(subMenu);

    subMenu = createMenu("Grid", "layout/grid-layout-demo.htm");
    menu.getChildren().add(subMenu);

    menu = createMenu("Repeater");
    rootMenu.add(menu);

    subMenu = createMenu("Basic Repeater", "repeat/basic-repeater.htm");
    menu.add(subMenu);

    subMenu = createMenu("Repeat Form", "repeat/repeat-form.htm");
    menu.add(subMenu);

    subMenu = createMenu("Repeat Field", "repeat/repeat-field.htm");
    menu.add(subMenu);

    subMenu = createMenu("Paging Repeater", "repeat/paging-repeater.htm");
    menu.add(subMenu);

    subMenu = createMenu("Edit Table Repeater", "repeat/edit-table-repeater.htm");
    menu.add(subMenu);

    menu = createJavaSourceMenu("Page Java");
    menu.setAttribute("target", "_blank");
    rootMenu.add(menu);

    menu = createPageHtmlMenu("Page HTML");
    menu.setAttribute("target", "_blank");
    rootMenu.add(menu);

    return rootMenu;
  }

  private static FlexiMenu createMenu(String label) {
    FlexiMenu menu = new FlexiMenu();
    menu.setLabel(label);
    menu.setTitle(label);
    return menu;
  }

  private static FlexiMenu createMenu(String label, String path) {
    FlexiMenu menu = new FlexiMenu();
    menu.setLabel(label);
    menu.setPath(path);
    menu.setTitle(label);
    return menu;
  }

  private static FlexiMenu createJavaSourceMenu(String label) {
    FlexiMenu menu = new FlexiMenu() {

      @Override public String getHref() {
        Context context = Context.getThreadLocalContext();
        Class<? extends Page> pageClass = context.getPageClass(context.getResourcePath());
        String pageClassName = pageClass.getName();
        String srcPath = pageClassName.replace('.', '/') + ".java";
        setPath("source-viewer.htm?filename=WEB-INF/classes/" + srcPath);
        return super.getHref();
      }
    };
    menu.setLabel(label);
    menu.setTitle(label);
    return menu;
  }

  private static FlexiMenu createPageHtmlMenu(String label) {
    FlexiMenu menu = new FlexiMenu() {

      public String getHref() {
        Context context = Context.getThreadLocalContext();
        Class<? extends Page> pageClass = context.getPageClass(context.getResourcePath());
        setPath("source-viewer.htm?filename=" + context.getPagePath(pageClass));
        return super.getHref();
      }
    };
    menu.setLabel(label);
    menu.setTitle(label);
    return menu;
  }
}