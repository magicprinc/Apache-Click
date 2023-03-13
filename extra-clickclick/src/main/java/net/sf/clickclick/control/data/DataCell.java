package net.sf.clickclick.control.data;

import net.sf.clickclick.control.html.table.Cell;



/**
 *
 */
public class DataCell extends Cell {
  private static final long serialVersionUID = -8867577629030108304L;


  protected DataControl dataControl;

  // ------------------------------------------------------------ Constructor

  public DataCell(Object dataSource, String expr, String format) {
    setDataControl(new DataControl(dataSource, expr, format));
  }

  public DataCell(String name, Object dataSource, String expr, String format) {
    super(name);
    setDataControl(new DataControl(dataSource, expr, format));
  }

  public DataCell(Object dataSource, String expr) {
    setDataControl(new DataControl(dataSource, expr));
  }

  public DataCell(String name, Object dataSource, String expr) {
    super(name);
    setDataControl(new DataControl(dataSource, expr));
  }

  public DataCell(String name, DataControl dataControl) {
    super(name);
    setDataControl(dataControl);
  }

  public DataCell(DataControl dataControl) {
    setDataControl(dataControl);
  }

  public DataCell(String name) {
    super(name);
  }

  public DataCell() {
  }

  /**
   * @return the dataControl
   */
  public DataControl getDataControl() {
    return dataControl;
  }

  /**
   * @param dataControl the dataControl to set
   */
  public void setDataControl(DataControl dataControl) {
    this.dataControl = dataControl;
    if (!contains(dataControl)) {
      add(dataControl);
    }
  }

  public void setDecorator(DataDecorator decorator) {
    getDataControl().setDecorator(decorator);
  }
}