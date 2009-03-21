/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.click;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.click.util.Format;
import org.apache.click.util.MessagesMap;
import org.apache.click.util.PageImports;

import org.apache.commons.lang.StringUtils;

/**
 * Provides the Page request event handler class.
 * <p/>
 * The Page class plays a central role in Click applications defining how the
 * application's pages are processed and rendered. All application pages
 * must extend the base Page class, and provide a no arguments constructor.
 *
 * <h4>Page Execution Sequence</h4>
 *
 * The default Page execution path for a GET request is:
 * <ol>
 * <li class="spaced">
 *   no-args constructor invoked to create a new Page instance.
 *   At this point no dependencies have been injected into the Page, and any
 *   request information is not available. You should put any "static"
 *   page initialization code, which doesn't depend upon request information,
 *   in the constructor. This will enable subclasses to have this code
 *   automatically initialized when they are created.
 * </li>
 * <li class="spaced">
 *   {@link #format} property is set
 * </li>
 * <li class="spaced">
 *   {@link #headers} property is set
 * </li>
 * <li class="spaced">
 *   {@link #path} property is set
 * </li>
 * <li class="spaced">
 *   {@link #onSecurityCheck()} method called to check whether the page should
 *   be processed. This method should return true if the Page should continue
 *   to be processed, or false otherwise.
 * </li>
 * <li class="spaced">
 *   {@link #onInit()} method called to complete the initialization of the page
 *   after all the dependencies have been set. This is where you should put
 *   any "dynamic" page initialization code which depends upon the request or any
 *   other dependencies.
 *   <p/>
 *   Form and field controls must be fully initialized by the time this method
 *   has completed.
 * </li>
 * <li class="spaced">
 *   ClickServlet processes all the page {@link #controls}
 *   calling their {@link Control#onProcess()} method. If any of these
 *   controls return false, continued control and page processing will be aborted.
 * </li>
 * <li class="spaced">
 *   {@link #onGet()} method called for any additional GET related processing.
 *   <p/>
 *   Form and field controls should <b>NOT</b> be created or initialized at this
 *   point as the control processing stage has already been completed.
 * </li>
 * <li class="spaced">
 *   {@link #onRender()} method called for any pre-render processing. This
 *   method is often use to perform database queries to load information for
 *   rendering tables.
 *   <p/>
 *   Form and field controls should <b>NOT</b> be created or initialized at this
 *   point as the control processing stage has already been completed.
 * </li>
 * <li class="spaced">
 *   ClickServlet renders the page merging the {@link #model} with the
 *   Velocity template defined by the {@link #getTemplate()} property.
 * </li>
 * <li class="spaced">
 *   {@link #onDestroy()} method called to clean up any resources. This method
 *   is guaranteed to be called, even if an exception occurs. You can use
 *   this method to close resources like database connections or Hibernate
 *   sessions.
 * </li>
 * </ol>
 *
 * For POST requests the default execution path is identical, except the
 * {@link #onPost()} method is called instead of {@link #onGet()}. The POST
 * request page execution sequence is illustrated below:
 * <p/>
 * <img src="post-sequence-diagram.png"/>
 *
 * <p/>
 * A good way to see the page event execution order is to view the log when
 * the application mode is set to <tt>trace</tt>:
 *
 * <pre class="codeConfig" style="padding:1em;background-color:#f0f0f0;">
 * [Click] [debug] GET http://localhost:8080/quickstart/home.htm
 * [Click] [trace]    invoked: HomePage.&lt;&lt;init&gt;&gt;
 * [Click] [trace]    invoked: HomePage.onSecurityCheck() : true
 * [Click] [trace]    invoked: HomePage.onInit()
 * [Click] [trace]    invoked: HomePage.onGet()
 * [Click] [trace]    invoked: HomePage.onRender()
 * [Click] [info ]    renderTemplate: /home.htm - 6 ms
 * [Click] [trace]    invoked: HomePage.onDestroy()
 * [Click] [info ] handleRequest:  /home.htm - 24 ms  </pre>
 *
 * <h4>Rendering Pages</h4>
 *
 * When a Velocity template is rendered the ClickServlet uses Pages:
 * <ul>
 * <li>{@link #getTemplate()} to find the Velocity template.</li>
 * <li>{@link #model} to populate the Velocity Context</li>
 * <li>{@link #format} to add to the Velocity Context</li>
 * <li>{@link #getContentType()} to set as the HttpServletResponse content type</li>
 * <li>{@link #headers} to set as the HttpServletResponse headers</li>
 * </ul>
 *
 * These Page properties are also used when rendering JSP pages.
 *
 * @author Malcolm Edgar
 */
public class Page {

    private static final long serialVersionUID = 1L;

