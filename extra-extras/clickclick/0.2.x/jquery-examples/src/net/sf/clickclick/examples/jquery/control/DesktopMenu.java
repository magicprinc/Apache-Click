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
package net.sf.clickclick.examples.jquery.control;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;

import net.sf.clickclick.jquery.helper.JQHelper;
import org.apache.click.Context;
import org.apache.click.element.CssImport;
import org.apache.click.element.JsImport;
import org.apache.click.element.JsScript;
import org.apache.click.extras.control.Menu;
import org.apache.click.extras.security.AccessController;
import org.apache.click.extras.security.RoleAccessController;
import org.apache.click.service.ConfigService;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 */
public class DesktopMenu extends Menu {

    private static final long serialVersionUID = 1L;

    // -------------------------------------------------------- Variables

    protected String cssImport = "/clickclick/example/desktopmenu/desktopmenu.css";

    // -------------------------------------------------------- Constructor

    public DesktopMenu() {
    }

    public DesktopMenu(String menu) {
        super(menu);
    }

    protected DesktopMenu(Element menuElement, AccessController accessController, final DesktopMenu rootMenu) {
        if (menuElement == null) {
            throw new IllegalArgumentException("Null menuElement parameter");
        }
        if (accessController == null) {
            throw new IllegalArgumentException("Null accessController parameter");
        }

        setAccessController(accessController);

        String labelAtr = menuElement.getAttribute("label");
        setLabel(labelAtr);

        setImageSrc(menuElement.getAttribute("imageSrc"));

        setPath(menuElement.getAttribute("path"));

        String titleAtr = menuElement.getAttribute("title");
        if (StringUtils.isNotBlank(titleAtr)) {
            setTitle(titleAtr);
        }

        String targetAtr = menuElement.getAttribute("target");
        if (StringUtils.isNotBlank(targetAtr)) {
            setTarget(targetAtr);
        }

        String externalAtr = menuElement.getAttribute("external");
        if ("true".equalsIgnoreCase(externalAtr)) {
            setExternal(true);
        }

        String separatorAtr = menuElement.getAttribute("separator");
        if ("true".equalsIgnoreCase(separatorAtr)) {
            setSeparator(true);
        }

        String pagesValue = menuElement.getAttribute("pages");
        if (!StringUtils.isBlank(pagesValue)) {
            StringTokenizer tokenizer = new StringTokenizer(pagesValue, ",");
            while (tokenizer.hasMoreTokens()) {
                String path = tokenizer.nextToken().trim();
                path = (path.startsWith("/")) ? path : "/" + path;
                getPages().add(path);
            }
        }

        String rolesValue = menuElement.getAttribute("roles");
        if (!StringUtils.isBlank(rolesValue)) {
            StringTokenizer tokenizer = new StringTokenizer(rolesValue, ",");
            while (tokenizer.hasMoreTokens()) {
                getRoles().add(tokenizer.nextToken().trim());
            }
        }

        NodeList childElements = menuElement.getChildNodes();
        for (int i = 0, size = childElements.getLength(); i < size; i++) {
            Node node = childElements.item(i);
            if (node instanceof Element) {
                Menu childMenu = new DesktopMenu((Element) node, accessController, rootMenu);
                getChildren().add(childMenu);
            }
        }

        String name = menuElement.getAttribute("name");
        setName(name);

        if (StringUtils.isNotBlank(name)) {
            if (StringUtils.isBlank(labelAtr)) {
                setLabel(null);
                setLabel(rootMenu.getLabel(this));
            }
            if (StringUtils.isBlank(titleAtr)) {
                setTitle(null);
                setTitle(rootMenu.getTitle(this));
            }
        }
    }

    // ------------------------------------------------------ Public Properties

