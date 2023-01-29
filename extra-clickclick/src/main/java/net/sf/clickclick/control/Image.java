package net.sf.clickclick.control;

import org.apache.click.Context;
import org.apache.click.control.AbstractControl;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.commons.lang.StringUtils;

import java.io.Serial;

/**
 * Provides an Image control: &nbsp; &lt;img src='edit.gif'&gt;.
 * <p/>
 * The Image control is useful for displaying static images. Below is an example.
 * <p/>
 *
 * <tt>Image.java</tt> page class:
 * <pre class="prettyprint">
 * public ImagePage() {
 *
 *     addControl(new Image("image", "/assets/images/penguin.png"));
 *
 * } </pre>
 *
 * Below is the <tt>image.htm</tt> template:
 *
 * <pre class="prettyprint">
 * $image </pre>
 *
 * See also W3C HTML reference
 * <a class="external" target="_blank" title="W3C HTML 4.01 Specification"
 *    href="http://www.w3.org/TR/html401/struct/objects.html#edef-IMG">IMG</a>
 *
 * @see DynamicImage
 */
public class Image extends AbstractControl {
  @Serial private static final long serialVersionUID = -7207028082322068391L;

  /** The Image src attribute. */
  protected String src;

  // ----------------------------------------------------------- Constructors

  /**
   * Create an Image.
   */
  public Image() {
  }

  /**
   * Create an Image with the given name.
   *
   * @param name the name of the control
   */
  public Image(String name) {
    if (name != null) {
      setName(name);
    }
  }

  /**
   * Create an Image with the given name and src attribute.
   * <p/>
   * If the src value begins with a <tt class="wr">"/"</tt>
   * character the src attribute will be prefixed with the web applications
   * <tt>context path</tt>. Note if the given src value is already prefixed
   * with the <tt>context path</tt>, Click won't add it a second time.
   *
   * @param name the name of the image
   * @param src the src attribute of the image
   */
  public Image(String name, String src) {
    if (name != null) {
      setName(name);
    }
    if (src != null) {
      setSrc(src);
    }
  }

  /**
   * Create an Image with the given name, src and ID attributes.
   *
   * @param name the name of the image
   * @param src the src attribute of the image
   * @param id the id attribute of the image
   */
  public Image(String name, String src, String id) {
    this(name, src);
    if (id != null) {
      setId(id);
    }
  }

  // ------------------------------------------------------ Public Properties

  /**
   * Return the image html tag: <tt>img</tt>.
   *
   * @see org.apache.click.control.AbstractControl#getTag()
   *
   * @return this controls html tag
   */
  public String getTag() {
    return "img";
  }

  /**
   * Return the src attribute of the image.
   *
   * @return the src attribute
   */
  public String getSrc() {
    return src;
  }

  /**
   * Set the src attribute of the image.
   * <p/>
   * If the src value begins with a <tt class="wr">"/"</tt>
   * character the src attribute will be prefixed with the web applications
   * <tt>context path</tt>. Note if the given src value is already prefixed
   * with the <tt>context path</tt>, Click won't add it a second time.
   *
   * @param src the src attribute of the image
   */
  public void setSrc(String src) {
    Context context = Context.getThreadLocalContext();
    if (StringUtils.isNotBlank(src)) {
      if (src.charAt(0) == '/') {
        String contextPath = context.getRequest().getContextPath();

        // Guard against adding duplicate context path
        if (!src.startsWith(contextPath + '/')) {
          src = contextPath + src;
        }
      }
    }
    this.src = src;
  }

  // --------------------------------------------------------- Public Methods

  /**
   * Render the HTML representation of the Image.
   *
   * @see #toString()
   *
   * @param buffer the specified buffer to render the control's output to
   */
  @Override public void render(HtmlStringBuffer buffer) {
    buffer.elementStart(getTag());
    buffer.appendAttribute("name", getName());
    buffer.appendAttribute("id", getId());
    buffer.appendAttribute("src", getSrc());
    appendAttributes(buffer);
    buffer.closeTag();
    buffer.elementEnd(getTag());
  }
}