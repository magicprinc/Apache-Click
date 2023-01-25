package net.sf.clickclick.examples.page.control;

import net.sf.clickclick.control.Text;
import net.sf.clickclick.control.html.Div;
import net.sf.clickclick.control.html.Span;
import net.sf.clickclick.control.html.list.HtmlList;
import net.sf.clickclick.control.html.list.ListItem;
import net.sf.clickclick.control.html.table.Cell;
import net.sf.clickclick.control.html.table.HeaderCell;
import net.sf.clickclick.control.html.table.HtmlTable;
import net.sf.clickclick.control.html.table.Row;
import net.sf.clickclick.examples.page.BorderPage;

import java.io.Serial;

/**
 *
 */
public class HtmlBasicsPage extends BorderPage {
  @Serial private static final long serialVersionUID = -3170453776998174837L;

  private Div div;
  private Span span;
  private HtmlList list;
  private HtmlTable table;

  @Override public void onInit() {
    // Create a div
    div = new Div("mydiv");
    div.setStyle("border", "1px dashed green");
    div.add(new Text("hello world"));
    addControl(div);

    // Create a span
    span = new Span("myspan");
    span.setStyle("border", "1px solid red");
    span.add(new Text("hello world"));
    addControl(span);

    // Create a list
    list = new HtmlList("mylist");
    populateList(list, 5);
    addControl(list);

    // Create a table
    table = new HtmlTable("mytable");
    table.setAttribute("border", "1");
    int numberOfRows = 5;
    populateTable(table, numberOfRows);
    addControl(table);
  }

  private void populateList(HtmlList list, int numberOfItems) {
    for (int i = 0; i < numberOfItems; i++) {
      ListItem item = new ListItem();
      item.add(new Text("Item " + ( i + 1 )));
      list.add(item);
    }
  }

  private void populateTable(HtmlTable table, int numberOfRows) {
    int numberOfCells = 5;
    Row row = new Row();

    // Add header cells
    for (int i = 0; i < numberOfCells; i++) {
      HeaderCell cell = new HeaderCell();
      cell.add(new Text("Headers " + ( i + 1 )));
      row.add(cell);
    }
    table.add(row);

    // Add data cells
    for (int i = 0; i < numberOfRows; i++) {
      row = new Row();
      table.add(row);

      // Add cells to row
      populateRow(row, numberOfCells);
    }

    // Add footer which spans all cells
    row = new Row();
    table.add(row);
    Cell cell = new Cell();
    cell.add(new Text("Footer"));
    cell.setAttribute("colspan", Integer.toString(numberOfCells));
    row.add(cell);
  }

  private void populateRow(Row row, int numberOfCells) {
    for (int i = 0; i < numberOfCells; i++) {
      Cell cell = new Cell();
      row.add(cell);

      // Add random text to cell
      cell.setText(Integer.toString(i + 1));
    }
  }
}