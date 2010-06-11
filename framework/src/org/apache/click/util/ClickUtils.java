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
package org.apache.click.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.click.Context;
import org.apache.click.Control;
import org.apache.click.Page;
import org.apache.click.Partial;
import org.apache.click.control.AbstractControl;
import org.apache.click.control.AbstractLink;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Container;
import org.apache.click.control.Field;
import org.apache.click.control.Form;
import org.apache.click.service.ConfigService;
import org.apache.click.service.LogService;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;

/**
 * Provides miscellaneous Form, String and Stream utility methods.
 */
public class ClickUtils {

    // ------------------------------------------------------- Public Constants

    /**
     * The resource <tt>versioning</tt> request attribute: key: &nbsp;
     * <tt>enable-resource-version</tt>.
     * <p/>
     * If this attribute is set to <tt>true</tt> and Click is running in
     * <tt>production</tt> or <tt>profile</tt> mode, Click resources returned
     * from {@link org.apache.click.Control#getHeadElements()} will have a
     * <tt>version indicator</tt> added to their path.
     *
     * @see org.apache.click.Control#getHeadElements()
     * @see org.apache.click.util.ClickUtils#getResourceVersionIndicator(Context)
     */
    public static final String ENABLE_RESOURCE_VERSION = "enable-resource-version";

    /**
     * The default Click configuration filename: &nbsp;
     * "<tt>/WEB-INF/click.xml</tt>".
     */
    public static final String DEFAULT_APP_CONFIG = "/WEB-INF/click.xml";

    /** The version indicator separator string. */
    public static final String VERSION_INDICATOR_SEP = "_";

    /** The static web resource version number indicator string. */
    public static final String RESOURCE_VERSION_INDICATOR =
        VERSION_INDICATOR_SEP + getClickVersion();

    // ------------------------------------------------------ Private Constants

    /** The cached resource version indicator. */
    private static String cachedResourceVersionIndicator;

    /** The static application-wide resource version indicator. */
    private static String applicationVersion;

    /** The cached application version indicator string. */
    private static String cachedApplicationVersionIndicator;

    /**
     * Character used to separate username and password in persistent cookies.
     * 0x13 == "Device Control 3" non-printing ASCII char. Unlikely to appear
     * in a username
     */
    private static final char DELIMITER = 0x13;

    /*
     * "Tweakable" parameters for the cookie encoding. NOTE: changing these
     * and recompiling this class will essentially invalidate old cookies.
     */
    private final static char ENCODE_CHAR_OFFSET1 = 'C';
    private final static char ENCODE_CHAR_OFFSET2 = 'i';

    /** Hexadecimal characters for MD5 encoding. */
    private static final char[] HEXADECIMAL = { '0', '1', '2', '3', '4', '5',
        '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /** Ajax request header or parameter: "<tt>X-Requested-With</tt>". */
    private static final String X_REQUESTED_WITH = "X-Requested-With";

    /**
     * The array of escaped HTML character values, indexed on char value.
     * <p/>
     * HTML entities values were derived from Jakarta Commons Lang
     * <tt>org.apache.commons.lang.Entities</tt> class.
     */
    private static final String[] HTML_ENTITIES = new String[63];

    static {
        HTML_ENTITIES[34] = "&quot;"; // " - double-quote
        HTML_ENTITIES[38] = "&amp;"; // & - ampersand
        HTML_ENTITIES[39] = "&#039;"; // ' - quote
        HTML_ENTITIES[60] = "&lt;"; // < - less-than
        HTML_ENTITIES[62] = "&gt;"; // > - greater-than
    };

    // --------------------------------------------------------- Public Methods

    /**
     * Perform an auto post redirect to the specified target using the given
     * response. If the params Map is defined then the form will post these
     * values as name value pairs. If the compress value is true, this method
     * will attempt to gzip compress the response content if requesting
     * browser accepts "gzip" encoding.
     * <p/>
     * Once this method has returned you should not attempt to write to the
     * servlet response.
     *
     * @param request the servlet request
     * @param response the servlet response
     * @param target the target URL to send the auto post redirect to
     * @param params the map of parameter values to post
     * @param compress the flag to specify whether to attempt gzip compression
     *         of the response content
     */
    public static void autoPostRedirect(HttpServletRequest request,
            HttpServletResponse response, String target, Map params,
            boolean compress) {

        Validate.notNull(request, "Null response parameter");
        Validate.notNull(response, "Null response parameter");
        Validate.notNull(target, "Null target parameter");

        HtmlStringBuffer buffer = new HtmlStringBuffer(1024);
        buffer.append("<html><body onload=\"document.forms[0].submit();\">");
        buffer.append("<form name=\"form\" method=\"post\" style=\"{display:none;}\" action=\"");
        buffer.append(target);
        buffer.append("\">");
        for (Iterator i = params.keySet().iterator(); i.hasNext();) {
            String name = i.next().toString();
            String value = params.get(name).toString();
            buffer.elementStart("textarea");
            buffer.appendAttribute("name", name);
            buffer.elementEnd();
            buffer.append(value);
            buffer.elementEnd("textarea");
        }
        buffer.append("</form></body></html>");

        // Determine whether browser will accept gzip compression
        if (compress) {
            compress = false;
            Enumeration e = request.getHeaders("Accept-Encoding");

            while (e.hasMoreElements()) {
                String name = (String) e.nextElement();
                if (name.indexOf("gzip") != -1) {
                    compress = true;
                    break;
                }
            }
        }

        OutputStream os = null;
        GZIPOutputStream gos = null;
        try {
            response.setContentType("text/html");

            if (compress) {
                response.setHeader("Content-Encoding", "gzip");

                os = response.getOutputStream();
                gos = new GZIPOutputStream(os);
                gos.write(buffer.toString().getBytes());

            } else {
                response.setContentLength(buffer.length());

                os = response.getOutputStream();
                os.write(buffer.toString().getBytes());
            }

        } catch (IOException ex) {
            ex.printStackTrace();

        } finally {
            ClickUtils.close(gos);
            ClickUtils.close(os);
        }
    }

    /**
     * A helper method which binds the submitted request value to the Field's
     * value. Since Field values are only bound during the <tt>"onProcess"</tt>
     * event, this method can be used to bind a submitted Field value during
     * the <tt>"onInit"</tt> event, which occurs <b>before</b> the
     * <tt>"onProcess"</tt> event.
     * <p/>
     * This is especially useful for dynamic Form and Page behavior where Field
     * values are inspected during the <tt>"onInit"</tt> event to add or remove
     * specific Fields.
     * <p/>
     * <b>Please note</b>: this method won't bind disabled fields, unless the
     * field has an incoming request parameter matching its name. If an incoming
     * request parameter is present, this method will switch off the Field's
     * disabled property.
     * <p/>
     * This method delegates to
     * {@link #canBind(org.apache.click.Control, org.apache.click.Context)}
     * to check if the Field value can be bound.
     * <p/>
     * <pre class="prettyprint">
     * public void onInit() {
     *     Form form = new Form("form");
     *     Select select = new Select("select");
     *     select.setAttribute("onchange", "Click.submit(form, false)");
     *
     *     // Bind the select Field request value
     *     ClickUtils.bind(select);
     *
     *     if (select.getValue() == COMPANY) {
     *         form.add(new TextField("companyName"));
     *     } else {
     *         form.add(new TextField("fullname"));
     *         form.add(new TextField("age"));
     *     }
     * } </pre>
     *
     * @param field the Field to bind
     */
    public static void bind(Field field) {
        Context context = Context.getThreadLocalContext();
        if (canBind(field, context)) {
            bindField(field, context);
        }
    }

    /**
     * A helper method which binds the submitted request value to the Link's
     * value. See {@link #bind(org.apache.click.control.Field)} for a detailed
     * description.
     * <p/>
     * This method delegates to
     * {@link #canBind(org.apache.click.Control, org.apache.click.Context)}
     * to check if the Link value can be bound.
     *
     * @param link the AbstractLink to bind
     */
    public static void bind(AbstractLink link) {
        Context context = Context.getThreadLocalContext();
        if (canBind(link, context)) {
            link.bindRequestValue();
        }
    }

    /**
     * A helper method which binds the submitted request values of all Fields
     * and Links inside the given container or child containers. See
     * {@link #bind(org.apache.click.control.Field)} for a detailed description.
     * <p/>
     * This method delegates to
     * {@link #canBind(org.apache.click.Control, org.apache.click.Context)}
     * to check if the Container Fields and Links can be bound.
     * <p/>
     * Below is an example to bind Form Field's during the onInit event:
     *
     * <pre class="prettyprint">
     * public void onInit() {
     *     Form form = new Form("form");
     *     Checkbox commentChk = new Checkbox("comment");
     *     Select select = new Select("select");
     *     select.setAttribute("onchange", "Click.submit(form, false)");
     *
     *     // Bind all Form Field request values
     *     ClickUtils.bind(form);
     *
     *     if (select.getValue() == COMPANY) {
     *         form.add(new TextField("companyName"));
     *     } else {
     *         form.add(new TextField("fullname"));
     *         form.add(new TextField("age"));
     *     }
     *
     *     if (commentChk.isChecked()) {
     *         form.add(new TextArea("feedback"));
     *     }
     * } </pre>
     *
     * @param container the container which Fields and Links to bind
     */
    public static void bind(Container container) {
        Context context = Context.getThreadLocalContext();
        if (canBind(container, context)) {
            bind(container, context);
        }
    }

    /**
     * A helper method which binds and validates the Field's submitted request
     * value. This method will return true if the validation succeeds, false
     * otherwise. See {@link #bind(org.apache.click.control.Field)} for a
     * detailed description.
     * <p/>
     * This method delegates to
     * {@link #canBind(org.apache.click.Control, org.apache.click.Context)}
     * to check if the Field value can be bound and validated.
     * <p/>
     * <b>Please note</b>: this method won't bind and validate disabled fields,
     * unless the field has an incoming request parameter matching its name.
     * If an incoming request parameter is present, this method will switch off
     * the Field's disabled property.
     * <p/>
     * <pre class="prettyprint">
     * public void onInit() {
     *     Form form = new Form("form");
     *     Select select = new Select("select", true);
     *     select.addOption(Option.EMPTY_OPTION);
     *
     *     select.setAttribute("onchange", "Click.submit(form, false)");
     *
     *     // Bind the Field request value and validate it before continuing
     *     if (ClickUtils.bindAndValidate(select)) {
     *         if (select.getValue() == COMPANY) {
     *             form.add(new TextField("companyName"));
     *         } else {
     *             form.add(new TextField("fullname"));
     *             form.add(new TextField("age"));
     *         }
     *     }
     * } </pre>
     *
     * @param field the Field to bind and validate
     * @return true if field was bound and valid, or false otherwise
     */
    public static boolean bindAndValidate(Field field) {
        Context context = Context.getThreadLocalContext();
        if (canBind(field, context)) {
            return bindAndValidate(field, context);
        }
        return false;
    }

    /**
     * A helper method which binds and validates the submitted request values
     * of all Fields and Links inside the given container or child containers.
     * This method will return true if the validation succeeds, false
     * otherwise. See {@link #bind(org.apache.click.control.Field)} for a
     * detailed description.
     * <p/>
     * This method delegates to
     * {@link #canBind(org.apache.click.Control, org.apache.click.Context)}
     * to check if the Container Fields and Links can be bound and
     * validated.
     *
     * <pre class="prettyprint">
     * public void onInit() {
     *     Form form = new Form("form");
     *     Checkbox commentChk = new Checkbox("comment");
     *     Select select = new Select("select", true);
     *     select.addOption(Option.EMPTY_OPTION);
     *
     *     select.setAttribute("onchange", "Click.submit(form, false)");
     *
     *     // Bind all Form field request values and validate it before continuing
     *     if (ClickUtils.bindAndValidate(form)) {
     *
     *         if (select.getValue() == COMPANY) {
     *             form.add(new TextField("companyName"));
     *         } else {
     *             form.add(new TextField("fullname"));
     *             form.add(new TextField("age"));
     *         }
     *
     *         if (commentChk.isChecked()) {
     *             form.add(new TextArea("feedback"));
     *         }
     *     }
     * } </pre>
     *
     * @param container the container which Fields and Links to bind and
     * validate
     * @return true if all Fields are valid, false otherwise
     */
    public static boolean bindAndValidate(Container container) {
        Context context = Context.getThreadLocalContext();
        if (canBind(container, context)) {
            return bindAndValidate(container, context);
        }
        return false;
    }

    /**
     * Return a new XML Document for the given input stream.
     *
     * @param inputStream the input stream
     * @return new XML Document
     * @throws RuntimeException if a parsing error occurs
     */
    public static Document buildDocument(InputStream inputStream) {
        return buildDocument(inputStream, null);
    }

    /**
     * Return a new XML Document for the given input stream and XML entity
     * resolver.
     *
     * @param inputStream the input stream
     * @param entityResolver the XML entity resolver
     * @return new XML Document
     * @throws RuntimeException if a parsing error occurs
     */
    public static Document buildDocument(InputStream inputStream,
                                         EntityResolver entityResolver) {
        try {
            DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = factory.newDocumentBuilder();

            if (entityResolver != null) {
                builder.setEntityResolver(entityResolver);
            }

            return builder.parse(inputStream);

        } catch (Exception ex) {
            throw new RuntimeException("Error parsing XML", ex);
        }
    }

    /**
     * Return true if the given control's request value can be bound, false
     * otherwise.
     * <p/>
     * The following algorithm is used to determine if the Control can be
     * bound to a request value or not.
     * <ul>
     * <li>return false if the request is a forward.
     * See {@link org.apache.click.Context#isForward()}</li>
     * <li>return true if the request is an Ajax request.
     * See {@link org.apache.click.Context#isAjaxRequest()}</li>
     * <li>return true if the control has no parent Form</li>
     * <li>return true if the control's parent Form was submitted, false otherwise.
     * See {@link org.apache.click.control.Form#isFormSubmission()}</li>
     * </ul>
     *
     * @param control the control to check if it can be bound or not
     * @param context the request context
     * @return true if the given control request value be bound, false otherwise
     */
    public static boolean canBind(Control control, Context context) {

        if (context.isForward()) {
            return false;
        }

        // This can cause issue with two fields inside a form with the same name.
        if (context.isAjaxRequest()) {
            return true;
        }

        Form form = ContainerUtils.findForm(control);
        if (form != null) {
            return form.isFormSubmission();
        }
        return true;
    }

    /**
     * Returns the <code>Class</code> object associated with the class or
     * interface with the given string name, using the current Thread context
     * class loader.
     *
     * @param classname the name of the class to load
     * @return the <tt>Class</tt> object
     * @throws ClassNotFoundException if the class cannot be located
     */
    public static Class classForName(String classname)
            throws ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return Class.forName(classname, true, classLoader);
    }

