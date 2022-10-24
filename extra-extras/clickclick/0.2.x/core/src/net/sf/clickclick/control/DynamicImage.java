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
package net.sf.clickclick.control;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.click.Context;
import org.apache.click.Page;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.commons.lang.StringUtils;

/**
 * Provides an abstract dynamic Image control: &nbsp; &lt;img src='chart?year=2009&width=200&height=200'&gt;.
 * <p/>
 * The DynamicImage control is useful for generating and displaying dynamic
 * images. When specifying a DynamicImage you have to provide the method
 * {@link #getImageData()} and return the image data as a byte array. Below is
 * an example.
 * <p/>
 *
 * <tt>DynamicImagePage.java</tt> page class:
 * <pre class="prettyprint">
 * public DynamicImagePage() {
 *
 *     DynamicImage image = new DynamicImage("dynamicImage") {
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
 *     private byte[] generateChart() throws IOException {
 *         Context context = getContext();
 *
 *         // This method returns a byte array that can be dynamically
 *         // retrieved from a database or generated on the fly.
 *         InputStream is = ...
 *
 *         return IOUtils.toByteArray(is);
 *     }
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
public abstract class DynamicImage extends Image {

    // -------------------------------------------------------------- Constants

    private static final long serialVersionUID = 1L;

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

    /** The DynamicImage request indicator "<tt>requestImage</tt>". */
    public static final String IMAGE_PARAM = "requestImage";

    // -------------------------------------------------------------- Variables

    /** The image request parameters. */
    protected Map parameters;

    /** The image type: {@link #PNG}, {@link #GIF} etc. */
    protected String imageType;

    /**
     * Indicates if the image should be rendered or not. This depends on
     * whether the {@link #IMAGE_PARAM} request attribute matches the image name.
     */
    protected boolean renderImage;

    /**
     * The target Page or URL from where the image data will be served from. If
     * no value is specified, the target defaults to the current Page.
     */
    protected String target;

    // ----------------------------------------------------------- Constructors

    /**
     * Create a DynamicImage. The {@link #imageType} will default to {@link #PNG}.
     */
    public DynamicImage() {
        this(null);
    }

    /**
     * Create a DynamicImage with the given name. The {@link #imageType} will
     * default to {@link #PNG}.
     *
     * @param name the name of the control
     */
    public DynamicImage(String name) {
        this(name, null, "png");
    }

    /**
     * Create a DynamicImage with the given name and ID attribute. The
     * {@link #imageType} will default to {@link #PNG}.
     *
     * @param name the name of the image
     * @param id the id attribute of the image
     */
    public DynamicImage(String name, String id) {
        this(name, id, "png");
    }

    /**
     * Create a DynamicImage with the given name, ID and imageType.
     *
     * @param name the name of the image
     * @param id the id attribute of the image
     * @param imageType the image type of the image
     */
    public DynamicImage(String name, String id, String imageType) {
        if (name != null) {
            setName(name);
        }
        if (id != null) {
            setId(id);
        }
        setListener(this, "onRenderImage");
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
     * Return the image request parameter value for the given name, or null if
     * the parameter value does not exist.
     *
     * @param name the name of request parameter
     * @return the link request parameter value
     */
    public String getParameter(String name) {
        if (hasParameters()) {
            return (String) getParameters().get(name);
        } else {
            return null;
        }
    }

    /**
     * Set the image parameter with the given parameter name and value.
     *
     * @param name the attribute name
     * @param value the attribute value
     * @throws IllegalArgumentException if name parameter is null
     */
    public void setParameter(String name, String value) {
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
     * Return the DynamicImage parameters Map.
     *
     * @return the DynamicImage parameters Map
     */
    public Map getParameters() {
        if (parameters == null) {
            parameters = new HashMap(1);
        }
        return parameters;
    }

    /**
     * Return true if the DynamicImage has parameters, false otherwise.
     *
     * @return true if the DynamicImage has parameters, false otherwise
     */
    public boolean hasParameters() {
        return parameters != null && !parameters.isEmpty();
    }

    /**
     * Return the target uri where the image data will be served from.
     * <p/>
     * If no value is specified, the target defaults to the current Page.
     *
     * @return the target uri where the image data will be served from
     */
    public String getTarget() {
        return target;
    }

    /**
     * Set the target uri where the image data will be served from.
     * <p/>
     * If no value is specified, the target defaults to the current Page.
     * <p/>
     * If the target begins with a <tt class="wr">"/"</tt>
     * character the target will be prefixed with the web applications
     * <tt>context path</tt>. Note if the given target is already prefixed
     * with the <tt>context path</tt>, Click won't add it a second time.
     *
     * @param target the target uri where the image data will be served from
     */
    public void setTarget(String target) {
        Context context = getContext();
        if (StringUtils.isNotBlank(target)) {
            if (target.charAt(0) == '/') {
                String contextPath = context.getRequest().getContextPath();

                // Guard against adding duplicate context path
                if (!target.startsWith(contextPath + '/')) {
                    target = contextPath + target;
                }
            }
        }
        this.target = target;
    }

    /**
     * Set the target Page class where the image data will be served from.
     * <p/>
     * If no value is specified, the target defaults to the current Page.
     *
     * @param pageClass the target Page class where the image data will be served
     * from
     */
    public void setTarget(Class pageClass) {
        String target = getContext().getPagePath(pageClass);

        // If page class maps to a jsp, convert to htm which allows ClickServlet
        // to process the target
        if (target != null && target.endsWith(".jsp")) {
            target = StringUtils.replaceOnce(target, ".jsp", ".htm");
        }
        this.target = target;
    }

    /**
     * Return the src attribute of the image.
     * <p/>
     * If no {@link #setTarget(java.lang.String) target} value was set, the
     * target <tt>uri</tt> will default to the current Page url.
     *
     * @return the src attribute
     */
    public String getSrc() {
        String uri = getTarget();
        Context context = getContext();
        if (StringUtils.isBlank(target)) {
            uri = ClickUtils.getRequestURI(context.getRequest());
        }

        HtmlStringBuffer buffer = new HtmlStringBuffer(uri.length() + getName().length() + 40);
        buffer.append(uri);
        buffer.append("?");
        buffer.append(IMAGE_PARAM);
        buffer.append("=");
        buffer.append(getName());
        if (hasParameters()) {
            String encodedValue;
            for (Iterator i = getParameters().keySet().iterator(); i.hasNext(); buffer.append(encodedValue)) {
                buffer.append("&amp;");
                String name = i.next().toString();
                Object paramValue = getParameters().get(name);
                encodedValue = ClickUtils.encodeUrl(paramValue, context);
                buffer.append(name);
                buffer.append("=");
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
    public void setSrc(String src) {
        throw new UnsupportedOperationException("DynamicImage does not support"
            + " the 'src' attribute. For static images rather use the Image"
            + " control.");
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Abstract method that must return the Image content as a byte array.
     * For example:
     *
     * <pre class="prettyprint">
     * InputStream is = getContext().getServletContext().getResourceAsStream("/assets/images/chart.png");
     * return IOUtils.toByteArray(is); </pre>
     *
     * @return the image content as a byte array
     */
    public abstract byte[] getImageData();

    /**
     * Utility method to convert an AWT {@link java.awt.image.BufferedImage}
     * to a byte array.
     *
     * @param bufferedImage the buffered image instance
     * @return the bufferedImage as a byte array
     */
    public byte[] toImageData(BufferedImage bufferedImage) {
        try {
            ByteArrayOutputStream outputStream;
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, getImageType(), outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The image {@link #setListener(java.lang.Object, java.lang.String) listener}
     * implementation.
     * <p/>
     * This method delegates to {@link #getImageData()} to return the image data
     * as a byte array.
     *
     * @return true to continue Page event processing or false otherwise
     */
    public boolean onRenderImage() {
        byte imageData[];
        boolean flag;
        Page page;
        imageData = getImageData();
        try {
            HttpServletResponse response = getContext().getResponse();
            response.setContentType("image/" + getImageType());
            java.io.OutputStream outputStream = response.getOutputStream();
            BufferedOutputStream bout = new BufferedOutputStream(outputStream);
            bout.write(imageData);
            bout.flush();
            bout.close();
            flag = true;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            page = ClickUtils.getParentPage(this);
            page.setPath(null);
        }
        return flag;
    }

    /**
     * Bind the DynamicImage request values. This method will only bind the
     * image parameters if the {@link #IMAGE_PARAM} request attribute value
     * matches the image name.
     */
    public void bindRequestValue() {
        if (getContext().isMultipartRequest()) {
            return;
        }
        renderImage = getName().equals(getContext().getRequestParameter(IMAGE_PARAM));
        if (renderImage) {
            HttpServletRequest request = getContext().getRequest();
            String name;
            String value;
            for (Enumeration paramNames = request.getParameterNames(); paramNames.hasMoreElements(); getParameters().put(name, value)) {
                name = paramNames.nextElement().toString();
                value = request.getParameter(name);
            }
        }
    }

    /**
     * @see org.apache.click.Control#onProcess()
     *
     * @return true to continue Page event processing or false otherwise
     */
    public boolean onProcess() {
        bindRequestValue();
        if (renderImage) {
            if (listener != null && listenerMethod != null) {
                return ClickUtils.invokeListener(listener, listenerMethod);
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}
