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

import net.sf.click.jquery.examples.control.JQMenu;
import net.sf.click.jquery.examples.page.BorderPage;
import org.apache.click.extras.security.AccessController;
import org.apache.click.extras.security.RoleAccessController;

public class SuperfishMenuPage extends BorderPage {

    private static final long serialVersionUID = 1L;

    JQMenu horizontalMenu = new JQMenu("horizontalMenu");

    JQMenu verticalMenu = new JQMenu("verticalMenu");

    @Override
    public void onInit() {
       
        horizontalMenu.setOrientation(JQMenu.HORIZONTAL);
        populateMenu(horizontalMenu);
        addControl(horizontalMenu);

        verticalMenu.setOrientation(JQMenu.VERTICAL);
        populateMenu(verticalMenu);
        addControl(verticalMenu);
    }
    
    public void populateMenu(JQMenu menu) {
        AccessController accessController = new RoleAccessController();

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
