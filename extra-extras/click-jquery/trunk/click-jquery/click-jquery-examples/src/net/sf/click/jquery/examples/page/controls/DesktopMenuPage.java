/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.click.jquery.examples.page.controls;

import net.sf.click.jquery.examples.control.DesktopMenu;
import net.sf.click.jquery.examples.page.BorderPage;
import org.apache.click.extras.security.AccessController;
import org.apache.click.extras.security.RoleAccessController;

public class DesktopMenuPage extends BorderPage {

	private static final long serialVersionUID = 1L;

    private DesktopMenu menu = new DesktopMenu("desktopMenu");

    public void onInit() {
        populateMenu(menu);
        addControl(menu);
    }

    public void populateMenu(DesktopMenu menu) {
        AccessController accessController = new RoleAccessController();
        DesktopMenu subMenu = createMenu("Home", "#", accessController);
        menu.add(subMenu);
        DesktopMenu childMenu = createMenu("BorderPage Class", "#", accessController);
        subMenu.add(childMenu);
        childMenu = createMenu("Border Template HTML", "#", accessController);
        subMenu.add(childMenu);
        childMenu = createMenu("sep", "#", accessController);
        childMenu.setSeparator(true);
        subMenu.add(childMenu);
        childMenu = createMenu("Javadoc API", "#", accessController);
        subMenu.add(childMenu);
        childMenu = createMenu("sep", "#", accessController);
        childMenu.setSeparator(true);
        subMenu.add(childMenu);
        childMenu = createMenu("click.xml", "#", accessController);
        subMenu.add(childMenu);
        childMenu = createMenu("spring-beans.xml", "#", accessController);
        subMenu.add(childMenu);
        childMenu = createMenu("web.xml", "#", accessController);
        subMenu.add(childMenu);
        DesktopMenu addressMenu = createMenu("Address", "#", accessController);
        menu.add(addressMenu);
        DesktopMenu physicalMenu = createMenu("Physical", "#", accessController);
        addressMenu.add(physicalMenu);
        DesktopMenu streetMenu = createMenu("Street", "#", accessController);
        physicalMenu.add(streetMenu);
        DesktopMenu cityMenu = createMenu("City", "#", accessController);
        physicalMenu.add(cityMenu);
        addressMenu.add(createMenu("Postal", "#", accessController));
        subMenu = createMenu("Products", "#", accessController);
        menu.add(subMenu);
    }

    public DesktopMenu createMenu(String label, String path, AccessController roleAccessController) {
        DesktopMenu menu = new DesktopMenu();
        menu.setAccessController(roleAccessController);
        menu.setLabel(label);
        menu.setPath(path);
        return menu;
    }
}
