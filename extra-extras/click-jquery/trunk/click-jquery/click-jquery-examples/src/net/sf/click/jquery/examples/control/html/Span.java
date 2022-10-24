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
import org.apache.click.control.AbstractControl;

/**
 * Provides an HTML span element: &lt;span&gt;.
 *
 * The following example shows how to use a Span control to style some text.
 * <p/>
 * Given the Page <tt>MyPage.java</tt>:
 *
 * <pre class="prettyprint">
 * public MyPage extends Page {
 *
 *     public onInit() {
 *         // Create a span and set the text "Hello World" as its content
 *         Span span = new Span("span");
 *         span.add(new Text("Hello World"));
 *
 *         // Style the span with a red background and large font size
 *         span.setStyle("color", "red");
 *         span.setStyle("font-size", "25px");
 *
 *         // Add the span to the Page control list
 *         addControl(span);
 *     }
 * } </pre>
 *
 * and the template <tt>my-page.htm</tt>:
 *
 * <pre class="prettyprint">
 * $span </pre>
 *
 * will render as:
 *
 * <div class="border">
 * <span name="span" id="span" style="color:red;font-size:25px;">
 * Hello World
 * </span>
 * </div>
 */
public class Span extends AbstractContainer {

    // -------------------------------------------------------------- Variables

    /**
     * The span text control. Please note that a span can contain any number
     * of other child controls as well.
     */
    private Text text;

    // ----------------------------------------------------------- Constructors

    /**
     * Create a default span.
     */
    public Span() {
    }

    /**
     * Create a span element with the given name.
     *
     * @param name the span name
     */
    public Span(String name) {
         if(name != null) {
            setName(name);
        }
    }

    /**
     * Create a span element with the given name and id.
     *
     * @param name the span name
     * @param text the span text
     */
    public Span(String name, String text) {
        this(name);
        setText(text);
    }

    // ------------------------------------------------------ Public Properties

    /**
     * Return the span's html tag: <tt>span</tt>.
     *
     * @see AbstractControl#getTag()
     *
     * @return this controls html tag
     */
    @Override
    public final String getTag() {
        return "span";
    }

    /**
     * Set the HTML id attribute for the control with the given value.
     *
     * @param id the element HTML id attribute value to set
     * @return this control instance
     */
    public Span id(String id) {
        setId(id);
        return this;
    }

    /**
     * Convenience method to set the given text as the span content. If the
     * given text is null, the span content will be cleared.
     *
     * @param text the text to set as the span content
     */
    public void setText(String value) {
        if (value == null) {
            if (text == null) {
                return;
            } else {
                remove(text);
            }

        } else {
            if (text == null) {
                text = new Text();
                add(text);
            }
            text.setText(value);
        }
    }

    /**
     * Return the span text content or null if no text was set.
     *
     * @return the span text content or null if no text was set
     */
    public String getText() {
        if (text == null) {
            text = new Text();
        }

        Object value = text.getText();
        String result = value == null ? null : value.toString();
        return result;
    }
}
