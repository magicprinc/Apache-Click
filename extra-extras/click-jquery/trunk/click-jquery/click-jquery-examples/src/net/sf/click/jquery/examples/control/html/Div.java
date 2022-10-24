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
package net.sf.click.jquery.examples.control.html;

import org.apache.click.control.AbstractContainer;

/**
 * Provides an HTML div element: &lt;div&gt;.
 * <p/>
 * The following example shows how to use a Div control to render red square.
 * <p/>
 * Given the Page <tt>MyPage.java</tt>:
 *
 * <pre class="prettyprint">
 * public MyPage extends Page {
 *
 *     public onInit() {
 *         // Create a div and style it with a red background
 *         Div div = new Div("div");
 *         div.setStyle("background", "red");
 *
 *         // Set the div size to 50 pixels
 *         div.setSize("50px", "50px");
 *
 *         // Add the div to the Page control list
 *         addControl(div);
 *     }
 * } </pre>
 *
 * and the template <tt>my-page.htm</tt>:
 *
 * <pre class="prettyprint">
 * $div </pre>
 *
 * will render as:
 *
 * <div class="border">
 * <div name="div" id="div" style="background:red;width:50px;height:50px;"></div>
 * </div>
 */
public class Div extends AbstractContainer {

    // ----------------------------------------------------------- Constructors

    /**
     * Create a default div.
     */
    public Div() {
    }

    /**
     * Create a div element with the given name.
     *
     * @param name the div name
     */
    public Div(String name) {
        if (name != null) {
            setName(name);
        }
    }

    // ------------------------------------------------------ Public Properties

    /**
     * Return the div's html tag: <tt>div</tt>.
     *
     * @see org.apache.click.control.AbstractControl#getTag()
     *
     * @return this controls html tag
     */
    @Override
    public final String getTag() {
        return "div";
    }

    /**
     * Set the HTML id attribute for the control with the given value.
     *
     * @param id the element HTML id attribute value to set
     * @return this control instance
     */
    public Div id(String id) {
        setId(id);
        return this;
    }

    /**
     * Set the div HTML <tt>height</tt> attribute value.
     *
     * @param height the div HTML height attribute value
     */
    public void setHeight(String height) {
        setStyle("height", height);
    }

    /**
     * Return the div HTML <tt>height</tt> attribute value.
     *
     * @return the div HTML height attribute value
     */
    public String getHeight() {
        return getStyle("height");
    }

    /**
     * Set the div HTML <tt>width</tt> attribute value
     *
     * @param width the div HTML width attribute value
     */
    public void setWidth(String width) {
        setStyle("width", width);
    }

    /**
     * Return the div HTML <tt>width</tt> attribute value.
     *
     * @return the div HTML width attribute value
     */
    public String getWidth() {
        return getStyle("width");
    }

    /**
     * Set the div HTML <tt>width</tt> and <tt>height</tt> attributes.
     *
     * @param width the div HTML width attribute
     * @param height the div HTML height attribute
     */
    public void setSize(String width, String height) {
        setWidth(width);
        setHeight(height);
    }
}
