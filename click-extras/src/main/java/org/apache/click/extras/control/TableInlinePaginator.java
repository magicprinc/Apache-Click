package org.apache.click.extras.control;

import lombok.val;
import org.apache.click.Context;
import org.apache.click.control.AbstractControl;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Table;
import org.apache.click.control.TablePaginator;
import org.apache.click.util.HtmlStringBuffer;

import java.io.Serial;
import java.util.Map;

/**
 * Provides an inline style table paging controls Paginator.
 *
 * <table class='htmlHeader' cellspacing='10'>
 * <tr>
 * <td>
 * <img align='middle' hspace='2'src='inline-paginator.png' title='Table'/>
 * </td>
 * </tr>
 * </table>
 */
public class TableInlinePaginator extends TablePaginator {
  @Serial private static final long serialVersionUID = 5320840429968221445L;

  /** Private Control which handles resource bundle properties. */
  private final AbstractControl paginatorMessages = new AbstractControl(){
    @Serial private static final long serialVersionUID = 1L;
    /**
     * Messages are defined in the resource bundle:
     * <tt>org/apache/click/extras/control/TableInlinePaginator.properties</tt>.
     */
    @Override public Map<String, String> getMessages(){
      if (messages == null){
        val context = Context.getThreadLocalContext();
        messages = context.createMessagesMap(TableInlinePaginator.class, "");
      }
      return messages;
    }
  };


  /**
   * Create a Paginator for the given Table.
   *
   * @param table the paginator's table
   */
  public TableInlinePaginator (Table table){
    super(table);
  }//new

  /**
   * @see org.apache.click.control.Renderable#render(HtmlStringBuffer)
   *
   * @param buffer the string buffer to render the paginator to
   */
  @Override
  public void render(HtmlStringBuffer buffer) {
    final Table table = getTable();

    if (table == null) {
      throw new IllegalStateException("No parent table defined."
          + " Ensure a parent Table is set using #setTable(Table).");
    }

    final ActionLink controlLink = table.getControlLink();

    if (table.getSortedColumn() != null) {
      controlLink.setParameter(Table.SORT, null);
      controlLink.setParameter(Table.COLUMN, table.getSortedColumn());
      controlLink.setParameter(Table.ASCENDING, String.valueOf(table.isSortedAscending()));
    } else {
      controlLink.setParameter(Table.SORT, null);
      controlLink.setParameter(Table.COLUMN, null);
      controlLink.setParameter(Table.ASCENDING, null);
    }

    String firstLabel;
    String previousLabel;

    if (table.getPageNumber() > 0) {
      controlLink.setDisabled(false);
      controlLink.setImageSrc(paginatorMessages.getMessage("table-inline-first-image"));
      controlLink.setParameter(Table.PAGE, String.valueOf(0));
      controlLink.setTitle(table.getMessage("table-first-title"));
      firstLabel = controlLink.toString();

      controlLink.setImageSrc(paginatorMessages.getMessage("table-inline-previous-image"));
      controlLink.setParameter(Table.PAGE, String.valueOf(table.getPageNumber() - 1));
      controlLink.setTitle(table.getMessage("table-previous-title"));
      previousLabel = controlLink.toString();

    } else {
      controlLink.setDisabled(true);

      controlLink.setImageSrc(paginatorMessages.getMessage("table-inline-first-disabled-image"));
      controlLink.setParameter(Table.PAGE, null);
      controlLink.setTitle(null);
      firstLabel = controlLink.toString();

      controlLink.setImageSrc(paginatorMessages.getMessage("table-inline-previous-disabled-image"));
      controlLink.setParameter(Table.PAGE, null);
      controlLink.setTitle(null);
      previousLabel = controlLink.toString();
    }

    HtmlStringBuffer pagesBuffer =
        new HtmlStringBuffer(table.getNumberPages() * 70);

    // Create sliding window of paging links
    int lowerBound = Math.max(0, table.getPageNumber() - 5);
    int upperBound = Math.min(lowerBound + 10, table.getNumberPages());
    if (upperBound - lowerBound < 10) {
      lowerBound = Math.max(upperBound - 10, 0);
    }

    controlLink.setImageSrc(null);
    controlLink.setDisabled(false);
    String gotoTitle = table.getMessage("table-goto-title");

    for (int i = lowerBound; i < upperBound; i++) {
      String pageNumber = String.valueOf(i + 1);
      if (i == table.getPageNumber()) {
        pagesBuffer.append("<strong>" + pageNumber + "</strong>");

      } else {
        controlLink.setLabel(pageNumber);
        controlLink.setParameter(Table.PAGE, String.valueOf(i));
        controlLink.setTitle(gotoTitle + " " + pageNumber);
        pagesBuffer.append(controlLink.toString());
      }

      if (i < upperBound - 1) {
        pagesBuffer.append("&#160; ");
      }
    }

    String nextLabel;
    String lastLabel;

    if (table.getPageNumber() < table.getNumberPages() - 1) {
      controlLink.setDisabled(false);
      controlLink.setImageSrc(paginatorMessages.getMessage("table-inline-next-image"));
      controlLink.setParameter(Table.PAGE, String.valueOf(table.getPageNumber() + 1));
      controlLink.setTitle(table.getMessage("table-next-title"));
      nextLabel = controlLink.toString();

      controlLink.setImageSrc(paginatorMessages.getMessage("table-inline-last-image"));
      controlLink.setParameter(Table.PAGE, String.valueOf(table.getNumberPages() - 1));
      controlLink.setTitle(table.getMessage("table-last-title"));
      lastLabel = controlLink.toString();

    } else {
      controlLink.setDisabled(true);

      controlLink.setImageSrc(paginatorMessages.getMessage("table-inline-next-disabled-image"));
      controlLink.setParameter(Table.PAGE, null);
      controlLink.setTitle(null);
      nextLabel = controlLink.toString();

      controlLink.setImageSrc(paginatorMessages.getMessage("table-inline-last-disabled-image"));
      controlLink.setParameter(Table.PAGE, null);
      controlLink.setTitle(null);
      lastLabel = controlLink.toString();
    }

    controlLink.setDisabled(false);
    controlLink.setImageSrc(null);
    controlLink.setTitle(null);

    final String pageLinks = pagesBuffer.toString();

    final Object[] args =
        { firstLabel, previousLabel, pageLinks, nextLabel, lastLabel };

    buffer.append(paginatorMessages.getMessage("table-inline-page-links", args));
  }

}