package org.apache.click;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.click.service.ConfigService;
import org.apache.click.service.LogService;
import org.apache.click.service.TemplateService;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.FlashAttribute;
import org.apache.commons.fileupload.FileItem;

import javax.annotation.Nullable;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Map;

/**
 * Provides the HTTP request context information for pages and controls.
 * A new Context object is created for each Page request. The request Context
 * object can be obtained from the thread local variable via the
 * {@link Context#getThreadLocalContext()} method.
 */
@Slf4j
public class Context {
  /** The user's session Locale key: &nbsp; <tt>locale</tt>. */
  public static final String LOCALE = "locale";

  /** The attribute key used for storing any error that occurred while Context is created. */
  static final String CONTEXT_FATAL_ERROR = "_context_fatal_error";

  /** The thread local context stack. */
  private static final ThreadLocal<Context> THREAD_LOCAL_CONTEXT_STACK = new ThreadLocal<>();


  /** The servlet context. */
  @Getter protected final ServletContext servletContext;

  /** The servlet config. */
  @Getter protected final ServletConfig servletConfig;

  /** The HTTP method is POST flag. true if the HTTP request method is "POST" */
  @Getter protected final boolean isPost;

  /** The servlet response. */
  @Getter protected final HttpServletResponse response;

  /** The click services interface. */
  @Getter final ClickServlet clickServlet;

  /** The servlet request. */
  @Getter final HttpServletRequestWrapper request;


  /**
   * Create a new request context.
   *
   * @param context the servlet context
   * @param config the servlet config
   * @param request the servlet request
   * @param response the servlet response
   * @param isPost the servlet request is a POST
   * @param clickServlet the click servlet instance
   */
  public Context (
      ServletContext context,
      ServletConfig config,
      HttpServletRequest request,
      HttpServletResponse response,
      boolean isPost,
      ClickServlet clickServlet
  ){
    this.clickServlet = clickServlet;
    this.servletContext = context;
    this.servletConfig = config;
    this.isPost = isPost;

    // CLK-312. Apply request.setCharacterEncoding before wrapping request in ClickRequestWrapper
    String charset = clickServlet.getConfigService().getCharset();
    if (charset != null){
      try {
        request.setCharacterEncoding(charset);

      } catch (UnsupportedEncodingException e){
        LogService logService = clickServlet.getConfigService().getLogService();
        logService.warn("The character encoding " + charset + " is invalid.", e);
      }
    }

    this.request = request instanceof ClickRequestWrapper r ? r
        : new ClickRequestWrapper(request, clickServlet.getConfigService().getFileUploadService());
    this.response = response;
  }//new


  /**
   * Return the thread local request context instance.
   *
   * @return the thread local request context instance.
   * @throws RuntimeException if a Context is not available on the thread.
   */
  public static Context getThreadLocalContext(){
    return THREAD_LOCAL_CONTEXT_STACK.get();
  }


  static int getContextStackSize () {
    int total = 0;
    var ctx = THREAD_LOCAL_CONTEXT_STACK.get();
    while (ctx != null){
      total++;
      ctx = ctx.prev;
    }
    return total;
  }


  /**
   * Return the user's HttpSession, creating one if necessary.
   *
   * @return the user's HttpSession, creating one if necessary.
   */
  public HttpSession getSession (){ return request.getSession();}

  /**
   * Return the page resource path from the request. For example:
   * <pre class="codeHtml">
   * <span class="blue">http://www.mycorp.com/banking/secure/login.htm</span>  ->  <span class="red">/secure/login.htm</span> </pre>
   *
   * @return the page resource path from the request
   */
  public String getResourcePath() {
    return ClickUtils.getResourcePath(request);
  }

  /**
   * Return true if the request has been forwarded. A forwarded request
   * will contain a {@link ClickServlet#CLICK_FORWARD} request attribute.
   *
   * @return true if the request has been forwarded
   */
  public boolean isForward() {
    return request.getAttribute(ClickServlet.CLICK_FORWARD) != null;
  }


  /**
   * Return true if the HTTP request method is "GET".
   *
   * @return true if the HTTP request method is "GET"
   */
  public boolean isGet() {
    return !isPost && "GET".equalsIgnoreCase(getRequest().getMethod());
  }