    /**
     * The global page messages bundle name: &nbsp; <tt>click-page</tt>.
     */
    public static final String PAGE_MESSAGES = "click-page";

    // ----------------------------------------------------- Instance Variables

    /** The list of page controls. */
    protected List controls;

    /**
     * The list of page HTML HEAD elements including: Javascript imports,
     * Css imports, inline Javascript and inline Css.
     */
    protected List headElements;

    /** The Velocity template formatter object. */
    protected Format format;

    /** The forward path. */
    protected String forward;

    /** The HTTP response headers. */
    protected Map headers;

    /** The headers have been edited flag, to support copy on write. */
    protected boolean headersEdited;

    /** The map of localized page resource messages. **/
    protected transient MessagesMap messages;

    /**
     * The page model. For Velocity templates the model is used to populate the
     * Velocity context. For JSP pages the model values are set as named
     * request attributes.
     */
    protected Map model = new HashMap();

    /** The Page header imports. */
    protected transient PageImports pageImports;

    /** The path of the page template to render. */
    protected String path;

    /** The redirect path. */
    protected String redirect;

    /**
     * The page is stateful and should be save to the users HttpSession
     * between requests.
     */
    protected boolean stateful;

    /** The path of the page border template to render.*/
    protected String template;

    // --------------------------------------------------------- Event Handlers

    /**
     * The on Security Check event handler. This event handler is invoked after
     * the pages constructor has been called and all the page properties have
     * been set.
     * <p/>
     * Security check provides the Page an opportunity to check the users
     * security credentials before processing the Page.
     * <p/>
     * If security check returns true the Page is processed as
     * normal. If the method returns then no other event handlers are invoked
     * (except <tt>onDestroy()</tt> and no page controls are processed.
     * <p/>
     * If the method returns false, the forward or redirect property should be
     * set to send the request to another Page.
     * <p/>
     * By default this method returns true, subclass may override this method
     * to provide their security authorization/authentication mechanism.
     *
     * @return true by default, subclasses may override this method
     */
    public boolean onSecurityCheck() {
        return true;
    }

    /**
     * The on Initialization event handler. This event handler is invoked after
     * the {@link #onInit()} method has been called.
     * <p/>
     * Subclasses should place any initialization code which has dependencies
     * on the context or other properties in this method. Generally light
     * weight initialization code should be placed in the Pages constructor.
     * <p/>
     * Time consuming operations such as fetching the results of a database
     * query should not be placed in this method. These operations should be
     * performed in the {@link #onRender()}, {@link #onGet()} or
     * {@link #onPost()} methods so that other event handlers may take
     * alternative execution paths without performing these expensive operations.
     * <p/>
     * <b>Please Note</b> however the qualifier for the previous statement is
     * that all form and field controls must be fully initialized before they
     * are processed, which is after the <tt>onInit()</tt> method has
     * completed. After this point their <tt>onProcess()</tt> methods will be
     * invoked by the <tt>ClickServlet</tt>.
     * <p/>
     * Select controls in particular must have their option list values populated
     * before the form is processed otherwise field validations cannot be performed.
     * <p/>
     * For initializing page controls the best practice is to place all the
     * control creation code in the pages constructor, and only place any
     * initialization code in the <tt>onInit()</tt> method which has an external
     * dependency to the context or some other object. By following this practice
     * it is easy to see what code is "design time" initialization code and what
     * is "runtime initialization code".
     * <p/>
     * When subclassing pages which also use the <tt>onInit()</tt> method is
     * is critical you call the <tt>super.onInit()</tt> method first, for
     * example:
     * <pre class="javaCode">
     * <span class="kw">public void</span> onInit() {
     *     <span class="kw">super</span>.onInit();
     *
     *     // Initialization code
     *     ..
     * } </pre>
     */
    public void onInit() {
    }

    /**
     * The on Get request event handler. This event handler is invoked if the
     * HTTP request method is "GET".
     * <p/>
     * The event handler is invoked after {@link #onSecurityCheck()} has been
     * called and all the Page {@link #controls} have been processed. If either
     * the security check or one of the controls cancels continued event
     * processing the <tt>onGet()</tt> method will not be invoked.
     *
     * <h4>Important Note</h4>
     *
     * Form and field controls should <b>NOT</b> be created
     * or initialized at this point as the control processing stage has already
     * been completed. Select option list values should also be populated
     * before the control processing stage is performed so that they can
     * validate the submitted values.
     */
    public void onGet() {
    }

