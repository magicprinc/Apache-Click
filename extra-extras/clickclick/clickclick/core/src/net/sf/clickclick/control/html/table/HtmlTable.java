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

import org.apache.click.control.AbstractContainer;
import org.apache.click.control.AbstractControl;
import org.apache.commons.lang.math.NumberUtils;

/**
 * Provides an HTML table control: &lt;table&gt;.
 * <p/>
 * <b>Please note:</b> HtmlTable is not related to nor extends from
 * {@link org.apache.click.control.Table}.
 * <p/>
 * The following example shows how to use an HtmlTable control to render a 2 X 2
 * grid.
 * <p/>
 * Given the Page <tt>MyPage.java</tt>:
 *
 * <pre class="prettyprint">
 * public MyPage extends Page {
 *
 *     public onInit() {
 *         // Create a new HtmlTable
 *         HtmlTable table = new HtmlTable("htmlTable");
 *
 *         // Create row 1 and add two cells
 *         Row row1 = new Row();
 *         table.add(row1);
 *         row1.add("row 1, cell 1");
 *         row1.add("row 1, cell 2");
 *
 *         // Create row 2 and add another two cells
 *         Row row2 = new Row();
 *         table.add(row2);
 *         row2.add("row 2, cell 1");
 *         row2.add("row 2, cell 2");
 *
 *         // Add the table to the Page control list
 *         addControl(table);
 *     }
 * } </pre>
 *
 * and the template <tt>my-page.htm</tt>:
 *
 * <pre class="prettyprint">
 * $htmlTable </pre>
 *
 * will render as:
 *
 * <div class="border">
 * <table name="table" id="table" border="1">
 *   <tr>
 *     <td>row 1, cell 1</td>
 *     <td>row 1, cell 2</td>
 *   </tr>
 *   <tr>
 *     <td>row 2, cell 1</td>
 *     <td>row 2, cell 2</td>
 *   </tr>
 * </table>
 * </div>
 */
public class HtmlTable extends AbstractContainer {

    // ----------------------------------------------------------- Constructors

    /**
     * Create a default HtmlTable.
     * <p/>
     * The table border is automatically set to 1.
     */
    public HtmlTable() {
        this(null);
    }

    /**
     * Create an HtmlTable for the given name.
     * <p/>
     * The table border is automatically set to 1.
     */
    public HtmlTable(String name) {
        super(name);
        setAttribute("border", "1");
    }

    // ------------------------------------------------------ Public Properties

    /**
     * Return the table's html tag: <tt>table</tt>.
     *
     * @see AbstractControl#getTag()
     *
     * @return this controls html tag
     */
    public String getTag() {
        return "table";
    }

    /**
     * Set the table <tt>cellspacing</tt> attribute value.
     *
     * @param cellspacing the table cellspacing attribute value
     */
    public void setCellspacing(int cellspacing) {
        setAttribute("cellspacing", Integer.toString(cellspacing));
    }

    /**
     * Return the table <tt>cellspacing</tt> attribute value.
     *
     * @return the table cellspacing attribute value
     */
    public int getCellspacing() {
        String cellspacing = getAttribute("cellspacing");
        if (NumberUtils.isNumber(cellspacing)) {
            return NumberUtils.toInt(cellspacing);
        }
        return 0;
    }

    /**
     * Set the table <tt>cellpadding</tt> attribute value.
     *
     * @param cellpadding the table cellpadding attribute value
     */
    public void setCellpadding(int cellpadding) {
        setAttribute("cellpadding", Integer.toString(cellpadding));
    }

    /**
     * Return the table <tt>cellpadding</tt> attribute value.
     *
     * @return the table cellpadding attribute value
     */
    public int getCellpadding() {
        String cellpadding = getAttribute("cellpadding");
        if (NumberUtils.isNumber(cellpadding)) {
            return NumberUtils.toInt(cellpadding);
        }
        return 0;
    }

    /**
     * Set the table <tt>border</tt> attribute value.
     *
     * @param border the table border attribute value
     */
    public void setBorder(int border) {
        setAttribute("border", Integer.toString(border));
    }

    /**
     * Return the table <tt>border</tt> attribute value.
     *
     * @return the table border attribute value
     */
    public int getBorder() {
        String border = getAttribute("border");
        if (NumberUtils.isNumber(border)) {
            return NumberUtils.toInt(border);
        }
        return 0;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Add the given headers to the table. A new {@link HeaderRow} is created
     * and for each header a new {@link HeaderCell} is created and added to the
     * row.
     * <p/>
     * This method delegates to {@link #insert(java.lang.Object, int)}.
     *
     * @param text the text array to add to the row
     * @return the header row that was created
     */
    public HeaderRow setHeader(Object... textArray) {
        HeaderRow headerRow = new HeaderRow();
        add(headerRow);
        for (Object text : textArray) {
            headerRow.add(text);
        }
        return headerRow;
    }

    /**
     * Add the given text array to the table. A new {@link Row} is created and
     * for each text object a new {@link Cell} is created and added to the row.
     * <p/>
     * This method delegates to {@link #insert(java.lang.Object, int)}.
     *
     * @param text the text array to add to the row
     * @return the row that was created
     */
    public Row addRow(Object... textArray) {
        Row row = new Row();
        add(row);
        for (Object text : textArray) {
            row.add(text);
        }
        return row;
    }
}
