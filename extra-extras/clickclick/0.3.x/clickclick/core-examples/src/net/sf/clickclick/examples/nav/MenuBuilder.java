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
package net.sf.clickclick.examples.nav;

import net.sf.clickclick.control.menu.FlexiMenu;

import org.apache.click.Context;
import org.apache.click.extras.control.Menu;
import org.apache.click.util.HtmlStringBuffer;

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

        subMenu = createMenu("Rich Editor", "control/email-form.htm");
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

        subMenu = createMenu("Table Repeater", "repeat/table-repeater.htm");
        menu.add(subMenu);

        menu = createMenu("Reload", "reload/resource-bundle.htm");
        rootMenu.add(menu);

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

            public String getHref() {
                Context context = Context.getThreadLocalContext();
                Class pageClass =
                    context.getPageClass(context.getResourcePath());
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
                Class pageClass =
                    context.getPageClass(context.getResourcePath());
                setPath("source-viewer.htm?filename=" + context.getPagePath(pageClass));
                return super.getHref();
            }
        };
        menu.setLabel(label);
        menu.setTitle(label);
        return menu;
    }
}