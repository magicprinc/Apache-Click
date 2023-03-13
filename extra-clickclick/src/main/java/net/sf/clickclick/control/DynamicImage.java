package net.sf.clickclick.control;

import org.apache.click.Context;
import org.apache.click.Page;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Provides a dynamic Image control: &nbsp; &lt;img src='chart.htm?pageAction=renderChart&year=2009&width=200&height=200'&gt;.
 * <p/>
 * The DynamicImage control is useful for generating and displaying dynamic
 * images. When specifying a DynamicImage you have to specify a page action
 * method that will be called from the browser. The page action method must
 * return an ActionResult containing the image data. Below is an example.
 * <p/>
 *
 * <tt>DynamicImagePage.java</tt> page class:
 * <pre class="prettyprint">
 * public DynamicImagePage() {
 *
 *     // renderChart is the name of the page method to invoke that will stream
 *     // back the image data
 *     DynamicImage image = new DynamicImage("dynamicImage", "renderChart") {
 *         public byte[] getImageData() {
 *             InputStream is = null;
 *
 *             try {
 *                 // Generate an image of a chart based on specific input parameters
 *                 return generateChart();
 *
 *                 } catch (IOException e) {
 *                     throw new RuntimeException(e);
 *                 } finally {
 *                     ClickUtils.close(is);
 *                 }
 *             }
 *        });
 *
 *        addControl(image);
 *     }
 *
 *     // The Page Action method that returns an ActionResult containing the image data
 *     public ActionResult renderChart() {
 *         ActionResult actionResult = new ActionResult();
 *
 *         // Set content type to a "png" image
 *         actionResult.setContentType(ClickUtils.getMimeType(".png"));
 *
 *         // Set the actionResult bytes to image data
 *         byte[] imageData = generateChart();
 *         actionResult.setBytes(imageData);
 *         return actionResult;
 *     }
 *
 *     private byte[] generateChart() {
 *        // This method returns a byte array that can be dynamically retrieved
 *        // from a database or generated on the fly.
 *
 *        try {
 *            InputStream is = getContext().getServletContext().getResourceAsStream("/assets/images/chart.png");
 *            return IOUtils.toByteArray(is);
 *
 *        } catch (IOException ioe) {
 *            throw new RuntimeException(ioe);
 *        }
 *    }
 * } </pre>
 *
 * Below is the <tt>dynamic-image.htm</tt> template:
 *
 * <pre class="prettyprint">
 * $dynamicImage </pre>
 *
 * See also W3C HTML reference
 * <a class="external" target="_blank" title="W3C HTML 4.01 Specification"
 *    href="http://www.w3.org/TR/html401/struct/objects.html#edef-IMG">IMG</a>
 *
 * @see DynamicImage
 */
public class DynamicImage extends Image {
  private static final long serialVersionUID = -9169134248378054470L;


  /** The PNG constant "<tt>png</tt>". */
  public static final String PNG = "png";

  /** The JPEG constant "<tt>jpeg</tt>". */
  public static final String JPEG = "jpeg";

  /** The GIF constant "<tt>gif</tt>". */
  public static final String GIF = "gif";

  /** The TIFF constant "<tt>tiff</tt>". */
  public static final String TIFF = "tiff";

  /** The BMP constant "<tt>bmp</tt>". */
  public static final String BMP = "bmp";

  /** The DynamicImage request indicator "<tt>imageAction</tt>". */
  public static final String IMAGE_ACTION_PARAM = "imageAction";

  // -------------------------------------------------------------- Variables

  /** The image request parameters. */
  protected Map<String, Object> parameters;

  /** The image type: {@link #PNG}, {@link #GIF} etc. */
  protected String imageType;

  /** The page action method that will serve the image data from. */
  protected String pageAction;

  /**
   * The target Page class that will serve the image data from. If
   * no value is specified, the target defaults to the current Page.
   */
  protected Class pageClass;

  // ----------------------------------------------------------- Constructors

  /**
   * Create a DynamicImage. The {@link #imageType} will default to {@link #PNG}.
   */
  public DynamicImage() {
    this(null, null);
  }

  /**
   * Create a DynamicImage with the given name. The {@link #imageType} will
   * default to {@link #PNG}.
   *
   * @param name the name of the control
   */
  public DynamicImage(String name, String pageAction) {
    this(name, pageAction, "png");
  }

  /**
   * Create a DynamicImage with the given name, ID and imageType.
   *
   * @param name the name of the image
   * @param id the id attribute of the image
   * @param imageType the image type of the image
   */
  public DynamicImage(String name, String pageAction, String imageType) {
    if (name != null) {
      setName(name);
    }
    setPageAction(pageAction);
    setImageType(imageType);
  }

