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
package net.sf.clickclick.examples.jquery.page.controls;

import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.control.JQMenu;

public class SuperfishMenuPage extends BorderPage {

    public String title = "Superfish Demo";
    
    JQMenu horizontalMenu = new JQMenu("horizontalMenu");

    JQMenu verticalMenu = new JQMenu("verticalMenu");

    public void onInit() {
       
        horizontalMenu.setOrientation(JQMenu.HORIZONTAL);
        populateMenu(horizontalMenu);
        addControl(horizontalMenu);

        verticalMenu.setOrientation(JQMenu.VERTICAL);
        populateMenu(verticalMenu);
        addControl(verticalMenu);
    }
    
    public void populateMenu(JQMenu menu) {
        JQMenu subMenu = createMenu("Client", "#");
        menu.add(subMenu);
        JQMenu addressMenu = createMenu("Address", "#");
        subMenu.add(addressMenu);
        addressMenu.add(createMenu("Physical", "#"));
        addressMenu.add(createMenu("Postal", "#"));
        subMenu = createMenu("Products", "#");
        menu.add(subMenu);
    }

    public JQMenu createMenu(String label, String path) {
        JQMenu menu = new JQMenu();
        menu.setLabel(label);
        menu.setPath(path);
        return menu;
    }
}
