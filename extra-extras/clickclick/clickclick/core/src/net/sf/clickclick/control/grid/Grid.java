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
package net.sf.clickclick.control.grid;

import java.util.List;
import net.sf.clickclick.control.Text;
import org.apache.click.Control;
import org.apache.click.control.Container;
import net.sf.clickclick.control.html.table.Cell;
import net.sf.clickclick.control.html.table.HtmlTable;
import net.sf.clickclick.control.html.table.Row;

/**
 * Provide a Grid control, specifying a number of rows and columns. The Grid
 * will dynamically add more rows and columns as needed.
 * <p/>
 * The following example shows how to render a 2 X 2 grid.
 * <p/>
 * Given the Page <tt>MyPage.java</tt>:
 *
 * <pre class="prettyprint">
 * public MyPage extends Page {
 *
 *     public onInit() {
 *
 *         // Create a 2 X 2 Grid
 *         Grid grid = new Grid("grid", 2, 2);
 *
 *         // Insert a Field and Label in row 1
 *         grid.insert(new Label("First Name:"), 1, 1);
 *         grid.insert(new TextField("firstname"), 1, 2);
 *
 *         // Insert another Field and Label in row 2
 *         grid.insert(new Label("Last Name:"), 2, 1);
 *         grid.insert(new TextField("lastname"), 2, 2);
 *
 *         // Add the grid to the Page control list
 *         addControl(grid);
 *     }
 * } </pre>
 *
 * and the template <tt>my-page.htm</tt>:
 *
 * <pre class="prettyprint">
 * $grid </pre>
 *
 * will render as:
 *
 * <div class="border">
 * <table name="mygrid" id="mygrid" border="1">
 *   <tr>
 *     <td>Firt Name:</td>
 *     <td><input type="text" name="firstname" id="firstname" value="" size="20"/></td>
 *   </tr>
 *   <tr>
 *     <td>Last Name:</td>
 *     <td><input type="text" name="lastname" id="lastname" value="" size="20"/></td>
 *   </tr>
 * </table>
 * </div>
 */
public class Grid extends HtmlTable {

    // -------------------------------------------------------------- Variables

    /** The number of rows in the grid. */
    private int numRows = 0;

    /** The number of columns in the grid. */
    private int numColumns = 0;

    // ----------------------------------------------------------- Constructors

    /**
     * Create a default Grid.
     */
    public Grid() {
    }

    /**
     * Create a Grid with the given name.
     *
     * @param name the name of the control
     */
    public Grid(String name) {
        super(name);
    }

    /**
     * Create a Grid with the given number of rows and columns.
     *
     * @param numRows the number of rows
     * @param numCols the number of columns
     */
    public Grid(int numRows, int numCols) {
        this(null, numRows, numCols);
    }

