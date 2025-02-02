package net.sf.clickclick.control.paginator;

import org.apache.click.Context;
import org.apache.click.control.AbstractControl;
import org.apache.click.control.AbstractLink;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Table;
import org.apache.click.element.CssImport;
import org.apache.click.element.Element;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serial;
import java.util.List;

/**
 * Provides a simple independent paginator control.
 * <p/>
 * The Paginator is based on the following article:
 * http://woork.blogspot.com/2008/03/perfect-pagination-style-using-css.html.
 *
 * <h3>Pre defined styles</h3>
 * The paginator CSS style sheet, ("/clickclick/core/paginator/SimplePaginator.css"),
 * defines the following predefined styles:
 * <ul>
 *  <li>pagination-digg</li>
 *  <li>pagination-clean</li>
 *  <li>pagination-flickr</li>
 * </ul>
 */
public class SimplePaginator extends AbstractControl implements Paginator {
  @Serial private static final long serialVersionUID = 722447277080781529L;


  /** The control ActionLink page number parameter name: <tt>"page"</tt>. */
  public static final String PAGE = "page";


  /** The total number of pages. */
  private int pageTotal;

  /** The current page number. */
  private int currentPage;

  /** Indicates the lower bound for rendering paging links. */
  protected int lowerBound;

  /** Indicates the upper bound for rendering paging links. */
  protected int upperBound;

  /** The css style class, default is <tt>"pagination-digg"</tt>. */
  protected String styleClass = "pagination-digg";

  /** The link to render. */
  protected ActionLink controlLink;

  protected int totalItems;

  protected int itemsPerPage = 0;

  private boolean processNextPage = true;

  // ----------------------------------------------------------- Constructors

  /**
   * Create a default SimplePaginator.
   */
  public SimplePaginator() {
    setAttribute("class", styleClass);
  }

  /**
   * Create a SimplePaginator for the given name.
   *
   * @param name the name of the paginator
   */
  public SimplePaginator(String name) {
    setName(name);
    setAttribute("class", styleClass);
  }

  // ------------------------------------------------------ Public Properties

  /**
   * Return the title of the <tt>first</tt> page link.
   * <p/>
   * The title can be localized through the message
   * <tt>"paginator-first-title"</tt>.
   *
   * @return the title of the first page link
   */
  public String getFirstTitleMessage() {
    return getMessage("paginator-first-title");
  }

  /**
   * Return the label of the <tt>first</tt> page link.
   * <p/>
   * The label can be localized through the message
   * <tt>"paginator-first-label"</tt>.
   *
   * @return the label of the first page link
   */
  public String getFirstLabelMessage() {
    return getMessage("paginator-first-label");
  }

  /**
   * Return the title of the <tt>last</tt> page link.
   * <p/>
   * The title can be localized through the message
   * <tt>"paginator-last-title"</tt>.
   *
   * @return the title of the last page link
   */
  public String getLastTitleMessage() {
    return getMessage("paginator-last-title");
  }

  /**
   * Return the label of the <tt>last</tt> page link.
   * <p/>
   * The label can be localized through the message
   * <tt>"paginator-last-label"</tt>.
   *
   * @return the label of the last page link
   */
  public String getLastLabelMessage() {
    return getMessage("paginator-last-label");
  }

  /**
   * Return the title of the <tt>next</tt> page link.
   * <p/>
   * The title can be localized through the message
   * <tt>"paginator-next-title"</tt>.
   *
   * @return the title of the next page link
   */
  public String getNextTitleMessage() {
    return getMessage("paginator-next-title");
  }

  /**
   * Return the label of the <tt>next</tt> page link.
   * <p/>
   * The label can be localized through the message
   * <tt>"paginator-next-label"</tt>.
   *
   * @return the label of the next page link
   */
  public String getNextLabelMessage() {
    return getMessage("paginator-next-label");
  }

  /**
   * Return the title of the <tt>previous</tt> page link.
   * <p/>
   * The title can be localized through the message
   * <tt>"paginator-previous-title"</tt>.
   *
   * @return the title of the previous page link
   */
  public String getPreviousTitleMessage() {
    return getMessage("paginator-previous-title");
  }

