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
package net.sf.clickclick.examples.page.control;

import net.sf.clickclick.control.menu.FlexiMenu;
import net.sf.clickclick.examples.page.BorderPage;

public class MenuPage extends BorderPage {

    FlexiMenu horizontalMenu = new FlexiMenu("horizontalMenu");

    FlexiMenu verticalMenu = new FlexiMenu("verticalMenu");

    public void onInit() {
        horizontalMenu.setOrientation(FlexiMenu.HORIZONTAL);
        populateMenu(horizontalMenu);
        addControl(horizontalMenu);

        verticalMenu.setOrientation(FlexiMenu.VERTICAL);
        populateMenu(verticalMenu);
        addControl(verticalMenu);
    }

    public void populateMenu(FlexiMenu menu) {
        FlexiMenu subMenu = createMenu("Client", "#");
        menu.add(subMenu);
        FlexiMenu addressMenu = createMenu("Address", "#");
        subMenu.add(addressMenu);
        addressMenu.add(createMenu("Physical", "#"));
        addressMenu.add(createMenu("Postal", "#"));
        subMenu = createMenu("Products", "#");
        menu.add(subMenu);
    }

    public FlexiMenu createMenu(String label, String path) {
        FlexiMenu menu = new FlexiMenu();
        menu.setLabel(label);
        menu.setPath(path);
        return menu;
    }
}
