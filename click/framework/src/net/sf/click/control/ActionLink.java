/*
 * Copyright 2004-2005 Malcolm A. Edgar
 *
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
package net.sf.click.control;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.sf.click.Context;
import net.sf.click.Control;
import net.sf.click.util.ClickUtils;
import net.sf.click.util.HtmlStringBuffer;

/**
 * Provides a Action Link control: &nbsp; &lt;a href=""&gt;&lt;/a&gt;.
 *
 * <table class='htmlHeader' cellspacing='6'>
 * <tr><td>
 * <a href='' title='ActionLink Control'>Action Link</a>
 * </td></tr>
 * </table>
 *
 * This control can render the "href" URL attribute using
 * {@link #getHref()}, or the entire ActionLink anchor tag using
 * {@link #toString()}.
 * <p/>
 * ActionLink support invoking control listeners. An example of using ActionLink
 * to call a logout method is illustrated below:
 *
 * <pre class="codeJava">
 * <span class="kw">public class</span> MyPage <span class="kw">extends</span> Page {
 *
 *     <span class="kw">public void</span> onInit() {
 *         ActionLink link = <span class="kw">new</span> ActionLink(<span class="st">"logoutLink"</span>);
 *         link.setListener(<span class="kw">this</span>, <span class="st">"onLogoutClick"</span>);
 *         addControl(link);
 *     }
 *
 *     <span class="kw">public boolean</span> onLogoutClick() {
 *         <span class="kw">if</span> (getContext().hasSession()) {
 *            getContext().getSession().invalidate();
 *         }
 *         setForward(<span class="st">"logout"</span>);
 *
 *         <span class="kw">return false</span>;
 *     }
 * } </pre>
 *
 * The corresponding template code is below. Note href is evaluated by Velocity
 * to {@link #getHref()}:
 *
 * <pre class="codeHtml">
 * &lt;a href="<span class="blue">$logoutLink</span>.href" title="Click to Logout"&gt;Logout&lt;/a&gt; </pre>
 *
 * ActionLink can also support a value parameter which is accessable
 * using {@link #getValue()}.
 * <p/>
 * For example a products table could include rows
 * of products, each with a get product details ActionLink and add product
 * ActionLink. The ActionLinks include the product's id as a parameter to
 * the {@link #getHref(Object)} method, which is then available when the
 * control is processed:
 *
 * <pre class="codeHtml">
 * &lt;table&gt;
 * <span class="red">#foreach</span> (<span class="blue">$product</span> <span class="red">in</span> <span class="blue">$productList</span>)
 *   &lt;tr&gt;
 *    &lt;td&gt;
 *      $product.name
 *    &lt;/td&gt;
 *    &lt;td&gt;
 *      &lt;a href="<span class="blue">$detailsLink</span>.getHref(<span class="blue">$product</span>.id)" title="Get product information"&gt;Details&lt;/a&gt;
 *    &lt;/td&gt;
 *    &lt;td&gt;
 *      &lt;a href="<span class="blue">$addLink</span>.getHref(<span class="blue">$product</span>.id)" title="Add to basket"&gt;Add&lt;/a&gt;
 *    &lt;/td&gt;
 *   &lt;/tr&gt;
 * <span class="red">#end</span>
 * &lt;/table&gt; </pre>
 *
 * The corresponding Page class for this template is:
 *
 * <pre class="codeJava">
 * <span class="kw">public class</span> ProductsPage <span class="kw">extends</span> Page {
 *
 *     ActionLink addLink;
 *     ActionLink detailsLink;
 *
 *     <span class="kw">public void</span> onInit() {
 *         addLink = <span class="kw">new</span> ActionLink(<span class="st">"addLink"</span>);
 *         addLink.setListener(<span class="kw">this</span>, <span class="st">"onAddClick"</span>);
 *         addControl(addLink);
 *
 *         detailsLink = <span class="kw">new</span> ActionLink(<span class="st">"detailsLink"</span>);
 *         detailsLink.setListener(<span class="kw">this</span>, <span class="st">"onDetailsClick"</span>);
 *         addControl(detailsLink);
 *     }
 *
 *     <span class="kw">public boolean</span> onAddClick() {
 *         <span class="cm">// Get the product clicked on by the user</span>
 *         Integer productId = addLink.getValueInteger();
 *         Product product = ProductDatabase.getProduct(productId);
 *
 *         <span class="cm">// Add product to basket</span>
 *         List basket = (List) getContext().getSessionAttribute(<span class="st">"basket"</span>);
 *         basket.add(product);
 *
 *         <span class="kw">return true</span>;
 *     }
 *
 *     <span class="kw">public boolean</span> onDetailsClick() {
 *         <span class="cm">// Get the product clicked on by the user</span>
 *         Integer productId = detailsLink.getValueInteger();
 *         Product product = ProductDatabase.getProduct(productId);
 *
 *         <span class="cm">// Store the product in the request and display in the details page</span>
 *         getContext().setRequestAttribute(<span class="st">"product"</span>, product);
 *         setForward(<span class="st">"productDetails.html"</span>);
 *
 *         <span class="kw">return false</span>;
 *     }
 *
 *     <span class="kw">public void</span> onGet() {
 *         <span class="cm">// Display the list of available products</span>
 *         List productList = ProductDatabase.getProducts();
 *         addModel("<span class="blue">productList</span>", productList);
 *     }
 * } </pre>
 *
 * See also the W3C HTML reference:
 * <a title="W3C HTML 4.01 Specification"
 *    href="../../../../../html/interact/links.html#h-12.2">A Links</a>
 *
 * @see Submit
 *
 * @author Malcolm Edgar
 * @version $Id$
 */