    /**
     * The on Post request event handler. This event handler is invoked if the
     * HTTP request method is "POST".
     * <p/>
     * The event handler is invoked after {@link #onSecurityCheck()} has been
     * called and all the Page {@link #controls} have been processed. If either
     * the security check or one of the controls cancels continued event
     * processing the <tt>onPost()</tt> method will not be invoked.
     *
     * <h4>Important Note</h4>
     *
     * Form and field controls should <b>NOT</b> be created
     * or initialized at this point as the control processing stage has already
     * been completed. Select option list values should also be populated
     * before the control processing stage is performed so that they can
     * validate the submitted values.
     */
    public void onPost() {
    }

    /**
     * The on render event handler. This event handler is invoked prior to the
     * page being rendered.
     * <p/>
     * This method will not be invoked if either the security check or one of
     * the controls cancels continued event processing.
     * <p/>
     * The on render method is typically used to populate tables performing some
     * database intensive operation. By putting the intensive operations in the
     * on render method they will not be performed if the user navigates away
     * to a different page.
     * <p/>
     * If you have code which you are using in both the <tt>onGet()</tt> and
     * <tt>onPost()</tt> methods, use the <tt>onRender()</tt> method instead.
     *
     * <h4>Important Note</h4>
     *
     * Form and field controls should <b>NOT</b> be created
     * or initialized at this point as the control processing stage has already
     * been completed. Select option list values should also be populated
     * before the control processing stage is performed so that they can
     * validate the submitted values.
     */
    public void onRender() {
    }