  /**
   * Return the label of the <tt>previous</tt> page link.
   * <p/>
   * The label can be localized through the message
   * <tt>"paginator-previous-label"</tt>.
   *
   * @return the label of the previous page link
   */
  public String getPreviousLabelMessage() {
    return getMessage("paginator-previous-label");
  }

  /**
   * Return the title of the <tt>page link</tt>.
   * <p/>
   * The title can be localized through the message
   * <tt>"paginator-goto-title"</tt>.
   *
   * @return the title of the page link
   */
  public String getGotoPageTitleMessage() {
    return getMessage("paginator-goto-title");
  }

  /**
   * Set the page link.
   *
   * @param pageLink the page link
   */
  public void setControlLink(ActionLink controlLink) {
    this.controlLink = controlLink;
  }

  /**
   * Return the page link.
   *
   * @return the page link
   */
  public ActionLink getControlLink() {
    if (controlLink == null) {
      String name = getName();
      if (name == null) {
        throw new RuntimeException("Paginator name is not defined. " +
            "Please set the Paginator name through #setName(String).");
      }
      controlLink = new ActionLink(name);
    }
    return controlLink;
  }

  /**
   * Set the HTML class attribute.
   * <p/>
   * <b>Note:</b> this method will replace the existing <tt>"class"</tt>
   * attribute value.
   * <p/>
   * Predefined paginator CSS classes are:
   * <ul>
   *  <li>pagination-digg</li>
   *  <li>pagination-clean</li>
   *  <li>pagination-flickr</li>
   * </ul>
   *
   * @param value the HTML class attribute
   */
  public void setClass(String value) {
    setAttribute("class", value);
  }

  /**
   * Set the current page value.
   *
   * @param currentPage the current page value
   */
  public void setCurrentPage(int currentPage) {
    this.currentPage = currentPage;
  }

  /**
   * Return the current page value.
   *
   * @return the current page value
   */
  public int getCurrentPage() {
    if (processNextPage) {
      currentPage = processNextPage();
      processNextPage = false;
    }
    return currentPage;
  }

  /**
   * Return the total number of pages.
   *
   * @return the total number of pages
   */
  public int getPageTotal() {
    return pageTotal;
  }

  /**
   * @return the totalItems
   */
  public int getTotalItems() {
    return totalItems;
  }

  /**
   * @param totalItems the totalItems to set
   */
  public void setTotalItems(int totalItems) {
    this.totalItems = totalItems;
  }

  /**
   * @return the itemsPerPage
   */
  public int getItemsPerPage() {
    return itemsPerPage;
  }

  /**
   * @param itemsPerPage the itemsPerPage to set
   */
  public void setItemsPerPage(int itemsPerPage) {
    this.itemsPerPage = itemsPerPage;
  }

  /**
   * Return the index of the first item to display. Index starts from 0.
   * <p/>
   * <b>Note:</b> {@link #setItemsPerPage(int) items per page} must be set for
   * this method to correctly calculate the first item index, otherwise this
   * method will return 0.
   *
   * @return the index of the first item to display
   */
  public int getFirstItem() {
    int firstItem = 0;

    if (getItemsPerPage() > 0) {
      int currentPage = getCurrentPage();
      if (currentPage > 0) {
        firstItem = getItemsPerPage() * currentPage;
      }
    }

    return firstItem;
  }

  /**
   * Return the index of the last item to display. Index starts from 0.
   * <p/>
   * <b>Note:</b> the paginator {@link #setTotalItems(int) total items} and
   * {@link #setItemsPerPage(int) items per page} must be set for this method to
   * correctly calculate the last item, otherwise this method will return 0.
   *
   * @return the index of the last item to display
   */
  public int getLastItem() {
    int numbItems = getTotalItems();
    int lastItem = numbItems;

    if (getItemsPerPage() > 0) {
      lastItem = getFirstItem() + getItemsPerPage();
      lastItem = Math.min(lastItem, numbItems);
    }
    return lastItem;
  }

  /**
   * Return the following head elements for the paginator:
   * <ul>
   * <li>
   * "/clickclick/core/paginator/SimplePaginator.css"
   * </li>
   * </ul>
   *
   * @return the head elements of the paginator
   */
  @Override
  public List<Element> getHeadElements() {
    if (headElements == null) {
      headElements = super.getHeadElements();
      CssImport cssImport = new CssImport("/clickclick/core/paginator/SimplePaginator.css");
      headElements.add(cssImport);
    }
    return headElements;
  }

