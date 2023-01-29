package org.apache.click.control;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.click.ActionListener;
import org.apache.click.Context;
import org.apache.click.Page;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.commons.lang.StringUtils;

import java.io.Serial;

/**
 * Provides a Page Link control: &nbsp; &lt;a href="" &gt;&lt;/a&gt;.
 *
 * <table class='htmlHeader' cellspacing='6'>
 * <tr><td>
 * <a href='' style='{text-decoration:underline;}' title='PageLink Control'>Page Link</a>
 * </td></tr>
 * </table>
 *
 * The <tt>PageLink</tt> control is used to create links to other pages in
 * your application.
 *
 * See also the W3C HTML reference:
 * <a class="external" target="_blank" title="W3C HTML 4.01 Specification"
 *    href="http://www.w3.org/TR/html401/struct/links.html#h-12.2">A Links</a>
 *
 * @see org.apache.click.control.AbstractLink
 * @see org.apache.click.control.ActionLink
 */
public class PageLink extends AbstractLink {
  @Serial private static final long serialVersionUID = -8328035709892349227L;

  /** The target page class.
   Set the target Page class. The page link href URL attribute will be  to the path of the target page.*/
  @Getter @Setter protected Class<? extends Page> pageClass;


  /**
   * Create an PageLink for the given name.
   *
   * @param name the page link name
   * @throws IllegalArgumentException if the name is null
   */
  public PageLink (String name){
    setName(name);
  }//new

  /**
   * Create an PageLink for the given name and target Page class.
   *
   * @param name the page link name
   * @param targetPage the target page class
   * @throws IllegalArgumentException if the name is null
   */
  public PageLink(String name, @NonNull Class<? extends Page> targetPage) {
    setName(name);
    pageClass = targetPage;
  }

  /**
   * Create an PageLink for the given name, label and target Page class.
   *
   * @param name the page link name
   * @param label the page link label
   * @param targetPage the target page class
   * @throws IllegalArgumentException if the name is null
   */
  public PageLink(String name, String label, @NonNull Class<? extends Page> targetPage) {
    setName(name);
    setLabel(label);
    pageClass = targetPage;
  }

  /**
   * Create an PageLink for the given target Page class.
   *
   * @param targetPage the target page class
   * @throws IllegalArgumentException if the name is null
   */
  public PageLink (@NonNull Class<? extends Page> targetPage) {
    pageClass = targetPage;
  }

  /**
   * Create an PageLink with no name defined.
   * <p/>
   * <b>Please note</b> the control's name and target pageClass must be
   * defined before it is valid.
   */
  public PageLink() {}


  /**
   * Return the PageLink anchor &lt;a&gt; tag href attribute.
   * This method will encode the URL with the session ID
   * if required using <tt>HttpServletResponse.encodeURL()</tt>.
   *
   * @return the PageLink HTML href attribute
   */
  @Override
  public String getHref() {
    if (getPageClass() == null) {
      throw new IllegalStateException("target pageClass is not defined");
    }

    Context context = Context.getThreadLocalContext();
    HtmlStringBuffer buffer = new HtmlStringBuffer();

    buffer.append(context.getRequest().getContextPath());

    String pagePath = context.getPagePath(getPageClass());

    if (pagePath != null && pagePath.endsWith(".jsp")) {
      pagePath = StringUtils.replace(pagePath, ".jsp", ".htm");
    }

    buffer.append(pagePath);

    if (hasParameters()) {
      buffer.append("?");

      renderParameters(buffer, getParameters(), context);
    }

    return context.getResponse().encodeURL(buffer.toString());
  }

  /**
   * This method does nothing.
   *
   * @see org.apache.click.control.AbstractControl#setActionListener(org.apache.click.ActionListener)
   */
  @Override public void setActionListener(ActionListener listener) {}


  /**
   * This method will return true.
   *
   * @see org.apache.click.Control#onProcess()
   */
  @Override public boolean onProcess (){ return true;}
}