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
package net.sf.clickclick.examples.page;

import org.apache.click.Page;
import org.apache.click.extras.control.Menu;
import org.apache.click.util.ClickUtils;
import net.sf.clickclick.control.breadcrumb.Breadcrumb;
import net.sf.clickclick.examples.nav.MenuBuilder;
import net.sf.clickclick.examples.services.ApplicationRegistry;
import net.sf.clickclick.examples.services.CustomerService;

/**
 *
 */
public class BorderPage extends Page {

    public Menu rootMenu = MenuBuilder.getMenus();

    private Breadcrumb breadcrumb;

    public BorderPage() {
        String className = getClass().getName();

        String shortName = className.substring(className.lastIndexOf('.') + 1);
        String title = ClickUtils.toLabel(shortName);
        addModel("title", title);

        breadcrumb = new Breadcrumb("breadcrumb", 4);
        breadcrumb.setSeparator(" | ");
        breadcrumb.getExcludedPaths().add("login");
        addControl(breadcrumb);
    }

    public String getTemplate() {
        return "/border-template.htm";
    }

    // -------------------------------------------------------- Service Methods

    public CustomerService getCustomerService() {
        return ApplicationRegistry.getInstance().getCustomerService();
    }
}
