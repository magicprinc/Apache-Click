package org.apache.click.util;

import com.google.errorprone.annotations.Keep;
import lombok.Getter;
import lombok.Setter;
import org.apache.click.Context;
import org.apache.click.Page;
import org.apache.click.service.ConfigService;

import java.io.Serial;

/**
 * Provides the base error handling Page. The ErrorPage handles any
 * unexpected Exceptions. When the application is not in "production" mode, the
 * ErrorPage will provide diagnostic information.
 * <p/>
 * The ErrorPage template "<span class="blue">click/error.htm</span>" can be
 * customized to your needs.
 * <p/>
 * Applications which require additional error handling logic must subclass
 * the ErrorPage. For example, to rollback a Connection if an SQLException occurred:
 *
 * <pre class="codeJava">
 * <span class="kw">package</span> com.mycorp.util;
 *
 * <span class="kw">import</span> java.sql.Connection;
 * <span class="kw">import</span> java.sql.SQLException;
 * <span class="kw">import</span> org.apache.click.util.ErrorPage;
 *
 * <span class="kw">public class</span> MyCorpErrorPage <span class="kw">extends</span> ErrorPage {
 *
 *     <span class="jd">/**
 *      * @see Page#onDestroy()
 *      * /</span>
 *     <span class="kw">public void</span> onDestroy() {
 *         Exception error = getError();
 *
 *         <span class="kw">if</span> (error <span class="kw">instanceof</span> SQLException ||
 *             error.getCause() <span class="kw">instanceof</span> SQLException) {

 *             Connection connection =
 *                 ConnectionProviderThreadLocal.getConnection();
 *
 *             <span class="kw">if</span> (connection != <span class="kw">null</span>) {
 *                 <span class="kw">try</span> {
 *                     connection.rollback();
 *                 }
 *                 <span class="kw">catch</span> (SQLException sqle) {
 *                 }
 *                 <span class="kw">finally</span> {
 *                     <span class="kw">try</span> {
 *                         connection.close();
 *                     }
 *                     <span class="kw">catch</span> (SQLException sqle) {
 *                     }
 *                 }
 *             }
 *         }
 *     }
 * } </pre>
 *
 * The ClickServlet sets the following ErrorPage properties in addition to
 * the normal Page properties:<ul>
 * <li>{@link #error} - the error causing exception</li>
 * <li>{@link #mode} - the Click application mode</li>
 * <li>{@link #pageClass} - the Page class which cause the error</li>
 * </ul>
 */
public class ErrorPage extends Page {
  @Serial private static final long serialVersionUID = 1L;

  /** The number of lines to display. */
  @Keep protected static final int NUMB_LINES = 8;

  /** The error causing exception. */
  @Getter @Setter protected Throwable error;

  /**
   * The application mode: &nbsp;
   * ["production", "profile", "development", "debug", "trace"].
   *
   * Set the application mode: <tt>["production", "profile", "development",
   * debug", "trace"]</tt>
   * <p/>
   * The application mode is added to the model by the {@link #onInit()} method.
   * This property is used to determine whether the error page template
   * should display error diagnostic information. The default "error.htm" will
   * display error diagnostic information so long as the application mode is
   * not "production".
   */
  @Getter @Setter protected ConfigService.Mode mode;

  /** The page class in error. */
  @Getter @Setter protected Class<? extends Page> pageClass;


  /**
   * This method initializes the ErrorPage, populating the model with error
   * diagnostic information.
   * <p/>
   * The following values are added to ErrorPage model for rendering by the
   * error page template:
   *
   * <ul style="margin-top: 0.5em;">
   * <li><tt>errorReport</tt> &nbsp; - &nbsp; the detailed error report
   * &lt;div&gt; element, with an id of 'errorReport'</li>
   * <li><tt>mode</tt> &nbsp; - &nbsp; the application mode</li>
   * </ul>
   *
   * @see Page#onInit()
   */
  @Override public void onInit(){
    addModel("mode", getMode());
    Context context = getContext();
    if (getError() != null) {
      ErrorReport errorReport =
          new ErrorReport(error,
              getPageClass(),
              false,
              context.getRequest(),
              context.getServletContext());

      addModel("errorReport", errorReport);
    }
  }
}