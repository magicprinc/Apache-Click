/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.click.element;

import org.apache.click.util.HtmlStringBuffer;
import org.apache.commons.lang.StringUtils;

/**
 * Provides a base class for rendering HEAD resources of an HTML page, for
 * example JavaScript (&lt;script&gt;) and Cascading Stylesheets
 * (&lt;link&gt;/&lt;style&gt;).
 * <p/>
 * Subclasses should override {@link #getTag()} to return a specific HTML tag.
 * <p/>
 * Below are some example Resource elements:
 * <ul>
 * <li>{@link JsImport}, for importing <tt>external</tt> JavaScript using the
 * &lt;script&gt; element.</li>
 * <li>{@link JsScript}, for including <tt>inline</tt> JavaScript using the
 * &lt;script&gt; element.</li>
 * <li>{@link CssImport}, for importing <tt>external</tt> Cascading Stylesheets
 * using the &lt;link&gt; element.</li>
 * <li>{@link CssStyle}, for including <tt>inline</tt> Cascading Stylesheets
 * using the &lt;style&gt; element.</li>
 * </ul>
 *
 * <h3>Remove duplicates</h3>
 * Click will ensure that duplicate Resource elements are removed by checking
 * the {@link #isUnique()} property. No matter how many Controls or Pages
 * import the same Resource, only one will be rendered if {@link #isUnique()}
 * returns <tt>true</tt>.
 * <p/>
 * The rules for defining a unique Resource is as follows:
 * <ul>
 * <li>{@link JsImport} and {@link CssImport} is unique based on the
 * attributes {@link JsImport#getSrc()} and {@link CssImport#getHref()}
 * respectively.</li>
 * <li>{@link JsScript} and {@link CssStyle} is unique if their HTML
 * {@link #setId(java.lang.String) ID} attribute is set. The HTML
 * spec defines that an element's HTML ID must be unique per page.</li>
 * </ul>
 * For example:
 * <pre class="prettyprint">
 * public class MyPage extends Page {
 *
 *     public List getHeadElements() {
 *         // We use lazy loading to ensure the JavaScript and Css is only added
 *         // the first time this method is called.
 *         if (headElements == null) {
 *             // Get the head elements from the super implementation
 *             headElements = super.getHeadElements();
 *
 *             JsImport jsImport = new JsImport("/js/mylib.js");
 *             // Click will ensure the library "/js/mylib.js" is only included
 *             // once in the Page
 *             headElements.add(jsImport);
 *
 *             JsScript jsScript = new JsScript("alert('Hello!');");
 *             // Click won't ensure the script is unique because its ID
 *             // attribute is not defined
 *             headElements.add(jsScript);
 *
 *             jsScript = new JsScript("alert('Hello!');");
 *             jsScript.setId("my-unique-script-id");
 *             // Click will ensure the script is unique because its ID attribute
 *             // is defined. Click will remove other scripts with the same ID
 *             headElements.add(jsScript);
 *
 *             CssImport cssImport = new CssImport("/css/style.css");
 *             // Click will ensure the library "/css/style.css" is only
 *             // included once in the Page
 *             headElements.add(cssImport);
 *
 *             CssScript cssScript = new CssScript("body { font-weight: bold; }");
 *             cssScript.setId("my-unique-style-id");
 *             // Click will ensure the css is unique because its ID attribute
 *             // is defined. Click will remove other css styles with the same ID
 *             headElements.add(cssScript);
 *         }
 *         return headElements;
 *     }
 * } </pre>
 *
 * <h3>Conditional comment support for Internet Explorer</h3>
 *
 * Sometimes it is necessary to provide additional JavaScript and Css for
 * Internet Explorer because it deviates quite often from the standards.
 * <p/>
 * Conditional comments allows you to wrap the resource in a special comment
 * which only IE understands, meaning other browsers won't process the resource.
 * <p/>
 * You can read more about conditional comments
 * <a target="_blank" href="http://msdn.microsoft.com/en-us/library/ms537512(VS.85).aspx#syntax">here</a>
 * and <a target="_blank" href="http://www.quirksmode.org/css/condcom.html">here</a>
 * <p/>
 * It has to be said that IE7 and up has much better support for Css, thus
 * conditional comments are mostly used for IE6 and below.
 * <pre class="prettyprint">
 * public class MyPage extends Page {
 *
 *     public List getHeadElements() {
 *         // We use lazy loading to ensure the JavaScript and Css is only added
 *         // the first time this method is called.
 *         if (headElements == null) {
 *             // Get the head elements from the super implementation
 *             headElements = super.getHeadElements();
 *
 *             CssImport cssImport = new CssImport("/css/ie-style.css");
 *             // Use one of the predefined conditional comments to target IE6
 *             // and below
 *             cssImport.setConditionalComment(IE_LESS_THAN_IE7);
 *             headElements.add(cssImport);
 *
 *             cssImport = new CssImport("/css/ie-style2.css");
 *             // Use a custom predefined conditional comments to target only IE6
 *             cssImport.setConditionalComment("[if IE 6]");
 *             headElements.add(cssImport);
 *         }
 *         return headElements;
 *     }
 * } </pre>
 *
 * ResourceElement contains some predefined Conditional Comments namely
 * {@link #IF_IE}, {@link #IF_LESS_THAN_IE7} and {@link #IF_IE7}.
 *
 * @author Bob Schellink
 */