    /*
    @Override
    public boolean onProcess() {
        boolean continueProcessing = super.onProcess();

        setCurrentPage(findNextPage());

        return continueProcessing;
    }
    */

  private int processNextPage() {
    int nextPage = 0;
    ActionLink controlLink = getControlLink();
    ClickUtils.bind(controlLink);

    if (controlLink.isClicked()) {
      String page = Context.getThreadLocalContext().getRequestParameter(PAGE);
      if (NumberUtils.isNumber(page)) {
        nextPage = Integer.parseInt(page);
      }
    }
    return nextPage;
  }

  @Override
  public void onDestroy() {
    processNextPage = true;
  }

  // --------------------------------------------------------- Public Methods

  /**
   * Render the paginator output to the specified buffer.
   *
   * @param buffer the buffer to render output to
   */
  @Override
  public void render(HtmlStringBuffer buffer) {
    calcPageTotal(getItemsPerPage(), getTotalItems());

    // If there are no pages to render, exit early
    if (getPageTotal() <= 0) {
      return;
    }

    calcLowerAndUpperBound();

    buffer.elementStart("ul");
    String styleClass = getAttribute("class");
    if (styleClass != null) {
      buffer.appendAttribute("class", styleClass);
    }
    buffer.closeTag();
    buffer.append("\n");

    renderFirst(buffer);
    buffer.append("\n");
    renderPrevious(buffer);
    buffer.append("\n");

    for (int i = lowerBound; i < upperBound; i++) {
      int pageNumber = i + 1;
      renderPagingLinkContainer(buffer, pageNumber);

      if (i < upperBound - 1) {
        renderPagingLinkSeparator(buffer);
      }
    }

    renderNext(buffer);
    buffer.append("\n");
    renderLast(buffer);
    buffer.append("\n");
    buffer.elementEnd("ul");
  }

  /**
   * Render the HTML representation of the paginator.
   *
   * @return the HTML representation of the paginator
   */
  @Override
  public String toString() {
    calcPageTotal(getItemsPerPage(), getTotalItems());
    HtmlStringBuffer buffer = new HtmlStringBuffer(getPageTotal() * 70);
    render(buffer);
    return buffer.toString();
  }

  // ------------------------------------------------------ Protected Methods

  /**
   * Render the page link container.
   *
   * @param buffer the buffer to render to
   * @param pageNumber the page number of the page link to render
   */
  protected void renderPagingLinkContainer(HtmlStringBuffer buffer, int pageNumber) {
    if (pageNumber - 1 == getCurrentPage()) {
      buffer.append("<li class=\"active\">");
    } else {
      buffer.append("<li>");
    }
    renderPagingLink(buffer, pageNumber);
    buffer.append("</li>");
    buffer.append("\n");
  }

  /**
   * Render the page link for the given page number.
   *
   * @param buffer the buffer to render to
   * @param pageNumber the page number of the page link to render
   */
  protected void renderPagingLink(HtmlStringBuffer buffer, int pageNumber) {
    if (pageNumber - 1 == getCurrentPage()) {
      buffer.append(pageNumber);
    } else {
      AbstractLink controlLink = getControlLink();
      controlLink.setLabel(String.valueOf(pageNumber));

      // Cater for zero based indexing and subtract 1 from pageNumber
      controlLink.setParameter(Table.PAGE, String.valueOf(pageNumber - 1));
      controlLink.setTitle(getGotoPageTitleMessage()
          + " " + pageNumber);
      controlLink.render(buffer);
    }
  }

  /**
   * Render a separator between page links.
   *
   * @param buffer the buffer to render to
   */
  protected void renderPagingLinkSeparator(HtmlStringBuffer buffer) {
  }

  /**
   * Render the <tt>first</tt> paginator link.
   *
   * @param buffer the buffer to render to
   */
  protected void renderFirst(HtmlStringBuffer buffer) {
    buffer.elementStart("li");
    String pageValue = String.valueOf(0);
    if (getCurrentPage() > 0) {
      buffer.appendAttribute("class", "first");
      buffer.closeTag();

      AbstractLink controlLink = getControlLink();
      controlLink.setLabel(getFirstLabelMessage());
      controlLink.setParameter(Table.PAGE, pageValue);
      controlLink.setTitle(getFirstTitleMessage());
      controlLink.render(buffer);
    } else {
      buffer.appendAttribute("class", "first-off");
      buffer.closeTag();

      buffer.append(getFirstLabelMessage());
    }

    buffer.elementEnd("li");
  }

