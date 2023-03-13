package net.sf.clickclick.control.html.table;

import net.sf.clickclick.control.Text;
import org.apache.click.Control;
import org.apache.click.control.AbstractContainer;
import org.apache.click.control.AbstractControl;
import org.apache.click.util.HtmlStringBuffer;



/**
 * Provide a table Cell control: <tt>&lt;td&gt;</tt> .
 */
public class Cell extends AbstractContainer {
  private static final long serialVersionUID = 6470577767114403302L;


  /**
   * Create a default table Cell.
   */
  public Cell() {
  }

  /**
   * Create a table Cell with the given name.
   */
  public Cell(String name) {
    super(name);
  }

  /**
   * Create a table Cell with the given control.
   */
  public Cell(Control control) {
    add(control);
  }

  // ------------------------------------------------------ Public Properties

  /**
   * Return the div's html tag: <tt>div</tt>.
   *
   * @see AbstractControl#getTag()
   *
   * @return this controls html tag
   */
  @Override public String getTag() {
    return "td";
  }

  /**
   * Set the content of this cell to the given value.
   *
   * @param value the text value
   */
  public void setText(String value) {
    Text text = new Text(value);
    add(text);
  }

  /**
   * Set the content of this cell to the given
   * {@link net.sf.clickclick.control.Text} object.
   *
   * @param text the Text object
   */
  public void setText(Text text) {
    add(text);
  }

  /**
   * Return the cell parent {@link Row} object.
   *
   * @return the cell parent Row object
   */
  public Row getRow() {
    return (Row) getParent();
  }

  // --------------------------------------------------------- Public Methods

  /**
   * Render the HTML representation of the table cell.
   *
   * @param buffer the buffer to render to
   */
  @Override public void render(HtmlStringBuffer buffer) {
    if (getTag() != null) {
      renderTagBegin(getTag(), buffer);
      buffer.closeTag();
      renderContent(buffer);
      renderTagEnd(getTag(), buffer);
    } else {
      if(hasControls()) {
        renderContent(buffer);
      }
    }
  }

  /**
   * Render the table cell child controls.
   *
   * @param buffer the buffer to render to
   */
  @Override protected void renderChildren(HtmlStringBuffer buffer) {
    if(hasControls()) {
      for (Control control : getControls()){
        control.render(buffer);
      }
    }
  }
}