  /**
   * Return true is this is an Ajax request, false otherwise.
   * <p/>
   * An Ajax request is identified by the presence of the request <tt>header</tt>
   * or request <tt>parameter</tt>: "<tt>X-Requested-With</tt>".
   * "<tt>X-Requested-With</tt>" is the de-facto standard identifier used by
   * Ajax libraries.
   * <p/>
   * <b>Note:</b> incoming requests that contains a request <tt>parameter</tt>
   * "<tt>X-Requested-With</tt>" will result in this method returning true, even
   * though the request itself was not initiated through a <tt>XmlHttpRequest</tt>
   * object. This allows one to programmatically enable Ajax requests. A common
   * use case for this feature is when uploading files through an IFrame element.
   * By specifying "<tt>X-Requested-With</tt>" as a request parameter the IFrame
   * request will be handled like a normal Ajax request.
   *
   * @return true if this is an Ajax request, false otherwise
   */
  public boolean isAjaxRequest() {
    return ClickUtils.isAjaxRequest(getRequest());
  }

  /**
   * Return true if the request contains the named attribute.
   *
   * @param name the name of the request attribute
   * @return true if the request contains the named attribute
   */
  public boolean hasRequestAttribute(String name) {
    return (getRequestAttribute(name) != null);
  }

  /**
   * Return the named request attribute, or null if not defined.
   *
   * @param name the name of the request attribute
   * @return the named request attribute, or null if not defined
   */
  public Object getRequestAttribute(String name) {
    return request.getAttribute(name);
  }

  /**
   * This method will set the named object in the HTTP request.
   *
   * @param name the storage name for the object in the request
   * @param value the object to store in the request
   */
  public void setRequestAttribute(String name, Object value) {
    request.setAttribute(name, value);
  }

  /**
   * Return true if the request contains the named parameter.
   *
   * @param name the name of the request parameter
   * @return true if the request contains the named parameter
   */
  public boolean hasRequestParameter(String name) {
    if (name == null) {
      throw new IllegalArgumentException("hasRequestParameter was called"
          + " with null name argument. This is often caused when a"
          + " Control binds to a request parameter, but its name was not"
          + " set.");
    }
    return (getRequestParameter(name) != null);
  }

  /**
   * Return the named request parameter. This method supports
   * <tt>"multipart/form-data"</tt> POST requests and should be used in
   * preference to the <tt>HttpServletRequest</tt> method
   * <tt>getParameter()</tt>.
   *
   * @see org.apache.click.control.Form#onProcess()
   * @see #isMultipartRequest()
   * @see #getFileItemMap()
   *
   * @param name the name of the request parameter
   * @return the value of the request parameter.
   */
  public String getRequestParameter(String name) {
    if (name == null) {
      throw new IllegalArgumentException("getRequestParameter was called"
          + " with null name argument. This is often caused when a"
          + " Control binds to a request parameter, but its name was not set.");
    }
    return request.getParameter(name);
  }

  /**
   * Returns an array of String objects containing all of the values the given
   * request parameter has, or null if the parameter does not exist.
   *
   * @param name a <tt>String</tt> containing the name of the parameter whose
   *     value is requested
   * @return an array of <tt>String</tt> objects containing the parameter's values
   */
  public String[] getRequestParameterValues(String name) {
    if (name == null) {
      throw new IllegalArgumentException("getRequestParameter was called"
          + " with null name argument. This is often caused when a"
          + " Control binds to a request parameter, but its name was not set.");
    }
    return request.getParameterValues(name);
  }

  /**
   * Return the named session attribute, or null if not defined.
   * <p/>
   * If the session is not defined this method will return null, and a
   * session will not be created.
   * <p/>
   * This method supports {@link FlashAttribute} which when accessed are then
   * removed from the session.
   *
   * @param name the name of the session attribute
   * @return the named session attribute, or null if not defined
   */
  public Object getSessionAttribute(String name) {
    if (hasSession()) {
      Object object = getSession().getAttribute(name);

      if (object instanceof FlashAttribute flashObject) {
        object = flashObject.getValue();
        removeSessionAttribute(name);
      }

      return object;
    } else {
      return null;
    }
  }

  /**
   * This method will set the named object in the HttpSession.
   * <p/>
   * This method will create a session if one does not already exist.
   *
   * @param name the storage name for the object in the session
   * @param value the object to store in the session
   */
  public void setSessionAttribute(String name, Object value) {
    getSession().setAttribute(name, value);
  }

