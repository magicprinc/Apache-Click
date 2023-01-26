package org.apache.click.extras.control;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.click.ActionListener;
import org.apache.click.Context;
import org.apache.click.control.AbstractLink;
import org.apache.click.util.HtmlStringBuffer;

import java.io.Serial;

/**
 * Provides an External Link control: &nbsp; &lt;a href="" &gt;&lt;/a&gt;.
 *
 * <table class='htmlHeader' cellspacing='6'>
 * <tr><td>
 * <a href='' title='External Control'>External Link</a>
 * </td></tr>
 * </table>
 *
 * The <tt>ExternalLink</tt> control is used to create links to external pages
 * and resources.
 *
 * See also the W3C HTML reference:
 * <a class="external" target="_blank" title="W3C HTML 4.01 Specification"
 *    href="http://www.w3.org/TR/html401/struct/links.html#h-12.2">A Links</a>
 */
public class ExternalLink extends AbstractLink {
  @Serial private static final long serialVersionUID = 3746134117554034349L;

  /** The target path.
   Set the link href target path */
  @Getter @Setter protected String targetPath;


  /**
   * Create an ExternalLink for the given name.
   *
   * @param name the page link name
   * @throws IllegalArgumentException if the name is null
   */
  public ExternalLink(String name) {
    setName(name);
  }

  /**
   * Create an ExternalLink for the given name and target Page class.
   *
   * @param name the page link name
   * @param targetPath the href target path
   * @throws IllegalArgumentException if the name is null
   */
  public ExternalLink (String name, @NonNull String targetPath) {
    setName(name);
    this.targetPath = targetPath;
  }

  /**
   * Create an ExternalLink with no name defined.
   * <p/>
   * <b>Please note</b> the control's name and target path must be
   * defined before it is valid.
   */
  public ExternalLink() {}


  /**
   * Return the ExternalLink anchor &lt;a&gt; tag href attribute.
   * This method will encode the URL with the session ID
   * if required using <tt>HttpServletResponse.encodeURL()</tt>.
   *
   * @return the ExternalLink HTML href attribute
   */
  @Override public String getHref() {
    if (getTargetPath() == null) {
      throw new IllegalStateException("targetPath is not defined");
    }

    HtmlStringBuffer buffer = new HtmlStringBuffer();

    buffer.append(getTargetPath());

    if (hasParameters()) {
      buffer.append("?");

      renderParameters(buffer, getParameters(), Context.getThreadLocalContext());
    }

    return buffer.toString();
  }

  /**
   * This method does nothing.
   *
   * @see org.apache.click.control.AbstractControl#setActionListener(org.apache.click.ActionListener)
   *
   * @param listener the listener to invoke
   */
  @Override public void setActionListener(ActionListener listener) {}

  /**
   * This method will return true.
   *
   * @see org.apache.click.Control#onProcess()
   *
   * @return true
   */
  @Override public boolean onProcess (){ return true;}
}