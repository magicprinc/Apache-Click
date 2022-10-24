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
package net.sf.clickclick.control.paginator;

import java.util.List;
import org.apache.click.control.AbstractControl;
import org.apache.click.control.AbstractLink;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Renderable;
import org.apache.click.control.Table;
import org.apache.click.element.CssImport;
import org.apache.click.util.HtmlStringBuffer;

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
public class SimplePaginator extends AbstractControl implements Renderable {

    // -------------------------------------------------------------- Constants

    private static final long serialVersionUID = 1L;

    // -------------------------------------------------------------- Variables

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
    protected AbstractLink pageLink;

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
    public void setPageLink(AbstractLink pageLink) {
        this.pageLink = pageLink;
    }

    /**
     * Return the page link.
     *
     * @return the page link
     */
    public AbstractLink getPageLink() {
        if (pageLink == null) {
            String name = getName();
            if (getName() == null) {
                throw new RuntimeException("Paginator name is not defined. " +
                    "Please set the Paginator name through #setName(String).");
            }
            pageLink = new ActionLink(getName());
        }
        return pageLink;
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
        return currentPage;
    }

    /**
     * Set the total number of pages.
     *
     * @param pageTotal the total number of pages
     */
    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
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
     * Return the following head elements for the paginator:
     * <ul>
     * <li>
     * "/clickclick/core/paginator/SimplePaginator.css"
     * </li>
     * </ul>
     *
     * @return the head elements of the paginator
     */
    public List getHeadElements() {
        if (headElements == null) {
            headElements = super.getHeadElements();
            CssImport cssImport = new CssImport("/clickclick/core/paginator/SimplePaginator.css");
            headElements.add(cssImport);
        }
        return headElements;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Provides a public method that calculates the {@link #pageTotal} from the
     * given pageSize and total number of rows.
     *
     * <pre class="prettyprint">
     * public MyPage() {
     *     int currentPage = 1;
     *     int pageSize = 10;
     *     int rowCount = 3000;
     *     SimplePaginator paginator = new SimplePaginator("my-paginator");
     *     paginator.setCurrentPage(currentPage);
     *     paginator.calcPageTotal(pageSize, rowCount);
     * }
     * </pre>
     *
     * @param pageSize the number of rows per page
     * @param rows the number of rows to paginate over
     */
    public void calcPageTotal(int pageSize, int rows) {
        // If pageTotal has value, exit early
        if (getPageTotal() > 0) {
            return;
        }

        if (pageSize == 0 || rows == 0) {
            setPageTotal(1);
            return;
        }

        double value = (double) rows / (double) pageSize;

        setPageTotal((int) Math.ceil(value));
    }

    /**
     * Render the paginator's output to the specified buffer.
     *
     * @param buffer the buffer to render output to
     */
    public void render(HtmlStringBuffer buffer) {

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
            renderPageLinkContainer(buffer, pageNumber);

            if (i < upperBound - 1) {
                renderPageLinkSeparator(buffer);
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
    public String toString() {
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
    protected void renderPageLinkContainer(HtmlStringBuffer buffer, int pageNumber) {
        if (pageNumber - 1 == getCurrentPage()) {
            buffer.append("<li class=\"active\">");
        } else {
            buffer.append("<li>");
        }
        renderPageLink(buffer, pageNumber);
        buffer.append("</li>");
        buffer.append("\n");
    }

    /**
     * Render the page link for the given page number.
     *
     * @param buffer the buffer to render to
     * @param pageNumber the page number of the page link to render
     */
    protected void renderPageLink(HtmlStringBuffer buffer, int pageNumber) {
        if (pageNumber - 1 == getCurrentPage()) {
            buffer.append(pageNumber);
        } else {
            AbstractLink pageLink = getPageLink();
            pageLink.setLabel(String.valueOf(pageNumber));

            // Cater for zero based indexing and subtract 1 from pageNumber
            pageLink.setParameter(Table.PAGE, String.valueOf(pageNumber - 1));
            pageLink.setTitle(getGotoPageTitleMessage()
                + " " + pageNumber);
            pageLink.render(buffer);
        }
    }

    /**
     * Render a separator between page links.
     *
     * @param buffer the buffer to render to
     */
    protected void renderPageLinkSeparator(HtmlStringBuffer buffer) {
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

            AbstractLink pageLink = getPageLink();
            pageLink.setLabel(getFirstLabelMessage());
            pageLink.setParameter(Table.PAGE, pageValue);
            pageLink.setTitle(getFirstTitleMessage());
            pageLink.render(buffer);
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

            AbstractLink pageLink = getPageLink();
            pageLink.setLabel(getPreviousLabelMessage());
            pageLink.setParameter(Table.PAGE, pageValue);
            pageLink.setTitle(getPreviousTitleMessage());
            pageLink.render(buffer);

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

            AbstractLink pageLink = getPageLink();
            pageLink.setLabel(getLastLabelMessage());
            pageLink.setParameter(Table.PAGE, pageValue);
            pageLink.setTitle(getLastTitleMessage());
            pageLink.render(buffer);
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

            AbstractLink pageLink = getPageLink();
            pageLink.setLabel(getNextLabelMessage());
            pageLink.setParameter(Table.PAGE, pageValue);
            pageLink.setTitle(getNextTitleMessage());
            pageLink.render(buffer);
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
}
