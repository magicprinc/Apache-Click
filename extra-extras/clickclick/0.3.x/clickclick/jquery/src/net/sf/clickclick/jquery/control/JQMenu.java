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
package net.sf.clickclick.jquery.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.click.element.Element;
import org.apache.click.extras.control.Menu;
import org.apache.click.util.HtmlStringBuffer;
import net.sf.clickclick.control.menu.FlexiMenu;
import net.sf.clickclick.jquery.helper.JQHelper;
import org.apache.click.element.CssImport;
import org.apache.click.element.JsImport;
import org.apache.click.element.JsScript;

/**
 * Provide a drop-down Menu control based on the JQuery Superfish plugin:
 * http://users.tpg.com.au/j_birch/plugins/superfish/.
 * <p/>
 * Example usage:
 *
 * <pre class="prettyprint">
 * JQMenu menu = new JQMenu("horizontalMenu");
 *
 * public MyPage() {
 *     menu.setOrientation(JQMenu.HORIZONTAL);
 *
 *     JQMenu subMenu = createMenu("Client", "client.htm");
 *     menu.add(subMenu);
 *     JQMenu addressMenu = createMenu("Address", "address.htm");
 *     subMenu.add(addressMenu);
 *     addressMenu.add(createMenu("Physical", "physical-address.htm"));
 *     addressMenu.add(createMenu("Postal", "postal-address.htm"));
 *     subMenu = createMenu("Products", "product.htm");
 *     menu.add(subMenu);
 *
 *     addControl(menu);
 * }
 *
 * public JQMenu createMenu(String label, String path) {
 *     JQMenu menu = new JQMenu();
 *     menu.setLabel(label);
 *     menu.setPath(path);
 *     return menu;
 * } </pre>
 */
public class JQMenu extends FlexiMenu {

    // -------------------------------------------------------------- Constants

    /**
     * The JQMenu CSS import:
     * "<tt>/clickclick/jquery/superfish/css/superfish.css</tt>".
     */
    public static String menuCssImport =
        "/clickclick/jquery/superfish/css/superfish.css";

    /**
     * The vertical JQMenu CSS import:
     * "<tt>/clickclick/jquery/superfish/css/superfish-vertical.css</tt>".
     */
    public static String  menuCssVerticalImport = "/clickclick/jquery/superfish/css/superfish-vertical.css";

    /**
     * The JQMenu JS library:
     * "<tt>/clickclick/jquery/superfish/js/superfish.js</tt>".
     */
    public static String menuJsImport =
        "/clickclick/jquery/superfish/js/superfish.js";

    /**
     * The JQMenu Hover-Intent plugin import:
     * "<tt>/clickclick/jquery/superfish/js/hoverIntent.js</tt>".
     */
    public static String hoverIntentImport =
        "/clickclick/jquery/superfish/js/hoverIntent.js";

    /**
     * The JQMenu BGI-Frame plugin import:
     * "<tt>/clickclick/jquery/superfish/js/bgiframe.js</tt>".
     */
    public static String bgiFrameImport =
        "/clickclick/jquery/superfish/js/bgiframe.js";

    // -------------------------------------------------------------- Variables

    /**
     * The menu JavaScript template:
     * "<tt>/clickclick/jquery/template/superfish/jquery.menu.template.js</tt>".
     */
    protected String template = "/clickclick/jquery/template/superfish/jquery.menu.template.js";

    /** The menu JavaScript template model. */
    protected Map jsModel;

    /** The default menu options. */
    protected String options = "animation : { opacity:'show', height:'show' }, speed: 'fast'";

    // ----------------------------------------------------------- Constructors

    /**
     * Create a default JQMenu.
     */
    public JQMenu() {
    }

    /**
     * Create a JQMenu with the given name.
     *
     * @param name the name of the control
     */
    public JQMenu(String name) {
        super(name);
    }

    // ------------------------------------------------------ Public Properties

    /**
     * Return the menu options.
     *
     * @see #setOptions(java.lang.String)
     *
     * @return the menu options
     */
    public String getOptions() {
        return options;
    }

    /**
     * Set the menu options.
     * <p/>
     * Please see the following link on how to set the menu Options:
     * http://users.tpg.com.au/j_birch/plugins/superfish/#options
     *
     * @param options the menu options
     */
    public void setOptions(String options) {
        this.options = options;
    }