  /**
   * Render the <tt>previous</tt> paginator link.
   *
   * @param buffer the buffer to render to
   */
  protected void renderPrevious(HtmlStringBuffer buffer) {
    buffer.elementStart("li");

    String pageValue = String.valueOf(getCurrentPage() - 1);
    if (getCurrentPage() > 0) {
      buffer.appendAttribute("class", "previous");
      buffer.closeTag();

      AbstractLink controlLink = getControlLink();
      controlLink.setLabel(getPreviousLabelMessage());
      controlLink.setParameter(Table.PAGE, pageValue);
      controlLink.setTitle(getPreviousTitleMessage());
      controlLink.render(buffer);

    } else {
      buffer.appendAttribute("class", "previous-off");
      buffer.closeTag();

      buffer.append(getPreviousLabelMessage());
    }

    buffer.elementEnd("li");
  }

  /**
   * Render the <tt>last</tt> paginator link.
   *
   * @param buffer the buffer to render to
   */
  protected void renderLast(HtmlStringBuffer buffer) {
    buffer.elementStart("li");
    String pageValue = String.valueOf(getPageTotal() - 1);
    if (getCurrentPage() < getPageTotal() - 1) {
      buffer.appendAttribute("class", "last");
      buffer.closeTag();

      AbstractLink controlLink = getControlLink();
      controlLink.setLabel(getLastLabelMessage());
      controlLink.setParameter(Table.PAGE, pageValue);
      controlLink.setTitle(getLastTitleMessage());
      controlLink.render(buffer);
    } else {
      buffer.appendAttribute("class", "last-off");
      buffer.closeTag();

      buffer.append(getLastLabelMessage());
    }

    buffer.elementEnd("li");
  }

  /**
   * Render the <tt>next</tt> paginator link.
   *
   * @param buffer the buffer to render to
   */
  protected void renderNext(HtmlStringBuffer buffer) {
    buffer.elementStart("li");
    String pageValue = String.valueOf(getCurrentPage() + 1);
    if (getCurrentPage() < getPageTotal() - 1) {
      buffer.appendAttribute("class", "next");
      buffer.closeTag();

      AbstractLink pagingLink = getControlLink();
      pagingLink.setLabel(getNextLabelMessage());
      pagingLink.setParameter(Table.PAGE, pageValue);
      pagingLink.setTitle(getNextTitleMessage());
      pagingLink.render(buffer);
    } else {
      buffer.appendAttribute("class", "next-off");
      buffer.closeTag();

      buffer.append(getNextLabelMessage());
    }

    buffer.elementEnd("li");
  }

  /**
   * Calculate the {@link #lowerBound} and {@link #upperBound} values.
   */
  protected void calcLowerAndUpperBound() {
    // Create sliding window of paging links
    lowerBound = Math.max(0, getCurrentPage() - 5);
    upperBound = Math.min(lowerBound + 10, getPageTotal());
    if (upperBound - lowerBound < 10) {
      lowerBound = Math.max(upperBound - 10, 0);
    }
  }

  // Private Methods --------------------------------------------------------

  /**
   * Set the total number of pages.
   *
   * @param pageTotal the total number of pages
   */
  private void setPageTotal(int pageTotal) {
    this.pageTotal = pageTotal;
  }

  /**
   * Provides a method that calculates the {@link #pageTotal} from the
   * given pageSize and total number of rows.
   *
   * @param pageSize the number of rows per page
   * @param rows the number of rows to paginate over
   */
  private void calcPageTotal(int itemsPerPage, int totalItems) {
    // If pageTotal has value, exit early
    if (getPageTotal() > 0) {
      return;
    }

    if (itemsPerPage == 0 || totalItems == 0) {
      // TODO should pageTotal be set to 1???
      setPageTotal(1);
      return;
    }

    double value = (double) totalItems / (double) itemsPerPage;

    setPageTotal((int) Math.ceil(value));
  }
}