    /**
     * The on Destroy request event handler. Subclasses may override this method
     * to add any resource clean up code.
     * <p/>
     * This method is guaranteed to be called before the Page object reference
     * goes out of scope and is available for garbage collection.
     * <p/>
     * Stateful pages will have this method invoked before they are saved to
     * the session.
     */
    public void onDestroy() {
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Add the control to the page. The control will be added to the pages model
     * using the controls name as the key. The Controls parent property will
     * also be set to the page instance.
     *
     * @param control the control to add
     * @throws IllegalArgumentException if the control is null, or if the name
     *      of the control is not defined
     */
    public void addControl(Control control) {
        if (control == null) {
            throw new IllegalArgumentException("Null control parameter");
        }
        if (StringUtils.isBlank(control.getName())) {
            throw new IllegalArgumentException("Control name not defined: "
                + control.getClass());
        }

        // Note: set parent first as setParent might veto further processing
        control.setParent(this);

        getControls().add(control);
        addModel(control.getName(), control);
    }

    /**
     * Remove the control from the page. The control will be removed from the
     * pages model and the control parent property will be set to null.
     *
     * @param control the control to remove
     * @throws IllegalArgumentException if the control is null, or if the name
     *      of the control is not defined
     */
    public void removeControl(Control control) {
        if (control == null) {
            throw new IllegalArgumentException("Null control parameter");
        }
        if (StringUtils.isBlank(control.getName())) {
            throw new IllegalArgumentException("Control name not defined");
        }

        getControls().remove(control);
        getModel().remove(control.getName());

        control.setParent(null);
    }

    /**
     * Return the list of page Controls.
     *
     * @return the list of page Controls
     */
    public List getControls() {
        if (controls == null) {
            controls = new ArrayList();
        }
        return controls;
    }

    /**
     * Return true if the page has any controls defined.
     *
     * @return true if the page has any controls defined
     */
    public boolean hasControls() {
        return (controls == null) ? false : !controls.isEmpty();
    }

    /**
     * Return the request context of the page.
     *
     * @return the request context of the page
     */
    public Context getContext() {
        return Context.getThreadLocalContext();
    }

    /**
     * Return the HTTP response content type. By default this method returns
     * <tt>"text/html"</tt>.
     * <p/>
     * If the request specifies a character encoding via
     * If {@link javax.servlet.ServletRequest#getCharacterEncoding()}
     * then this method will return <tt>"text/html; charset=encoding"</tt>.
     * <p/>
     * The ClickServlet uses the pages content type for setting the
     * HttpServletResponse content type.
     *
     * @return the HTTP response content type
     */
    public String getContentType() {
        String charset = getContext().getRequest().getCharacterEncoding();

        if (charset == null) {
            return "text/html";

        } else {
            return "text/html; charset=" + charset;
        }
    }

    /**
     * Return the Velocity template formatter object.
     * <p/>
     * The ClickServlet adds the format object to the Velocity context using
     * the key <tt>"format"</tt> so that it can be used in the page template.
     *
     * @return the Velocity template formatter object
     */
    public Format getFormat() {
        return format;
    }

    /**
     * Set the Velocity template formatter object.
     *
     * @param value the Velocity template formatter object.
     */
    public void setFormat(Format value) {
        format = value;
    }

    /**
     * Return the path to forward the request to.
     * <p/>
     * If the {@link #forward} property is not null it will be used to forward
     * the request to in preference to rendering the template defined by the
     * {@link #path} property. The request is forwarded using the
     * RequestDispatcher.
     * <p/>
     * See also {@link #getPath()}, {@link #getRedirect()}
     *
     * @return the path to forward the request to
     */
    public String getForward() {
        return forward;
    }

    /**
     * Set the path to forward the request to.
     * <p/>
     * If the {@link #forward} property is not null it will be used to forward
     * the request to in preference to rendering the template defined by the
     * {@link #path} property. The request is forwarded using the Servlet
     * RequestDispatcher.
     * <p/>
     * If forward paths start with a <span class="wr">"/"</span>
     * character the forward path is
     * relative to web applications root context, otherwise the path is
     * relative to the requests current location.
     * <p/>
     * For example given a web application deployed to context <tt>mycorp</tt>
     * with the pages:
     * <pre class="codeConfig" style="color:navy">
     *  /index.htm
     *  /customer/search.htm
     *  /customer/details.htm
     *  /customer/management/add-customer.htm </pre>
     *
     * To forward to the customer <tt class="wr">search.htm</tt> page from
     * the web app root you could set forward as
     * <tt>setFoward(<span class="navy">"/customer/search.htm"</span>)</tt>
     * or <tt>setFoward(<span class="navy">"customer/search.htm"</span>)</tt>.
     * <p/>
     * If a user was currently viewing the <tt class="wr">add-customer.htm</tt>
     * to forward to customer <span class="wr">details.htm</span> you could
     * set forward as
     * <tt>setFoward(<span class="navy">"/customer/details.htm"</span>)</tt>
     * or <tt>setFoward(<span class="navy">"../details.htm"</span>)</tt>.
     * <p/>
     * See also {@link #setPath(String)}, {@link #setRedirect(String)}
     *
     * @param value the path to forward the request to
     */
    public void setForward(String value) {
        forward = value;
    }

    /**
     * The Page instance to forward the request to. The given Page object
     * must have a valid {@link #path} defined, as the {@link #path} specifies
     * the location to forward to.
     *
     * @see #setForward(java.lang.String)
     *
     * @param page the Page object to forward the request to.
     */
    public void setForward(Page page) {
        if (page == null) {
            throw new IllegalArgumentException("Null page parameter");
        }
        if (page.getPath() == null) {
            throw new IllegalArgumentException("Page has no path defined");
        }
        setForward(page.getPath());
        getContext().setRequestAttribute(ClickServlet.FORWARD_PAGE, page);
    }

    /**
     * Set the request to forward to the given page class.
     *
     * @see #setForward(java.lang.String)
     *
     * @param pageClass the class of the Page to forward the request to
     * @throws IllegalArgumentException if the Page Class is not configured
     * with a unique path
     */
    public void setForward(Class pageClass) {
        String target = getContext().getPagePath(pageClass);

        // If page class maps to a jsp, convert to htm which allows ClickServlet
        // to process the redirect
        if (target != null && target.endsWith(".jsp")) {
            target = StringUtils.replaceOnce(target, ".jsp", ".htm");
        }
        setForward(target);
    }

    /**
     * Return the map of HTTP header to be set in the HttpServletResponse.
     * Note to edit header values use {@link #setHeader(String, Object)} as
     * headers Map is initially unmodifiable.
     *
     * @return the map of HTTP header to be set in the HttpServletResponse
     */
    public Map getHeaders() {
        return headers;
    }

    /**
     * Set the named header with the given value. This method uses copy on
     * write to the headers Map, as the initial loaded headers Map is
     * unmodifiable.
     *
     * @param name the name of the header
     * @param value the value of the header
     */
    public void setHeader(String name, Object value) {
        if (name == null) {
            throw new IllegalArgumentException("Null header name parameter");
        }
        if (!headersEdited) {
            headersEdited = true;
            headers = new HashMap(headers);
        }
        headers.put(name, value);
    }

    /**
     * Set the map of HTTP header to be set in the HttpServletResponse.
     *
     * @param value the map of HTTP header to be set in the HttpServletResponse
     */
    public void setHeaders(Map value) {
        headers = value;
    }

    /**
     * Return the HTML import string to include in the page, by default
     * this method returns null.
     * <p/>
     * Override this method to specify JavaScript and CSS includes for the
     * page. For example:
     *
     * <pre class="prettyprint">
     * public MyPage extends Page {
     *
     *     // Define a constant for the Page Javascript import.
     *     protected static final String JAVASCRIPT_IMPORT =
     *         "&lt;script type='text/javascript' src='{0}/click/my-page.js'&gt;&lt;/script&gt;\n";
     *
     *     // Define a constant for the Page CSS import
     *     protected static final String CSS_IMPORT =
     *         "&lt;link type='text/css' rel='stylesheet' href='text/css' src='{0}/click/my-page.css'/&gt;\n";
     *
     *     ...
     *
     *     // Override getHtmlImports and return the Javascript and CSS imports.
     *     public String getHtmlImports() {
     *         Context context = getContext();
     *
     *         // Concatenate Javascript and CSS imports
     *         return ClickUtils.createHtmlImport(MyPage.JAVASCRIPT_IMPORT, ClickUtils.getResourceVersionIndicator(), context)
     *         + ClickUtils.createHtmlImport(MyPage.CSS_IMPORT, ClickUtils.getResourceVersionIndicator(), context);
     *     }
     * } </pre>
     *
     * <b>Note</b> multiple import lines should be separated by a
     * <tt>'\n'</tt> char, as the {@link org.apache.click.util.PageImports} will
     * parse multiple import lines on the <tt>'\n'</tt> char and ensure that
     * imports are not included twice.
     * <p/>
     * The order in which JS and CSS files are included will be preserved in the
     * page.
     * <p/>
     * If you need to customize the HTML imports included in your page override
     * the method {@link #getPageImports()}.
     *
     * @return the HTML includes statements for the control stylesheet and
     * JavaScript files, by default this method returns null
     *
     * @deprecated use the new {@link #getHeadElements()} instead
     */
    public String getHtmlImports() {
        return null;
    }

    /**
     * Return the list of {@link org.apache.click.element.Element HEAD elements}
     * to be included in the page. Example HEAD elements include
     * {@link org.apache.click.element.JsImport JavaScript imports},
     * {@link org.apache.click.element.JsScript inline JavasSript},
     * {@link org.apache.click.util.CssImport Css imports} and
     * {@link org.apache.click.util.Css inline Css}.
     * <p/>
     * Pages can include their own list of HEAD elements by implementing
     * this method.
     * <p/>
     * The recommended approach when implementing this method is to use
     * <tt>lazy loading</tt> to only add HEAD elements once and when needed.
     * For example:
     *
     * <pre class="prettyprint">
     * public MyPage extends Page {
     *
     *     public List getHeadElements() {
     *         // Use lazy loading to ensure the JS is only added the
     *         // first time this method is called.
     *         if (headElements == null) {
     *             // Get the head elements from the super implementation
     *             headElements = super.getHeadElements();
     *
     *             // Include the page's external Javascript resource
     *             JsImport jsImport = new JsImport("/mycorp/js/mypage.js");
     *             headElements.add(jsImport);
     *
     *             // Include the page's external Css resource
     *             CssImport cssImport = new CssImport("/mycorp/js/mypage.css");
     *             headElements.add(cssImport);
     *         }
     *         return headElements;
     *     }
     * } </pre>
     *
     * Alternatively one can add the HEAD elements in the Page constructor:
     *
     * <pre class="prettyprint">
     * public MyPage extends Page {
     *
     *     public MyPage() {
     *         JsImport jsImport = new JsImport("/mycorp/js/mypage.js");
     *         getHeadElements().add(jsImport);
     *         CssImport cssImport = new CssImport("/mycorp/js/mypage.css");
     *         getHeadElements().add(cssImport);
     *     }
     * } </pre>
     *
     * One can also add HEAD elements from event handler methods such as
     * {@link #onInit()}, {@link #onGet()}, {@link #onPost()}, {@link #onRender()}
     * etc. <b>Please note:</b> when using {@link #stateful Stateful} pages, you
     * will need to set the HEAD elements list to <tt>null</tt> in the
     * {@link #onDestroy()} event handler, otherwise the HEAD elements list will
     * continue to grow with each request:
     *
     * <pre class="prettyprint">
     * public MyPage extends Page {
     *
     *     public MyPage() {
     *         // Activate stateful page
     *         setStateful(true);
     *     }
     *
     *     // Set HEAD elements in the onInit event handler
     *     public void onInit() {
     *         // Add HEAD elements
     *         JsImport jsImport = new JsImport("/mycorp/js/mypage.js");
     *         getHeadElements().add(jsImport);
     *         CssImport cssImport = new CssImport("/mycorp/js/mypage.css");
     *         getHeadElements().add(cssImport);
     *     }
     *
     *     public void onDestroy() {
     *         // Nullify the HEAD elements
     *         headElements = null;
     *     }
     * } </pre>
     *
     * The order in which JS and CSS files are included will be preserved in the
     * page.
     * <p/>
     * <b>Note:</b> this method must never return null. If no HEAD elements
     * are available this method must return an empty {@link java.util.List}.
     * <p/>
     * <b>Also note:</b> a common problem when overriding getHeadElements in
     * subclasses is forgetting to call <em>super.getHeadElements</em>. Consider
     * carefully whether you should call <em>super.getHeadElements</em> or not.
     *
     * @return the list of HEAD elements to be included in the page
     */
    public List getHeadElements() {
        if (headElements == null) {
            headElements = new ArrayList(2);
        }
        return headElements;
    }

    /**
     * Return the localized Page resource message for the given resource
     * property key. The resource message returned will use the Locale obtained
     * from the Context.
     * <p/>
     * Pages can define text properties files to store localized messages. These
     * properties files must be stored on the Page class path with a name
     * matching the class name. For example:
     * <p/>
     * The page class:
     * <pre class="codeJava">
     *  <span class="kw">package</span> com.mycorp.pages;
     *
     *  <span class="kw">public class</span> Login <span class="kw">extends</span> Page {
     *     .. </pre>
     *
     * The page class property filenames and their path:
     * <pre class="codeConfig">
     *  /com/mycorp/pages/Login.properties
     *  /com/mycorp/pages/Login_en.properties
     *  /com/mycorp/pages/Login_fr.properties </pre>
     *
     * Page messages can also be defined in the optional global messages
     * bundle:
     *
     * <pre class="codeConfig">
     *  /click-page.properties </pre>
     *
     * To define global page messages simply add <tt>click-page.properties</tt>
     * file to your application's class path. Message defined in this properties
     * file will be available to all of your application pages.
     * <p/>
     * Note messages in your page class properties file will override any
     * messages in the global <tt>click-page.properties</tt> file.
     * <p/>
     * Page messages can be accessed directly in the page template using
     * the <span class="st">$messages</span> reference. For examples:
     *
     * <pre class="codeHtml">
     * <span class="blue">$messages.title</span> </pre>
     *
     * Please see the {@link org.apache.click.util.MessagesMap} adaptor for more details.
     *
     * @param key the message property key name
     * @return the Page message for the given message property key
     */
    public String getMessage(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Null key parameter");
        }
        return (String) getMessages().get(key);
    }

