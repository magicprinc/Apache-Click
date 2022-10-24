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
import net.sf.clickclick.control.html.table.HtmlTable;
import net.sf.clickclick.control.html.table.Row;

/**
 * Provides an abstract Panel that layout its controls inside an HTML table.
 */
public abstract class AbstractTablePanel extends SimplePanel {

    // -------------------------------------------------------------- Constants

    /** The align left, panel layout constant: &nbsp; <tt>"left"</tt>. */
    public static final String ALIGN_LEFT = "left";

    /** The align center, panel layout constant: &nbsp; <tt>"center"</tt>. */
    public static final String ALIGN_CENTER = "center";

    /** The align right, panel layout constant: &nbsp; <tt>"right"</tt>. */
    public static final String ALIGN_RIGHT = "right";

    /** The align top, panel layout constant: &nbsp; <tt>"top"</tt>. */
    public static final String ALIGN_TOP = "top";

    /** The align middle, panel layout constant: &nbsp; <tt>"middle"</tt>. */
    public static final String ALIGN_MIDDLE = "middle";

    /** The align bottom, panel layout constant: &nbsp; <tt>"bottom"</tt>. */
    public static final String ALIGN_BOTTOM = "bottom";

    // -------------------------------------------------------------- Variables

    /** The vertical alignment, default value is "<tt>top</tt>". */
    protected String verticalAlignment = ALIGN_TOP;

    /** The horizontal alignment, default value is "<tt>left</tt>". */
    protected String horizontalAlignment = ALIGN_LEFT;

    /** Internal table used for rendering the layout. */
    protected HtmlTable table = new HtmlTable();

    // ----------------------------------------------------------- Constructors

    /**
     * Create a default AbstractTablePanel.
     */
    public AbstractTablePanel() {
        this(null);
    }

    /**
     * Create a AbstractTablePanel with the given name.
     *
     * @param name the name of the panel
     */
    public AbstractTablePanel(String name) {
        super(name);
        super.insert(table, 0);
        table.setAttribute("cellpadding", "0");
    }

    // ------------------------------------------------------- Abstract Methods

    /**
     * Subclasses must provide implementations for how to add the given control.
     *
     * @param control the control to add
     * @return the control that was added
     */
    public abstract Control add(Control control);

    /**
     * Subclasses must provide implementations for how to remove the given
     * control.
     *
     * @param control the control to remove
     * @return true if the control was removed, false otherwise
     */
    public abstract boolean remove(Control control);

    // --------------------------------------------------------- Public Methods

    /**
     * Returns the panel html tag as a &lt;div&gt;.
     *
     * @return the panel html tag as a &lt;div&gt;.
     */
    public String getTag() {
        return "div";
    }

    /**
     * @throws UnsupportedOperationException as insert is not supported
     */
    public Control insert(Control control, int index) {
        throw new UnsupportedOperationException("insert is not supported by this Panel");
    }

    /**
     * Return the internal table used for laying out controls.
     *
     * @return the internal table used for laying out controls
     */
    public HtmlTable getTable() {
        return table;
    }

    /**
     * Set the internal table used for laying out controls.
     *
     * @param table the internal table used for laying out controls
     */
    public void setTable(HtmlTable table) {
        super.remove(this.table);
        this.table = table;
        super.insert(table, 0);
    }

    /**
     * Set the horizontal alignment for controls rendered by the panel.
     *
     * @param horizontalAlignment the horizontal alignment of controls
     */
    public void setHorizontalAlignment(String horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    /**
     * Return the horizontal alignment for controls rendered by the panel.
     *
     * @return the horizontal alignment of controls
     */
    public String getHorizontalAlignment() {
        return horizontalAlignment;
    }

    /**
     * Set the vertical alignment for controls rendered by the panel.
     *
     * @param verticalAlignment the vertical alignment of controls
     */
    public void setVerticalAlignment(String verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }

    /**
     * Return the vertical alignment for controls rendered by the panel.
     *
     * @return the vertical alignment of controls
     */
    public String getVerticalAlignment() {
        return verticalAlignment;
    }

    /**
     * Set the spacing between table cells rendered by the panel.
     *
     * @param spacing the spacing between table cells
     */
    public void setSpacing(int spacing) {
        table.setCellspacing(spacing);
    }

    /**
     * Return the spacing between table cells rendered by the panel.
     *
     * @return the spacing between table cells
     */
    public int getSpacing() {
        return table.getCellspacing();
    }

    /**
     * Set the table border width.
     *
     * @param border the table border width
     */
    public void setBorder(int border) {
        table.setBorder(border);
    }

    /**
     * Return the table border width.
     *
     * @return the table border width
     */
    public int getBorder() {
        return table.getBorder();
    }

    /**
     * Set the size (width + height) for the given control.
     *
     * @param control the control which size to set
     * @param width the width of the control
     * @param height the height of the control
     */
    public void setSize(Control control, String width, String height) {
        Cell cell = getCell(control);
        if (cell == null) {
            return;
        }
        cell.setAttribute("width", width);
        cell.setAttribute("height", height);
    }

    /**
     * Return the parent {@link net.sf.clickclick.control.html.table.Row} that
     * contains the given control.
     *
     * @param control the control which parent row to retrieve
     * @return the parent row foe the given control
     */
    public Row getRow(Control control) {
        Cell cell = getCell(control);
        if (cell == null) {
            return null;
        }
        return (Row) cell.getParent();
    }

    /**
     * Return the parent {@link net.sf.clickclick.control.html.table.Cell} that
     * contains the given control.
     *
     * @param control the control which parent cell to retrieve
     * @return the parent cell for the given control
     */
    public Cell getCell(Control control) {
        return (Cell) control.getParent();
    }
}