  // ------------------------------------------------------ Public Properties

  /**
   * Return the image type of the image, {@link #PNG}, {@link #GIF} etc.
   *
   * @return the image type of the image
   */
  public String getImageType() {
    return imageType;
  }

  /**
   * Set the image type of the image, {@link #PNG}, {@link #GIF} etc.
   *
   * @param imageType the image type of the image
   */
  public void setImageType(String imageType) {
    this.imageType = imageType;
  }

  /**
   * Set the image parameter with the given parameter name and value.
   *
   * @param name the attribute name
   * @param value the attribute value
   * @throws IllegalArgumentException if name parameter is null
   */
  public void setParameter(String name, Object value) {
    if (name == null) {
      throw new IllegalArgumentException("Null name parameter");
    }
    if (value != null) {
      getParameters().put(name, value);
    } else {
      getParameters().remove(name);
    }
  }

  /**
   * Return the target uri where the image data will be served from.
   * <p/>
   * If no value is specified, the target defaults to the current Page.
   *
   * @return the target uri where the image data will be served from
   */
  public String getPageAction() {
    return pageAction;
  }

  /**
   * Set the page action that will will serve the image data from.
   *
   * @param target the target uri where the image data will be served from
   */
  public void setPageAction(String pageAction) {
    this.pageAction = pageAction;
  }

  /**
   * Set the target Page class where the image data will be served from.
   * <p/>
   * If no value is specified, the target defaults to the current Page.
   *
   * @param pageClass the target Page class where the image data will be served
   * from
   */
  public void setPageClass(Class pageClass) {
    this.pageClass = pageClass;
  }

  /**
   * Return the target Page class where the image data will be served from.
   * <p/>
   * If no value is specified, the target defaults to the current Page.
   *
   * @return the target Page class where the image data will be served from
   */
  public Class<? extends Page> getPageClass() {
    return this.pageClass;
  }

  /**
   * Return the src attribute of the image.
   * <p/>
   * If no {@link #setTarget(java.lang.String) target} value was set, the
   * target <tt>uri</tt> will default to the current Page url.
   *
   * @return the src attribute
   */
  @Override
  public String getSrc() {
    String pageAction = getPageAction();
    if (StringUtils.isBlank(pageAction)) {
      throw new IllegalStateException("PageAction must be defined");
    }

    String pagePath;

    Context context = Context.getThreadLocalContext();
    Class pageClass = getPageClass();
    if (pageClass == null) {
      pagePath = ClickUtils.getRequestURI(context.getRequest());
    } else {
      pagePath = context.getPagePath(pageClass);
    }

    // If page class maps to a jsp, convert to htm which allows ClickServlet
    // to process the target
    if (pagePath != null && pagePath.endsWith(".jsp")) {
      pagePath = pagePath.replace(".jsp", ".htm");
    }

    HtmlStringBuffer buffer = new HtmlStringBuffer(pagePath.length() + getName().length() + 40);
    buffer.append(pagePath);
    buffer.append("?");
    buffer.append(Page.PAGE_ACTION);
    buffer.append("=");
    buffer.append(pageAction);
    if (hasParameters()) {

      for (Entry<String, Object> entry : getParameters().entrySet()) {
        buffer.append("&amp;");
        buffer.append(entry.getKey());
        buffer.append("=");
        buffer.append(ClickUtils.encodeUrl(entry.getValue(), context));
      }
    }
    return context.getResponse().encodeURL(buffer.toString());
  }

  /**
   * The src attribute is generated and cannot be set directly. You can however
   * set the src attribute's {@link #setTarget(java.lang.String) target request URI}
   * and {@link #setParameter(java.lang.String, java.lang.String) parameters}.
   *
   * @param src the src attribute of the image
   * @throws UnsupportedOperationException if invoked
   */
  @Override
  public void setSrc(String src) {
    throw new UnsupportedOperationException("DynamicImage does not support"
        + " the 'src' attribute. For static images rather use the Image"
        + " control.");
  }

  // Private Methods --------------------------------------------------------

  /**
   * Return the DynamicImage parameters Map.
   *
   * @return the DynamicImage parameters Map
   */
  private Map<String, Object> getParameters() {
    if (parameters == null) {
      parameters = new HashMap<>(1);
    }
    return parameters;
  }

  /**
   * Return true if the DynamicImage has parameters, false otherwise.
   *
   * @return true if the DynamicImage has parameters, false otherwise
   */
  private boolean hasParameters() {
    return parameters != null && !parameters.isEmpty();
  }
}