  /**
   * Remove the named attribute from the session. If the session does not
   * exist or the name is null, this method does nothing.
   *
   * @param name of the attribute to remove from the session
   */
  public void removeSessionAttribute(String name) {
    if (hasSession() && name != null) {
      getSession().removeAttribute(name);
    }
  }

  /**
   * Return true if there is a session and it contains the named attribute.
   *
   * @param name the name of the attribute
   * @return true if the session contains the named attribute
   */
  public boolean hasSessionAttribute(String name) {
    return getSessionAttribute(name) != null;
  }

  /**
   * Return true if a HttpSession exists, or false otherwise.
   *
   * @return true if a HttpSession exists, or false otherwise
   */
  public boolean hasSession (){
    return request.getSession(false) != null;
  }

  /**
   * This method will set the named object as a flash HttpSession object.
   * <p/>
   * The flash object will exist in the session until it is accessed once,
   * and then removed. Flash objects are typically used to display a message
   * once.
   *
   * @param name the storage name for the object in the session
   * @param value the object to store in the session
   */
  public void setFlashAttribute(String name, Object value) {
    getSession().setAttribute(name, new FlashAttribute(value));
  }

  /**
   * Return the cookie for the given name or null if not found.
   *
   * @param name the name of the cookie
   * @return the cookie for the given name or null if not found
   */
  public Cookie getCookie(String name) {
    return ClickUtils.getCookie(getRequest(), name);
  }

  /**
   * Return the cookie value for the given name or null if not found.
   *
   * @param name the name of the cookie
   * @return the cookie value for the given name or null if not found
   */
  public String getCookieValue(String name) {
    return ClickUtils.getCookieValue(getRequest(), name);
  }

  /**
   * Sets the given cookie value in the servlet response with the path "/".
   * <p/>
   * @see ClickUtils#setCookie(HttpServletRequest, HttpServletResponse, String, String, int, String)
   *
   * @param name the cookie name
   * @param value the cookie value
   * @param maxAge the maximum age of the cookie in seconds. A negative
   * value will expire the cookie at the end of the session, while 0 will delete
   * the cookie.
   * @return the Cookie object created and set in the response
   */
  public Cookie setCookie(String name, String value, int maxAge) {
    return ClickUtils.setCookie(getRequest(),
        getResponse(),
        name,
        value,
        maxAge,
        "/");
  }

  /**
   * Invalidate the specified cookie and delete it from the response object.
   * Deletes only cookies mapped against the root "/" path.
   *
   * @see ClickUtils#invalidateCookie(HttpServletRequest, HttpServletResponse, String)
   *
   * @param name the name of the cookie you want to delete.
   */
  public void invalidateCookie(String name) {
    ClickUtils.invalidateCookie(getRequest(), getResponse(), name);
  }

  /**
   * Return a new Page instance for the given path.
   * <p/>
   * This method can be used to create a target page for the
   * {@link Page#setForward(Page)}, for example:
   *
   * <pre class="codeJava">
   * UserEdit userEdit = (UserEdit) getContext().createPage(<span class="st">"/user-edit.htm"</span>);
   * userEdit.setUser(user);
   *
   * setForward(userEdit); </pre>
   *
   * The given page path must start with a <tt>'/'</tt>.
   *
   * @param path the Page path as configured in the click.xml file
   * @return a new Page object
   * @throws IllegalArgumentException if the Page is not found
   */
  public <T extends Page> T createPage (@NonNull String path){
    if (!path.startsWith("/") ){
      throw new IllegalArgumentException("Page path must start with a '/', but "+path);// incl non-empty
    }
    return clickServlet.createPage(path, request);
  }

  /**
   * Return a new Page instance for the given class.
   * <p/>
   * This method can be used to create a target page for the
   * {@link Page#setForward(Page)}, for example:
   *
   * <pre class="codeJava">
   * UserEdit userEdit = (UserEdit) getContext().createPage(UserEdit.<span class="kw">class</span>);
   * userEdit.setUser(user);
   *
   * setForward(userEdit); </pre>
   *
   * @param pageClass the Page class as configured in the click.xml file
   * @return a new Page object
   * @throws IllegalArgumentException if the Page is not found, or is not
   * configured with a unique path
   */
  public <T extends Page> T createPage(Class<T> pageClass) {
    return clickServlet.createPage(pageClass, request);
  }

