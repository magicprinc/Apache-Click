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

import org.apache.click.control.AbstractControl;
import org.apache.click.util.HtmlStringBuffer;

/**
 * This control renders a string of text. If no text is specified an empty
 * String is rendered.
 * <p/>
 * The following example shows how to use a Text control to render text inside
 * a {@link net.sf.click.jquery.examples.control.html.Div}.
 * <p/>
 * Given the Page <tt>MyPage.java</tt>:
 *
 * <pre class="prettyprint">
 * public MyPage extends Page {
 *
 *     public onInit() {
 *         Div div = new Div("div");
 *         Text text = new Text("Hello World");
 *
 *         // Add the Text as the Div content
 *         div.add(text);
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
 * <div name="div" id="div">
 * Hello World
 * </div>
 * </div>
 */
public class Text extends AbstractControl {

    /** The text object. */
    private Object text;

    /**
     * Create a default Text control.
     */
    public Text() {
    }

    /**
     * Create a Text control with the given text.
     *
     * @param text the text for this control
     */
    public Text(Object text) {
        this.text = text;
    }

    /**
     * Return the text of this control.
     *
     * @return the text of this control
     */
    public Object getText() {
        return text;
    }

    /**
     * Set the text of this control.
     *
     * @param text the text of this control
     */
    public void setText(Object text) {
        this.text = text;
    }

    /**
     * Render the {@link #getText() text}.
     *
     * @param buffer the specified buffer to render the control's output to
     */
    public void render(HtmlStringBuffer buffer) {
        if(getText() != null) {
            buffer.append(getText());
        } else {
            buffer.append("");
        }
    }

    /**
     * Returns the {@link #getText() text}.
     *
     * @see Object#toString()
     *
     * @return the control's text value
     */
    public String toString() {
        HtmlStringBuffer buffer = new HtmlStringBuffer(20);
        render(buffer);
        return buffer.toString();
    }
}