    /**
     * Return the formatted page message for the given resource name
     * and message format argument and for the context request locale.
     *
     * @param name resource name of the message
     * @param arg the message argument to format
     * @return the named localized message for the page
     */
    public String getMessage(String name, Object arg) {
        Object[] args = new Object[] { arg };
        return getMessage(name, args);
    }

    /**
     * Return the formatted page message for the given resource name and
     * message format arguments and for the context request locale.
     *
     * @param name resource name of the message
     * @param args the message arguments to format
     * @return the named localized message for the page
     */
    public String getMessage(String name, Object[] args) {
        if (args == null) {
            throw new IllegalArgumentException("Null args parameter");
        }
        String value = getMessage(name);

        return MessageFormat.format(value, args);
    }

    /**
     * Return a Map of localized messages for the Page.
     *
     * @see #getMessage(String)
     *
     * @return a Map of localized messages for the Page
     * @throws IllegalStateException if the context for the Page has not be set
     */
    public Map getMessages() {
        if (messages == null) {
            if (getContext() != null) {
                messages = new MessagesMap(getClass(), PAGE_MESSAGES);

            } else {
                String msg = "Context not set cannot initialize messages";
                throw new IllegalStateException(msg);
            }
        }
        return messages;
    }

    /**
     * Add the named object value to the Pages model map.
     *
     * @param name the key name of the object to add
     * @param value the object to add
     * @throws IllegalArgumentException if the name or value parameters are
     * null, or if there is already a named value in the model
     */
    public void addModel(String name, Object value) {
        if (name == null) {
            String msg = "Cannot add null parameter name to "
                    + getClass().getName() + " model";
            throw new IllegalArgumentException(msg);
        }
        if (value == null) {
            String msg = "Cannot add null " + name + " parameter "
                    + "to " + getClass().getName() + " model";
            throw new IllegalArgumentException(msg);
        }
        if (getModel().containsKey(name)) {
            String msg = getClass().getName() + " model already contains "
                    + "value named " + name;
            throw new IllegalArgumentException(msg);
        } else {
            getModel().put(name, value);
        }
    }

