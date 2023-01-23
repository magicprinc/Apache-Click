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
import org.apache.click.control.AbstractControl;

/**
 * Provide a table row control: &lt;tr&gt;.
 */
public class Row extends AbstractContainer {

    // ----------------------------------------------------------- Constructors

    /**
     * Create a default table row.
     */
    public Row() {
    }

    /**
     * Create a table row with the given name.
     *
     * @param name the name of the row
     */
    public Row(String name) {
        super(name);
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Return the table row html tag: <tt>tr</tt>.
     *
     * @see AbstractControl#getTag()
     *
     * @return this controls html tag
     */
    public String getTag() {
        return "tr";
    }

    /**
     * Add the given text array by creating and adding {@link Cell}
     * instances to the row and setting each Cell content to a text object.
     * <p/>
     * This method delegates to {@link #insert(java.lang.Object, int)}.
     *
     * @param text the text array to add to the row
     */
    public void add(Object... textArray) {
        for (Object text : textArray) {
            insert(text, getControls().size());
        }
    }

    /**
     * Add the given text object by creating and adding a {@link Cell}
     * instance to the row and setting the Cell content to the given text
     * object.
     * <p/>
     * This method delegates to {@link #insert(java.lang.Object, int)}.
     *
     * @param text the text to add to the row
     * @return the newly created Cell object
     */
    public Cell add(Object text) {
        return insert(text, getControls().size());
    }

    /**
     * Add the control to the row at the given column. If the column does not
     * exist, the row will {@link #expandCells(int) expand} until enough columns
     * are available to add the Cell. If the column does exist the new Cell will
     * be inserted at the given column and all cells to the right will move one
     * column up.
     *
     * @param control the control to add
     * @param column the column to add the control to
     * @return the control that was added
     */
    public Control insert(Control control, int column) {
        if (column > getColumnCount()) {
            expandCells(column);
        }
        super.insert(control, column);
        return control;
    }

    /**
     * Add the Cell to the row at the given column.
     * <p/>
     * This method delegates to {@link #insert(org.apache.click.Control, int)}.
     *
     * @param cell the Cell to add
     * @param column the column to add the cell to
     * @return the cell that was added
     */
    public Cell insert(Cell cell, int column) {
        if (column > getColumnCount()) {
            expandCells(column);
        }
        super.insert(cell, column);
        return cell;
    }

    /**
     * Add the given text object by creating and adding a {@link Cell}
     * instance to the row and setting the Cell content to the given text
     * object. The text object will be added to the given column. If the column
     * does not exist, the row will {@link #expandCells(int) expand} until
     * enough columns are available to add the control.
     * <p/>
     * This method delegates to {@link #createCell()} to instantiate new Cell
     * objects.
     *
     * @param text the text to add to the row
     * @param column the column to add the text to
     * @return the newly created Cell object
     */
    public Cell insert(Object text, int column) {
        if (column > getColumnCount()) {
            expandCells(column);
        }
        if (text == null) {
            text = "";
        }
        Cell cell = createCell();
        cell.setText(text.toString());
        return insert(cell, column);
    }

    /**
     * Insert and return a new Cell at the given column. If the column
     * does not exist, the row will {@link #expandCells(int) expand} until
     * enough columns are available to add the control. If the column does
     * exist the new Cell will be inserted at the given column and all cells
     * to the right will move one column up.
     * <p/>
     * This method delegates to {@link #createCell()} to instantiate new Cell
     * objects.
     *
     * @param column the column to add the new Cell to
     * @return the newly created Cell object
     */
    public Cell insertCell(int column) {
        if (column < 0) {
            throw new IndexOutOfBoundsException("column must be >= 0");
        }
        if (column > getColumnCount()) {
            expandCells(column);
            return getCell(column);
        } else {
            Cell newCell = createCell();
            insert(newCell, column);
            return newCell;
        }
    }

    /**
     * Remove and return the Cell at the given column, or null if no cell was
     * found.
     *
     * @param column the column which cell to remove
     * @return the removed cell or null if no cell was found
     */
    public Cell removeCell(int column) {
        Cell cell = getCell(column);
        if (cell == null) {
            return null;
        }

        remove(cell);
        return cell;
    }

    /**
     * Return the number of columns in this row.
     *
     * @return the number of columns in this row
     */
    public int getColumnCount() {
        return hasControls() ? getControls().size() : 0;
    }

    /**
     * Return the Cell at the given column.
     *
     * @param column the column which Cell to return
     * @return the Cell at the given column
     */
    public Cell getCell(int column) {
        if (hasControls()) {
            if (getControls().size() >= column) {
                return (Cell) getControls().get(column);
            }
        }
        return null;
    }

    /**
     * Add cells to the row up to and including the specified column.
     * <p/>
     * This method delegates to {@link #createCell()} to instantiate new Cell
     * objects.
     *
     * @param column specifies number of cells the row should have
     */
    public void expandCells(int column) {
        //Try and find the cell. If it exists already, return
        Cell tableCell = getCell(column);
        if (tableCell != null) {
            return;
        }

        //It does not exist, create it and all previous cells
        int columnCount = getColumnCount();
        Cell newCell = null;
        while(columnCount < column) {
            newCell = createCell();
            add(newCell);
            columnCount++;
        }
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Create and return a new {@link Cell} instance.
     *
     * @return a newly created Cell instance
     */
    protected Cell createCell() {
        return new Cell();
    }
}
