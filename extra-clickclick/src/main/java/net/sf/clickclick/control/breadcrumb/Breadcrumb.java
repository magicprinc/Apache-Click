package net.sf.clickclick.control.breadcrumb;

import lombok.val;
import org.apache.click.Context;
import org.apache.click.control.AbstractControl;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.HtmlStringBuffer;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Provides a history bar, sometimes referred to as a breadcrumb.
 * <p/>
 * The Breadcrumb records all pages a user visits and builds up a trail so one
 * can navigate back to previous pages.
 * <p/>
 * <b>Please note</b> there are different ways to implement a breadcrumb. The
 * following article outlines a different approach than what was taken here:
 * http://www.useit.com/alertbox/breadcrumbs.html.
 */
public class Breadcrumb extends AbstractControl {
  private static final long serialVersionUID = -7233634877909814929L;


  /**
   * The value under which the breadcrumb trail is stored in the HttpSession,
   * "<tt>breadcrumb</tt>".
   */
  public static final String BREADCRUMB_KEY = "breadcrumb";


  /** The maximum number of breadcrumbs to render. */
  protected int trailLength = 5;

  /** The separator between breadcrumbs, defaults to <tt>"/"</tt>. */
  protected String separator = " / ";

  /** Tracks the page paths the user visits. */
  protected transient Trail<String, String> trail;

  /** The breadcrumb label. */
  protected String label;

  /** Set of URL paths that should not be tracked. */
  protected transient Set<String> excludedPaths = new HashSet<>();

  // ----------------------------------------------------------- Constructors

  /**
   * Create a default Breadcrumb.
   */
  public Breadcrumb() {
  }

  /**
   * Create a Breadcrumb with the given name.
   *
   * @param name the name of the breadcrumb
   */
  public Breadcrumb(String name) {
    setName(name);
  }

  /**
   * Create a Breadcrumb with the given name and label.
   *
   * @param name the name of the breadcrumb
   * @param label the label of the breadcrumb
   */
  public Breadcrumb(String name, String label) {
    setName(name);
    setLabel(label);
  }

  /**
   * Create a Breadcrumb with the given name and trail length.
   *
   * @param name the name of the breadcrumb
   * @param trailLength the number of breadcrumbs to render
   */
  public Breadcrumb(String name, int trailLength) {
    this(name, null, trailLength, " / ");
  }

  /**
   * Create a Breadcrumb with the given name, label and trail length.
   *
   * @param name the name of the breadcrumb
   * @param label the label of the breadcrumb
   * @param trailLength the number of breadcrumbs to render
   */
  public Breadcrumb(String name, String label, int trailLength) {
    this(name, label, trailLength, " / ");
  }

  /**
   * Create a Breadcrumb with the given name, label, trail length and
   * separator.
   *
   * @param name the name of the breadcrumb
   * @param label the label of the breadcrumb
   * @param trailLength the number of breadcrumbs to render
   * @param separator the separator to render between breadcrumbs
   */
  public Breadcrumb(String name, String label, int trailLength, String separator) {
    setName(name);
    setLabel(label);
    setTrailLength(trailLength);
    setSeparator(separator);
  }

  // ------------------------------------------------------ Public Properties

  /**
   * Return the number of breadcrumbs to render.
   *
   * @return the number of breadcrumbs to render
   */
  public int getTrailLength() {
    return trailLength;
  }

  /**
   * Set the number of breadcrumbs to render.
   *
   * @param trailLength the number of breadcrumbs to render
   */
  public void setTrailLength(int trailLength) {
    this.trailLength = trailLength;
  }

  /**
   * Return the separator to render between breadcrumbs.
   *
   * @return the separator to render between breadcrumbs
   */
  public String getSeparator() {
    return separator;
  }

  /**
   * Set the separator to render between breadcrumbs.
   *
   * @param separator the separator to render between breadcrumbs
   */
  public void setSeparator(String separator) {
    this.separator = separator;
  }

  /**
   * Return the breadcrumb label.
   *
   * @return the breadcrumb label
   */
  public String getLabel() {
    return label;
  }

  /**
   * Set the breadcrumb label.
   *
   * @param label the breadcrumb label
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * Return the set of URL paths that should not be tracked.
   *
   * @return the set of URL paths that should not be tracked
   */
  public Set<String> getExcludedPaths() {
    return excludedPaths;
  }

  /**
   * Set the set of URL paths that should not be tracked.
   *
   * @param excludedPaths the set of URL paths that should not be tracked
   */
  public void setLabel(Set<String> excludedPaths) {
    this.excludedPaths = excludedPaths;
  }

  // --------------------------------------------------------- Public Methods

  /**
   * The onInit event handler performs the following operations:
   * <ul>
   * <li>Invokes {@link #restoreState(org.apache.click.Context)} to retrieve
   * the stored {@link Trail} instance between requests.</li>
   * <li>Stores the {@link org.apache.click.util.ClickUtils#getResourcePath(javax.servlet.http.HttpServletRequest) path}
   * of the parent Page in the {@link Trail} instance.</li>
   * </ul>
   */
  @Override public void onInit() {
    Context context = Context.getThreadLocalContext();
    restoreState(context);
    String contextPath = context.getRequest().getContextPath();
    String path = ClickUtils.getResourcePath(context.getRequest());
    addTrail(contextPath + path);
  }

  /**
   * The onDestroy event handler performs the following operations:
   * <ul>
   * <li>Invokes {@link #saveState(org.apache.click.Context)} to store the
   * {@link Trail} instance between requests.</li>
   * </ul>
   */
  @Override public void onDestroy() {
    Context context = Context.getThreadLocalContext();
    saveState(context);
  }