  /**
   * Return the path for the given page Class.
   *
   * @param pageClass the class of the Page to lookup the path for
   * @return the path for the given page Class
   * @throws IllegalArgumentException if the Page Class is not configured
   * with a unique path
   */
  public String getPagePath(Class<? extends Page> pageClass) {
    return clickServlet.getConfigService().getPagePath(pageClass);
  }

  /**
   * Return the page <tt>Class</tt> for the given path.
   *
   * @param path the page path
   * @return the page class for the given path
   * @throws IllegalArgumentException if the Page Class for the path is not
   * found
   */
  public Class<? extends Page> getPageClass(String path) {
    return clickServlet.getConfigService().getPageClass(path);
  }

  /**
   * Return the Click application charset or ISO-8859-1 if not is defined.
   * <p/>
   * The charset is defined in click.xml through the charset attribute
   * on the click-app element.
   *
   * <pre class="codeConfig">
   * &lt;?xml version="1.0" encoding="UTF-8" standalone="yes"?&gt;
   * &lt;click-app <span class="blue">charset</span>="<span class="red">UTF-8</span>"&gt;
   *    ..
   * &lt;/click-app&gt; </pre>
   *
   * @return the application charset or ISO-8859-1 if not defined
   */
  public String getCharset() {
    String charset = clickServlet.getConfigService().getCharset();
    if (charset == null) {
      charset = "UTF-8";// "ISO-8859-1";
    }
    return charset;
  }

  /**
   * Return a new messages map for the given baseClass (a page or control)
   * and the given global resource bundle name.
   *
   * @param baseClass the target class
   * @param globalResource the global resource bundle name
   * @return a new messages map with the messages for the target.
   */
  public Map<String,String> createMessagesMap (@NonNull Class<?> baseClass, String globalResource){
    return clickServlet.getConfigService().createMessagesMap(baseClass, globalResource, getLocale());
  }

  /**
   * Returns a map of <tt>FileItem arrays</tt> keyed on request parameter
   * name for "multipart" POST requests (file uploads). Thus each map entry
   * will consist of one or more <tt>FileItem</tt> objects.
   *
   * @return map of <tt>FileItem arrays</tt> keyed on request parameter name
   * for "multipart" POST requests
   */
  public Map<String, FileItem[]> getFileItemMap() {
    return findClickRequestWrapper(request).getFileItemMap();
  }

  /**
   * Returns the value of a request parameter as a FileItem, for
   * "multipart" POST requests (file uploads), or null if the parameter
   * is not found.
   * <p/>
   * If there were multivalued parameters in the request (ie two or more
   * file upload fields with the same name), the first fileItem
   * in the array is returned.
   *
   * @param name the name of the parameter of the fileItem to retrieve
   *
   * @return the fileItem for the specified name
   */
  public FileItem getFileItem(String name) {
    FileItem[] value = findClickRequestWrapper(request).getFileItemMap().get(name);

    if (value != null && value.length > 0){
      return value[0];
    }
    return null;
  }

  /**
   * Return the users Locale.
   * <p/>
   * If the users Locale is stored in their session this will be returned.
   * Else if the click-app configuration defines a default Locale this
   * value will be returned, otherwise the request's Locale will be returned.
   * <p/>
   * To override the default request Locale set the users Locale using the
   * {@link #setLocale(Locale)} method.
   * <p/>
   * Pages and Controls obtain the users Locale using this method.
   *
   * @return the users Locale in the session, or if null the request Locale
   */
  public Locale getLocale() {
    Locale locale = (Locale) getSessionAttribute(LOCALE);
    if (locale != null){ return locale;}

    locale = clickServlet.getConfigService().getLocale();
    if (locale != null){ return locale;}

    locale = getRequest().getLocale();
    if (locale != null){ return locale;}

    return Locale.getDefault();
  }

