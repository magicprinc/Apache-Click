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
import org.apache.click.control.AbstractControl;
import net.sf.clickclick.control.html.Div;

/**
 * Provides a panel for absolute positioning of controls.
 * <p/>
 * When adding controls the left and top positions can be specified where the
 * control should be rendered.
 */
public class AbsolutePanel extends SimplePanel {

    // -------------------------------------------------------------- Variables

    /** Internal div used for rendering the layout. */
    protected Div div;

    // ------------------------------------------------------------ Constructor

    /**
     * Create a default AbsolutePanel.
     */
    public AbsolutePanel() {
        this(null);
    }

    /**
     * Create a AbsolutePanel with the given name.
     *
     * @param name the name of the panel
     */
    public AbsolutePanel(String name) {
        super(name);
        div = new Div();
        super.insert(div, 0);
        div.setStyle("position", "relative");
        div.setStyle("overflow", "hidden");
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Adds the control to the panel at positions left=0, top=0.
     *
     * @param control the control to add
     * @return the added control
     */
    public Control add(Control control) {
        return add(control, 0, 0);
    }

    /**
     * Adds the control to the panel at the given left and top positions.
     *
     * @param control the control to add
     * @param left the left position
     * @param top the top position
     * @return the added control
     */
    public Control add(Control control, int left, int top) {
        
        AbstractControl abstractControl = null;

        if (control instanceof AbstractControl) {
            // If control is AbstractControl, modify its attributes directly
            abstractControl = (AbstractControl) control;
        } else {
            // If the control is not an AbstractControl, wrap it in an inlined Div
            Div div = new Div();
            div.setStyle("display", "inline");
            abstractControl = div;
        }
        abstractControl.setStyle("position", "absolute");
        abstractControl.setStyle("left", left + "px");
        abstractControl.setStyle("top", top + "px");
        div.add(abstractControl);
        return control;
    }

    /**
     * @throws UnsupportedOperationException as insert is not supported
     */
    public Control insert(Control control, int index) {
        throw new UnsupportedOperationException("insert is not supported by this Panel");
    }
}
