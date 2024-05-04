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
package net.sf.click.jquery.taconite;

import org.apache.click.ActionResult;
import org.apache.click.Context;
import org.apache.click.Control;
import org.apache.click.element.CssStyle;
import org.apache.click.element.Element;
import org.apache.click.element.JsScript;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.click.util.PageImports;
import org.apache.commons.lang3.StringUtils;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Provides a Java API for the JQuery JQTaconite plugin:
 * <a href="http://www.malsup.com/jquery/taconite">http://www.malsup.com/jquery/taconite</a>.
 * <p/>
 * JQTaconite allows you to perform multiple DOM updates for a single Ajax request.
 * JQTaconite produces an XML document consisting of commands that is executed
 * in order by the browser.
 * <p/>
 * Common usages include: updating Controls, adding new Controls, removing Controls,
 * replace content of Controls and even executing raw JavaScript code.
 * <p/>
 * The API consists of two classes: JQTaconite and {@link JQCommand} although in
 * most cases you will only use the JQTaconite class.
 * <p/>
 * This API provides a number of pre-defined commands for example:
 * {@link #REPLACE}, {@link #REPLACE_CONTENT}, {@link #AFTER}, {@link #EVAL}.
 * Do note that you are not limited to the predefined commands and can use any
 * other JQuery functions available, even your own custom defined functions.
 *
 * <h3>Example usage</h3>
 *
 * The example below demonstrates how to replace the content of a Span target
 * with today's Date. A JQTaconite command "<tt>replaceContent</tt>" is used to
 * replace the content of the Span.
 *
 * <pre class="prettyprint">
 * public MyPage extends Page {
 *
 *     private JQActionLink link = new JQActionLink("update", "Update Text", "updateId");
 *     private Span span = new Span("span", "spanId");
 *
 *     public void onInit() {
 *
 *         addControl(link);
 *         addControl(span);
 *
 *         link.addBehavior(new AjaxBehavior() {
 *
 *             // Implement the onAjaxAction method which receives Ajax requests
 *             public ActionResult onAction(Control source) {
 *                 // Note JQTaconite is an ActionResult subclass
 *                 JQTaconite actionResult = new JQTaconite();
 *
 *                 // Using a CSS selector to replace the Span content with the latest
 *                 // Date
 *                 actionResult.replaceContent(span, "Updated at " + new Date());
 *
 *                 return actionResult;
 *             }
 *         });
 *     }
 * } </pre>
 *
 * <h3>Multiple Commands</h3>
 *
 * You can add an unlimited amount of commands to the JQTaconite instance. For
 * example:
 *
 * <pre class="prettyprint">
 * public MyPage extends Page {
 *
 *     ...
 *
 *     public void onInit() {
 *
 *         link.addBehavior(new AjaxBehavior() {
 *
 *             // Implement the onAjaxAction method which receives Ajax requests
 *             public ActionResult onAction(Control source) {
 *                 // Note JQTaconite is an ActionResult subclass
 *                 JQTaconite actionResult = new JQTaconite();
 *
 *                 // Using a CSS selector to replace the Span content with the latest
 *                 // Date
 *                 actionResult.replaceContent(span, "Updated at " + new Date());
 *
 *                 // Add a Form after the span
 *                 actionResult.after(span, form);
 *
 *                 // Add a Table after the Form
 *                 actionResult.after(form, table);
 *
 *                 return actionResult;
 *             }
 *         });
 *     }
 * } </pre>
 *
 * The commands are executed one after the other in the order they were added.
 * Note the last two Commands above where first the Form is added and then the
 * Table is added after the Form. This works because the Form has been added
 * by the time the Table is added.
 * <p/>
 * <b>Please note:</b> because Click Pages are <tt>stateless</tt> by default,
 * any Controls added to the Page via Ajax will be removed when the Page is
 * refreshed. It is therefore recommended to consider <tt>stateful</tt> Pages
 * when working with highly dynamic Pages.
 *
 * <h3>Creating custom commands</h3>
 *
 * It is very easy to create your own custom commands. See
 * <a href="JQCommand.html#custom-commands">custom commands</a> for more details.
 *
 * <h3>JavaScript and CSS resource handling</h3>
 *
 * JQTaconite uses the JavaScript, "<tt>/click-jquery/jquery.click.js</tt>",
 * to enable smooth integration with Apache Click.
 * <p/>
 * The "<tt>jquery.click.js</tt>" library includes the following JQuery plugins:
 * <ol>
 * <li><a href="http://www.malsup.com/jquery/taconite/">JQuery JQTaconite</a> - provides
 * ability to execute a list of commands in the browser</li>
 * <li><a href="http://docs.jquery.com/Plugins/livequery">JQuery LiveQuery</a> - allows
 * JavaScript events (onclick, onblur etc) that are bound on Controls
 * to persist even if the underlying Control is replaced by an Ajax request. The
 * latest versions of JQuery supports the new "<tt>live()</tt>" functionality
 * which is similar to LiveQuery, but it does not support all events yet.</li>
 * <li>Click specific functionality - to allow a Control's resource dependencies
 * (JavaScript and CSS) to be added through Ajax requests.</li>
 * </ol>
 *
 * Its worth discussing how Control resources (JavaScript and CSS) are handled
 * by JQTaconite.
 * <p/>
 * Firstly, JQTaconite ensures that all duplicate JavaScript and CSS resoures are
 * removed before returning the response. For example if you return two DateFields,
 * JQTaconite will ensure that only one set of JavaScript and CSS resources are returned.
 * Duplicate resources are identified as follows:
 *
 * <ul>
 * <li>{@link org.apache.click.element.JsImport} and {@link org.apache.click.element.CssImport}
 * are unique based on their attributes "<tt>src</tt>" and "<tt>href</tt>"
 * respectively.
 * <li>{@link org.apache.click.element.JsScript} and
 * {@link org.apache.click.element.CssStyle} are unique based on their "<tt>ID</tt>"
 * attribute. Note, if the ID attribute is not set JQTaconite won't be able to
 * determine if the resource is a duplicate or not.
 * </li>
 * </ul>
 *
 * Secondly, all JavaScript and CSS resources returned to the browser are checked
 * whether they should be added to the Page or not. The identification procedure
 * is the same as above:
 *
 * <ul>
 * <li>if a {@link org.apache.click.element.JsImport} or {@link org.apache.click.element.CssImport}
 * is already part of the Page, it is not added again.</li>
 * <li>if a {@link org.apache.click.element.JsScript} or {@link org.apache.click.element.CssStyle}
 * has an ID attribute defined, the Page is checked for <tt>Script</tt> and <tt>Style</tt>
 * elements with the same ID. If an existing element is found the
 * <tt>Script</tt> or <tt>Style</tt> is not added to the Page. Note, sometimes
 * it is desirable to execute a Script regardless if it has executed before. In
 * those situations simply ignore the ID attribute.
 * </li>
 * </ul>
 */
public class JQTaconite extends ActionResult {

    // -------------------------------------------------------------- Constants

    /** The "<tt>addClass</tt>" command constant. */
    public static final String ADD_CLASS = "addClass";

    /**
     * The "<tt>ADD_HEADER</tt>" command constant for adding JavaScript and CSS
     * resources to the Ajax response.
     */
    public static final String ADD_HEADER = "addHeader";

    /** The "<tt>after</tt>" command constant. */
    public static final String AFTER = "after";

    /** The "<tt>append</tt>" command constant. */
    public static final String APPEND = "append";

    /** The "<tt>attr</tt>" command constant. */
    public static final String ATTR = "attr";

    /** The "<tt>before</tt>" command constant. */
    public static final String BEFORE = "before";

    /** The "<tt>css</tt>" command constant. */
    public static final String CSS = "css";

    /** The "<tt>empty</tt>" command constant. */
    public static final String EMPTY = "empty";

    /**
     * The "<tt>eval</tt>" command constant for executed raw JavaScript in the
     * browser.
     */
    public static final String EVAL = "eval";

    /** The "<tt>hide</tt>" command constant. */
    public static final String HIDE = "hide";

    /** The "<tt>prepend</tt>" command constant. */
    public static final String PREPEND = "prepend";

    /** The "<tt>remove</tt>" command constant. */
    public static final String REMOVE = "remove";

    /** The "<tt>removeClass</tt>" command constant. */
    public static final String REMOVE_CLASS = "removeClass";

    /** The "<tt>after</tt>" command constant. */
    public static final String REPLACE = "replace";

    /** The "<tt>replaceContent</tt>" command constant. */
    public static final String REPLACE_CONTENT = "replaceContent";

    /** The "<tt>show</tt>" command constant. */
    public static final String SHOW = "show";

    /** The "<tt>wrap</tt>" command constant. */
    public static final String WRAP = "wrap";

    /**
     * The "<tt>CUSTOM</tt>" command constant. This command will be ignored by
     * taconite and allow users to pass content to other Ajax callbacks on the
     * browser.
     */
    public static final String CUSTOM = "custom";

    // -------------------------------------------------------------- Variables

    /** The list of commands to execute. */
    protected List<JQCommand> commands = new ArrayList<JQCommand>();

    protected boolean skipHeadElements = false;

    // ----------------------------------------------------------- Constructors

    /**
     * Create a default JQTaconite instance.
     * <p/>
     * Please note, the {@link #setContentType(java.lang.String)} is automatically
     * set to {@link #XML} since JQTaconite returns an XML document.
     */
    public JQTaconite() {
        setContentType(XML);
    }

    /**
     * Create a JQTaconite instance with the given command.
     * <p/>
     * Please note, the {@link #setContentType(java.lang.String)} is automatically
     * set to {@link #XML} since JQTaconite returns an XML document.
     *
     * @param command the command for this JQTaconite instance
     */
    public JQTaconite(JQCommand command) {
        setContentType(XML);
        add(command);
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Return the JQTaconite XML document root tag: "<tt>taconite</tt>".
     *
     * @return the the JQTaconite XML document root tag
     */
    public String getTag() {
        return "taconite";
    }

    /**
     * @return the skipHeadElements
     */
    public boolean isSkipHeadElements() {
        return skipHeadElements;
    }

    /**
     * @param skipHeadElements the skipHeadElements to set
     */
    public void setSkipHeadElements(boolean skipHeadElements) {
        this.skipHeadElements = skipHeadElements;
    }

    /**
     * Add the given command to the list of commands.
     *
     * @param command the command to add
     * @return the JQTaconite instance
     */
    public JQTaconite add(JQCommand command) {
        commands.add(command);
        return this;
    }

    /**
     * Add the given command to the list of commands at the given index.
     *
     * @param command the command to add
     * @param index the index to add the command to
     * @return the JQTaconite instance
     */
    public JQTaconite insert(JQCommand command, int index) {
        commands.add(index, command);
        return this;
    }

    /**
     * Remove the command from the list of commands.
     *
     * @param command the command to remove
     * @return the JQTaconite instance
     */
    public JQTaconite remove(JQCommand command) {
        commands.remove(command);
        return this;
    }

    /**
     * Create an {@link #ADD_CLASS} command for the given CSS selector and name/value
     * pair and add it to the list of commands.
     * <p/>
     * Set the attribute name/value pair of the element/s targeted by
     * the CSS selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param value the class to add
     * @return the command that was created
     */
    public JQCommand addClass(String selector, String value) {
        if (selector == null) {
            throw new IllegalArgumentException("Selector is null");
        }
        return create(JQTaconite.ADD_CLASS, selector, value);
    }

    /**
     * Create an {@link #ADD_CLASS} command for the target Control and name/value
     * pair and add it to the list of commands.
     * <p/>
     * Set the attribute name/value pair of the target Control.
     *
     * @param target the target Control which attribute to set
     * @param value the class to add
     * @return the command that was created
     */
    public JQCommand addClass(Control target, String value) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.ADD_CLASS, getCssSelector(target), value);
    }

    /**
     * Create an {@link #ADD_CLASS} command for the given CSS selector and attributes
     * array and add it to the list of commands.
     * <p/>
     * Set the attributes of the element/s targeted by the CSS selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param attributes the attributes to set
     * @return the command that was created
     */
    public JQCommand addClass(String selector, String... attributes) {
        if (selector == null) {
            throw new IllegalArgumentException("Selector is null");
        }
        return create(JQTaconite.ADD_CLASS, selector, attributes);
    }

    /**
     * Create an {@link #ADD_CLASS} command for the target Control and attributes
     * array and add it to the list of commands.
     * <p/>
     * Set the attributes of the target Control.
     *
     * @param target the target Control which attributes to set
     * @param attributes the attributes to set
     * @return the command that was created
     */
    public JQCommand addClass(Control target, String... attributes) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.ADD_CLASS, getCssSelector(target), attributes);
    }

    /**
     * Create an {@link #ADD_HEADER} command for the given Element and add it to
     * the list of commands.
     * <p/>
     * If the Element is an instance of {@link org.apache.click.element.JsImport},
     * {@link org.apache.click.element.CssImport} or
     * {@link org.apache.click.element.CssStyle} it will be added to the Page
     * HEAD (&lt;head&gt;).
     * <p/>
     * If the given Element is an instance of {@link org.apache.click.element.JsScript}
     * it will be evaluated instead.
     *
     * @param element the Element to add
     * @return the command that was created
     */
    public JQCommand addHeader(Element element) {
        if (element == null) {
            throw new IllegalArgumentException("Element is null");
        }
        JQCommand command = newCommand(JQTaconite.ADD_HEADER);
        command.add(element);
        return command;
    }

    /**
     * Create an {@link #ADD_HEADER} command for the given list of Elements and
     * add it to the list of commands.
     * <p/>
     * If an Element in the list is an instance of {@link org.apache.click.element.JsImport},
     * {@link org.apache.click.element.CssImport} or
     * {@link org.apache.click.element.CssStyle} it will be added to the Page
     * HEAD (&lt;head&gt;).
     * <p/>
     * If an Element in the list is an instance of {@link org.apache.click.element.JsScript}
     * it will be evaluated instead.
     *
     * @param elements the list of Elements to add
     * @return the command that was created
     */
    public JQCommand addHeader(List<Element> elements) {
        if (elements == null) {
            throw new IllegalArgumentException("Elements are null");
        }
        JQCommand command = newCommand(JQTaconite.ADD_HEADER);
        command.setContent(elements);
        return command;
    }

    /**
     * Create an {@link #AFTER} command for the given CSS selector and content
     * and add it to the list of commands.
     * <p/>
     * The given content will be added after the element/s targeted by the CSS
     * selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param content the content to add after the element/s targeted by the
     * CSS selector
     * @return the command that was created
     */
    public JQCommand after(String selector, String content) {
        return create(JQTaconite.AFTER, selector, content);
    }

    /**
     * Create an {@link #AFTER} command for the given CSS selector and Control
     * and add it to the list of commands.
     * <p/>
     * The given Control will be added after the element/s targeted by the CSS
     * selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param content the Control to add after the element/s targeted by
     * the CSS selector
     * @return the command that was created
     */
    public JQCommand after(String selector, Control content) {
        if (content == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.AFTER, selector, content);
    }

    /**
     * Create an {@link #AFTER} command for the given Control and content
     * and add it to the list of commands.
     * <p/>
     * The given content will be added after the target Control.
     *
     * @param target the target Control
     * @param content the content to add after the target Control
     * @return the command that was created
     */
    public JQCommand after(Control target, String content) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.AFTER, getCssSelector(target), content);
    }

    /**
     * Create an {@link #AFTER} command for the target Control and content
     * and add it to the list of commands.
     * <p/>
     * The content Control will be added after the target Control.
     *
     * @param target the target Control
     * @param content the content to add after the target Control
     * @return the command that was created
     */
    public JQCommand after(Control target, Control content) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.AFTER, getCssSelector(target), content);
    }

    /**
     * Create an {@link #APPEND} command for the given CSS selector and content
     * and add it to the list of commands.
     * <p/>
     * The given content will be appended to the element/s targeted by the CSS
     * selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param content the content to append to the element/s targeted by the
     * CSS selector
     * @return the command that was created
     */
    public JQCommand append(String selector, String content) {
        return create(JQTaconite.APPEND, selector, content);
    }

    /**
     * Create an {@link #APPEND} command for the given CSS selector and Control
     * and add it to the list of commands.
     * <p/>
     * The given Control will be appended to the element/s targeted by the CSS
     * selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param content the Control to append to the element/s targeted by
     * the CSS selector
     * @return the command that was created
     */
    public JQCommand append(String selector, Control content) {
        if (content == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.APPEND, selector, content);
    }

    /**
     * Create an {@link #APPEND} command for the given Control and content
     * and add it to the list of commands.
     * <p/>
     * The given content will be appended to the target Control.
     *
     * @param target the target Control
     * @param content the content to append to the target Control
     * @return the command that was created
     */
    public JQCommand append(Control target, String content) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.APPEND, getCssSelector(target), content);
    }

    /**
     * Create an {@link #APPEND} command for the target Control and content
     * and add it to the list of commands.
     * <p/>
     * The content Control will be appended to the target Control.
     *
     * @param target the target Control
     * @param content the content to append to the target Control
     * @return the command that was created
     */
    public JQCommand append(Control target, Control content) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.APPEND, getCssSelector(target), content);
    }

    /**
     * Create an {@link #ATTR} command for the given CSS selector and name/value
     * pair and add it to the list of commands.
     * <p/>
     * Set the attribute name/value pair of the element/s targeted by
     * the CSS selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param name the name of the attribute
     * @param value the value of the attribute
     * @return the command that was created
     */
    public JQCommand attr(String selector, String name, String value) {
        if (selector == null) {
            throw new IllegalArgumentException("Selector is null");
        }
        return create(JQTaconite.ATTR, selector, name, value);
    }

    /**
     * Create an {@link #ATTR} command for the target Control and name/value
     * pair and add it to the list of commands.
     * <p/>
     * Set the attribute name/value pair of the target Control.
     *
     * @param target the target Control which attribute to set
     * @param name the name of the attribute
     * @param value the value of the attribute
     * @return the command that was created
     */
    public JQCommand attr(Control target, String name, String value) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.ATTR, getCssSelector(target), name, value);
    }

    /**
     * Create an {@link #ATTR} command for the given CSS selector and attributes
     * array and add it to the list of commands.
     * <p/>
     * Set the attributes of the element/s targeted by the CSS selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param attributes the attributes to set
     * @return the command that was created
     */
    public JQCommand attr(String selector, String... attributes) {
        if (selector == null) {
            throw new IllegalArgumentException("Selector is null");
        }
        return create(JQTaconite.ATTR, selector, attributes);
    }

    /**
     * Create an {@link #ATTR} command for the target Control and attributes
     * array and add it to the list of commands.
     * <p/>
     * Set the attributes of the target Control.
     *
     * @param target the target Control which attributes to set
     * @param attributes the attributes to set
     * @return the command that was created
     */
    public JQCommand attr(Control target, String... attributes) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.ATTR, getCssSelector(target), attributes);
    }

    /**
     * Create a {@link #BEFORE} command for the given CSS selector and content
     * and add it to the list of commands.
     * <p/>
     * The given content will be added before the element/s targeted by the CSS
     * selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param content the content to add before the element/s targeted by the
     * CSS selector
     * @return the command that was created
     */
    public JQCommand before(String selector, String content) {
        return create(JQTaconite.BEFORE, selector, content);
    }

    /**
     * Create a {@link #BEFORE} command for the given CSS selector and Control
     * and add it to the list of commands.
     * <p/>
     * The given Control will be added before the element/s targeted by the CSS
     * selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param content the Control to add before the element/s targeted by
     * the CSS selector
     * @return the command that was created
     */
    public JQCommand before(String selector, Control content) {
        if (content == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.BEFORE, selector, content);
    }

    /**
     * Create a {@link #BEFORE} command for the given Control and content
     * and add it to the list of commands.
     * <p/>
     * The given content will be added before the target Control.
     *
     * @param target the target Control
     * @param content the content to add before the target Control
     * @return the command that was created
     */
    public JQCommand before(Control target, String content) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.BEFORE, getCssSelector(target), content);
    }

    /**
     * Create a {@link #BEFORE} command for the target Control and content
     * and add it to the list of commands.
     * <p/>
     * The content Control will be added before the target Control.
     *
     * @param target the target Control
     * @param content the content to add before the target Control
     * @return the command that was created
     */
    public JQCommand before(Control target, Control content) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.BEFORE, getCssSelector(target), content);
    }

    /**
     * Create an {@link #CSS} command for the given CSS selector and name/value
     * pair and add it to the list of commands.
     * <p/>
     * Set the attribute name/value pair of the element/s targeted by
     * the CSS selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param name the name of the attribute
     * @param value the value of the attribute
     * @return the command that was created
     */
    public JQCommand css(String selector, String name, String value) {
        if (selector == null) {
            throw new IllegalArgumentException("Selector is null");
        }
        return create(JQTaconite.CSS, selector, name, value);
    }

    /**
     * Create an {@link #CSS} command for the target Control and name/value
     * pair and add it to the list of commands.
     * <p/>
     * Set the attribute name/value pair of the target Control.
     *
     * @param target the target Control which attribute to set
     * @param name the name of the attribute
     * @param value the value of the attribute
     * @return the command that was created
     */
    public JQCommand css(Control target, String name, String value) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.CSS, getCssSelector(target), name, value);
    }

    /**
     * Create an {@link #ATTR} command for the given CSS selector and attributes
     * array and add it to the list of commands.
     * <p/>
     * Set the attributes of the element/s targeted by the CSS selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param attributes the attributes to set
     * @return the command that was created
     */
    public JQCommand css(String selector, String... attributes) {
        if (selector == null) {
            throw new IllegalArgumentException("Selector is null");
        }
        return create(JQTaconite.CSS, selector, attributes);
    }

    /**
     * Create an {@link #CSS} command for the target Control and attributes
     * array and add it to the list of commands.
     * <p/>
     * Set the attributes of the target Control.
     *
     * @param target the target Control which attributes to set
     * @param attributes the attributes to set
     * @return the command that was created
     */
    public JQCommand css(Control target, String... attributes) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.CSS, getCssSelector(target), attributes);
    }

    /**
     * Create a {@link #CUSTOM} command for the given content* and add it to the
     * list of commands. This method provides a simple way to transfer the
     * content to the browser, without JQTaconite processing it.
     *
     * @param content the content to add to the command
     * @return the command that was created
     */
    public JQCommand custom(String content) {
        if (StringUtils.isBlank(content)) {
            throw new IllegalArgumentException("content is null or blank");
        }
        JQCommand command = newCommand(JQTaconite.CUSTOM);
        command.add(content);
        return command;
    }

    /**
     * Create a {@link #CUSTOM} command for the given Control and add it to the
     * list of commands. This method provides a simple way to transfer the
     * control content to the browser, without JQTaconite processing it.
     *
     * @param content the Control to add to command
     * @return the command that was created
     */
    public JQCommand custom(Control content) {
        if (content == null) {
            throw new IllegalArgumentException("content is null");
        }
        JQCommand command = newCommand(JQTaconite.CUSTOM);
        command.add(content);
        return command;
    }

    /**
     * Create an {@link #EMPTY} command for the target Control and add
     * it to the list of commands.
     * <p/>
     * The target Control's child nodes will be removed.
     *
     * @param target the target Control which child nodes to remove
     * @return the command that was created
     */
    public JQCommand empty(Control target) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.EMPTY, getCssSelector(target));
    }

    /**
     * Create an {@link #EMPTY} command for the given CSS selector and add
     * it to the list of commands.
     * <p/>
     * The child nodes of the element/s targeted by the CSS selector will be
     * removed.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @return the command that was created
     */
    public JQCommand empty(String selector) {
        if (selector == null) {
            throw new IllegalArgumentException("Selector is null");
        }
        return create(JQTaconite.EMPTY, selector);
    }

    /**
     * Create a {@link #HIDE} command for the target Control and add
     * it to the list of commands.
     * <p/>
     * The target Control to hide.
     *
     * @param target the target Control to hide
     * @return the command that was created
     */
    public JQCommand hide(Control target) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.HIDE, getCssSelector(target));
    }

    /**
     * Create a {@link #HIDE} command for the given CSS selector and add
     * it to the list of commands.
     * <p/>
     * The element/s targeted by the CSS selector will be hidden.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @return the command that was created
     */
    public JQCommand hide(String selector) {
        if (selector == null) {
            throw new IllegalArgumentException("Selector is null");
        }
        return create(JQTaconite.HIDE, selector);
    }

    /**
     * Create an {@link #EVAL} command for the given JavaScript content and add
     * it to the list of commands.
     * <p/>
     * The given JavaScript content will evaluated in the browser.
     *
     * @param content the JavaScript content to be evaluated in the browser
     * @return the command that was created
     */
    public JQCommand eval(String content) {
        if (StringUtils.isBlank(content)) {
            throw new IllegalArgumentException("Content cannot be empty.");
        }
        JQCommand command = newCommand(JQTaconite.EVAL);
        command.getContent().add(content);
        return command;
    }

    /**
     * Create an {@link #EVAL} command for the given
     * {@link org.apache.click.element.JsScript} and add it to the list of
     * commands.
     * <p/>
     * The given JsScript content will evaluated in the browser.
     *
     * @param script the JsScript content to be evaluated in the browser
     * @return the command that was created
     */
    public JQCommand eval(JsScript script) {
        if (script == null) {
            throw new IllegalArgumentException("Script cannot be null.");
        }
        JQCommand command = newCommand(JQTaconite.EVAL);
        command.add(script);
        return command;
    }

    /**
     * Create a {@link #PREPEND} command for the given CSS selector and content
     * and add it to the list of commands.
     * <p/>
     * The given content will be prepended to the element/s targeted by the CSS
     * selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param content the content to prepend to the element/s targeted by the
     * CSS selector
     * @return the command that was created
     */
    public JQCommand prepend(String selector, String content) {
        return create(JQTaconite.PREPEND, selector, content);
    }

    /**
     * Create a {@link #PREPEND} command for the given CSS selector and Control
     * and add it to the list of commands.
     * <p/>
     * The given Control will be prepended to the element/s targeted by the CSS
     * selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param content the Control to prepend to the element/s targeted by
     * the CSS selector
     * @return the command that was created
     */
    public JQCommand prepend(String selector, Control content) {
        if (content == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.PREPEND, selector, content);
    }

    /**
     * Create a {@link #PREPEND} command for the given Control and content
     * and add it to the list of commands.
     * <p/>
     * The given content will be prepended to the target Control.
     *
     * @param target the target Control
     * @param content the content to prepend to the target Control
     * @return the command that was created
     */
    public JQCommand prepend(Control target, String content) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.PREPEND, getCssSelector(target), content);
    }

    /**
     * Create a {@link #PREPEND} command for the target Control and content
     * and add it to the list of commands.
     * <p/>
     * The content Control will be prepended to the target Control.
     *
     * @param target the target Control
     * @param content the content to prepend to the target Control
     * @return the command that was created
     */
    public JQCommand prepend(Control target, Control content) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.PREPEND, getCssSelector(target), content);
    }

    /**
     * Create a {@link #REMOVE} command for the target Control and add it to the
     * list of commands.
     * <p/>
     * The target Control will be remove.
     *
     * @param target the target Control
     * @return the command that was created
     */
    public JQCommand remove(Control target) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.REMOVE, getCssSelector(target));
    }

    /**
     * Create a {@link #REMOVE} command for the CSS selector and add it to the
     * list of commands.
     * <p/>
     * The element/s targeted by the CSS selector will be removed.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @return the command that was created
     */
    public JQCommand remove(String selector) {
        return create(JQTaconite.REMOVE, selector);
    }

    /**
     * Create an {@link #REMOVE_CLASS} command for the given CSS selector and name/value
     * pair and add it to the list of commands.
     * <p/>
     * Set the attribute name/value pair of the element/s targeted by
     * the CSS selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param value the class to remove
     * @return the command that was created
     */
    public JQCommand removeClass(String selector, String value) {
        if (selector == null) {
            throw new IllegalArgumentException("Selector is null");
        }
        return create(JQTaconite.REMOVE_CLASS, selector, value);
    }

    /**
     * Create an {@link #REMOVE_CLASS} command for the target Control and name/value
     * pair and add it to the list of commands.
     * <p/>
     * Set the attribute name/value pair of the target Control.
     *
     * @param target the target Control which attribute to set
     * @param value the class to remove
     * @return the command that was created
     */
    public JQCommand removeClass(Control target, String value) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.REMOVE_CLASS, getCssSelector(target), value);
    }

    /**
     * Create an {@link #REMOVE_CLASS} command for the given CSS selector and attributes
     * array and add it to the list of commands.
     * <p/>
     * Set the attributes of the element/s targeted by the CSS selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param attributes the attributes to set
     * @return the command that was created
     */
    public JQCommand removeClass(String selector, String... attributes) {
        if (selector == null) {
            throw new IllegalArgumentException("Selector is null");
        }
        return create(JQTaconite.REMOVE_CLASS, selector, attributes);
    }

    /**
     * Create an {@link #REMOVE_CLASS} command for the target Control and attributes
     * array and add it to the list of commands.
     * <p/>
     * Set the attributes of the target Control.
     *
     * @param target the target Control which attributes to set
     * @param attributes the attributes to set
     * @return the command that was created
     */
    public JQCommand removeClass(Control target, String... attributes) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.REMOVE_CLASS, getCssSelector(target), attributes);
    }

    /**
     * Create a {@link #REPLACE} command for the given CSS selector and content
     * and add it to the list of commands.
     * <p/>
     * The given content will replace the element/s targeted by the CSS
     * selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param content the content to replace the element/s targeted by the
     * CSS selector
     * @return the command that was created
     */
    public JQCommand replace(String selector, String content) {
        return create(JQTaconite.REPLACE, selector, content);
    }

    /**
     * Create a {@link #REPLACE} command for the target Control and add it to the
     * list of commands.
     * <p/>
     * The target Control will be replace with itself.
     *
     * @param target the target Control to replace itself with
     * @return the command that was created
     */
    public JQCommand replace(Control target) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.REPLACE, getCssSelector(target), target);
    }

    /**
     * Create a {@link #REPLACE} command for the given CSS selector and Control
     * and add it to the list of commands.
     * <p/>
     * The given Control will replace the element/s targeted by the CSS
     * selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param content the Control to replace the element/s targeted by
     * the CSS selector
     * @return the command that was created
     */
    public JQCommand replace(String selector, Control content) {
        if (content == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.REPLACE, selector, content);
    }

    /**
     * Create a {@link #REPLACE} command for the target Control and content
     * and add it to the list of commands.
     * <p/>
     * The content will replace the target Control.
     *
     * @param target the target Control
     * @param content the content to replace the target Control
     * @return the command that was created
     */
    public JQCommand replace(Control target, String content) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.REPLACE, getCssSelector(target), content);
    }

    /**
     * Create a {@link #REPLACE} command for the target Control and content
     * and add it to the list of commands.
     * <p/>
     * The content Control will replace the target Control.
     *
     * @param target the target Control
     * @param content the content to replace the target Control with
     * @return the command that was created
     */
    public JQCommand replace(Control target, Control content) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.REPLACE, getCssSelector(target), content);
    }

    /**
     * Create a {@link #REPLACE_CONTENT} command for the given CSS selector and
     * content and add it to the list of commands.
     * <p/>
     * The given content will replace the content of the element/s targeted by
     * the CSS selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param content the content to replace the content of the element/s
     * targeted by the CSS selector
     * @return the command that was created
     */
    public JQCommand replaceContent(String selector, String content) {
        return create(JQTaconite.REPLACE_CONTENT, selector, content);
    }

    /**
     * Create a {@link #REPLACE_CONTENT} command for the target Control and add
     * it to the list of commands.
     * <p/>
     * The target Control will replace its content.
     *
     * @param target the target Control to replace its content with
     * @return the command that was created
     */
    public JQCommand replaceContent(Control target) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.REPLACE_CONTENT, getCssSelector(target), target);
    }

    /**
     * Create a {@link #REPLACE_CONTENT} command for the target Control and
     * content and add it to the list of commands.
     * <p/>
     * The content Control will replace the content of the target Control.
     *
     * @param target the target Control
     * @param content the content to replace the content of the target Control
     * with
     * @return the command that was created
     */
    public JQCommand replaceContent(Control target, Control content) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.REPLACE_CONTENT, getCssSelector(target), content);
    }

    /**
     * Create a {@link #REPLACE_CONTENT} command for the target Control and
     * content and add it to the list of commands.
     * <p/>
     * The content will replace the content of the target Control.
     *
     * @param target the target Control
     * @param content the content to replace the content of the target Control
     * with
     * @return the command that was created
     */
    public JQCommand replaceContent(Control target, String content) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.REPLACE_CONTENT, getCssSelector(target), content);
    }

    /**
     * Create a {@link #REPLACE_CONTENT} command for the given CSS selector and
     * Control and add it to the list of commands.
     * <p/>
     * The given Control will replace the content of the element/s targeted by
     * the CSS selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param content the Control to replace the content of the element/s
     * targeted by the CSS selector
     * @return the command that was created
     */
    public JQCommand replaceContent(String selector, Control content) {
        if (content == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.REPLACE_CONTENT, selector, content);
    }

    /**
     * Create a {@link #SHOW} command for the target Control and add
     * it to the list of commands.
     * <p/>
     * The target Control to show.
     *
     * @param target the target Control to show
     * @return the command that was created
     */
    public JQCommand show(Control target) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.SHOW, getCssSelector(target));
    }

    /**
     * Create a {@link #SHOW} command for the given CSS selector and add
     * it to the list of commands.
     * <p/>
     * The element/s targeted by the CSS selector will be shown.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @return the command that was created
     */
    public JQCommand show(String selector) {
        if (selector == null) {
            throw new IllegalArgumentException("Selector is null");
        }
        return create(JQTaconite.SHOW, selector);
    }

    /**
     * Create a {@link #WRAP} command for the given CSS selector and content
     * and add it to the list of commands.
     * <p/>
     * The given content will wrap the element/s targeted by the CSS
     * selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param content the content that will wrap the element/s targeted by the
     * CSS selector
     * @return the command that was created
     */
    public JQCommand wrap(String selector, String content) {
        return create(JQTaconite.WRAP, selector, content);
    }

    /**
     * Create a {@link #WRAP} command for the given CSS selector and Control
     * and add it to the list of commands.
     * <p/>
     * The given Control will wrap the element/s targeted by the CSS
     * selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param content the Control that will wrap the element/s targeted by
     * the CSS selector
     * @return the command that was created
     */
    public JQCommand wrap(String selector, Control content) {
        if (content == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.WRAP, selector, content);
    }

    /**
     * Create a {@link #WRAP} command for the target Control and content
     * and add it to the list of commands.
     * <p/>
     * The content Control will wrap the target Control.
     *
     * @param target the target Control
     * @param content the content that will wrap the target Control
     * @return the command that was created
     */
    public JQCommand wrap(Control target, Control content) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.WRAP, getCssSelector(target), content);
    }

    /**
     * Create a {@link #WRAP} command for the target Control and content
     * and add it to the list of commands.
     * <p/>
     * The content that will wrap the target Control.
     *
     * @param target the target Control
     * @param content the content that will wrap the target Control
     * @return the command that was created
     */
    public JQCommand wrap(Control target, String content) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return create(JQTaconite.WRAP, getCssSelector(target), content);
    }

    /**
     * Create a new command for the given command name and CSS selector.
     *
     * @param commandName the name of the command to execute
     * @param selector the CSS selector to identify the target of the operation
     * @return the command created
     */
    public JQCommand create(String commandName, String selector) {
        if (StringUtils.isBlank(commandName)) {
            throw new IllegalArgumentException("Command name is null or blank: "
                + commandName);
        }
        JQCommand command = newCommand(commandName, selector);
        return command;
    }

    /**
     * Create a new command for the given command name, CSS selector and content.
     *
     * @param commandName the name of the command to execute
     * @param selector the CSS selector to identify the target of the operation
     * @param content the command content
     * @return the command created
     */
    public JQCommand create(String commandName, String selector, Control content) {
        if (StringUtils.isBlank(commandName)) {
            throw new IllegalArgumentException("Command name is null or blank: "
                + commandName);
        }
        if (content == null) {
            throw new IllegalArgumentException("Control is null");
        }
        JQCommand command = newCommand(commandName, selector);
        command.add(content);
        return command;
    }

    /**
     * Create a new command for the given command name, CSS selector and content.
     *
     * @param commandName the name of the command to execute
     * @param selector the CSS selector to identify the target of the operation
     * @param content the command content
     * @return the command created
     */
    public JQCommand create(String commandName, String selector, String content) {
        if (StringUtils.isBlank(commandName)) {
            throw new IllegalArgumentException("Command name is null or blank: "
                + commandName);
        }
        if (StringUtils.isBlank(content)) {
            throw new IllegalArgumentException("Content is null or blank: " + content);
        }
        JQCommand command = newCommand(commandName, selector);
        command.add(content);
        return command;
    }

    /**
     * Create a new command for the given command name, CSS selector and
     * name/value attribute pair.
     *
     * @param commandName the name of the command to execute
     * @param selector the CSS selector to identify the target of the operation
     * @param name the command name attribute
     * @param value the command value attribute
     * @return the command created
     */
    public JQCommand create(String commandName, String selector, String name, String value) {
        if (StringUtils.isBlank(commandName)) {
            throw new IllegalArgumentException("Command name is null or blank: "
                + commandName);
        }
        if (name == null) {
            throw new IllegalArgumentException("name is null");
        }
        if (value == null) {
            throw new IllegalArgumentException("value is null");
        }
        JQCommand command = newCommand(commandName, selector);
        command.setName(name);
        command.setValue(value);
        return command;
    }

    /**
     * Create a new command for the given command name, CSS selector and
     * attributes.
     *
     * @param commandName the name of the command to execute
     * @param selector the CSS selector to identify the target of the operation
     * @param attributes the command attributes
     * @return the command created
     */
    public JQCommand create(String commandName, String selector, String... attributes) {
        if (StringUtils.isBlank(commandName)) {
            throw new IllegalArgumentException("Command name is null or blank: "
                + commandName);
        }
        if (attributes == null) {
            throw new IllegalArgumentException("attributes is null");
        }
        JQCommand command = newCommand(commandName, selector);
        command.arguments(attributes);
        return command;
    }

   /**
     * Render JQTaconite to the specified response.
     *
     * @param request the page servlet request
     * @param response the page servlet response
     */
    @Override
    protected void renderActionResult(Context context) {
        setReader(new StringReader(toString()));
        super.renderActionResult(context);
    }

    /**
     * Render the XML representation of JQTaconite to the given buffer.
     *
     * @param buffer the buffer to render output to
     */
    public void render(HtmlStringBuffer buffer) {
        if (!isSkipHeadElements()) {
            processHeadElements();
        }

        String tag = getTag();
        renderTagBegin(tag, buffer);
        buffer.closeTag();
        buffer.append("\n");

        renderContent(buffer);

        renderTagEnd(tag, buffer);
    }

    /**
     * Return a XML representation of JQTaconite.
     *
     * @return the XML representation of JQTaconite
     */
    @Override
    public String toString() {
        HtmlStringBuffer buffer = new HtmlStringBuffer(commands.size() + 150);
        render(buffer);
        return buffer.toString();
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Return the CSS selector for the given control.
     * <p/>
     * This method delegates to {@link org.apache.click.Control#getCssSelector()}
     * to retrieve the CSS selector for the given control.
     *
     * @param control the control to lookup a CSS selector for
     * @return the CSS selector for the given control
     */
    protected String getCssSelector(Control control) {
        String selector = ClickUtils.getCssSelector(control);
        if (selector == null) {
            throw new IllegalArgumentException("No selector could be found for"
                + " the control: " + control.getClass().getName() + "#"
                + control.getName());
        }
        return selector;
    }

    /**
     * Render the {@link #getTag() tag} name to the given buffer.
     *
     * @param tagName the tag name to render
     * @param buffer the buffer to render to
     */
    protected void renderTagBegin(String tagName, HtmlStringBuffer buffer) {
        buffer.elementStart(tagName);
    }

    /**
     * Render the JQTaconite content to the given buffer.
     *
     * @param buffer the buffer to render to
     */
    protected void renderContent(HtmlStringBuffer buffer) {
        for (JQCommand command : commands) {
            command.render(buffer);
            buffer.append("\n");
        }
    }

    /**
     * Closes the specified tag.
     *
     * @param tagName the tag name to render
     * @param buffer the buffer to render to
     */
    protected void renderTagEnd(String tagName, HtmlStringBuffer buffer) {
        buffer.elementEnd(tagName);
    }

    /**
     * Process the HEAD elements of Controls contained within JQTaconite commands.
     * This method will recursively process Containers and all child controls.
     * <p/>
     * All non JsScript HEAD elements will be added to a new JQCommand which is
     * set as the first command in JQTaconite. This ensures that HEAD elements
     * are present in the Page before any DOM content is added.
     * <p/>
     * All JsScript HEAD elements will be added to a new JQCommand which is set
     * as the last command in JQTaconite. This ensures that JavaScript snippets
     * are executed after the DOM content is available. If JsScript elements
     * was not added after DOM content you would run into situations where
     * JavaScript code would reference DOM elements which have not been added
     * to the DOM yet.
     */
    protected void processHeadElements() {
        if (commands.isEmpty()) {
            return;
        }

        PageImports pageImports = new PageImports(null);

        Iterator it = commands.iterator();
        List<JQCommand> evalCommands = null;
        while (it.hasNext()) {
            JQCommand command = (JQCommand) it.next();

            if (EVAL.equals(command.getCommand())) {
                // Eval commands are not added to the head section of the taconite
                // response. These commands are processed in the order they were
                // added, but after all other commands were processed
                if (evalCommands == null) {
                    evalCommands = new ArrayList<JQCommand>();
                }
                evalCommands.add(command);
                it.remove();
                continue;
            }
            processHeadElements(command, pageImports);

            // Ensure the addHeader commands are removed since they were already
            // processed by the line above
            if (ADD_HEADER.equals(command.getCommand())) {
                it.remove();
            }
        }

        List<Element> headElements = pageImports.getHeadElements();

        // Ensure CssStyle content is wrapped in CDATA tags because the content
        // must be valid XML.
        for (int i = 0, size = headElements.size(); i < size; i++) {
            Element element = headElements.get(i);
            if (element instanceof CssStyle) {
                ((CssStyle) element).setCharacterData(true);
            }
        }

        List<Element> jsElements = pageImports.getJsElements();

        JQCommand headElementsCommand = new JQCommand(JQTaconite.ADD_HEADER);
        headElementsCommand.setContent(headElements);

        // Place all JsScripts at the bottom of the command list which ensures
        // that new HTML elements added through JQTaconite are present in the DOM
        // when scripts are executed. Otherwise scripts which target elements
        // will fail as the elements are only loaded after the script executes
        JQCommand jsScriptsCommand = new JQCommand(JQTaconite.ADD_HEADER);
        splitJavaScriptElements(headElementsCommand, jsScriptsCommand, jsElements);

        // Add headElements at the top of the command list, but after any CUSTOM
        // commands
        if (headElementsCommand.getContent().size() > 0) {
            int i = 0;
            for (int size = commands.size(); i < size; i++) {
                JQCommand command = commands.get(i);
                if (!CUSTOM.equals(command.getCommand())) {
                    break;
                }
            }
            commands.add(i, headElementsCommand);
        }

        // Add JsScript elements at the bottom of the command list
        if(jsScriptsCommand.getContent().size() > 0) {
            commands.add(jsScriptsCommand);
        }
        // Add eval commands last
        if (evalCommands != null) {
            commands.addAll(evalCommands);
        }
    }

    /**
     * Create a new command for the given command name and add the command to
     * JQTaconite's {@link #commands} list.
     *
     * @param commandName the name of the command to execute
     * @return the command created
     */
    protected JQCommand newCommand(String commandName) {
        JQCommand command = new JQCommand(commandName);
        add(command);
        return command;
    }

    /**
     * Create a new command for the given command name and CSS selector and add
     * the command to JQTaconite's {@link #commands} list.
     *
     * @param commandName the name of the command to execute
     * @param selector the CSS selector of the command
     * @return the command created
     */
    protected JQCommand newCommand(String commandName, String selector) {
        JQCommand command = new JQCommand(commandName);
        if (StringUtils.isNotBlank(selector)) {
            command.setSelector(selector);
        }
        add(command);
        return command;
    }

    // ------------------------------------------------ Package Private Methods

    /**
     * Process the HEAD elements of the given command and add the elements to
     * pageImports.
     *
     * @param command the command to process
     * @param pageImports the pageImports to add HEAD elements to
     */
    void processHeadElements(JQCommand command, PageImports pageImports) {
        for (Object content : command.getContent()) {
            // Add all controls to the PageImports instance
            if (content instanceof Control) {
                Control control = (Control) content;

                // Ensure the head elements are included
                pageImports.processControl(control);
            } else if (content instanceof Element) {
                // Add all Elements to the PageImports instance

                pageImports.add((Element) content);
            }
        }
    }

    /**
     * Add the JavaScript elements (JsImport and JsScript) to either one of the
     * two given commands. JsScript elements must be added to jsScriptsCommand
     * and JsImport elements must be added to headElementsCommand.
     *
     * @param headElementsCommand the command added as the first taconite command
     * @param jsScriptsCommand the command added as the last taconite command
     * @param jsElements list of JsScript elements
     */
    void splitJavaScriptElements(JQCommand headElementsCommand, JQCommand jsScriptsCommand,
        List jsElements) {

        Iterator it = jsElements.iterator();

        while(it.hasNext()) {
            Element element = (Element) it.next();
            if (element instanceof JsScript) {
                JsScript jsScript = (JsScript) element;
                jsScript.setCharacterData(true);

                // If any JavaScript relies on a DOM ready function, we need to
                // ensure this function is *not* rendered because Ajax requests
                // does not trigger DOM ready functions.
                if (jsScript.isExecuteOnDomReady()) {
                    jsScript.setExecuteOnDomReady(false);

                    // Nullify the ID attribute otherwise the script won't be
                    // included in the Page by the jquery.click.js script
                    jsScript.setId(null);
                }
                jsScriptsCommand.add(element);

            } else {
                headElementsCommand.add(element);
            }
        }
    }
}