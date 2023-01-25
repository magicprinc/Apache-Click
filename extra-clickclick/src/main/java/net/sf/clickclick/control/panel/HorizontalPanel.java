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
import net.sf.clickclick.control.html.table.Cell;
import net.sf.clickclick.control.html.table.Row;

/**
 * Provides a panel that lays out its controls in a single horizontal row.
 */
public class HorizontalPanel extends AbstractTablePanel {

    // ----------------------------------------------------------- Constructors

    /**
     * Create a default HorizontalPanel.
     */
    public HorizontalPanel() {
        this(null);
    }

    /**
     * Create a HorizontalPanel with the given name.
     *
     * @param name the name of the panel
     */
    public HorizontalPanel(String name) {
        super(name);
        setBorder(0);
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Adds the control to the panel.
     *
     * @param control the control to add
     * @return the control that was added
     */
    public Control add(Control control) {
        Row row = null;
        if (!table.hasControls()) {
            row = new Row();
            table.add(row);
        } else {
            row = (Row) table.getControls().get(0);
        }
        
        Cell cell = new Cell();
        cell.setStyle("text-align", getHorizontalAlignment());
        cell.setStyle("vertical-align", getVerticalAlignment());
        row.add(cell);
        cell.add(control);
        return control;
    }

    /**
     * Remove the control from the panel.
     *
     * @param control the control that was removed
     * @return true if the control was removed, false otherwise
     */
    public boolean remove(Control control) {
        Cell cell = getCell(control);
        return table.remove(cell);
    }
}
