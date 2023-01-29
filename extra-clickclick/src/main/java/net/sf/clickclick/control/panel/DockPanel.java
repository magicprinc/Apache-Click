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

import net.sf.clickclick.control.html.table.Cell;
import net.sf.clickclick.control.html.table.Row;
import org.apache.click.Control;
import org.apache.click.util.HtmlStringBuffer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Provides a dock panel where controls can be docked to either the
 * {@link #NORTH}, {@link #SOUTH}, {@link #EAST}, {@link #WEST} or
 * {@link #CENTER}.
 */
public class DockPanel extends AbstractTablePanel {

    // -------------------------------------------------------------- Constants

    /** The center layout position - middle of the panel. */
    public static final int CENTER = 0;

    /** The north layout position - top of the panel. */
    public static final int NORTH = 1;

    /** The east layout position - right side of the panel. */
    public static final int EAST = 2;

    /** The south layout position - bottom of the panel. */
    public static final int SOUTH = 3;

    /** The west layout position - left side of the panel. */
    public static final int WEST = 4;

    // -------------------------------------------------------------- Variables

    /** The list of controls docked on this panel. */
    protected List dockedControls = new ArrayList();

    /** Default colspan value is 1 which caters for center control. */
    protected int dockColspan = 1;

    // ----------------------------------------------------------- Constructors

    /**
     * Create a default DockPanel.
     */
    public DockPanel() {
        this(null);
    }

    /**
     * Create a DockPanel for the given name.
     *
     * @param name the name of the panel
     */
    public DockPanel(String name) {
        super(name);
        setHorizontalAlignment(ALIGN_CENTER);
        setBorder(0);
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Adds the control at the specified dock position.
     *
     * @param control the control to add
     * @param position the position to dock the control at
     * @return the added control
     */
    public Control add(Control control, int position) {
        PositionHolder holder = new PositionHolder();
        holder.control = control;
        holder.position = position;
        // Need to increase the colspan if controls are docked to west or east
        if (position == WEST || position == EAST) {
            dockColspan++;
        }
        dockedControls.add(holder);
        return control;
    }

    /**
     * Adds the control at the {@link #CENTER} dock position.
     *
     * @param control the control to ad
     * @return the added control
     */
    public Control add(Control control) {
        return add(control, CENTER);
    }

    /**
     * Remove the control from the panel.
     *
     * @param control the control to remove
     * @return true if the control was removed, false otherwise
     */
    public boolean remove(Control control) {
        Cell cell = getCell(control);
        return table.remove(cell);
    }

    /**
     * Render the HTML representation of the DockPanel.
     *
     * @param buffer the specified buffer to render the Panel's output to
     */
    @Override public void render(HtmlStringBuffer buffer) {
        assemblePanel();
        super.render(buffer);
    }

    /**
     * Assemble the layout of the DockPanel.
     */
    protected void assemblePanel() {
        Row middleRow = new Row();
        Cell centerCell = createCell();
        table.add(middleRow);
        middleRow.add(centerCell);

        int northCount = 0;
        int westCount = 0;
        for (Iterator it = dockedControls.iterator(); it.hasNext();) {
            PositionHolder holder = (PositionHolder) it.next();
            switch (holder.position) {
                case NORTH: {
                    Row row = new Row();
                    table.insert(row, northCount);
                    Cell cell = createCell();
                    cell.setAttribute("colspan", Integer.toString(dockColspan));
                    cell.add(holder.control);
                    row.add(cell);
                    northCount++;
                    break;
                }
                case SOUTH: {
                    Row row = new Row();
                    table.insert(row, northCount + 1);
                    Cell cell = createCell();
                    cell.setAttribute("colspan", Integer.toString(dockColspan));
                    cell.add(holder.control);
                    row.add(cell);
                    break;
                }
                case EAST: {
                    Cell cell = createCell();
                    cell.add(holder.control);
                    middleRow.insert(cell, westCount + 1);
                    break;
                }
                case WEST: {
                    Cell cell = createCell();
                    cell.add(holder.control);
                    middleRow.insert(cell, westCount);
                    westCount++;
                    break;
                }
                default: {
                    centerCell.add(holder.control);
                    break;
                }
            }
        }
    }

    /**
     * Create a new Cell for the DockPanel.
     *
     * @return a newly created Cell instance
     */
    protected Cell createCell() {
        Cell cell = new Cell();
        cell.setStyle("text-align", getHorizontalAlignment());
        cell.setStyle("vertical-align", getVerticalAlignment());
        return cell;
    }

    // ---------------------------------------------------------- Inner classes

    /**
     * Provide a class that tracks the position of a given control.
     */
    class PositionHolder {

        /** The control which position to track. */
        public Control control;

        /** The position of a control. */
        public int position;
    }
}