public class ActionLink implements Control {

    // -------------------------------------------------------------- Constants

    private static final long serialVersionUID = 8469056846426557956L;

    /** The action link parameter name: &nbsp; <tt>actionLink</tt> */
    public static final String ACTION_LINK = "actionLink";

    /** The value parameter name: &nbsp; <tt>value</tt> */
    public static final String VALUE = "value";

    // ----------------------------------------------------- Instance Variables

    /** The link attributes map. */
    protected Map attributes;

    /** The button is clicked. */
    protected boolean clicked;

    /** The context. */
    protected Context context;

    /** The Field disabled value. */
    protected boolean disabled;

    /** The link name. */
    protected String name;

    /** The link label. */
    protected String label;

    /** The listener target object. */
    protected Object listener;

    /** The listener method name. */
    protected String listenerMethod;

    /** The processed link value. */
    protected String value;

    // ----------------------------------------------------------- Constructors

    /**
     * Create an ActionLink for the given name.
     *
     * @param name the action link name
     * @throws IllegalArgumentException if the name is null
     */
    public ActionLink(String name) {
        setName(name);
    }

    /**
     * Create an ActionLink for the given name, listener object and listener
     * method.
     *
     * @param name the action link name
     * @param listener the listener target object
     * @param method the listener method to call
     * @throws IllegalArgumentException if the name, listener or method is null
     * or if the method is blank
     */
    public ActionLink(String name, Object listener, String method) {
        setName(name);
        if (listener == null) {
            throw new IllegalArgumentException("Null listener parameter");
        }
        if (StringUtils.isBlank(method)) {
            throw new IllegalArgumentException("Blank listener method");
        }
        setListener(listener, method);
    }

    /**
     * Create an ActionLink with no name defined, <b>please note</b> the
     * control's name must be defined before it is valid.
     * <p/>
     * <div style="border: 1px solid red;padding:0.5em;">
     * No-args constructors are provided for Java Bean tools support and are not
     * intended for general use. If you create a control instance using a
     * no-args constructor you must define its name before adding it to its
     * parent. </div>
     */
    public ActionLink() {
    }

    // ------------------------------------------------------ Public Attributes

    /**
     * Return the link HTML attribute with the given name, or null if the
     * attribute does not exist.
     *
     * @param name the name of link HTML attribute
     * @return the link HTML attribute
     */
    public String getAttribute(String name) {
        if (attributes != null) {
            return (String) attributes.get(name);
        } else {
            return null;
        }
    }

    /**
     * Set the link attribute with the given attribute name and value. You would
     * generally use attributes if you were creating the entire ActionLink
     * programatically and rendering it with the {@link #toString()} method.
     * <p/>
     * For example:
     *
     * <div class="codeJava"> <span class="cm">// Java code</span>
     * ActionLink addLink = <span class="kw">new</span> ActionLink("<span class="blue">addLink</span>");
     * addLink.setLabel("Add");
     * addLink.setAttribute("title", "Add Product to Cart");
     * addLink.setAttribute("class", "table");
     *
     * &lt;-- Page template --&gt;
     * $<span class="blue">addLink</span>
     *
     * &lt;-- HTML output --&gt;
     * &lt;a href='actionLink=addLink' class='table' title='Add Product to Cart'&gt;Add&lt;/a&gt;
     * </div>
     *
     * @param name the attribute name
     * @param value the attribute value
     * @throws IllegalArgumentException if name parameter is null
     */
    public void setAttribute(String name, String value) {
        if (name == null) {
            throw new IllegalArgumentException("Null name parameter");
        }

        if (attributes == null) {
            attributes = new HashMap(5);
        }

        if (value != null) {
            attributes.put(name, value);
        } else {
            attributes.remove(name);
        }
    }

    /**
     * Return the ActionLink attributes Map.
     *
     * @return the ActionLink attributes Map
     */
    public Map getAttributes() {
        if (attributes == null) {
            attributes = new HashMap(5);
        }
        return attributes;
    }