    /**
     * Create a Grid with the given name and number of rows and columns.
     *
     * @param name the name of the control
     * @param numRows the number of rows
     * @param numCols the number of columns
     */
    public Grid(String name, int numRows, int numCols) {
        super(name);
        ensureCapacity(numRows, numCols);
        this.numRows = numRows;
        this.numColumns = numCols;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * @throws UnsupportedOperationException use
     * {@link #insert(org.apache.click.Control, int, int)} instead
     */
    @Override
    public Control add(Control control) {
        throw new UnsupportedOperationException("Method not supported by Grid. Use "
            + "insert(Control, int, int) instead");
    }

    /**
     * @throws UnsupportedOperationException use
     * {@link #insert(org.apache.click.Control, int, int)} instead
     */
    @Override
    public Control insert(Control control, int index) {
        throw new UnsupportedOperationException("Method not supported by Grid. Use "
            + "insert(Control, int, int) instead");
    }

    /**
     * Insert the control at the given row and column. If the row or column does
     * not exist, the grid will {@link #ensureCapacity(int, int) expand} until
     * there are enough rows and columns to add the control. If the row and
     * column does exist the Control will be added to the Cell at the given
     * row and column.
     *
     * @param control the control to add to the grid
     * @param row the row to insert the control at
     * @param column the column to insert the control at
     * @return the added control
     */
    public Control insert(Control control, int row, int column) {
        if (row <= 0) {
            throw new IndexOutOfBoundsException("row must be > 0");
        }
        if (column <= 0) {
            throw new IndexOutOfBoundsException("column must be > 0");
        }
        if (control instanceof Cell || control instanceof Row) {
            throw new IllegalArgumentException("Rows and Cells cannot be added" +
                " to Grid.");
        }
        Cell cell = insertCell(row, column);
        cell.add(control);
        return control;
    }

    /**
     * Insert text at the given row and column. If the row or column does
     * not exist, the grid will {@link #ensureCapacity(int, int) expand} until
     * there are enough rows and columns to add the text. If the row and
     * column does exist the text will be added to the Cell at the given
     * row and column.
     *
     * @param text the text to add to the grid
     * @param row the row to insert the control at
     * @param column the column to insert the control at
     * @return the cell to which the text was added
     */
    public Cell setText(String text, int row, int column) {
        if (row <= 0) {
            throw new IndexOutOfBoundsException("row must be > 0");
        }
        if (column <= 0) {
            throw new IndexOutOfBoundsException("column must be > 0");
        }

        Cell cell = insertCell(row, column);
        cell.setText(text);
        return cell;
    }

    /**
     * Insert text at the given row and column. If the row or column does
     * not exist, the grid will {@link #ensureCapacity(int, int) expand} until
     * there are enough rows and columns to add the text. If the row and
     * column does exist the text will be added to the Cell at the given
     * row and column.
     *
     * @param text the text to add to the grid
     * @param row the row to insert the control at
     * @param column the column to insert the control at
     * @return the cell to which the text was added
     */
    public Cell setText(Text text, int row, int column) {
        if (row <= 0) {
            throw new IndexOutOfBoundsException("row must be > 0");
        }
        if (column <= 0) {
            throw new IndexOutOfBoundsException("column must be > 0");
        }

        Cell cell = insertCell(row, column);
        cell.setText(text);
        return cell;
    }

    /**
     * Remove the content at the given row and column.
     *
     * @param row the row  which content to remove
     * @param column the column which content to remove
     * @return true if the content at the given row and column was removed
     */
    public boolean remove(int row, int column) {
        Cell td = getCell(row, column);
        if (td == null) {
            return false;
        }

        return removeChildControls(td);
    }

    /**
     * Return the number of rows in the grid.
     *
     * @return the number of rows in the grid
     */
    public int getRowCount() {
        return hasControls() ? getControls().size() : 0;
    }

    /**
     * Return the number of columns in the grid.
     *
     * @return the number of columns in the grid
     */
    public int getColumnCount() {
        Row row = getRow(1);
        return row.getColumnCount();
    }

    // -------------------------------------------------------- Package Private Methods

    /**
     * Adds rows to the table up to and including the specified row index.
     *
     * @param row specifies number of rows the table should have
     */
    void expandRows(int row) {
        //Try and find the row. If it exists already, return
        Row tableRow = getRow(row);
        if (tableRow != null) {
            return;
        }

        //It does not exist, create it and all previous rows
        int rowCount = getRowCount();
        Row newRow = null;
        while (rowCount < row) {
            newRow = new GridRow();
            super.insert(newRow, getControls().size());
            rowCount++;
            newRow.expandCells(numColumns);
        }
    }

    // ---------------------------------------------------------- Inner classes

    /**
     * Provides a row that inserts content at index 1 instead of 0.
     */
    class GridRow extends Row {

        /**
         * Add the control at the given column + 1.
         * <p/>
         * This method delegates to {@link #insert(net.sf.clickclick.control.html.table.Cell, int)}.
         *
         * @param control the control to add
         * @param column the column to add the control to
         * @return the added control
         * @throws IllegalArgumentException if the control is not a Cell
         */
        @Override
        public Control insert(Control control, int column) {
            if (!(control instanceof Cell)) {
                throw new IllegalArgumentException("Only cells can be inserted.");
            }
            return insert((Cell) control, column + 1);
        }

        /**
         * Add the cell at the given column - 1. Thus invoking the method
         * <code>row.insert(cell, 1)</code>, will become
         * <code>row.insert(cell, 0)</code>. This ensures callers of this
         * method can use a 1 based index which is translated to a 0 based index.
         *
         * @param cell the cell to add
         * @param column the column to add the cell to
         * @return the added cell
         * @throws IndexOutOfBoundsException if the column is <= 0
         */
        @Override
        public Cell insert(Cell cell, int column) {
            if (column <= 0) {
                throw new IndexOutOfBoundsException("For Grids the column must be > 0");
            }
            super.insert(cell, column - 1);
            return cell;
        }

        /**
         * Return the cell at the given column - 1, or null if no cell was found.
         * This ensures callers of this method can use a 1 based index which is
         * translated to a 0 based index.
         *
         * @param column the column which cell to retrieve
         * @return the cell at the given column or null if no cell was found
         */
        @Override
        public Cell getCell(final int column) {
            if (hasControls()) {
                if (getControls().size() >= column) {
                    int realColumn = column - 1;
                    return (Cell) getControls().get(realColumn);
                }
            }
            return null;
        }

        /**
         * Insert a new cell at the given column.
         *
         * @param column the column to insert the cell at
         * @return the cell that was inserted
         * @throws IndexOutOfBoundsException if the column is <= 0
         */
        @Override
        public Cell insertCell(int column) {
            if (column <= 0) {
                throw new IndexOutOfBoundsException("For Grids the column must be > 0");
            }
            if (column > getColumnCount()) {
                expandCells(column);
                return getCell(column);
            } else {
                Cell newCell = new Cell();
                insert(newCell, column);
                return newCell;
            }
        }
    }

    // -------------------------------------------------------- Private Methods

    /**
     * Ensures the grid contains the specified number of rows and columns. If
     * the grid has less rows or columns it is expanded accordingly.
     *
     * @param newRowCount the minimum number of rows of the grid
     * @param newColumnCount the minimum number of columns of the grid
     */
    private void ensureCapacity(int newRowCount, int newColumnCount) {
        if (newRowCount > numRows) {
            this.numRows = newRowCount;
            expandRows(numRows);
        }
        if (newColumnCount > numColumns) {
            this.numColumns = newColumnCount;

            List rows = (List) getControls();
            for (int i = 0; i < rows.size(); i++) {
                Row row = (Row) rows.get(i);
                row.expandCells(numColumns);
            }
        }
    }

    /**
     * Adds and creates a new Cell at the given row and column. If the row or column does
     * not exist, the grid will {@link #ensureCapacity(int, int) expand} until
     * there are enough rows and columns to add the control.
     * <p/>
     * This method delegates to {@link #ensureCapacity(int, int)}.
     *
     * @param row the row to insert the cell at
     * @param column the column to insert the cell at
     * @return the new cell
     */
    private Cell insertCell(int row, int column) {
        if (row <= 0) {
            throw new IndexOutOfBoundsException("row must be > 0");
        }
        if (column <= 0) {
            throw new IndexOutOfBoundsException("column must be > 0");
        }
        ensureCapacity(row, column);
        return getRow(row).getCell(column);
    }

    /**
     * Remove all the child controls from the given container and return true
     * if any control was removed, false otherwise.
     *
     * @param container the container to remove child controls from
     * @return true if any control was removed, false otherwise
     */
    private boolean removeChildControls(Container container) {
        boolean hasChanged = false;
        if (container.hasControls()) {
            List localControls = container.getControls();
            for (int i = localControls.size() - 1; i >= 0; i--) {
                Control control = (Control) localControls.get(i);
                if (container.remove(control)) {
                    hasChanged = true;
                }
            }
        }
        return hasChanged;
    }

    /**
     * Return the cell at the given row and column.
     *
     * @param row the row which cell to return
     * @param column the column which cell to return
     * @return the cell at the given row and column
     */
    private Cell getCell(int row, int column) {
        Row tableRow = getRow(row);
        if(tableRow == null) {
            return null;
        }

        return tableRow.getCell(column);
    }

    /**
     * Return the Row object at the given row.
     *
     * @param row the row which Row object to return
     * @return the Row object at the given row
     */
    private Row getRow(int row) {
        if (hasControls()) {
            if (getControls().size() >= row) {
                int realRow = row - 1;
                return (Row) getControls().get(realRow);
            }
        }
        return null;
    }
}
