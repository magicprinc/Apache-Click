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
package net.sf.clickclick.control.html.table;

import org.apache.click.Control;
import org.apache.click.control.AbstractContainer;
import org.apache.click.util.HtmlStringBuffer;
import net.sf.clickclick.control.Text;
import org.apache.click.control.AbstractControl;

/**
 * Provide a table Cell control: &lt;td&gt;.
 */
public class Cell extends AbstractContainer {

    // ----------------------------------------------------------- Constructors

    /**
     * Create a default table Cell.
     */
    public Cell() {
    }

    /**
     * Create a table Cell with the given name.
     */
    public Cell(String name) {
        super(name);
    }

    /**
     * Create a table Cell with the given control.
     */
    public Cell(Control control) {
        add(control);
    }

    // ------------------------------------------------------ Public Properties

    /**
     * Return the div's html tag: <tt>div</tt>.
     *
     * @see AbstractControl#getTag()
     *
     * @return this controls html tag
     */
    public String getTag() {
        return "td";
    }

    /**
     * Set the content of this cell to the given value.
     *
     * @param value the text value
     */
    public void setText(String value) {
        Text text = new Text(value);
        add(text);
    }

    /**
     * Set the content of this cell to the given
     * {@link net.sf.clickclick.control.Text} object.
     *
     * @param text the Text object
     */
    public void setText(Text text) {
        add(text);
    }

    /**
     * Return the cell parent {@link Row} object.
     *
     * @return the cell parent Row object
     */
    public Row getRow() {
        return (Row) getParent();
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Render the HTML representation of the table cell.
     *
     * @param buffer the buffer to render to
     */
    public void render(HtmlStringBuffer buffer) {
        if (getTag() != null) {
            renderTagBegin(getTag(), buffer);
            buffer.closeTag();
            renderContent(buffer);
            renderTagEnd(getTag(), buffer);
        } else {
            if(hasControls()) {
                renderContent(buffer);
            }
        }
    }

    /**
     * Render the table cell child controls.
     *
     * @param buffer the buffer to render to
     */
    protected void renderChildren(HtmlStringBuffer buffer) {
        if(hasControls()) {
            for(int i = 0; i < getControls().size(); i++) {
                Control control = (Control) getControls().get(i);
                control.render(buffer);
            }
        }
    }
}