    /**
     * Return the Page's model map. The model is used populate the
     * Velocity Context with is merged with the page template before rendering.
     *
     * @return the Page's model map
     */
    public Map getModel() {
        return model;
    }

    /**
     * Return the Page header imports.
     * <p/>
     * PageImports are used define the CSS and JavaScript imports and blocks
     * to be included in the page template.
     * <p/>
     * The PageImports object will be included in the Page template when the
     * following methods are invoked:
     * <ul>
     * <li>{@link ClickServlet#createTemplateModel(Page)} - for template pages</li>
     * <li>{@link ClickServlet#setRequestAttributes(Page)} - for JSP pages</li>
     * </ul>
     * <p/>
     * If you need to tailor the page imports rendered, override this method
     * and modify the PageImports object returned.
     * <p/>
     * If you need to create a custom PageImports, override the method
     * {@link ClickServlet#createPageImports(org.apache.click.Page)}
     *
     * @return the Page header imports
     */
    public PageImports getPageImports() {
        return pageImports;
    }

    /**
     * Set the Page header imports.
     * <p/>
     * PageImports are used define the CSS and JavaScript imports and blocks
     * to be included in the page template.
     * <p/>
     * The PageImports references will be included in the Page model when the
     * following methods are invoked:
     * <ul>
     * <li>{@link ClickServlet#createTemplateModel(Page)} - for template pages</li>
     * <li>{@link ClickServlet#setRequestAttributes(Page)} - for JSP pages</li>
     * </ul>
     * <p/>
     * If you need to tailor the page imports rendered, override the
     * {@link #getPageImports()} method and modify the PageImports object
     * returned.
     * <p/>
     * If you need to create a custom PageImports, override the method
     * {@link ClickServlet#createPageImports(org.apache.click.Page)}
     *
     * @param pageImports the new pageImports instance to set
     */
    public void setPageImports(PageImports pageImports) {
        this.pageImports = pageImports;
    }