    /**
     * Return true if the ActionLink has attributes or false otherwise.
     *
     * @return true if the ActionLink has attributes on false otherwise
     */
    public boolean hasAttributes() {
        if (attributes != null && !attributes.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns true if the ActionLink was clicked, otherwise returns false.
     *
     * @return true if the ActionLink was clicked, otherwise returns false.
     */
    public boolean isClicked() {
        return clicked;
    }

    /**
     * @see Control#getContext()
     */
    public Context getContext() {
        return context;
    }

    /**
     * @see Control#setContext(Context)
     */
    public void setContext(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Null context parameter");
        }
        this.context = context;
    }

    /**
     * Return true if the ActionLink is a disabled.
     *
     * @return true if the ActionLink is a disabled
     */
    public boolean isDisabled() {
        return disabled;
    }

    /**
     * Set the disabled flag.
     *
     * @param disabled the disabled flag
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * Return the ActionLink anchor &lt;a&gt; tag href attribute for the
     * given value. This method will encode the URL with the session ID
     * if required using <tt>HttpServletResponse.encodeURL()</tt>.
     *
     * @param value the ActionLink value parameter
     * @return the ActionLink HTML href attribute
     */
    public String getHref(Object value) {
        String uri = getContext().getRequest().getRequestURI();

        StringBuffer buffer =
            new StringBuffer(uri.length() + getName().length() + 40);

        buffer.append(uri);
        buffer.append("?");
        buffer.append(ACTION_LINK);
        buffer.append("=");
        buffer.append(getName());
        if (value != null) {
            buffer.append("&amp;");
            buffer.append(VALUE);
            buffer.append("=");
            buffer.append(value);
        }

        return getContext().getResponse().encodeURL(buffer.toString());
    }

    /**
     * Return the ActionLink anchor &lt;a&gt; tag href attribute value.
     *
     * @return the ActionLink anchor &lt;a&gt; tag HTML href attribute value
     */
    public String getHref() {
        return getHref(getValue());
    }

    /**
     * Return the "id" attribute value if defined, or the ActionLink name
     * otherwise.
     *
     * @see net.sf.click.Control#getId()
     */
    public String getId() {
        if (hasAttributes() && getAttributes().containsKey("id")) {
            return getAttribute("id");
        } else {
            return getName();
        }
    }

    /**
     * Return the label for the ActionLink.
     *
     * @return the label for the ActionLink
     */
    public String getLabel() {
        return label;
    }

    /**
     * Set the label for the ActionLink.
     *
     * @param label the label for the ActionLink
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @see net.sf.click.Control#getName()
     */
    public String getName() {
        return name;
    }

    /**
     * @see net.sf.click.Control#setName(String)
     */
    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Null name parameter");
        }
        this.name = name;
    }

    /**
     * Returns the ActionLink value if the action link was processed and has
     * a value, or null otherwise.
     *
     * @return the ActionLink value if the ActionLink was processed
     */
    public String getValue() {
        return value;
    }

    /**
     * Returns the action link <tt>Double</tt> value if the action link was
     * processed and has a value, or null otherwise.
     *
     * @return the action link <tt>Double</tt> value if the action link was processed
     */
    public Double getValueDouble() {
        if (value != null) {
            return Double.valueOf(value);
        } else {
            return null;
        }
    }

    /**
     * Returns the ActionLink <tt>Integer</tt> value if the ActionLink was
     * processed and has a value, or null otherwise.
     *
     * @return the ActionLink <tt>Integer</tt> value if the action link was processed
     */
    public Integer getValueInteger() {
        if (value != null) {
            return Integer.valueOf(value);
        } else {
            return null;
        }
    }

    /**
     * Returns the ActionLink <tt>Long</tt> value if the ActionLink was
     * processed and has a value, or null otherwise.
     *
     * @return the ActionLink <tt>Long</tt> value if the action link was processed
     */
    public Long getValueLong() {
        if (value != null) {
            return Long.valueOf(value);
        } else {
            return null;
        }
    }

    /**
     * Set the ActionLink value.
     *
     * @param value the ActionLink value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @see net.sf.click.Control#setListener(Object, String)
     */
    public void setListener(Object target, String methodName) {
        listener = target;
        listenerMethod = methodName;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * This method will set the {@link #isClicked()} property to true if the
     * ActionLink was clicked, and if an action callback listener was set
     * this will be invoked.
     *
     * @see net.sf.click.Control#onProcess()
     */
    public boolean onProcess() {
        clicked = getName().equals(getContext().getRequestParameter(ACTION_LINK));

        if (clicked) {
            value = getContext().getRequestParameter(VALUE);

            if (listener != null && listenerMethod != null) {
                return ClickUtils.invokeListener(listener, listenerMethod);

            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    /**
     * Return the HTML rendered anchor link string. This method
     * will render the entire anchor link including the tags, the label and
     * any attributes, see {@link #setAttribute(String, String)} for an
     * example.
     *
     * @see Object#toString()
     */
    public String toString() {
        HtmlStringBuffer buffer = new HtmlStringBuffer();

        buffer.elementStart("a");

        buffer.appendAttribute("href", getHref());
        buffer.appendAttribute("id", getId());
        if (hasAttributes()) {
            buffer.appendAttributes(getAttributes());
        }
        if (isDisabled()) {
            buffer.appendAttributeDisabled();
        }
        buffer.closeTag();

        buffer.append(getLabel());

        buffer.elementEnd("a");

        return buffer.toString();
    }
}
