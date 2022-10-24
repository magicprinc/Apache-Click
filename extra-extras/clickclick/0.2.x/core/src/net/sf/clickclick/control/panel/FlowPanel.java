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
package net.sf.clickclick.control.panel;

import org.apache.click.Control;
import org.apache.click.control.AbstractContainer;
import net.sf.clickclick.control.html.Div;
import org.apache.click.util.HtmlStringBuffer;

/**
 * Provides a panel where controls flow from left to right. Controls will
 * flow to the next line if they cannot fit next to the previous control.
 */
public class FlowPanel extends SimplePanel {

    // -------------------------------------------------------------- Variables

    /** Internal container used for rendering the layout. */
    protected Div container = new Div();

    // ----------------------------------------------------------- Constructors

    /**
     * Create a default FlowPanel.
     * <p/>
     * The panel can be styled through the style class <tt>"c-flowpanel"</tt>.
     */
    public FlowPanel() {
        this(null);
    }

    /**
     * Create a FlowPanel with the given name.
     * <p/>
     * The panel can be styled through the style class <tt>"c-flowpanel"</tt>.
     *
     * @param name the name of the panel
     */
    public FlowPanel(String name) {
        super(name);
        super.insert(container, 0);
        container.setAttribute("class", "c-flowpanel");
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Adds the control to the panel.
     * <p/>
     * Controls added to the panel are wrapped in a parent container that can
     * be styled through the style class <tt>c-flowpanel-section</tt>.
     *
     * @param control the control to add
     * @return the added control
     */
    public Control add(Control control) {
        // Wrap the control in a container element
        Div div = new Div();
        div.setAttribute("class", "c-flowpanel-section");
        div.setStyle("display", "inline");
        div.add(control);
        container.add(div);
        return control;
    }

    /**
     * @throws UnsupportedOperationException as insert is not supported
     */
    public Control insert(Control control, int index) {
        throw new UnsupportedOperationException("insert is not supported by this Panel");
    }

    /**
     * Remove the control from the panel.
     *
     * @param control the control to remove
     * @return true if the control was removed, false otherwise
     */
    public boolean remove(Control control) {
        AbstractContainer container = getContainer(control);
        return this.container.remove(container);
    }

    /**
     * Return the control's parent container.
     *
     * @param control the control which parent container to return
     * @return the control's parent container
     */
    public AbstractContainer getContainer(Control control) {
        return (AbstractContainer) control.getParent();
    }

    /**
     * Set the size of the panel.
     *
     * @param width the width of the panel
     * @param height the height of the panel
     */
    public void setSize(String width, String height) {
        setWidth(width);
        setHeight(height);
    }

    /**
     * Set the height of the panel.
     *
     * @param height the height of the panel
     */
    public void setHeight(String height) {
        container.setHeight(height);
    }

    /**
     * Set the width of the panel.
     *
     * @param width the width of the panel
     */
    public void setWidth(String width) {
        container.setWidth(width);
    }

    /**
     * @see org.apache.click.control.Panel#render(org.apache.click.util.HtmlStringBuffer)
     *
     * @param buffer the specified buffer to render the Panel's output to
     */
    public void render(HtmlStringBuffer buffer) {
        container.getAttributes().putAll(getAttributes());
        container.setId(getId());
        super.render(buffer);
    }
}
