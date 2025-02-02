package org.apache.click;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.click.servlet.MockRequest;
import org.apache.click.servlet.MockResponse;
import org.apache.click.servlet.MockServletConfig;
import org.apache.click.servlet.MockServletContext;
import org.apache.click.servlet.MockSession;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;

/**
 * Provides a mock {@link org.apache.click.Context} object for unit testing.
 * <p/>
 * <b>Note:</b> if you want to test your Click Page instances use
 * {@link MockContainer} instead.
 * <p/>
 * This class defines a couple of helper methods to quickly create all the mock
 * objects needed for unit testing. Please see the following methods:
 * <ul>
 *   <li>{@link #initContext()}</li>
 *   <li>{@link #initContext(Locale)}</li>
 *   <li>{@link #initContext(String)}</li>
 *   <li>{@link #initContext(Locale, String)}</li>
 *   <li>{@link #initContext(MockServletConfig, MockRequest, MockResponse, ClickServlet)}</li>
 * </ul>
 * To use this class in your own tests invoke one of the methods above.
 * For example:
 * <pre class="prettyprint">
 * public class FormTest extends TestCase {
 *     // Create a mock context
 *     MockContext context = MockContext.initContext("test-form.htm");
 *     MockRequest request = context.getMockRequest();
 *
 *     // The request value that should be set as the textField value
 *     String requestValue = "one";
 *
 *     // Set form name and field name parameters
 *     request.setParameter("form_name", "form");
 *     request.setParameter("name", requestValue);
 *
 *     // Create form and fields
 *     Form form = new Form("form");
 *     TextField nameField = new TextField("name");
 *     form.add(nameField);
 *
 *     // Check that nameField value is null
 *     Assert.assertNull(nameField.getValueObject());
 *
 *     // Simulate a form onProcess event
 *     form.onProcess();
 *
 *     // Check that nameField value is now bound to request value
 *     Assert.assertEquals(requestValue, nameField.getValueObject());
 * } </pre>
 *
 * <b>Please note:</b> using MockContext to run performance tests over a large
 * number of Controls could lead to <tt>out of memory</tt> errors. If you run
 * into memory issues, you can either re-recreate a MockContext or invoke
 * {@link #reset()}, which removes all references to Controls,
 * ActionListeners and Behaviors.
 */
@Slf4j
public class MockContext extends Context {
  /**
   * Create a new MockContext instance for the specified Mock objects.
   *
   * @param servletConfig the mock servletConfig
   * @param request the mock request
   * @param response the mock response
   * @param isPost specified if this a POST or GET request
   * @param clickServlet the mock clickServlet
   */
  MockContext(ServletConfig servletConfig, HttpServletRequest request, HttpServletResponse response, boolean isPost, ClickServlet clickServlet) {
    super(servletConfig == null ? null : servletConfig.getServletContext(), servletConfig, request, response, isPost, clickServlet);
  }

  /**
   * Return the mock {@link org.apache.click.ClickServlet} instance for this
   * context.
   *
   * @return the clickServlet instance
   */
  public ClickServlet getServlet (){ return clickServlet; }

  /**
   * Return the {@link org.apache.click.servlet.MockRequest} instance for this
   * context.
   *
   * @return the MockRequest instance
   */
  public MockRequest getMockRequest() {
    return MockContainer.findMockRequest(request);
  }


  /**
   * Creates and returns a new Context instance.
   *<p/>
   * <b>Note:</b> servletPath will default to '/mock.htm'.
   *
   * @return new Context instance
   */
  public static MockContext initContext() {
    return initContext("/mock.htm");
  }

  /**
   * Creates and returns a new Context instance for the specified servletPath.
   *
   * @param servletPath the requests servletPath
   * @return new Context instance
   */
  public static MockContext initContext(String servletPath) {
    return initContext(Locale.getDefault(), servletPath);
  }

  /**
   * Creates and returns a new Context instance for the specified locale.
   *
   * <b>Note:</b> servletPath will default to '/mock.htm'.
   *
   * @param locale the requests locale
   * @return new Context instance
   */
  public static MockContext initContext(Locale locale) {
    return initContext(locale, "/mock.htm");
  }

  /**
   * Creates and returns a new Context instance for the specified locale and servletPath.
   *
   * @param locale the requests locale
   * @param servletPath the requests servletPath
   * @return new Context instance
   */
  public static MockContext initContext (@NonNull Locale locale, String servletPath) {
    val servletContext = new MockServletContext();
    String servletName = "click-servlet";
    val servletConfig = new MockServletConfig(servletName, servletContext);

    val servlet = new ClickServlet();
    val response = new MockResponse();
    val session = new MockSession(servletContext);
    val request = new MockRequest(locale, MockServletContext.DEFAULT_CONTEXT_PATH, servletPath, servletContext, session);

    return initContext(servletConfig, request, response, servlet);
  }

	public static MockContext initContext (Map<String,String> initParameters) {
		val servletContext = new MockServletContext();
		servletContext.addInitParameters(initParameters);
		String servletName = "click-servlet";
		val servletConfig = new MockServletConfig(servletName, servletContext);

		val servlet = new ClickServlet();
		val response = new MockResponse();
		val session = new MockSession(servletContext);
		val request = new MockRequest(Locale.getDefault(), MockServletContext.DEFAULT_CONTEXT_PATH, "/mock.htm", servletContext, session);

		return initContext(servletConfig, request, response, servlet);
	}

