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
package net.sf.clickclick.examples.jquery.page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.clickclick.examples.jquery.control.DesktopMenu;
import net.sf.clickclick.examples.jquery.services.ApplicationRegistry;
import net.sf.clickclick.examples.jquery.services.CustomerService;
import net.sf.clickclick.examples.jquery.services.PostCodeService;
import net.sf.clickclick.examples.jquery.util.UIUtils;
import org.apache.click.element.CssStyle;
import org.apache.click.element.Element;
import org.apache.click.element.JsScript;
import org.apache.click.extras.control.Menu;
import org.apache.click.util.ClickUtils;

public class BorderPage extends BasePage {

    public Menu rootMenu = DesktopMenu.getRootMenu();

    public BorderPage() {
        // Set the default JQuery UI style
        UIUtils.style = "ui-lightness";

        String className = getClass().getName();

        String shortName = className.substring(className.lastIndexOf('.') + 1);
        String title = ClickUtils.toLabel(shortName);
        addModel("title", title);

        String srcPath = className.replace('.', '/') + ".java";
        addModel("srcPath", srcPath);
    }

    /**
     * This implementation of getHeadElements provides a convenient way to
     * associate JavaScript and CSS templates with a Page. Simply create a
     * .css or .js template in the same folder as the Page template and it will
     * be picked up and rendered automatically.
     * <p/>
     * For example, to associate a JavaScript and CSS template with the
     * CustomerPage create the following files:
     *
     * com.mycorp.page.CustomerPage.java
     * customer.htm
     * customer.js (This JavaScript file can be treated as a Velocity template)
     * customer.css (This CSS file can be treated as a Velocity template)
     *
     * The JavaScript and CSS templates can have variables passed to them via
     * the methods {@link #getJsTemplateModel()} and {@link #getCssTemplateModel()}.
     */
    @Override
    public List<Element> getHeadElements() {
        JsScript jsScript = new JsScript();
        jsScript.setId("page-js-template");

        CssStyle cssStyle = new CssStyle();
        cssStyle.setId("page-css-template");

        if (headElements == null) {
            headElements = super.getHeadElements();
            String jsTemplate = getPath().replace(".htm", ".js");
            if (getContext().getServletContext().getResourceAsStream(jsTemplate) != null) {
                jsScript.setTemplate(jsTemplate);
                headElements.add(jsScript);
            }

            String cssTemplate = getPath().replace(".htm", ".css");
            if (getContext().getServletContext().getResourceAsStream(cssTemplate) != null) {
                cssStyle.setTemplate(cssTemplate);
                headElements.add(cssStyle);
            }
        }

        if (headElements.contains(jsScript)) {
            Map jsModel = getJsTemplateModel();
            if (jsModel != null) {
                jsScript.setModel(jsModel);
            }
        }

        if (headElements.contains(cssStyle)) {
            Map cssModel = getCssTemplateModel();
            if (cssModel != null) {
                cssStyle.setModel(cssModel);
            }
        }

        return headElements;
    }

    /**
     * Returns a model that is passed to the Page css template.
     *
     * @return the Page css template model
     */
    protected Map getCssTemplateModel() {
        return new HashMap();
    }

    /**
     * Returns a model that is passed to the Page js template.
     *
     * @return the Page js template model
     */
    protected Map getJsTemplateModel() {
        return new HashMap();
    }

    /**
     * @see #getTemplate()
     */
    @Override
    public String getTemplate() {
        return "border-template.htm";
    }

    public CustomerService getCustomerService() {
        return ApplicationRegistry.getInstance().getCustomerService();
    }

    public PostCodeService getPostCodeService() {
        return ApplicationRegistry.getInstance().getPostCodeService();
    }
}