    /**
     * Return the path of the Template or JSP to render.
     * <p/>
     * If this method returns <tt>null</tt>, Click will not perform any rendering.
     * This is useful when you want to stream or write directly to the
     * HttpServletResponse.
     * <p/>
     * See also {@link #getForward()}, {@link #getRedirect()}
     *
     * @return the path of the Template or JSP to render
     */
    public String getPath() {
        return path;
    }

    /**
     * Set the path of the Template or JSP to render.
     * <p/>
     * By default Click will set the path to the requested page url. Meaning
     * if the url <tt>/edit-customer.htm</tt> is requested, path will be set
     * to <tt>/edit-customer.htm</tt>.
     * <p/>
     * Here is an example if you want to change the path to a different Template:
     * <p/>
     * <pre class="prettyprint">
     * public void onGet() {
     *     setPath("/some-other-template.htm");
     * }</pre>
     * And here is an example if you want to change the path to a different JSP.
     * <pre class="prettyprint">
     * public void onGet() {
     *     setPath("/some-other-jsp.jsp");
     * }</pre>
     * <p/>
     * If path is set to <tt>null</tt>, Click will not perform any rendering.
     * This is useful when you want to stream or write directly to the
     * HttpServletResponse.
     * <p/>
     * See also {@link #setForward(String)}, {@link #setRedirect(String)}
     *
     * @param value the path of the Template or JSP to render
     */
    public void setPath(String value) {
        path = value;
    }

    /**
     * Return the path to redirect the request to.
     * <p/>
     * If the {@link #redirect} property is not null it will be used to redirect
     * the request in preference to {@link #forward} or {@link #path} properties.
     * The request is redirected to using the HttpServletResponse.setRedirect()
     * method.
     * <p/>
     * See also {@link #getForward()}, {@link #getPath()}
     *
     * @return the path to redirect the request to
     */
    public String getRedirect() {
        return redirect;
    }

    /**
     * Return true if the page is stateful and should be saved in the users
     * HttpSession between requests.
     *
     * @return true if the page is stateful and should be save in the users session
     */
    public boolean isStateful() {
        return stateful;
    }

    /**
     * Set whether the page is stateful and should be saved in the users
     * HttpSession between requests.
     * <p/>
     * Click will synchronize on the page instance. This ensures that if
     * multiple requests arrive from the same user for the page, only one
     * request can access the page at a time.
     * <p/>
     * Stateful pages are stored in the HttpSession using the key
     * <tt>page.getClass().getName()</tt>.
     * <p/>
     * It is worth noting that Click checks a Page's stateful property after
     * each request. Thus it becomes possible to enable a stateful Page for a
     * number of request and then setting it to <tt>false</tt> again. Click will
     * remove the Page from the HttpSession, freeing up memory for the server.
     *
     * @param stateful the flag indicating whether the page should be saved
     *         between user requests
     */
    public void setStateful(boolean stateful) {
        this.stateful = stateful;
        if (isStateful()) {
            getContext().getSession();
        }
    }