  /**
   * Creates and returns a new Context instance for the specified mock objects.
   *
   * @param servletConfig the mock servletConfig
   * @param request the mock request
   * @param response the mock response
   * @param clickServlet the mock clickServlet
   * @return new Context instance
   */
  public static MockContext initContext (MockServletConfig servletConfig, MockRequest request, MockResponse response, ClickServlet clickServlet) {
    return initContext(servletConfig, request, response, clickServlet, null, null);
  }

  /**
   * Creates and returns a new Context instance for the specified mock
   * objects.
   *
   * @param servletConfig the mock servletConfig
   * @param request the mock request
   * @param response the mock response
   * @param clickServlet the mock clickServlet
   * @param actionEventDispatcher action and behavior dispatcher
   * @param controlRegistry the control registry
   * @return new Context instance
   */
  public static MockContext initContext (
			@NonNull MockServletConfig servletConfig,
      @NonNull MockRequest request,
			@NonNull MockResponse response,
			@NonNull ClickServlet clickServlet,
      ActionEventDispatcher actionEventDispatcher,
			ControlRegistry controlRegistry
	){
    try {// Sanity checks
      if (servletConfig.getServletContext() == null) {
        throw new IllegalArgumentException("ServletConfig.getServletContext() cannot return null");
      }
      boolean isPost = "POST".equalsIgnoreCase(request.getMethod());

      val servletContext = servletConfig.getServletContext();

      servletContext.setAttribute(ClickServlet.MOCK_MODE_ENABLED, Boolean.TRUE);
      request.setAttribute(ClickServlet.MOCK_MODE_ENABLED, Boolean.TRUE);
			if (servletContext.getInitParameter("pages") == null){
				log.warn("MockServletContext.initParameter.pages == null: fallback to org.apache.click.pages");
				servletContext.addInitParameter("pages", "org.apache.click.pages");
			}

      clickServlet.init(servletConfig);

      val configService = clickServlet.getConfigService();
      if (configService == null) {
        throw new IllegalArgumentException("ClickServlet.getConfigService() cannot return null");
      }

      val mockContext = new MockContext(servletConfig, request, response, isPost, clickServlet);

      if (actionEventDispatcher == null){
        actionEventDispatcher = new ActionEventDispatcher(configService);
      }

      if (controlRegistry == null) {
        controlRegistry = new ControlRegistry(configService);
      }

      // Remove lingering ThreadLocal variables of the Mock stack
      mockContext.cleanup();

      ActionEventDispatcher.pushThreadLocalDispatcher(actionEventDispatcher);
      ControlRegistry.pushThreadLocalRegistry(controlRegistry);
      Context.pushThreadLocalContext(mockContext);

      return (MockContext) Context.getThreadLocalContext();
    } catch (Exception e) {
      throw new MockContainer.CleanRuntimeException(e);
    }
  }

  /**
   * Execute all listeners that was registered by processed Controls.
   *
   * @return true if all listeners returned true, false otherwise
   */
  public boolean executeActionListeners() {
    ActionEventDispatcher dispatcher = ActionEventDispatcher.getThreadLocalDispatcher();
    // Fire action events
    return dispatcher.fireActionEvents(this);
  }

  /**
   * Execute all behaviors that was registered by processed Controls.
   *
   * @return true if all behaviors returned true, false otherwise
   */
  public boolean executeBehaviors() {
    ActionEventDispatcher dispatcher = ActionEventDispatcher.getThreadLocalDispatcher();
    // Fire behaviors
    return dispatcher.fireAjaxBehaviors(this);
  }

  /**
   * Execute the preResponse method for all registered behaviors.
   */
  public void executePreResponse () {
    ControlRegistry registry = ControlRegistry.getThreadLocalRegistry();

    registry.processPreResponse(this);
  }

  /**
   * Execute the preRenderHeadElements method for all registered behaviors.
   */
  public void executePreRenderHeadElements() {
    ControlRegistry registry = ControlRegistry.getThreadLocalRegistry();

    registry.processPreRenderHeadElements(this);
  }

  /**
   * Execute the preDestroy method for all registered behaviors.
   */
  public void executePreDestroy() {
    ControlRegistry registry = ControlRegistry.getThreadLocalRegistry();

    registry.processPreDestroy(this);
  }


  /**
   * Reset mock internal state. Running a large number of tests using the same
   * MockContext could lead to <tt>out of memory</tt> errors. Calling this
   * method will remove any references to objects, thus freeing up memory.
   */
  public void reset () {
    if (ControlRegistry.hasThreadLocalRegistry()){
      ControlRegistry registry = ControlRegistry.getThreadLocalRegistry();
      registry.clear();
    }

    if (ActionEventDispatcher.hasThreadLocalDispatcher()){
      ActionEventDispatcher actionEventDispatcher = ActionEventDispatcher.getThreadLocalDispatcher();
      actionEventDispatcher.clear();
    }
  }

  /**
   * Cleanup the MockContext.
   * <p/>
   * This method removes any lingering ThreadLocal variables from the Mock stack.
   */
  void cleanup() {
    // Cleanup ThreadLocals
    Context.clearThreadLocalContext();
    ControlRegistry.clearThreadLocalRegistry();
    ActionEventDispatcher.clearThreadLocalDispatcher();
  }

  /**
   @see ServletContext
   @see MockServletContext
   */
  @Override public MockServletContext getServletContext (){ return (MockServletContext) servletContext;}
}