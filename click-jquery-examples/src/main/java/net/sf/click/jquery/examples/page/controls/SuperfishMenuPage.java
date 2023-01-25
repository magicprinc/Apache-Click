package net.sf.click.jquery.examples.page.controls;

import net.sf.click.jquery.examples.control.JQMenu;
import net.sf.click.jquery.examples.page.BorderPage;
import org.apache.click.extras.security.AccessController;
import org.apache.click.extras.security.RoleAccessController;

import java.io.Serial;

public class SuperfishMenuPage extends BorderPage {
  @Serial private static final long serialVersionUID = 6358836315945270560L;

  final JQMenu horizontalMenu = new JQMenu("horizontalMenu");
  final JQMenu verticalMenu = new JQMenu("verticalMenu");

  @Override public void onInit() {

    horizontalMenu.setOrientation(JQMenu.HORIZONTAL);
    populateMenu(horizontalMenu);
    addControl(horizontalMenu);

    verticalMenu.setOrientation(JQMenu.VERTICAL);
    populateMenu(verticalMenu);
    addControl(verticalMenu);
  }

  public void populateMenu(JQMenu menu) {
    AccessController accessController = new RoleAccessController();
    menu.setAccessController(accessController);

    JQMenu subMenu = createMenu("Client", "#", accessController);
    menu.add(subMenu);
    JQMenu addressMenu = createMenu("Address", "#", accessController);
    subMenu.add(addressMenu);
    addressMenu.add(createMenu("Physical", "#", accessController));
    addressMenu.add(createMenu("Postal", "#", accessController));
    subMenu = createMenu("Products", "#", accessController);
    menu.add(subMenu);
  }

  public JQMenu createMenu(String label, String path, AccessController accessController) {
    JQMenu menu = new JQMenu();
    menu.setLabel(label);
    menu.setPath(path);
    menu.setAccessController(accessController);
    return menu;
  }
}