  /**
   * Override default implementation as no processing is necessary.
   *
   * @see org.apache.click.Control#onProcess()
   *
   * @return true to continue Page event processing or false otherwise
   */
  @Override public boolean onProcess() {
    return true;
  }

  /**
   * Add the specified path to the trail.
   *
   * @param path the path to add to the trail
   */
  public void addTrail(String path) {
    for (String excludedPath : getExcludedPaths()) {
      if (path.contains(excludedPath)) {
        return;
      }
    }

    if (getTrail().containsKey(path)) {
      collapseTrail(path);
      return;
    }
    String pageName = getDisplayLabel(path);
    getTrail().put(path, pageName);
  }

  /**
   * Remove the specified path from the trail.
   *
   * @param path the path to remove from the trail
   */
  public void removeTrail(String path) {
    getTrail().remove(path);
  }

  /**
   * Return the size of the breadcrumb.
   *
   * @return the size of the breadcrumb
   */
  public int size() {
    return getTrail().size();
  }

  /**
   * Remove all entries from the breadcrumb.
   */
  public void clear() {
    getTrail().clear();
  }

  /**
   * Return the trail of breadcrumbs as a Map.
   *
   * @return the trail of breadcrumb
   */
  public Map<String, String> getTrail() {
    if (trail == null) {
      trail = new Trail<>(this);
    }
    return trail;
  }

  /**
   * Render the breadcrumb's output to the specified buffer.
   * <p/>
   * @see org.apache.click.Control#render(org.apache.click.util.HtmlStringBuffer)
   *
   * @param buffer the specified buffer to render the control's output to
   */
  @Override public void render(HtmlStringBuffer buffer) {
    buffer.elementStart("div");
    buffer.appendAttribute("id", getId());
    buffer.closeTag();
    if (getLabel() != null) {
      buffer.append("<span>");
      buffer.append(getLabel());
      buffer.append(" - ");
      buffer.append("</span>");
    }

    // Only render when there are at least 1 trail entries
    if (getTrail().size() > 0){
      val it = getTrail().entrySet().iterator();
      while (it.hasNext()){
        val entry = it.next();

        String path = entry.getKey();
        String label = entry.getValue();
        renderPath(buffer, path, label, !it.hasNext());

        if (it.hasNext()) {
          buffer.append("<span style=\"padding:0 2px;\">");
          buffer.append(getSeparator());
          buffer.append("</span>");
        }
      }
    }
    buffer.append("</div>");
  }

  /**
   * Returns the HTML representation of this control.
   * <p/>
   * This method delegates the rendering to the method
   * {@link #render(org.apache.click.util.HtmlStringBuffer)}.
   *
   * @see Object#toString()
   *
   * @return the HTML representation of this control
   */
  public String toString() {
    HtmlStringBuffer buffer = new HtmlStringBuffer(getTrail().size() * 20);
    render(buffer);
    return buffer.toString();
  }

  // ------------------------------------------------------ Protected Methods

  /**
   * Render a breadcrumb path to the given buffer.
   *
   * @param buffer the buffer to render to
   * @param path the path to render
   * @param label the label to render for the given path
   * @param isLastEntry true if this is the last path to render, false otherwise
   */
  protected void renderPath(HtmlStringBuffer buffer, String path,
      String label, boolean isLastEntry) {

    // If its the last entry only render a string not a hyperlink.
    if (isLastEntry) {
      buffer.append(label);
      return;
    }

    buffer.elementStart("a");

    buffer.appendAttribute("href", path);

    // Append all attributes
    appendAttributes(buffer);

    buffer.closeTag();

    buffer.append(label);

    buffer.elementEnd("a");
  }

  /**
   * Return the label to display for the given path.
   *
   * @param path the path of the current page
   * @return the label to display for the given path
   */
  protected String getDisplayLabel(String path) {

    // We start off optimistic
    String pagePath = path;

    // Extract the page path
    int pageIndex = pagePath.lastIndexOf("/");
    if (pageIndex != -1) {
      pagePath = pagePath.substring(pageIndex + 1);
    }

    // Chop off the page extension eg. ".htm"
    int extensionIndex = pagePath.lastIndexOf(".");
    if (extensionIndex != -1) {
      pagePath = pagePath.substring(0, extensionIndex);
    }
    return pagePath;
  }

  /**
   * This method ensures that when a user navigates to a URL that is already
   * present in the breadcrumb trail, all entries after this path is removed.
   *
   * @param newPath the current URL path
   */
  protected void collapseTrail(String newPath) {
    // Indicates if trail entries must be removed
    boolean remove = false;
    for (var it = getTrail().keySet().iterator(); it.hasNext();){
      String path = it.next();

      if (remove) {
        // Remove the current trail entry and continue iterating
        it.remove();
        continue;
      }

      // Check if current trail path equals newPath
      if (path.equals(newPath)) {
        // Activate remove indicator to remove all remaining entries
        remove = true;
      }
    }
  }

  /**
   * Retrieves the breadcrumb {@link #getTrail() trail} from the HttpSession.
   * The trail is retrieved from the HttpSession with the key {@link #BREADCRUMB_KEY}.
   *
   * @param context the current request context
   */
  protected void restoreState(Context context) {
    val existingTrail = (Trail<String,String>) context.getSession().getAttribute(BREADCRUMB_KEY);
    if (existingTrail == null) {
      return;
    }
    this.trail = existingTrail;
    this.trail.setBreadcrumb(this);

  }

  /**
   * Stores the breadcrumb {@link #getTrail() trail} in the HttpSession.
   * The trail is stored in the HttpSession under the key {@link #BREADCRUMB_KEY}.
   *
   * @param context the current request context
   */
  protected void saveState(Context context) {
    HttpSession session = context.getRequest().getSession(false);
    if (session != null) {
      session.setAttribute(BREADCRUMB_KEY, trail);
    }
  }
}