    /**
     * Return the menu JavaScript template.
     *
     * @return the template the JavaScript template
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Set the menu JavaScript template.
     *
     * @param template the JavaScript template
     */
    public void setTemplate(String template) {
        this.template = template;
    }

    /**
     * Return the JavaScript template model.
     *
     * @return the JavaScript template model
     */
    public Map getJsModel() {
        if(jsModel == null) {
            jsModel = new HashMap();
        }
        return jsModel;
    }

    /**
     * Set the JavaScript template model.
     *
     * @param model the JavaScript template model
     */
    public void setJsModel(Map model) {
        this.jsModel = model;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Return the JQColorPicker resources: {@link #menuCssImport},
     * {@link #menuCssVerticalImport},
     * {@link net.sf.clickclick.jquery.helper.JQHelper#jqueryImport},
     * {@link #hoverIntentImport}, {@link #bgiFrameImport},
     * {@link #menuJsImport}.
     *
     * @return the list of head elements
     */
    public List<Element> getHeadElements() {
        if (headElements == null) {
            headElements = new ArrayList<Element>();

            headElements.add(new CssImport(menuCssImport));

            if (VERTICAL.equals(getOrientation())) {
                headElements.add(new CssImport(menuCssVerticalImport));
            }

            headElements.add(new JsImport(JQHelper.jqueryImport));
            headElements.add(new JsImport(hoverIntentImport));
            headElements.add(new JsImport(bgiFrameImport));
            headElements.add(new JsImport(menuJsImport));
        }

        addJsTemplate(headElements);

        return headElements;
    }

    /**
     * Render the HTML representation of the JQMenu.
     *
     * @see #toString()
     *
     * @param buffer the specified buffer to render the control's output to
     */
    public void render(HtmlStringBuffer buffer) {
        buffer.elementStart("ul");
        int depth = 0;
        renderMenuClassAttribute(buffer, this, depth);
        appendAttributes(buffer);
        buffer.closeTag();
        buffer.append("\n");
        renderMenu(buffer, this, depth);
        buffer.elementEnd("ul");
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * @see net.sf.clickclick.control.menu.FlexiMenu#renderMenuClassAttribute(org.apache.click.util.HtmlStringBuffer, org.apache.click.extras.control.Menu, int)
     *
     * @param buffer the buffer to render the class attribute to
     * @param menu the menu to render
     * @param depth the depth of the menu in the hierarchy
     */
    @Override
    protected void renderMenuClassAttribute(HtmlStringBuffer buffer, Menu menu, int depth) {
        buffer.append(" class=\"");
        if (depth == 0) {
            buffer.append("sf-menu");
            if (VERTICAL.equals(getOrientation())) {
                buffer.append(" sf-vertical");
            }
        }
        buffer.append("\" ");
    }

    /**
     * Add the JQMenu JavaScript {@link #template} to the list of head elements.
     * <p/>
     * You can override this method to add your own template.
     *
     * @param headElements the list of head elements to include for this control
     */
    protected void addJsTemplate(List<Element> headElements) {
        String name = getName();
        if (name == null) {
            throw new IllegalStateException("Menu name is not set.");
        }

        JsScript jsScript = new JsScript();
        jsScript.setId(name + "_superfish");

        if (!headElements.contains(jsScript)) {
            // Get the data model to pass to the templates
            Map model = getJsModel();
            model.put("options", getOptions());
            jsScript.setModel(model);
            jsScript.setTemplate(getTemplate());
            headElements.add(jsScript);
        }
    }

    /**
     * Render the given menu.
     *
     * @param buffer the buffer to render to
     * @param menu the menu to render
     * @param depth the depth of the menu in the hierarchy
     */
    @Override
    protected void renderMenu(HtmlStringBuffer buffer, Menu menu, int depth) {
        for (Menu child : menu.getChildren()) {
            if (child.isUserInRoles()) {
                buffer.elementStart("li");
                if (child.getChildren().size() == 0) {
                    buffer.closeTag();
                    buffer.append("\n");
                    renderMenuLink(buffer, child);
                } else {
                    buffer.closeTag();
                    buffer.append("\n");
                    renderMenuLink(buffer, child);
                    buffer.elementStart("ul");
                    buffer.closeTag();
                    buffer.append("\n");
                    renderMenu(buffer, child, ++depth);
                    buffer.elementEnd("ul");
                    buffer.append("\n");
                }
                buffer.elementEnd("li");
                buffer.append("\n");
            }
        }
    }
}