public class ResourceElement extends Element {

    // -------------------------------------------------------------- Constants

    /**
     * A predefined conditional comment to test if browser is IE. Value:
     * <tt>[if IE]</tt>.
     */
    public static final String IF_IE = "[if IE]";

    /**
     * A predefined conditional comment to test if browser is less than IE7.
     * Value: <tt>[if lt IE 7]</tt>.
     */
    public static final String IF_LESS_THAN_IE7 = "[if lt IE 7]";

    /**
     * A predefined conditional comment to test if browser is IE7. Value:
     * <tt>[if IE 7]</tt>.
     */
    public static final String IF_IE7 = "[if IE 7]";

    // -------------------------------------------------------------- Variables

    /**
     * The Internet Explorer conditional comment to wrap the Resource with.
     */
    private String conditionalComment;

    /**
     * Indicates if Click should ensure the import is unique, default value is
     * <tt>false</tt>.
     */
    private boolean unique = false;

    // ------------------------------------------------------ Public properties

    /**
     * Return true if the Resource should be unique, false otherwise. The default
     * value is <tt>true</tt> if the {@link #getId() ID} attribute is defined,
     * false otherwise.
     *
     * @return true if the Resource should be unique, false otherwise.
     */
    public boolean isUnique() {
        String id = getId();

        // If id is defined, import will be any duplicate import found will be
        // filtered out
        if (StringUtils.isNotBlank(id)) {
            return true;
        }
        return unique;
    }

    /**
     * Return Internal Explorer's <tt>conditional comment</tt> to wrap the
     * Resource with.
     *
     * @return Internal Explorer's conditional comment to wrap the Resource with.
     */
    public String getConditionalComment() {
        return conditionalComment;
    }

    /**
     * Set Internet Explorer's conditional comment to wrap the Resource with.
     *
     * @param conditionalComment Internet Explorer's conditional comment to wrap
     * the Resource with
     */
    public void setConditionalComment(String conditionalComment) {
        this.conditionalComment = conditionalComment;
    }

    // --------------------------------------------------------- Public methods

    /**
     * Render the HTML representation of the Resource element to the specified
     * buffer.
     * <p/>
     * If {@link #getTag()} returns null, this method will return an empty
     * string.
     *
     * @param buffer the specified buffer to render the Resource element output
     * to
     */
    public void render(HtmlStringBuffer buffer) {
        renderConditionalCommentPrefix(buffer);

        if (getTag() == null) {
            return;
        }
        renderTagBegin(getTag(), buffer);
        renderTagEnd(getTag(), buffer);

        renderConditionalCommentSuffix(buffer);
    }

    // ------------------------------------------------ Package Private Methods

    /**
     * Render the {@link #getConditionalComment() conditional comment} prefix
     * to the specified buffer. If the conditional comment is not defined this
     * method won't append to the buffer.
     *
     * @param buffer buffer to append the conditional comment prefix
     */
    void renderConditionalCommentPrefix(HtmlStringBuffer buffer) {
        String conditional = getConditionalComment();

        // Render IE conditional comment
        if (StringUtils.isNotBlank(conditional)) {
            buffer.append("<!--").append(conditional).append(">\n");
        }
    }

    /**
     * Render the {@link #getConditionalComment() conditional comment} suffix
     * to the specified buffer. If the conditional comment is not defined this
     * method won't append to the buffer.
     *
     * @param buffer buffer to append the conditional comment suffix
     */
    void renderConditionalCommentSuffix(HtmlStringBuffer buffer) {
        String conditional = getConditionalComment();

        // Close IE conditional comment
        if (StringUtils.isNotBlank(conditional)) {
            buffer.append("\n<![endif]-->");
        }
    }
}