  /**
   * This method stores the given Locale in the users session. If the given
   * Locale is null, the "locale" attribute will be removed from the session.
   * <p/>
   * The Locale object is stored in the session using the {@link #LOCALE}
   * key.
   *
   * @param locale the Locale to store in the users session using the key
   * "locale"
   */
  public void setLocale (@Nullable Locale locale) {
    if (locale == null && hasSession()) {
      getSession().removeAttribute(LOCALE);
    } else {
      setSessionAttribute(LOCALE, locale);
    }
  }

  /**
   * Return true if the request is a multi-part content type POST request.
   *
   * @return true if the request is a multi-part content type POST request
   */
  public boolean isMultipartRequest() {
    return ClickUtils.isMultipartRequest(request);
  }

  /**
   * Return a rendered Velocity template and model for the given
   * class and model data.
   * <p/>
   * This method will merge the class <tt>.htm</tt> Velocity template and
   * model data using the applications Velocity Engine.
   * <p/>
   * An example of the class template resolution is provided below:
   * <pre class="codeConfig">
   * <span class="cm">// Full class name</span>
   * com.mycorp.control.CustomTextField
   *
   * <span class="cm">// Template path name</span>
   * /com/mycorp/control/CustomTextField.htm </pre>
   *
   * Example method usage:
   * <pre class="codeJava">
   * <span class="kw">public String</span> toString() {
   *     Map model = getModel();
   *     <span class="kw">return</span> getContext().renderTemplate(getClass(), model);
   * } </pre>
   *
   * @param templateClass the class to resolve the template for
   * @param model the model data to merge with the template
   * @return rendered Velocity template merged with the model data
   * @throws RuntimeException if an error occurs
   */
  public String renderTemplate (@NonNull Class<?> templateClass, Map<String,Object> model){
    String templatePath = templateClass.getName();
    templatePath = '/'+ templatePath.replace('.', '/') +".htm";
    return renderTemplate(templatePath, model);
  }

  /**
   * Return a rendered Velocity template and model data.
   * <p/>
   * Example method usage:
   * <pre class="codeJava">
   * <span class="kw">public String</span> toString() {
   *     Map model = getModel();
   *     <span class="kw">return</span> getContext().renderTemplate(<span class="st">"/custom-table.htm"</span>, model);
   * } </pre>
   *
   * @param templatePath the path of the Velocity template to render
   * @param model the model data to merge with the template
   * @return rendered Velocity template merged with the model data
   * @throws RuntimeException if an error occurs
   */
  public String renderTemplate (@NonNull String templatePath, @NonNull Map<String,Object> model) {
    val stringWriter = new StringWriter(1024);
    TemplateService templateService = clickServlet.getConfigService().getTemplateService();
    try {
      templateService.renderTemplate(templatePath, model, stringWriter);
    } catch (Exception e){
			log.error("Error occurred rendering template: {} @ {}\n", templatePath, model, e);
      throw new IllegalStateException(e);
    }
    return stringWriter.toString();
  }


  /**
   * Adds the specified Context on top of the Context stack.
   *
   * @param context a context instance
   */
  static void pushThreadLocalContext (@NonNull Context context){
    var ctx = THREAD_LOCAL_CONTEXT_STACK.get();
    if (ctx == context)
      return;

    context.prev = ctx;
    THREAD_LOCAL_CONTEXT_STACK.set(context);
  }

  /**
   * Remove and return the context instance on top of the context stack.
   */
  static void clearThreadLocalContext (){
    var ctx = THREAD_LOCAL_CONTEXT_STACK.get();
    if (ctx == null || ctx.prev == null)
      THREAD_LOCAL_CONTEXT_STACK.remove();
    else
      THREAD_LOCAL_CONTEXT_STACK.set(ctx.prev);
  }

  volatile Context prev;

  /**
   * Find and return the ClickRequestWrapper that is wrapped by the specified
   * request.
   *
   * @param request the servlet request that wraps the ClickRequestWrapper
   * @return the wrapped servlet request
   */
  static ClickRequestWrapper findClickRequestWrapper(ServletRequest request) {
    while (!( request instanceof ClickRequestWrapper ) && request instanceof HttpServletRequestWrapper){
      request = ((HttpServletRequestWrapper) request).getRequest();
    }

    if (request instanceof ClickRequestWrapper){
      return (ClickRequestWrapper) request;
    } else {
      throw new IllegalStateException("Click request is not present");
    }
  }

  public ConfigService getConfigService () {
    return clickServlet.getConfigService();
  }

}