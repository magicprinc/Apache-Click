package org.apache.click;

import junit.framework.TestCase;
import lombok.SneakyThrows;
import org.apache.click.control.Form;
import org.apache.click.pages.BinaryPage;
import org.apache.click.pages.ListenerPage;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

import static org.apache.click.ClickServlet.MOCK_MODE_ENABLED;

/**
 * Provides tests for ClickServlet behavior.
 */
public class ClickServletTest extends TestCase {

  /**
   * Assert the ListenerPage that the Submit button listener is invoked *after*
   * the TextField value was bound to the request value.
   *
   * CLK-365.
   */
  public void testRegisterListener() {
    MockContainer container = new MockContainer("web");
    container.start();
    container.setParameter(Form.FORM_NAME, "form"); // Simulate form submitted
    container.setParameter("save", "save"); // Simulate Submit button clicked
    container.setParameter("field", "one"); // Simulate TextField value set

    ListenerPage page = container.testPage(ListenerPage.class);

    // assert that the Page did successfully execute
    assertTrue(page.success);
    container.stop();
  }

  /**
   * Check that ClickServlet still renders an ErrorPage for cases where the
   * response outputStream has been retrieved and an exception occurs.
   */
  public void testBinaryExceptionHandling() {
    MockContainer container = new MockContainer("web");
    container.start();

    container.testPage(BinaryPage.class);

    container.stop();
  }


  public void testDeepRecursion () throws ServletException, IOException {
    MockContainer container = new MockContainer("web");

    final Deque<ActionEventDispatcher> qActionEventDispatcher = new ConcurrentLinkedDeque<>();
    final Deque<ControlRegistry> qControlRegistry = new ConcurrentLinkedDeque<>();
    final Deque<Context> qContext = new ConcurrentLinkedDeque<>();
    final AtomicInteger stackDepth = new AtomicInteger();

    container.setClickServlet(new ClickServlet(){
      @Override @SneakyThrows
      protected Page createPage (final Context context){
        ActionEventDispatcher a = ActionEventDispatcher.getThreadLocalDispatcher();
        assertNotNull(a);
        ControlRegistry b = ControlRegistry.getThreadLocalRegistry();
        assertNotNull(b);
        Context c = Context.getThreadLocalContext();
        assertNotNull(c);
        assertSame(c, context);
        qActionEventDispatcher.add(a);
        qControlRegistry.add(b);
        qContext.add(c);
        stackDepth.incrementAndGet();

        if (qContext.size() < 500){
          doGet(context.getRequest(), context.getResponse());
        } else {
          assertEquals(500, qActionEventDispatcher.size());
          assertEquals(500, qControlRegistry.size());
          assertEquals(500, qContext.size());
          assertEquals(500, stackDepth.get());
          assertEquals(500, ActionEventDispatcher.getDispatcherStackSize());
          assertEquals(500, ControlRegistry.getRegistryStackSize());
          assertEquals(500, Context.getContextStackSize());
          System.err.println("===🙏 Everything is all right with recursion :-)");
        }

        assertSame(a, ActionEventDispatcher.getThreadLocalDispatcher());
        assertSame(b, ControlRegistry.getThreadLocalRegistry());
        assertSame(c, Context.getThreadLocalContext());

        assertSame(a, qActionEventDispatcher.pollLast());
        assertSame(b, qControlRegistry.pollLast());
        assertSame(c, qContext.pollLast());

        return null;
      }
    });
    container.start();
    container.getRequest().setPathInfo("foo.htm");
    container.getRequest().setAttribute(MOCK_MODE_ENABLED, null);

    container.getClickServlet().service(container.getRequest(), container.getResponse());

    container.stop();
    assertEquals(500, stackDepth.get());
  }
}