    /**
     * Return true if the menu contains any child submenus.
     *
     * @return true if the menu contains any child submenus
     */
    public boolean hasChildren() {
        if (getChildren().size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * Return the HTML href attribute value.
     *
     * @return the HTML href attribute value
     */
    @Override
    public String getHref() {
        if (getPath() == null) {
            setPath("#");
        } else if (hasChildren() && "".equals(getPath())) {
            setPath("#");
        }
        return super.getHref();
    }

    // --------------------------------------------------------- Public Methods

    public static Menu getRootMenu() {
        return getRootMenu(new RoleAccessController());
    }

    public static Menu getRootMenu(AccessController accessController) {
        if (accessController == null) {
            throw new IllegalArgumentException("Null accessController parameter");
        }

        // If menu is cached return it
        if (rootMenu != null) {
            return rootMenu;
        }

        Menu loadedMenu = loadRootMenu(accessController);

        ServletContext servletContext = Context.getThreadLocalContext().getServletContext();
        ConfigService configService = ClickUtils.getConfigService(servletContext);

        if (configService.isProductionMode() || configService.isProfileMode()) {
            // Cache menu in production modes
            rootMenu = loadedMenu;
        }

        return loadedMenu;
    }

    public void add(Menu menu) {
        getChildren().add(menu);
    }

    // -------------------------------------------------------- Public Methods

    @Override
    public String getHtmlImports() {
        return null;
    }

    @Override
    public List getHeadElements() {
        if (headElements == null) {
            headElements = new ArrayList(3);

            headElements.add(new CssImport(cssImport));

            JsImport jsImport = new JsImport(JQHelper.jqueryImport);
            headElements.add(jsImport);

            jsImport = new JsImport("/clickclick/example/desktopmenu/jquery.menu.js");
            headElements.add(jsImport);

            Context context = getContext();
            JsScript jsScript = getJsTemplate(context);
            headElements.add(jsScript);

            jsImport = new JsImport("/clickclick/core/menu/menu-fix-ie6.js");
            jsImport.setConditionalComment(JsImport.IF_LESS_THAN_IE7);
            headElements.add(jsImport);

            // ID is created by jQuery menu plugin
            String id = "root-menu-div";

            HtmlStringBuffer buffer = new HtmlStringBuffer();
            buffer.append("jQuery(document).ready( function() {\n");
            buffer.append(" if(typeof Click != 'undefined' && typeof Click.menu != 'undefined') {\n");
            buffer.append("   if(typeof Click.menu.fixHiddenMenu != 'undefined') {\n");
            buffer.append("     Click.menu.fixHiddenMenu(\"").append(id).append("\");\n");
            buffer.append("   }\n");
            buffer.append(" }\n");
            buffer.append("});\n");
            jsScript = new JsScript(buffer.toString());
            headElements.add(jsScript);
        }

        return headElements;
    }

    @Override
    public boolean isUserInRoles() {
        List roles = getRoles();
        if (roles == null || roles.isEmpty()) {
            return true;
        }
        return super.isUserInRoles();
    }

    @Override
    public boolean isUserInChildMenuRoles() {
        List roles = getRoles();
        if (roles == null || roles.isEmpty()) {
            return true;
        }
        return super.isUserInChildMenuRoles();
    }

    /**
     * Render the HTML representation of the Menu.
     *
     * @see #toString()
     *
     * @param buffer the specified buffer to render the control's output to
     */
    @Override
    public void render(HtmlStringBuffer buffer) {
        buffer.elementStart("ul");
        int depth = 0;
        renderMenuClassAttribute(buffer, this, depth);
        String id = getAttribute("id");
        id = (id != null) ? id : getName();
        buffer.appendAttribute("id", id);
        buffer.closeTag();
        buffer.append("\n");
        renderMenu(buffer, this, depth);
        buffer.elementEnd("ul");
    }

    /**
     * Render the HTML representation of the Menu.
     *
     * @return the HTML representation of the Menu
     */
    @Override
    public String toString() {
        HtmlStringBuffer buffer = new HtmlStringBuffer(256);
        render(buffer);
        return buffer.toString();
    }

    // ------------------------------------------------------ Protected Methods

    protected JsScript getJsTemplate(Context context) {
        String id = getAttribute("id");
        id = (id != null) ? id : getName();
        Map model = new HashMap();
        model.put("context", context.getRequest().getContextPath());
        model.put("selector", '#' + id);
        JsScript jsScript = new JsScript("/clickclick/example/template/desktopmenu.template.js", model);
        return jsScript;
    }

    /**
     * Render the given menu.
     *
     * @param buffer the buffer to render to
     * @param menu the menu to render
     * @param depth the depth of the menu in the hierarchy
     */
    protected void renderMenu(HtmlStringBuffer buffer, Menu menu, int depth) {
        Iterator it = menu.getChildren().iterator();
        while (it.hasNext()) {
            Menu child = (Menu) it.next();
            if (displayMenu(child, depth + 1)) {
                buffer.elementStart("li");
                renderMenuItemClassAttribute(buffer, child, depth);
                buffer.closeTag();
                renderMenuLink(buffer, child);
                if (child.getChildren().size() > 0) {
                    buffer.elementStart("ul");
                    renderMenuClassAttribute(buffer, child, depth + 1);
                    buffer.closeTag();
                    buffer.append("\n");
                    renderMenu(buffer, child, depth + 1);
                    buffer.elementEnd("ul");
                    buffer.append("\n");
                }
                buffer.elementEnd("li");
                buffer.append("\n");
            }
        }
    }

    protected boolean displayMenu(Menu menu, int depth) {
        if (depth == 1 && "hidden".equals(menu.getLabel())) {
            return false;
        }

        if (menu.isUserInRoles() || menu.isUserInChildMenuRoles()) {
            return true;
        }
        return false;
    }

    /**
     * Render the given menu as a link.
     *
     * @param buffer the buffer to render to
     * @param menu the menu to render as a link
     */
    protected void renderMenuLink(HtmlStringBuffer buffer, Menu menu) {
        if (menu.isSeparator()) {
            renderSeparator(buffer, menu);
            return;
        }

        buffer.elementStart("a");

        String href = menu.getHref();
        buffer.appendAttribute("href", href);
        if (menu.hasAttributes()) {
            buffer.appendAttributes(menu.getAttributes());
        }
        if ("#".equals(href)) {
            //If hyperlink does not return false here, clicking on it will scroll
            //to the top of the page.
            buffer.appendAttribute("onclick", "return false;");
        }
        String menuTitle = menu.getTitle();
        String menuLabel = menu.getLabel();

        buffer.appendAttribute("title", menuTitle);
        buffer.closeTag();
        buffer.append("\n");

        if (StringUtils.isNotBlank(menu.getImageSrc())) {
            buffer.elementStart("img");
            buffer.appendAttribute("border", "0");
            buffer.appendAttribute("class", "link");

            if (menuTitle != null) {
                buffer.appendAttributeEscaped("alt", menuTitle);
            } else {
                buffer.appendAttributeEscaped("alt", menuLabel);
            }

            String src = menu.getImageSrc();
            if (StringUtils.isNotBlank(src)) {
                if (src.charAt(0) == '/') {
                    src = getContext().getRequest().getContextPath() + src;
                }
                buffer.appendAttribute("src", src);
            }

            buffer.elementEnd();

            if (menuLabel != null) {
                buffer.append(menuLabel);
            }

        } else {
            buffer.append(menuLabel);
        }

        buffer.elementEnd("a");
        buffer.append("\n");
    }

    /**
     * Return the menu css class that is applied to the &lt;ul&gt; element.
     *
     * @param buffer the buffer to render the class attribute to
     * @param menu the menu to render
     * @param depth the depth of the menu in the hierarchy
     */
    protected void renderMenuClassAttribute(HtmlStringBuffer buffer, Menu menu, int depth) {
        if (depth == 0) {
            buffer.appendAttribute("class", "desktopmenu");
        } else {
            buffer.appendAttribute("class", "submenu");
        }
        buffer.append(" ");
    }

    /**
     * Return the menu item css class that is applied to the &lt;li&gt; element.
     *
     * @param buffer the buffer to render the class attribute to
     * @param menu the menu to render
     * @param depth the depth of the menu in the hierarchy
     */
    protected void renderMenuItemClassAttribute(HtmlStringBuffer buffer, Menu menu, int depth) {
        buffer.append(" class=\"menuitem");
        if (depth == 0) {
            buffer.append(" topitem");
            if (menu.getChildren().size() == 0) {
                buffer.append(" empty");
            }
        }

        buffer.append("\" ");
    }

    protected void renderSeparator(HtmlStringBuffer buffer, Menu menu) {
    }

    protected static Menu loadRootMenu(AccessController accessController) {
        if (accessController == null) {
            throw new IllegalArgumentException("Null accessController parameter");
        }

        Context context = Context.getThreadLocalContext();

        DesktopMenu menu = new DesktopMenu("rootMenu");
        menu.setAccessController(accessController);

        ServletContext servletContext = context.getServletContext();
        InputStream inputStream =
            servletContext.getResourceAsStream(DEFAULT_CONFIG_FILE);

        if (inputStream == null) {
            inputStream = ClickUtils.getResourceAsStream("/menu.xml", Menu.class);
            String msg =
                "could not find configuration file:" + DEFAULT_CONFIG_FILE
                + " or menu.xml on classpath";
            throw new RuntimeException(msg);
        }

        Document document = ClickUtils.buildDocument(inputStream);

        Element rootElm = document.getDocumentElement();

        NodeList list = rootElm.getChildNodes();

        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node instanceof Element) {
                Menu childMenu = new DesktopMenu((Element) node, accessController, menu);
                menu.getChildren().add(childMenu);
            }
        }

        return menu;
    }

    private String getLabel(Menu menu) {
        String label = menu.getLabel();
        if (label == null) {
            label = getMessage(menu.getName() + ".label");
        }
        if (label == null) {
            label = ClickUtils.toLabel(menu.getName());
        }
        if (label != null) {
            menu.setLabel(label);
        }
        return label;
    }

    private String getTitle(Menu menu) {
        String title = menu.getTitle();
        if (title == null) {
            title = getMessage(menu.getName() + ".title");
        }
        if (title != null) {
            menu.setTitle(title);
        }
        return title;
    }
}