    /**
     * Set the location to redirect the request to.
     * <p/>
     * If the {@link #redirect} property is not null it will be used to redirect
     * the request in preference to the {@link #forward} and {@link #path}
     * properties. The request is redirected using the HttpServletResponse.setRedirect()
     * method.
     * <p/>
     * If the redirect location begins with a <tt class="wr">"/"</tt>
     * character the redirect location will be prefixed with the web applications
     * <tt>context path</tt>. Note if the given location is already prefixed
     * with the <tt>context path</tt>, Click won't add it a second time.
     * <p/>
     * For example if an application is deployed to the context
     * <tt class="wr">"mycorp"</tt> calling
     * <tt>setRedirect(<span class="navy">"/customer/details.htm"</span>)</tt>
     * will redirect the request to:
     * <tt class="wr">"/mycorp/customer/details.htm"</tt>
     * <p/>
     * If the redirect location does not begin with a <tt class="wr">"/"</tt>
     * character the redirect location will be used as specified. Thus if the
     * location is <tt class="wr">http://somehost.com/myapp/customer.jsp</tt>,
     * Click will redirect to that location.
     * <p/>
     * <b>JSP note:</b> when redirecting to a JSP template keep in mind that the
     * JSP template won't be processed by Click, as ClickServlet is mapped to
     * <em>*.htm</em>. Instead JSP templates are processed by the Servlet
     * container JSP engine.
     * <p/>
     * So if you have a situation where a Page Class
     * (<span class="navy">Customer.class</span>) is mapped to the JSP
     * (<span class="navy">"/customer.jsp"</span>) and you want to redirect to
     * Customer.class, you could either redirect to
     * (<span class="navy">"/customer<span class="red">.htm</span>"</span>) or
     * use the alternative redirect utility {@link #setRedirect(java.lang.Class)}.
     * <p/>
     * <b>Please note</b> that Click will url encode the location by invoking
     * <tt>response.encodeRedirectURL(location)</tt> before redirecting.
     * <p/>
     * See also {@link #setForward(String)}, {@link #setPath(String)}
     *
     * @param location the path to redirect the request to
     */
    public void setRedirect(String location) {
        if (location != null) {
            if (location.charAt(0) == '/') {
                Context context = getContext();
                String contextPath = context.getRequest().getContextPath();

                // Guard against adding duplicate context path
                if (!location.startsWith(contextPath + '/')) {
                    location = contextPath + location;
                }
            }
        }
        redirect = location;
    }

    /**
     * Set the request to redirect to the give page class.
     *
     * @see #setRedirect(java.lang.String)
     *
     * @param pageClass the class of the Page to redirect the request to
     * @throws IllegalArgumentException if the Page Class is not configured
     * with a unique path
     */
    public void setRedirect(Class pageClass) {
        String target = getContext().getPagePath(pageClass);

        // If page class maps to a jsp, convert to htm which allows ClickServlet
        // to process the redirect
        if (target != null && target.endsWith(".jsp")) {
            target = StringUtils.replaceOnce(target, ".jsp", ".htm");
        }
        setRedirect(target);
    }

    /**
     * Return the path of the page border template to render, by default this
     * method returns {@link #getPath()}.
     * <p/>
     * Pages can override this method to return an alternative border page
     * template. This is very useful when implementing an standardized look and
     * feel for a web site. The example below provides a BorderedPage base Page
     * which other site templated Pages should extend.
     *
     * <pre class="codeJava">
     * <span class="kw">public class</span> BorderedPage <span class="kw">extends</span> Page {
     *     <span class="kw">public</span> String getTemplate() {
     *         <span class="kw">return</span> <span class="st">"border.htm"</span>;
     *     }
     * } </pre>
     *
     * The BorderedPage returns the page border template <span class="st">"border.htm"</span>:
     *
     * <pre class="codeHtml">
     * &lt;html&gt;
     *   &lt;head&gt;
     *     &lt;title&gt; <span class="blue">$title</span> &lt;/title&gt;
     *     &lt;link rel="stylesheet" type="text/css" href="style.css" title="Style"/&gt;
     *   &lt;/head&gt;
     *   &lt;body&gt;
     *
     *     &lt;h1&gt; <span class="blue">$title</span> &lt;/h1&gt;
     *     &lt;hr/&gt;
     *
     *     <span class="red">#parse</span>( <span class="blue">$path</span> )
     *
     *   &lt;/body&gt;
     * &lt;/html&gt; </pre>
     *
     * Other pages insert their content into this template, via their
     * {@link #path} property using the Velocity
     * <a href="../../../../velocity/vtl-reference-guide.html#parse">#parse</a>
     * directive. Note the <span class="blue">$path</span> value is automatically
     * added to the VelocityContext by the ClickServlet.
     *
     * @return the path of the page template to render, by default returns
     * {@link #getPath()}
     */
    public String getTemplate() {
        if (template == null) {
            return getPath();

        } else {
            return template;
        }
    }

    /**
     * Set the page border template path.
     * <p/>
     * <b>Note:</b> if this value is not set, {@link #getTemplate()} will default
     * to {@link #getPath()}.
     *
     * @param template the border template path
     */
    public void setTemplate(String template) {
        this.template = template;
    }

}