    /**
     * Close the given closeable (Reader, Writer, Stream) and ignore any
     * exceptions thrown.
     *
     * @param closeable the closeable (Reader, Writer, Stream) to close.
     */
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ioe) {
                // Ignore
            }
        }
    }

    /**
     * Creates a template model of key/value pairs which can be used by template
     * engines such as Velocity and Freemarker.
     * <p/>
     * The following objects will be added to the model:
     * <ul>
     * <li>the Page {@link org.apache.click.Page#model model} Map key/value
     * pairs
     * </li>
     * <li>context - the Servlet context path, e.g. <span class="">/mycorp</span>
     * </li>
     * <li>format - the Page {@link Format} object for formatting the display
     * of objects.
     * </li>
     * <li>messages - the {@link MessagesMap} adaptor for the
     * {@link org.apache.click.Page#getMessages()} method.
     * </li>
     * <li>path - the {@link org.apache.click.Page#path path} of the <tt>page</tt>
     * template.
     * </li>
     * <li>request - the page {@link javax.servlet.http.HttpServletRequest}
     * object.
     * </li>
     * <li>response - the page {@link javax.servlet.http.HttpServletResponse}
     * object.
     * </li>
     * <li>session - the {@link SessionMap} adaptor for the users
     * {@link javax.servlet.http.HttpSession}.
     * </li>
     * </ul>
     *
     * @param page the page to populate the template model from
     * @param context the request context
     * @return a template model as a map
     */
    public static Map<String, Object> createTemplateModel(final Page page, Context context) {

        ConfigService configService = ClickUtils.getConfigService(context.getServletContext());
        LogService logger = configService.getLogService();

        final Map<String, Object> model = new HashMap<String, Object>(page.getModel());

        final HttpServletRequest request = context.getRequest();

        Object pop = model.put("request", request);
        if (pop != null && !page.isStateful()) {
            String msg = page.getClass().getName() + " on " + page.getPath()
                         + " model contains an object keyed with reserved "
                         + "name \"request\". The page model object "
                         + pop + " has been replaced with the request object";
            logger.warn(msg);
        }

        pop = model.put("response", context.getResponse());
        if (pop != null && !page.isStateful()) {
            String msg = page.getClass().getName() + " on " + page.getPath()
                         + " model contains an object keyed with reserved "
                         + "name \"response\". The page model object "
                         + pop + " has been replaced with the response object";
            logger.warn(msg);
        }

        SessionMap sessionMap = new SessionMap(request.getSession(false));
        pop = model.put("session", sessionMap);
        if (pop != null && !page.isStateful()) {
            String msg = page.getClass().getName() + " on " + page.getPath()
                         + " model contains an object keyed with reserved "
                         + "name \"session\". The page model object "
                         + pop + " has been replaced with the request "
                         + " session";
            logger.warn(msg);
        }

        pop = model.put("context", request.getContextPath());
        if (pop != null && !page.isStateful()) {
            String msg = page.getClass().getName() + " on " + page.getPath()
                         + " model contains an object keyed with reserved "
                         + "name \"context\". The page model object "
                         + pop + " has been replaced with the request "
                         + " context path";
            logger.warn(msg);
        }

        Format format = page.getFormat();
        if (format != null) {
            pop = model.put("format", format);
            if (pop != null && !page.isStateful()) {
                String msg = page.getClass().getName() + " on "
                        + page.getPath()
                        + " model contains an object keyed with reserved "
                        + "name \"format\". The page model object " + pop
                        + " has been replaced with the format object";
                logger.warn(msg);
            }
        }

        String path = page.getPath();
        if (path != null) {
           pop = model.put("path", path);
            if (pop != null && !page.isStateful()) {
                String msg = page.getClass().getName() + " on "
                        + page.getPath()
                        + " model contains an object keyed with reserved "
                        + "name \"path\". The page model object " + pop
                        + " has been replaced with the page path";
                logger.warn(msg);
            }
        }

        pop = model.put("messages", page.getMessages());
        if (pop != null && !page.isStateful()) {
            String msg = page.getClass().getName() + " on " + page.getPath()
                         + " model contains an object keyed with reserved "
                         + "name \"messages\". The page model object "
                         + pop + " has been replaced with the request "
                         + " messages";
            logger.warn(msg);
        }

        return model;
    }

    /**
     * Invalidate the specified cookie and delete it from the response object.
     * <p/>
     * This method was derived from Atlassian <tt>CookieUtils</tt> method of
     * the same name, release under the BSD License.
     *
     * @param request the servlet request
     * @param response the servlet response
     * @param cookieName The name of the cookie you want to delete
     * @param path of the path the cookie you want to delete
     */
    public static void invalidateCookie(HttpServletRequest request,
            HttpServletResponse response, String cookieName, String path) {

        setCookie(request, response, cookieName, null, 0, path);
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
     * @param request the servlet request
     * @return true if this is an Ajax request, false otherwise
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        return request.getHeader(X_REQUESTED_WITH) != null
            || request.getParameter(X_REQUESTED_WITH) != null;
    }

    /**
     * Return true if the request is a multi-part content type POST request.
     *
     * @param request the page servlet request
     * @return true if the request is a multi-part content type POST request
     */
    public static boolean isMultipartRequest(HttpServletRequest request) {
        return ServletFileUpload.isMultipartContent(request);
    }

    /**
     * Invalidate the specified cookie and delete it from the response object. Deletes only cookies mapped
     * against the root "/" path. Otherwise use
     * {@link #invalidateCookie(HttpServletRequest, HttpServletResponse, String, String)}
     * <p/>
     * This method was derived from Atlassian <tt>CookieUtils</tt> method of
     * the same name, release under the BSD License.
     *
     * @see #invalidateCookie(HttpServletRequest, HttpServletResponse, String, String)
     *
     * @param request the servlet request
     * @param response the servlet response
     * @param cookieName The name of the cookie you want to delete.
     */
    public static void invalidateCookie(HttpServletRequest request,
            HttpServletResponse response, String cookieName) {

        invalidateCookie(request, response, cookieName, "/");
    }

    /**
     * Return a resource bundle using the specified base name.
     *
     * @param baseName the base name of the resource bundle, a fully qualified class name
     * @return a resource bundle for the given base name
     * @throws MissingResourceException if no resource bundle for the specified base name can be found
     */
    public static ResourceBundle getBundle(String baseName) {
        return getBundle(baseName, Locale.getDefault());
    }

    /**
     * Return a resource bundle using the specified base name and locale.
     *
     * @param baseName the base name of the resource bundle, a fully qualified class name
     * @param locale the locale for which a resource bundle is desired
     * @return a resource bundle for the given base name and locale
     * @throws MissingResourceException if no resource bundle for the specified base name can be found
     */
    public static ResourceBundle getBundle(String baseName, Locale locale) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return ResourceBundle.getBundle(baseName, locale, classLoader);
    }

    /**
     * Return the first XML child Element for the given parent Element and child
     * Element name.
     *
     * @param parent the parent element to get the child from
     * @param name the name of the child element
     * @return the first child element for the given name and parent
     */
    public static Element getChild(Element parent, String name) {
        NodeList nodeList = parent.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node instanceof Element) {
                if (node.getNodeName().equals(name)) {
                    return (Element) node;
                }
            }
        }
        return null;
    }

    /**
     * Return the list of XML child Element elements with the given name from
     * the given parent Element.
     *
     * @param parent the parent element to get the child from
     * @param name the name of the child element
     * @return the list of XML child elements for the given name
     */
    public static List<Element> getChildren(Element parent, String name) {
        List<Element> list = new ArrayList<Element>();
        NodeList nodeList = parent.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node instanceof Element) {
                if (node.getNodeName().equals(name)) {
                    list.add((Element) node);
                }
            }
        }
        return list;
    }

    /**
     * Return the InputStream for the Click configuration file <tt>click.xml</tt>.
     * This method will first lookup the <tt>click.xml</tt> under the
     * applications <tt>WEB-INF</tt> directory, and then if not found it will
     * attempt to find the configuration file on the classpath root.
     *
     * @param servletContext the servlet context to obtain the Click configuration
     *     from
     * @return the InputStream for the Click configuration file
     * @throws RuntimeException if the resource could not be found
     */
    public static InputStream getClickConfig(ServletContext servletContext) {
        InputStream inputStream =
            servletContext.getResourceAsStream(DEFAULT_APP_CONFIG);

        if (inputStream == null) {
            inputStream = ClickUtils.getResourceAsStream("/click.xml", ClickUtils.class);
            if (inputStream == null) {
                String msg =
                    "could not find click app configuration file: "
                    + DEFAULT_APP_CONFIG + " or click.xml on classpath";
                throw new RuntimeException(msg);
            }
        }

        return inputStream;
    }

    /**
     * Return the application configuration service instance from the given
     * servlet context.
     *
     * @param servletContext the servlet context to get the config service instance
     * @return the application config service instance
     */
    public static ConfigService getConfigService(ServletContext servletContext) {
        ConfigService configService = (ConfigService)
            servletContext.getAttribute(ConfigService.CONTEXT_NAME);

        if (configService != null) {
            return configService;

        } else {
            String msg =
                "could not find ConfigService in the SerlvetContext under the"
                + " name '" + ConfigService.CONTEXT_NAME + "'.\nThis can occur"
                + " if ClickUtils.getConfigService() is called before"
                + " ClickServlet is initialized by the servlet container.\n"
                + "To fix ensure that ClickServlet is loaded at startup by"
                + " editing your web.xml and setting the load-on-startup to 0:\n\n"
                + " <servlet>\n"
                + "   <servlet-name>ClickServlet</servlet-name>\n"
                + "   <servlet-class>org.apache.click.ClickServlet</servlet-class>\n"
                + "   <load-on-startup>0</load-on-startup>\n"
                + " </servlet>\n";

            throw new RuntimeException(msg);
        }
    }

    /**
     * Returns the specified Cookie object, or null if the cookie does not exist.
     * <p/>
     * This method was derived from Atlassian <tt>CookieUtils</tt> method of
     * the same name, release under the BSD License.
     *
     * @param request the servlet request
     * @param name the name of the cookie
     * @return the Cookie object if it exists, otherwise null
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie cookies[] = request.getCookies();

        if (cookies == null || name == null || name.length() == 0) {
            return null;
        }

        //Otherwise, we have to do a linear scan for the cookie.
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie;
            }
        }

        return null;
    }

    /**
     * Sets the given cookie values in the servlet response.
     * <p/>
     * This will also put the cookie in a list of cookies to send with this request's response
     * (so that in case of a redirect occurring down the chain, the first filter
     * will always try to set this cookie again)
     * <p/>
     * The cookie secure flag is set if the request is secure.
     * <p/>
     * This method was derived from Atlassian <tt>CookieUtils</tt> method of
     * the same name, release under the BSD License.
     *
     * @param request the servlet request
     * @param response the servlet response
     * @param name the cookie name
     * @param value the cookie value
     * @param maxAge the maximum age of the cookie in seconds. A negative
     * value will expire the cookie at the end of the session, while 0 will delete
     * the cookie.
     * @param path the cookie path
     * @return the Cookie object created and set in the response
     */
    public static Cookie setCookie(HttpServletRequest request, HttpServletResponse response,
            String name, String value, int maxAge, String path) {

        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath(path);
        cookie.setSecure(request.isSecure());
        response.addCookie(cookie);

        return cookie;
    }

    /**
     * Returns the value of the specified cookie as a String. If the cookie
     * does not exist, the method returns null.
     * <p/>
     * This method was derived from Atlassian <tt>CookieUtils</tt> method of
     * the same name, release under the BSD License.
     *
     * @param request the servlet request
     * @param name the name of the cookie
     * @return the value of the cookie, or null if the cookie does not exist.
     */
    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie cookie = getCookie(request, name);

        if (cookie != null) {
            return cookie.getValue();
        }

        return null;
    }

    /**
     * Return the Click Framework version string.
     *
     * @return the Click Framework version string
     */
    public static String getClickVersion() {
        ResourceBundle bundle = getBundle("click-control");
        return bundle.getString("click-version");
    }

    /**
     * Return the web application version string.
     *
     * @return the web application version string
     */
    public static String getApplicationVersion() {
        return applicationVersion;
    }

    /**
     * Set the web application version string.
     *
     * @param applicationVersion the web application version string
     */
    public static void setApplicationVersion(String applicationVersion) {
        ClickUtils.applicationVersion = applicationVersion;
        cachedApplicationVersionIndicator = null;
    }

    /**
     * Return Click's version indicator for static web resources
     * (eg css, js and image files) if resource versioning is active,
     * otherwise this method will return an empty string.
     * <p/>
     * Click's resource versioning becomes active under the following
     * conditions:
     * <ul>
     * <li>the {@link #ENABLE_RESOURCE_VERSION} request attribute must be set
     * to <tt>true</tt></li>
     * <li>the application mode must be either "production" or "profile"</li>
     * </ul>
     *
     * The version indicator is based on the current Click release version.
     * For example when using Click 1.4 this method will return the string
     * <tt>"_1.4"</tt>.
     *
     * @param context the request context
     * @return a version indicator for web resources
     */
    public static String getResourceVersionIndicator(Context context) {
        if (cachedResourceVersionIndicator != null) {
            return cachedResourceVersionIndicator;
        }

        ConfigService configService = getConfigService(context.getServletContext());

        boolean isProductionModes = configService.isProductionMode()
            || configService.isProfileMode();

        if (isProductionModes
            && isEnableResourceVersion(context)) {

            cachedResourceVersionIndicator = RESOURCE_VERSION_INDICATOR;
            return cachedResourceVersionIndicator;

        } else {
            return "";
        }
    }

    /**
     * If resource versioning is active this method will return the
     * application version indicator for static web resources
     * (eg JavaScript and Css) otherwise this method will return an empty string.
     * <p/>
     * Application resource versioning becomes active under the following
     * conditions:
     * <ul>
     * <li>the {@link #ENABLE_RESOURCE_VERSION} request attribute must be set
     * to <tt>true</tt></li>
     * <li>the application mode must be either "production" or "profile"</li>
     * </ul>
     *
     * The version indicator is based on the application version.
     * For example if the application version is 1.2 this method will
     * return the string <tt>"_1.2"</tt>.
     * <p/>
     * The application version can be set through the static method
     * {@link #setApplicationVersion(java.lang.String)}.
     *
     * @return an application version indicator for web resources
     */
    public static String getApplicationResourceVersionIndicator() {
        // Return the cached version first
        if (cachedApplicationVersionIndicator != null) {
            return cachedApplicationVersionIndicator;
        }

        // Check if the Context has been set
        if (Context.hasThreadLocalContext()) {

            Context context = Context.getThreadLocalContext();
            ConfigService configService = ClickUtils.getConfigService(context.getServletContext());

            boolean isProductionModes = configService.isProductionMode()
                || configService.isProfileMode();

            if (isProductionModes && ClickUtils.isEnableResourceVersion(context)) {
                String version = getApplicationVersion();
                if (StringUtils.isNotBlank(version)) {
                    cachedApplicationVersionIndicator = VERSION_INDICATOR_SEP
                        + version;
                    return cachedApplicationVersionIndicator;
                }
            }
        }
        return "";
    }

    /**
     * Return the given control CSS selector or null if no selector can be found.
     * <p/>
     * <b>Please note:</b> it is highly recommended to set a control's ID
     * attribute when dealing with Ajax requests.
     * <p/>
     * The algorith returns the selector in the following order:
     * <ol>
     *   <li>if control.getId() is set, prepend it with a '#' char
     *   and return the value. An example selector will be: <tt>#field-id</tt></li>
     *   <li>if control.getName() is not null the following checks are made:
     *     <ol>
     *       <li>if the control is of type {@link org.apache.click.control.ActionLink},
     *       its "<tt>class</tt>" attribute selector will be returned. For example:
     *       <tt>input[class=red]</tt>.
     *       <b>Please note:</b> if no class attribute is set, this method will
     *       automatically set link's "class" attribute to its name value and
     *       prefix the name with an underscore '_'. For example:
     *       <tt>input[class=_my-link]</tt>.
     *       </li>
     *       <li>if the control is not an ActionLink, it is assumed the control
     *       will render its "<tt>name</tt>" attribute and the name attribute
     *       selector will be returned. For example: <tt>input[name=my-button]</tt>.
     *       </li>
     *     </ol>
     *   </li>
     *   <li>otherwise this method returns null.
     *   </li>
     * </ol>
     *
     * @param control the control which CSS selector to return
     * @return the control CSS selector or null if no selector can be found
     * @throws IllegalArgumentException if control is null
     */
    public static String getCssSelector(Control control) {
        if (control == null) {
            throw new IllegalArgumentException("Control cannot be null");
        }

        String id = control.getId();
        String name = control.getName();
        String cssSelector = null;

        if (StringUtils.isNotBlank(id)) {
            cssSelector = '#' + id;
        } else if (StringUtils.isNotBlank(name)) {
            String tag = null;

            // Try and create a more specific selector by retrieving the
            // control's tag
            if (control instanceof AbstractControl) {
                tag = StringUtils.defaultString(((AbstractControl) control).getTag());
            }

            HtmlStringBuffer buffer = new HtmlStringBuffer(20);

            // Handle ActionLink (perhaps other link controls too?) differently
            // as it doesn't render the "name" attribute. The "name" attribute
            // is used by links for bookmarking purposes. Instead set the class
            // attribute to the link's name and use that as the selector.
            if (control instanceof ActionLink) {
                ActionLink link = (ActionLink) control;
                if (!link.hasAttribute("class")) {
                    link.setAttribute("class", '_' + name);
                }
                buffer.append(tag).append("[class*=");
                buffer.append(link.getAttribute("class")).append("]");

            } else {
                buffer.append(tag).append("[name=");
                buffer.append(name).append("]");
            }
            cssSelector = buffer.toString();
        }
        return cssSelector;
    }

    /**
     * Populate the given object's attributes with the Form's field values.
     * <p/>
     * The specified Object can either be a POJO (plain old java object) or
     * a {@link java.util.Map}. If a POJO is specified, its attributes are
     * populated from  matching form fields. If a map is specified, its
     * key/value pairs are populated from matching form fields.
     *
     * @param form the Form to obtain field values from
     * @param object the object to populate with field values
     * @param debug log debug statements when populating the object
     */
    public static void copyFormToObject(Form form, Object object,
            boolean debug) {

        ContainerUtils.copyContainerToObject(form, object);
    }

    /**
     * Populate the given Form field values with the object's attributes.
     * <p/>
     * The specified Object can either be a POJO (plain old java object) or
     * a {@link java.util.Map}. If a POJO is specified, its attributes are
     * copied to matching form fields. If a map is specified, its key/value
     * pairs are copied to matching form fields.
     *
     * @param object the object to obtain attribute values from
     * @param form the Form to populate
     * @param debug log debug statements when populating the form
     */
    public static void copyObjectToForm(Object object, Form form,
            boolean debug) {

        ContainerUtils.copyObjectToContainer(object, form);
    }

    /**
     * Deploy the specified classpath resource to the given target directory
     * under the web application root directory.
     * <p/>
     * This method will <b>not</b> override any existing resources found in the
     * target directory.
     * <p/>
     * If an IOException or SecurityException occurs this method will log a
     * warning message.
     *
     * @param servletContext the web applications servlet context
     * @param resource the classpath resource name
     * @param targetDir the target directory to deploy the resource to
     */
    public static void deployFile(ServletContext servletContext,
        String resource, String targetDir) {

        if (servletContext == null) {
            throw new IllegalArgumentException("Null servletContext parameter");
        }

        if (StringUtils.isBlank(resource)) {
            String msg = "Null resource parameter not defined";
            throw new IllegalArgumentException(msg);
        }

        String realTargetDir = servletContext.getRealPath("/") + File.separator;

        if (StringUtils.isNotBlank(targetDir)) {
            realTargetDir = realTargetDir + targetDir;
        }


        LogService logger = getConfigService(servletContext).getLogService();

        try {

            // Create files deployment directory
            File directory = new File(realTargetDir);
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    String msg =
                        "could not create deployment directory: " + directory;
                    throw new IOException(msg);
                }
            }

            String destination = resource;
            int index = resource.lastIndexOf('/');
            if (index != -1) {
                destination = resource.substring(index + 1);
            }

            destination = realTargetDir + File.separator + destination;

            File destinationFile = new File(destination);

            if (!destinationFile.exists()) {
                InputStream inputStream =
                    getResourceAsStream(resource, ClickUtils.class);

                if (inputStream != null) {
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(destinationFile);
                        byte[] buffer = new byte[1024];
                        while (true) {
                            int length = inputStream.read(buffer);
                            if (length <  0) {
                                break;
                            }
                            fos.write(buffer, 0, length);
                        }

                        if (logger.isTraceEnabled()) {
                            int lastIndex =
                                destination.lastIndexOf(File.separatorChar);
                            if (lastIndex != -1) {
                                destination =
                                    destination.substring(lastIndex + 1);
                            }
                            String msg =
                                "deployed " + targetDir + "/" + destination;
                            logger.trace(msg);
                        }

                    } finally {
                        close(fos);
                        close(inputStream);
                    }
                } else {
                    String msg =
                        "could not locate classpath resource: " + resource;
                    throw new IOException(msg);
                }
            }

        } catch (IOException ioe) {
            String msg =
                "error occurred deploying resource " + resource
                + ", error " + ioe;
            logger.warn(msg);

        } catch (SecurityException se) {
            String msg =
                "error occurred deploying resource " + resource
                + ", error " + se;
            logger.warn(msg);
        }
    }

    /**
     * Deploy the specified classpath resources to the given target directory
     * under the web application root directory.
     *
     * @param servletContext the web applications servlet context
     * @param resources the array of classpath resource names
     * @param targetDir the target directory to deploy the resource to
     */
    public static void deployFiles(ServletContext servletContext,
            String[] resources, String targetDir) {

        if (resources == null) {
            throw new IllegalArgumentException("Null resources parameter");
        }

        for (String resource : resources) {
            ClickUtils.deployFile(servletContext, resource, targetDir);
        }
    }

    /**
     * Deploys required files (from a file list) for a control that repsects a specific convention.
     * <p/>
     * <b>Convention:</b>
     * <p/>
     * There's a descriptor file generated by the <code>tools/standalone/dev-tasks/ListFilesTask</code>.
     * The files to deploy are all in a subdirectory placed in the same directory with the control.
     * See documentation for more details. <p/>
     *
     * <b>Usage:</b><p/>
     * In your Control simply use the code below, and everything should work automatically.
     * <pre class="prettyprint">
     * public void onDeploy(ServletContext servletContext) {
     *    ClickUtils.deployFileList(servletContext, HeavyControl.class, "click");
     * } </pre>
     *
     * @param servletContext the web applications servlet context
     * @param controlClass the class of the Control that has files for deployment
     * @param targetDir target directory where to deploy the files to. In most cases this
     * is only the reserved directory <code>click</code>
     */
    public static void deployFileList(ServletContext servletContext, Class controlClass, String targetDir) {

        String packageName = ClassUtils.getPackageName(controlClass);
        packageName = StringUtils.replaceChars(packageName, '.', '/');
        packageName = "/" + packageName;
        String controlName = ClassUtils.getShortClassName(controlClass);

        ConfigService configService = getConfigService(servletContext);
        LogService logService = configService.getLogService();
        String descriptorFile = packageName + "/" + controlName + ".files";
        logService.debug("Use deployment descriptor file:" + descriptorFile);

        try {
            InputStream is = getResourceAsStream(descriptorFile, ClickUtils.class);
            List fileList = IOUtils.readLines(is);
            if (fileList == null || fileList.isEmpty()) {
                logService.info("there are no files to deploy for control " + controlClass.getName());
                return;
            }

            // a target dir list is required cause the ClickUtils.deployFile() is too inflexible to autodetect
            // required subdirectories.
            List targetDirList = new ArrayList(fileList.size());
            for (int i = 0; i < fileList.size(); i++) {
                String filePath = (String) fileList.get(i);
                String destination = "";
                int index = filePath.lastIndexOf('/');
                if (index != -1) {
                    destination = filePath.substring(0, index + 1);
                }
                targetDirList.add(i, targetDir + "/" + destination);
                fileList.set(i, packageName + "/" + filePath);
            }

            for (int i = 0; i < fileList.size(); i++) {
                String source = (String) fileList.get(i);
                String targetDirName = (String) targetDirList.get(i);
                ClickUtils.deployFile(servletContext, source, targetDirName);
            }

        } catch (IOException e) {
            String msg = "error occurred getting resource " + descriptorFile + ", error " + e;
            logService.warn(msg);
        }
    }

    /**
     * Return an encoded version of the <tt>Serializable</tt> object. The object
     * will be serialized, compressed and Base 64 encoded.
     *
     * @param object the object to encode
     * @return a serialized, compressed and Base 64 string encoding of the
     * given object
     * @throws IOException if an I/O error occurs
     * @throws IllegalArgumentException if the object parameter is null, or if
     *      the object is not Serializable
     */
    public static String encode(Object object) throws IOException {
        if (object == null) {
            throw new IllegalArgumentException("null object parameter");
        }
        if (!(object instanceof Serializable)) {
            throw new IllegalArgumentException("parameter not Serializable");
        }

        ByteArrayOutputStream bos = null;
        GZIPOutputStream gos = null;
        ObjectOutputStream oos = null;

        try {
            bos = new ByteArrayOutputStream();
            gos = new GZIPOutputStream(bos);
            oos = new ObjectOutputStream(gos);

            oos.writeObject(object);

        } finally {
            close(oos);
            close(gos);
            close(bos);
        }

        Base64 base64 = new Base64();

        try {
            byte[] byteData = base64.encode(bos.toByteArray());

            return new String(byteData);

        } catch (Throwable t) {
            String message =
                "error occurred Base64 encoding: " + object + " : " + t;
            throw new IOException(message);
        }
    }

    /**
     * Return an object from the {@link #encode(Object)} string.
     *
     * @param string the encoded string
     * @return an object from the encoded
     * @throws ClassNotFoundException if the class could not be instantiated
     * @throws IOException if an data I/O error occurs
     */
    public static Object decode(String string)
            throws ClassNotFoundException, IOException {

        Base64 base64 = new Base64();
        byte[] byteData = null;

        try {
            byteData = base64.decode(string.getBytes());

        } catch (Throwable t) {
            String message =
                "error occurred Base64 decoding: " + string + " : " + t;
            throw new IOException(message);
        }

        ByteArrayInputStream bis = null;
        GZIPInputStream gis = null;
        ObjectInputStream ois = null;
        try {
            bis = new ByteArrayInputStream(byteData);
            gis = new GZIPInputStream(bis);
            ois = new ObjectInputStream(gis);

            return ois.readObject();

        } finally {
            close(ois);
            close(gis);
            close(bis);
        }
    }

    /**
     * Builds a cookie string containing a username and password.
     * <p/>
     * Note: with open source this is not really secure, but it prevents users
     * from snooping the cookie file of others and by changing the XOR mask and
     * character offsets, you can easily tweak results.
     * <p/>
     * This method was derived from Atlassian <tt>CookieUtils</tt> method of
     * the same name, release under the BSD License.
     *
     * @param username the username
     * @param password the password
     * @param xorMask the XOR mask to encrypt the value with, must be same as
     *      as the value used to decrypt the cookie password
     * @return String encoding the input parameters, an empty string if one of
     *      the arguments equals <code>null</code>
     */
    public static String encodePasswordCookie(String username, String password, int xorMask) {
        String encoding = new String(new char[]{DELIMITER, ENCODE_CHAR_OFFSET1, ENCODE_CHAR_OFFSET2});

        return encodePasswordCookie(username, password, encoding, xorMask);
    }

    /**
     * Builds a cookie string containing a username and password, using offsets
     * to customize the encoding.
     * <p/>
     * Note: with open source this is not really secure, but it prevents users
     * from snooping the cookie file of others and by changing the XOR mask and
     * character offsets, you can easily tweak results.
     * <p/>
     * This method was derived from Atlassian <tt>CookieUtils</tt> method of
     * the same name, release under the BSD License.
     *
     * @param username the username
     * @param password the password
     * @param encoding a String used to customize cookie encoding (only the first 3 characters are used)
     * @param xorMask the XOR mask to encrypt the value with, must be same as
     *      as the value used to decrypt the cookie password
     * @return String encoding the input parameters, an empty string if one of
     *      the arguments equals <code>null</code>.
     */
    public static String encodePasswordCookie(String username, String password, String encoding, int xorMask) {
        StringBuffer buf = new StringBuffer();

        if (username != null && password != null) {

            char offset1 = (encoding != null && encoding.length() > 1)
                ? encoding.charAt(1) : ENCODE_CHAR_OFFSET1;

            char offset2 = (encoding != null && encoding.length() > 2)
                ? encoding.charAt(2) : ENCODE_CHAR_OFFSET2;

            byte[] bytes = (username + DELIMITER + password).getBytes();
            int b;

            for (int n = 0; n < bytes.length; n++) {
                b = bytes[n] ^ (xorMask + n);
                buf.append((char) (offset1 + (b & 0x0F)));
                buf.append((char) (offset2 + ((b >> 4) & 0x0F)));
            }
        }

        return buf.toString();
    }

    /**
     * Decodes a cookie string containing a username and password.
     * <p/>
     * This method was derived from Atlassian <tt>CookieUtils</tt> method of
     * the same name, release under the BSD License.
     *
     * @param cookieVal the encoded cookie username and password value
     * @param xorMask the XOR mask to decrypt the value with, must be same as
     *      as the value used to encrypt the cookie password
     * @return String[] containing the username at index 0 and the password at
     *      index 1, or <code>{ null, null }</code> if cookieVal equals
     *      <code>null</code> or the empty string.
     */
    public static String[] decodePasswordCookie(String cookieVal, int xorMask) {
        String encoding = new String(new char[]{DELIMITER, ENCODE_CHAR_OFFSET1, ENCODE_CHAR_OFFSET2});

        return decodePasswordCookie(cookieVal, encoding, xorMask);
    }

    /**
     * Decodes a cookie string containing a username and password.
     * <p/>
     * This method was derived from Atlassian <tt>CookieUtils</tt> method of
     * the same name, release under the BSD License.
     *
     * @param cookieVal the encoded cookie username and password value
     * @param encoding  a String used to customize cookie encoding (only the first 3 characters are used)
     *      - should be the same string you used to encode the cookie!
     * @param xorMask the XOR mask to decrypt the value with, must be same as
     *      as the value used to encrypt the cookie password
     * @return String[] containing the username at index 0 and the password at
     *      index 1, or <code>{ null, null }</code> if cookieVal equals
     *      <code>null</code> or the empty string.
     */
    public static String[] decodePasswordCookie(String cookieVal, String encoding,
            int xorMask) {

        // Check that the cookie value isn't null or zero-length
        if (cookieVal == null || cookieVal.length() <= 0) {
            return null;
        }

        char offset1 = (encoding != null && encoding.length() > 1)
            ? encoding.charAt(1) : ENCODE_CHAR_OFFSET1;

        char offset2 = (encoding != null && encoding.length() > 2)
            ? encoding.charAt(2) : ENCODE_CHAR_OFFSET2;

        // Decode the cookie value
        char[] chars = cookieVal.toCharArray();
        byte[] bytes = new byte[chars.length / 2];
        int b;

        for (int n = 0, m = 0; n < bytes.length; n++) {
            b = chars[m++] - offset1;
            b |= (chars[m++] - offset2) << 4;
            bytes[n] = (byte) (b ^ (xorMask + n));
        }

        cookieVal = new String(bytes);
        int pos = cookieVal.indexOf(DELIMITER);

        String username = (pos < 0) ? "" : cookieVal.substring(0, pos);
        String password = (pos < 0) ? "" : cookieVal.substring(pos + 1);

        return new String[]{username, password};
    }

    /**
     * URL encode the specified value using the "UTF-8" encoding scheme.
     * <p/>
     * For example <tt>(http://host?name=value with spaces)</tt> will become
     * <tt>(http://host?name=value%20with%20spaces)</tt>.
     * <p/>
     * This method uses {@link URLEncoder#encode(java.lang.String, java.lang.String)}
     * internally.
     *
     * @param value the value to encode using "UTF-8"
     * @return an encoded URL string
     */
    public static String encodeURL(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("Null object parameter");
        }

        try {
            return URLEncoder.encode(value.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * URL decode the specified value using the "UTF-8" encoding scheme.
     * <p/>
     * For example <tt>(http://host?name=value%20with%20spaces)</tt> will become
     * <tt>(http://host?name=value with spaces)</tt>.
     * <p/>
     * This method uses {@link URLDecoder#decode(java.lang.String, java.lang.String)}
     * internally.
     *
     * @param value the value to decode using "UTF-8"
     * @return an encoded URL string
     */
    public static String decodeURL(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("Null object parameter");
        }

        try {
            return URLDecoder.decode(value.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Return an encoded URL value for the given object using the context
     * request character encoding.
     * <p/>
     * For example <tt>(http://host?name=value with spaces)</tt> will become
     * <tt>(http://host?name=value%20with%20spaces)</tt>.
     * <p/>
     * This method uses
     * {@link URLEncoder#encode(java.lang.String, java.lang.String)} internally.
     *
     * @param object the object value to encode as a URL string
     * @param context the context providing the request character encoding
     * @return an encoded URL string
     */
    public static String encodeUrl(Object object, Context context) {
        if (object == null) {
            throw new IllegalArgumentException("Null object parameter");
        }
        if (context == null) {
            throw new IllegalArgumentException("Null context parameter");
        }

        String charset = context.getRequest().getCharacterEncoding();

        try {
            if (charset == null) {
                return URLEncoder.encode(object.toString());

            } else {
                return URLEncoder.encode(object.toString(), charset);
            }

        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException(uee);
        }
    }

    /**
     * Return a HTML escaped string for the given string value.
     *
     * @param value the string value to escape
     * @return the HTML escaped string value
     */
    public static String escapeHtml(String value) {
        if (requiresHtmlEscape(value)) {

            HtmlStringBuffer buffer = new HtmlStringBuffer(value.length() * 2);

            buffer.appendEscaped(value);

            return buffer.toString();

        } else {
            return value;
        }
    }

    /**
     * Return true if the control has a submitted request value, false otherwise.
     *
     * @param control the control which request parameter to check
     * @return true if the control has a submitted request value, false otherwise
     */
    public static boolean hasRequestParameter(Control control) {
        Context context = Context.getThreadLocalContext();
        if (canBind(control, context)) {
            return context.hasRequestParameter(control.getName());
        }
        return false;
    }

    /**
     * Invoke the named method on the given object and return the boolean
     * result.
     *
     * @see org.apache.click.Control#setListener(Object, String)
     *
     * @param listener the object with the method to invoke
     * @param method the name of the method to invoke
     * @return true if the listener method returned true
     */
    public static boolean invokeListener(Object listener, String method) {

        Object result = invokeMethod(listener, method);

        if (result instanceof Boolean) {
            return (Boolean) result;

        } else {

            Method targetMethod = null;
            try {
                targetMethod = listener.getClass().getMethod(method);

                String msg =
                    "Invalid listener method, missing boolean return type: "
                    + targetMethod;
                throw new RuntimeException(msg);
            } catch (Exception e) {
                String msg = "Exception occurred invoking public method: " + targetMethod;
                throw new RuntimeException(msg, e);
            }
        }
    }

    /**
     * Invoke the named method on the given target and return the Object result.
     *
     * @param target the target object with the method to invoke
     * @param method the name of the method to invoke
     * @return a Partial response
     */
    public static Partial invokeAction(Object target, String method) {

        Object result = invokeMethod(target, method);

        if (result == null || result instanceof Partial) {
            return (Partial) result;

        } else {

            Method targetMethod = null;
            try {
                targetMethod = target.getClass().getMethod(method);

                String msg =
                    "Invalid target method, missing Partial return type: "
                    + targetMethod;
                throw new RuntimeException(msg);
            } catch (Exception e) {
                String msg = "Exception occurred invoking public method: " + targetMethod;
                throw new RuntimeException(msg, e);
            }
        }
    }

    /**
     * Return true if static web content resource versioning is enabled.
     *
     * @param context the request context
     * @return true if static web content resource versioning is enabled
     */
    public static boolean isEnableResourceVersion(Context context) {
        return "true".equals(context.getRequestAttribute(ENABLE_RESOURCE_VERSION));
    }

    /**
     * Return the value string limited to maxlength characters. If the string
     * gets curtailed, "..." is appended to it.
     * <p/>
     * Adapted from Velocity Tools Formatter.
     *
     * @param value the string value to limit the length of
     * @param maxlength the maximum string length
     * @return a length limited string
     */
    public static String limitLength(String value, int maxlength) {
        return limitLength(value, maxlength, "...");
    }

    /**
     * Return the value string limited to maxlength characters. If the string
     * gets curtailed and the suffix parameter is appended to it.
     * <p/>
     * Adapted from Velocity Tools Formatter.
     *
     * @param value the string value to limit the length of
     * @param maxlength the maximum string length
     * @param suffix the suffix to append to the length limited string
     * @return a length limited string
     */
    public static String limitLength(String value, int maxlength, String suffix) {
        String ret = value;
        if (value.length() > maxlength) {
            ret = value.substring(0, maxlength - suffix.length()) + suffix;
        }
        return ret;
    }

    /**
     * Return the application LogService instance using thread local Context
     * to perform the lookup.
     *
     * @return the application LogService instance
     */
    public static LogService getLogService() {
        Context context = Context.getThreadLocalContext();
        ServletContext servletContext = context.getServletContext();
        ConfigService configService = getConfigService(servletContext);
        LogService logService = configService.getLogService();
        return logService;
    }

    /**
     * Return the list of Fields for the given Form, including any Fields
     * contained in FieldSets. The list of returned fields will exclude any
     * <tt>Button</tt>, <tt>FieldSet</tt> or <tt>Label</tt> fields.
     *
     * @param form the form to obtain the fields from
     * @return the list of contained form fields
     */
    public static List getFormFields(Form form) {
        if (form == null) {
            throw new IllegalArgumentException("Null form parameter");
        }
        return ContainerUtils.getInputFields(form);
    }

    /**
     * Return the mime-type or content-type for the given filename.
     *
     * @param filename the filename to obtain the mime-type for
     * @return the mime-type for the given filename, or null if not found
     */
    public static String getMimeType(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("null filename parameter");
        }

        int index = filename.lastIndexOf(".");

        if (index != -1) {
            String ext = filename.substring(index + 1);

            try {
                ResourceBundle bundle = getBundle("org/apache/click/util/mime-type");

                return bundle.getString(ext.toLowerCase());

            } catch (MissingResourceException mre) {
                return null;
            }

        } else {
            return null;
        }
    }

    /**
     * Return the given control's top level parent's localized messages Map.
     * <p/>
     * This method will walk up to the control's parent page object and
     * return pages messages. If the control's top level parent is a control
     * then the parent's messages map will be returned. If the top level
     * parent is not a Page or Control instance an empty map will be returned.
     *
     * @param control the control to get the parent messages Map for
     * @return the top level parent's Map of localized messages
     */
    public static Map<String, String> getParentMessages(Control control) {
        if (control == null) {
            throw new IllegalArgumentException("Null control parameter");
        }

        Object parent = control.getParent();
        if (parent == null) {
            return Collections.emptyMap();

        } else {
            while (parent != null) {
                if (parent instanceof Control) {
                    control = (Control) parent;
                    parent = control.getParent();

                    if (parent == null) {
                        return control.getMessages();
                    }

                } else if (parent instanceof Page) {
                    Page page = (Page) parent;
                    return page.getMessages();

                } else if (parent != null) {
                    // Unknown parent class
                    return Collections.emptyMap();
                }
            }
        }

        return Collections.emptyMap();
    }

    /**
     * Return the given control's top level parent's localized message for the
     * specified name.
     * <p/>
     * This method will walk up to the control's parent page object and for each
     * parent control found, look for a message of the specified name. A
     * message found in a parent control will override the message of a child
     * control.
     * <p/>
     * Given the following property files:
     * <p/>
     * MyPage.properties
     * <pre class="prettyprint">
     * myfield.label=Page </pre>
     *
     * and MyForm.properties
     * <pre class="prettyprint">
     * myfield.label=Form </pre>
     *
     * and a the following snippet:
     *
     * <pre class="prettyprint">
     * public MyPage extends Page {
     *     public void onInit() {
     *         MyForm form = new MyForm("form");
     *         TextField field = new TextField("myfield");
     *         form.add(field);
     *
     *         //1.
     *         System.out.println(ClickUtils.getParentMessage(field, "myfield.label"));
     *
     *         addControl(form);
     *
     *         //2.
     *         System.out.println(ClickUtils.getParentMessage(field, "myfield.label"));
     *     }
     * }
     * </pre>
     *
     * The first (1.) println statement above will output <tt>Form</tt> because
     * at that stage MyForm is the highest level parent of field.
     * <tt>getParentMessage</tt> will find the property <tt>myfield.label</tt>
     * in the MyForm message properties and return <tt>Form</tt>
     * <p/>
     * The second (2.) println statement will output <tt>Page</tt> as now
     * MyPage is the highest level parent. On its first pass up the hierarchy,
     * <tt>getParentMessage</tt> will find the property <tt>myfield.label</tt>
     * in the MyForm message properties and on its second pass will find the
     * same property in MyPage message properties. As MyPage is higher up the
     * hierarchy than MyForm, MyPage will override MyForm and the property value
     * will be <tt>Page</tt>.
     *
     * @param control the control to get the parent message for
     * @param name the specific property name to find
     * @return the top level parent's Map of localized messages
     */
    public static String getParentMessage(Control control, String name) {
        if (control == null) {
            throw new IllegalArgumentException("Null control parameter");
        }
        if (name == null) {
            throw new IllegalArgumentException("Null name parameter");
        }

        Object parent = control.getParent();
        if (parent == null) {
            return null;

        } else {
            String message = null;
            while (parent != null) {
                if (parent instanceof Control) {
                    control = (Control) parent;
                    if (control != null) {
                        if (control.getMessages().containsKey(name)) {
                            message = control.getMessages().get(name);
                        }
                    }

                    parent = control.getParent();
                    if (parent == null) {
                        return message;
                    }

                } else if (parent instanceof Page) {
                    Page page = (Page) parent;
                    if (page.getMessages().containsKey(name)) {
                        message = page.getMessages().get(name);
                    }
                    return message;

                } else if (parent != null) {
                    // Unknown parent class
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * Get the parent page of the given control or null if the control has no
     * parent. This method will walk up the control's parent hierarchy to
     * find its parent page.
     *
     * @param control the control to get the parent page from
     * @return the parent page of the control or null if the control has no
     * parent
     */
    public static Page getParentPage(Control control) {
        Object parent = control.getParent();

        while (parent != null) {
            if (parent instanceof Control) {
                control = (Control) parent;
                parent = control.getParent();

            } else if (parent instanceof Page) {
                return (Page) parent;

            } else if (parent != null) {
                throw new RuntimeException("Invalid parent class");
            }
        }

        return null;
    }

    /**
     * Return an ordered map of request parameters from the given request.
     *
     * @param request the servlet request to obtain request parameters from
     * @return the ordered map of request parameters
     */
    public static Map getRequestParameterMap(HttpServletRequest request) {

        TreeMap requestParams = new TreeMap();

        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String name = paramNames.nextElement().toString();

            String[] values = request.getParameterValues(name);

            if (values.length == 1) {
                requestParams.put(name, values[0]);

            } else {
                requestParams.put(name, values);
            }
        }

        return requestParams;
    }

    /**
     * Return the page resource path from the request. For example:
     * <pre class="codeHtml">
     * <span class="blue">http://www.mycorp.com/banking/secure/login.htm</span>  ->  <span class="red">/secure/login.htm</span> </pre>
     *
     * @param request the page servlet request
     * @return the page resource path from the request
     */
    public static String getResourcePath(HttpServletRequest request) {
        // Adapted from VelocityViewServlet.handleRequest() method:

        // If we get here from RequestDispatcher.include(), getServletPath()
        // will return the original (wrong) URI requested.  The following
        // special attribute holds the correct path.  See section 8.3 of the
        // Servlet 2.3 specification.

        String path = (String)
            request.getAttribute("javax.servlet.include.servlet_path");

        // Also take into account the PathInfo stated on
        // SRV.4.4 Request Path Elements.
        String info = (String)
            request.getAttribute("javax.servlet.include.path_info");

        if (path == null) {
            path = request.getServletPath();
            info = request.getPathInfo();
        }

        if (info != null) {
            path += info;
        }

        return path;
    }

    /**
     * Return the requestURI from the request. For example:
     * <pre class="codeHtml">
     * <span class="blue">http://www.mycorp.com/banking/secure/login.htm</span>  ->  <span class="red">/banking/secure/login.htm</span> </pre>
     *
     * @param request the page servlet request
     * @return the requestURI from the request
     */
    public static String getRequestURI(HttpServletRequest request) {
        // CLK-334. Adapted from VelocityViewServlet.handleRequest() method:

        // If we get here from RequestDispatcher.include(), getServletPath()
        // will return the original (wrong) URI requested.  The following
        // special attribute holds the correct path.  See section 8.3 of the
        // Servlet 2.3 specification.

        String requestURI = (String) request.getAttribute("javax.servlet.include.request_uri");

        if (requestURI == null) {
            requestURI = request.getRequestURI();
        }

        if (requestURI != null && requestURI.endsWith(".jsp")) {
            requestURI = StringUtils.replace(requestURI, ".jsp", ".htm");
        }

        return requestURI;
    }

    /**
     * Finds a resource with a given name. This method returns null if no
     * resource with this name is found.
     * <p>
     * This method uses the current <tt>Thread</tt> context <tt>ClassLoader</tt> to find
     * the resource. If the resource is not found the class loader of the given
     * class is then used to find the resource.
     *
     * @param name the name of the resource
     * @param aClass the class lookup the resource against, if the resource is
     *     not found using the current <tt>Thread</tt> context <tt>ClassLoader</tt>.
     * @return the input stream of the resource if found or null otherwise
     */
    public static InputStream getResourceAsStream(String name, Class aClass) {
        Validate.notNull(name, "Parameter name is null");
        Validate.notNull(aClass, "Parameter aClass is null");

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        InputStream inputStream = classLoader.getResourceAsStream(name);
        if (inputStream == null) {
            inputStream = aClass.getResourceAsStream(name);
        }

        return inputStream;
    }

    /**
     * Finds a resource with a given name. This method returns null if no
     * resource with this name is found.
     * <p>
     * This method uses the current <tt>Thread</tt> context <tt>ClassLoader</tt> to find
     * the resource. If the resource is not found the class loader of the given
     * class is then used to find the resource.
     *
     * @param name the name of the resource
     * @param aClass the class lookup the resource against, if the resource is
     *     not found using the current <tt>Thread</tt> context <tt>ClassLoader</tt>.
     * @return the URL of the resource if found or null otherwise
     */
    public static URL getResource(String name, Class aClass) {
        Validate.notNull(name, "Parameter name is null");
        Validate.notNull(aClass, "Parameter aClass is null");

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        URL url = classLoader.getResource(name);
        if (url == null) {
            url = aClass.getResource(name);
        }

        return url;
    }

    /**
     * Return the getter method name for the given property name.
     *
     * @param property the property name
     * @return the getter method name for the given property name.
     */
    public static String toGetterName(String property) {
        HtmlStringBuffer buffer = new HtmlStringBuffer(property.length() + 3);

        buffer.append("get");
        buffer.append(Character.toUpperCase(property.charAt(0)));
        buffer.append(property.substring(1));

        return buffer.toString();
    }

    /**
     * Return the is getter method name for the given property name.
     *
     * @param property the property name
     * @return the is getter method name for the given property name.
     */
    public static String toIsGetterName(String property) {
        HtmlStringBuffer buffer = new HtmlStringBuffer(property.length() + 3);

        buffer.append("is");
        buffer.append(Character.toUpperCase(property.charAt(0)));
        buffer.append(property.substring(1));

        return buffer.toString();
    }

    /**
     * Return a field label string from the given field name. For example:
     * <pre class="codeHtml">
     * <span class="blue">faxNumber</span> &nbsp; -&gt; &nbsp; <span class="red">Fax Number</span> </pre>
     * <p/>
     * <b>Note</b> toLabel will return an empty String ("") if a <tt>null</tt>
     * String name is specified.
     *
     * @param name the field name
     * @return a field label string from the given field name
     */
    public static String toLabel(String name) {
        if (name == null) {
            return "";
        }

        HtmlStringBuffer buffer = new HtmlStringBuffer();

        for (int i = 0, size = name.length(); i < size; i++) {
            char aChar = name.charAt(i);

            if (i == 0) {
                buffer.append(Character.toUpperCase(aChar));

            } else {
                buffer.append(aChar);

                if (i < name.length() - 1) {
                    char nextChar = name.charAt(i + 1);
                    if (Character.isLowerCase(aChar)
                        && (Character.isUpperCase(nextChar)
                            || Character.isDigit(nextChar))) {

                        // Add space before digits or uppercase letters
                        buffer.append(" ");

                    } else if (Character.isDigit(aChar)
                        && (!Character.isDigit(nextChar))) {

                        // Add space after digits
                        buffer.append(" ");
                    }
                }
            }
        }

        return buffer.toString();
    }

    /**
     * Return an 32 char MD5 encoded string from the given plain text.
     * The returned value is MD5 hash compatible with Tomcat catalina Realm.
     * <p/>
     * Adapted from <tt>org.apache.catalina.util.MD5Encoder</tt>
     *
     * @param plaintext the plain text value to encode
     * @return encoded MD5 string
     */
    public static String toMD5Hash(String plaintext) {
        if (plaintext == null) {
            throw new IllegalArgumentException("Null plaintext parameter");
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            md.update(plaintext.getBytes("UTF-8"));

            byte[] binaryData = md.digest();

            char[] buffer = new char[32];

            for (int i = 0; i < 16; i++) {
                int low = (int) (binaryData[i] & 0x0f);
                int high = (int) ((binaryData[i] & 0xf0) >> 4);
                buffer[i * 2] = HEXADECIMAL[high];
                buffer[i * 2 + 1] = HEXADECIMAL[low];
            }

            return new String(buffer);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Return a field name string from the given field label.
     * <p/>
     * A label of <tt>" OK do it!"</tt> is returned as <tt>"okDoIt"</tt>. Any <tt>&amp;nbsp;</tt>
     * characters will also be removed.
     * <p/>
     * A label of <tt>"customerSelect"</tt> is returned as <tt>"customerSelect"</tt>.
     *
     * @param label the field label or caption
     * @return a field name string from the given field label
     */
    public static String toName(String label) {
        if (label == null) {
            throw new IllegalArgumentException("Null label parameter");
        }

        boolean doneFirstLetter = false;
        boolean lastCharBlank = false;
        boolean hasWhiteSpace = (label.indexOf(' ') != -1);

        HtmlStringBuffer buffer = new HtmlStringBuffer(label.length());
        for (int i = 0, size = label.length(); i < size; i++) {
            char aChar = label.charAt(i);

            if (aChar != ' ') {
                if (Character.isJavaIdentifierPart(aChar)) {
                    if (lastCharBlank) {
                        if (doneFirstLetter) {
                            buffer.append(Character.toUpperCase(aChar));
                            lastCharBlank = false;
                        } else {
                            buffer.append(Character.toLowerCase(aChar));
                            lastCharBlank = false;
                            doneFirstLetter = true;
                        }
                    } else {
                        if (doneFirstLetter) {
                            if (hasWhiteSpace) {
                                buffer.append(Character.toLowerCase(aChar));
                            } else {
                                buffer.append(aChar);
                            }
                        } else {
                            buffer.append(Character.toLowerCase(aChar));
                            doneFirstLetter = true;
                        }
                    }
                }
            } else {
                lastCharBlank = true;
            }
        }

        return buffer.toString();
    }

    /**
     * Return the setter method name for the given property name.
     *
     * @param property the property name
     * @return the setter method name for the given property name.
     */
    public static String toSetterName(String property) {
        HtmlStringBuffer buffer = new HtmlStringBuffer(property.length() + 3);

        buffer.append("set");
        buffer.append(Character.toUpperCase(property.charAt(0)));
        buffer.append(property.substring(1));

        return buffer.toString();
    }

    /**
     * Returns true if Click resources (JavaScript, CSS, images etc) packaged
     * in jars can be deployed to the root directory of the webapp, false
     * otherwise.
     * <p/>
     * This method will return false in restricted environments where write
     * access to the underlying file system is disallowed. Examples where
     * write access is not allowed include the WebLogic JEE server (this can be
     * changed though) and Google App Engine.
     *
     * @param servletContext the application servlet context
     * @return true if writes are allowed, false otherwise
     */
    public static boolean isResourcesDeployable(ServletContext servletContext) {
        try {
            boolean canWrite = (servletContext.getRealPath("/") != null);
            if (!canWrite) {
                return false;
            }

            // Since Google App Engine returns a value for getRealPath, check
            // SecurityManager if writes are allowed
            SecurityManager security = System.getSecurityManager();
            if (security != null) {
                security.checkWrite("/click");
            }
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    // -------------------------------------------------------- Package Methods

    /**
     * Return true if the given character requires HTML escaping.
     *
     * @param aChar the character value to test
     * @return true if the given character requires HTML escaping
     */
    static boolean requiresEscape(char aChar) {
        int index = aChar;

        if (index < HTML_ENTITIES.length) {
            return HTML_ENTITIES[index] != null;

        } else {
            return false;
        }
    }

    /**
     * Append the HTML escaped string for the given character value to the
     * buffer.
     *
     * @param aChar the character value to escape
     * @param buffer the string buffer to append the escaped value to
     */
    static void appendEscapeChar(char aChar, HtmlStringBuffer buffer) {
        int index = aChar;

        if (index < HTML_ENTITIES.length && HTML_ENTITIES[index] != null) {
            buffer.append(HTML_ENTITIES[index]);

        } else {
            buffer.append(aChar);
        }
    }

    /**
     * Append the HTML escaped string for the given character value to the
     * buffer.
     *
     * @param value the String value to escape
     * @param buffer the string buffer to append the escaped value to
     */
    static void appendEscapeString(String value, HtmlStringBuffer buffer) {
        char aChar;
        for (int i = 0, size = value.length(); i < size; i++) {
            aChar = value.charAt(i);
            int index = aChar;

            if (index < HTML_ENTITIES.length && HTML_ENTITIES[index] != null) {
                buffer.append(HTML_ENTITIES[index]);

            } else {
                buffer.append(aChar);
            }
        }
    }

    // -------------------------------------------------------- Private Methods

    /**
     * A helper method which binds the submitted request values of all Fields
     * and Links inside the given container or child containers.
     * <p/>
     * For Field controls, this method delegates to
     * {@link #bindField(org.apache.click.control.Field, org.apache.click.Context)}.
     *
     * @param container the container which Fields and Links to bind
     * @param context the request context
     */
    private static void bind(Container container, Context context) {
        for (int i = 0; i < container.getControls().size(); i++) {
            Control control = container.getControls().get(i);
            if (control instanceof Container) {
                // Include fields but skip fieldSets
                if (control instanceof Field) {
                    Field field = (Field) control;
                    bindField(field, context);

                } else if (control instanceof AbstractLink) {
                    ((AbstractLink) control).bindRequestValue();
                }
                Container childContainer = (Container) control;
                bind(childContainer, context);

            } else if (control instanceof Field) {
                Field field = (Field) control;
                bindField(field, context);

            } else if (control instanceof AbstractLink) {
                ((AbstractLink) control).bindRequestValue();
            }
        }
    }

    /**
     * A helper method which binds and validates the submitted request values
     * of all Fields and Links inside the given container or child containers.
     * <p/>
     * For Field controls, this method delegates to
     * {@link #bindField(org.apache.click.control.Field, org.apache.click.Context)}.
     *
     * @param container the container which Fields and Links to bind and
     * validate
     * @param context the request context
     * @return true if container fields and links was bound and valid, false
     * otherwise
     */
    private static boolean bindAndValidate(Container container, Context context) {
        boolean valid = true;
        for (int i = 0; i < container.getControls().size(); i++) {
            Control control = container.getControls().get(i);
            if (control instanceof Container) {

                // Include fields but skip fieldSets
                if (control instanceof Field) {
                    if (!bindAndValidate((Field) control, context)) {
                        valid = false;
                    }

                } else if (control instanceof AbstractLink) {
                    ((AbstractLink) control).bindRequestValue();
                }

                Container childContainer = (Container) control;
                if (!bindAndValidate(childContainer, context)) {
                    valid = false;
                }

            } else if (control instanceof Field) {
                if (!bindAndValidate((Field) control, context)) {
                    valid = false;
                }

            } else if (control instanceof AbstractLink) {
                ((AbstractLink) control).bindRequestValue();
            }
        }
        return valid;
    }

    /**
     * A helper method which binds and validates the Field's submitted request
     * value.
     * <p/>
     * This method delegates to
     * {@link #bindField(org.apache.click.control.Field, org.apache.click.Context)}
     * to bind the field value.
     *
     * @param field the Field to bind and validate
     * @param context the request context
     * @return true if field was bound and valid, or false otherwise
     */
    private static boolean bindAndValidate(Field field, Context context) {
        boolean continueProcessing = bindField(field, context);
        if (!continueProcessing) {
            return true;
        }

        if (field.getValidate()) {
            // Keep reference to current error
            String errorReference = field.getError();

            // Validate field. If validation fails the field error will be changed
            field.validate();

            boolean valid = field.isValid();

            // Revert back to original error
            field.setError(errorReference);

            return valid;
        }
        return true;
    }

    /**
     * Bind the field to its incoming request parameter, returning true if the
     * field value was bound, false otherwise.
     * <p/>
     * <b>Please note</b>: this method won't bind disabled fields,
     * unless the field has an incoming request parameter matching its name.
     * If an incoming request parameter is present, this method will switch off
     * the Field's disabled property.
     *
     * @param field the field which value to bind to its request parameter
     * @param context the request context
     * @return true if the field was bound to its request parameter, false
     * otherwise
     */
    private static boolean bindField(Field field, Context context) {
        if (field.isDisabled()) {
            // Switch off disabled property if control has incoming request
            // parameter. Normally this means the field was enabled via JS
            if (context.hasRequestParameter(field.getName())) {
                field.setDisabled(false);
            } else {
                return false;
            }
        }
        field.bindRequestValue();
        return true;
    }

    private static void ensureObjectPathNotNull(Object object, String path) {

        final int index = path.indexOf('.');

        if (index == -1) {
            return;
        }

        try {
            String value = path.substring(0, index);
            String getterName = toGetterName(value);
            String isGetterName = toIsGetterName(value);

            Method foundMethod = null;
            Method[] methods = object.getClass().getMethods();
            for (Method method : methods) {
                String name = method.getName();
                if (name.equals(getterName)) {
                    foundMethod = method;
                    break;

                } else if (name.equals(isGetterName)) {
                    foundMethod = method;
                    break;
                }
            }

            if (foundMethod == null) {
                String msg =
                    "Getter method not found for path value : " + value;
                throw new RuntimeException(msg);
            }

            Object result = foundMethod.invoke(object);

            if (result == null) {
                result = foundMethod.getReturnType().newInstance();

                String setterName = toSetterName(value);
                Class[] classArgs = { foundMethod.getReturnType() };

                Method setterMethod =
                    object.getClass().getMethod(setterName, classArgs);

                Object[] objectArgs = { result };

                setterMethod.invoke(object, objectArgs);
            }

            String remainingPath = path.substring(index + 1);

            ensureObjectPathNotNull(result, remainingPath);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Return true if the given string requires HTML escaping of characters.
     *
     * @param value the string value to test
     * @return true if the given string requires HTML escaping of characters
     */
    private static boolean requiresHtmlEscape(String value) {
        if (value == null) {
            return false;
        }

        int length = value.length();
        for (int i = 0; i < length; i++) {
            if (requiresEscape(value.charAt(i))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Invoke the named method on the given target object and return the result.
     *
     * @param target the target object with the method to invoke
     * @param method the name of the method to invoke
     * @return Object the target method result
     */
    private static Object invokeMethod(Object target, String method) {
        if (target == null) {
            throw new IllegalArgumentException("Null target parameter");
        }
        if (method == null) {
            throw new IllegalArgumentException("Null method parameter");
        }

        Method targetMethod = null;
        boolean isAccessible = true;
        try {
            Class targetClass = target.getClass();
            targetMethod = targetClass.getMethod(method);

            // Change accessible for anonymous inner classes public methods
            // only. Conditional checks:
            // #1 - Target method is not accessible
            // #2 - Anonymous inner classes are not public
            // #3 - Only modify public methods
            // #4 - Anonymous inner classes have no declaring class
            // #5 - Anonymous inner classes have $ in name
            if (!targetMethod.isAccessible()
                && !Modifier.isPublic(targetClass.getModifiers())
                && Modifier.isPublic(targetMethod.getModifiers())
                && targetClass.getDeclaringClass() == null
                && targetClass.getName().indexOf('$') != -1) {

                isAccessible = false;
                targetMethod.setAccessible(true);
            }

            return targetMethod.invoke(target);

        } catch (InvocationTargetException ite) {

            Throwable e = ite.getTargetException();
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;

            } else if (e instanceof Exception) {
                String msg =
                    "Exception occurred invoking public method: " + targetMethod;

                throw new RuntimeException(msg, e);

            } else if (e instanceof Error) {
                String msg =
                    "Error occurred invoking public method: " + targetMethod;

                throw new RuntimeException(msg, e);

            } else {
                String msg =
                    "Error occurred invoking public method: " + targetMethod;

                throw new RuntimeException(msg, e);
            }

        } catch (Exception e) {
            String msg =
                "Exception occurred invoking public method: " + targetMethod;

            throw new RuntimeException(msg, e);

        } finally {
            if (targetMethod != null && !isAccessible) {
                targetMethod.setAccessible(false);
            }
        }
    }
}
