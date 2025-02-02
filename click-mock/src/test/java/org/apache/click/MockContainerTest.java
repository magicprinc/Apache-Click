package org.apache.click;

import junit.framework.TestCase;
import org.apache.click.control.Form;
import org.apache.click.pages.BorderTestPage;
import org.apache.click.pages.FormPage;
import org.apache.click.pages.ForwardPage;
import org.apache.click.pages.RedirectPage;
import org.apache.click.pages.TestPage;

import java.io.File;
import java.net.URI;
import java.net.URL;

/**
 * Sanity tests for MockContainer.
 */
public class MockContainerTest extends TestCase {
	/**
	 * Test TestPage.
	 */
	public void testPage() {
		try {
			MockContainer container = new MockContainer("web").pages("org.apache.click.pages");
			container.start();

			// Set a parameter
			String expectedValue = "Some parameter";
			container.setParameter("myparam", expectedValue);

			// Prepare a file for upload
			URL resource = container.getClass().getResource("/web/test.htm");
			URI uri = new URI(resource.toString());
			container.setParameter("myfile", new File(uri), "text/html");

			// Test 1 ---------------------------------------------------------

			// First test exectes the test against the TestPage class
			Page testPage = container.testPage(TestPage.class);

			// Check that the expected value was rendered by the Page template
			assertTrue(container.getHtml().indexOf(expectedValue) > 0);

			// Check that the page set a ID value as a request attribute
			assertEquals(TestPage.ID_VALUE, testPage.getContext().getRequestAttribute("id"));

			// Remove request attribute
			testPage.getContext().setRequestAttribute("id", null);
			assertNull(testPage.getContext().getRequestAttribute("id"));

			// Test 2 ---------------------------------------------------------

			// Note: the test continues without restart the container

			// Second test executes the test against the test.htm template
			testPage = container.testPage("test.htm");

			// Check that the page set a ID value as a request attribute
			assertEquals(TestPage.ID_VALUE, testPage.getContext().getRequestAttribute("id"));

			// Check that the expected value was rendered by the Page template
			assertTrue(container.getHtml().indexOf(expectedValue) > 0);

			// Remove request attribute
			testPage.getContext().setRequestAttribute("id", null);
			assertNull(testPage.getContext().getRequestAttribute("id"));

			// Test 3 ---------------------------------------------------------

			// Note: container is restarted for this test
			container.stop();
			container.start();

			// Third test executes the test against the test.htm template
			testPage = container.testPage("/test.htm");

			// Check that the page set a ID value as a request attribute
			assertEquals(TestPage.ID_VALUE, testPage.getContext().getRequestAttribute("id"));

			// Check that the expected value was rendered by the Page template
			assertTrue(container.getHtml().indexOf(expectedValue) > 0);

			container.stop();

		} catch (Exception exception) {
			exception.printStackTrace(System.err);
			fail();
		}
	}

	/**
	 * Test BorderPage.
	 */
	public void testBroderPage() {
		try {
			MockContainer container = new MockContainer("web");

			container.start();

			// Set a parameter
			String expectedValue = "Some parameter";

			// Set a parameter
			container.setParameter("myparam", expectedValue);

			// Prepare a file for upload
			URL resource = container.getClass().getResource("/web/test.htm");
			URI uri = new URI(resource.toString());
			container.setParameter("myfile", new File(uri), "text/html");

			// Process page
			Page testPage = container.testPage(BorderTestPage.class);

			// Check that the page set a ID value as a request attribute
			assertEquals(BorderTestPage.ID_VALUE, testPage.getContext().getRequestAttribute("id"));

			// Check that border page token was rendered
			int borderTokenIndex = container.getHtml().indexOf("<h1>Header</h1>");
			assertTrue(borderTokenIndex >= 0);

			// Check that the expected value was rendered by the Page template
			int pageTokenIndex = container.getHtml().indexOf(expectedValue);
			assertTrue(pageTokenIndex > 0);

			// Check that the border token was rendered *before* the page template value
			assertTrue(borderTokenIndex < pageTokenIndex);

		} catch (Exception exception) {
			exception.printStackTrace(System.err);
			fail();
		}
	}

	/**
	 * Test ForwardPage.
	 */
	public void testForward() {
		try {
			MockContainer container = new MockContainer("web").pages("org.apache.click.pages");

			container.start();

			// Set a parameter
			String expectedValue = "Some parameter";

			// Set a parameter
			container.setParameter("myparam", expectedValue);

			// Upload a file
			URL resource = container.getClass().getResource("/web/test.htm");
			URI uri = new URI(resource.toString());
			container.setParameter("myfile", new File(uri), "text/html");

			// Process page
			Page testPage = container.testPage(TestPage.class);

			// Check that the expected value was rendered by the Page template
			assertTrue(container.getHtml().indexOf(expectedValue) > 0);

			// Check that the page set a ID value as a request attribute
			assertEquals(ForwardPage.ID_VALUE, testPage.getContext().getRequestAttribute("id"));

			// Remove request attribute
			testPage.getContext().setRequestAttribute("id", null);
			assertNull(testPage.getContext().getRequestAttribute("id"));

			// ForwardPage forwards to TestPage.class
			testPage = container.testPage(ForwardPage.class);

			// Assert that forwardUrl was set
			assertEquals("/test.htm", container.getForward());
			assertEquals(TestPage.class, container.getForwardPageClass());

			// MockContainer does not process forwarded requests. getHtml should be empty
			assertEquals("", container.getHtml());

		} catch (Exception exception) {
			exception.printStackTrace(System.err);
			fail();
		}
	}

	/**
	 * Test RedirectPage.
	 */
	public void testRedirect() {
		try {
			MockContainer container = new MockContainer("web");

			container.start();

			// Set a parameter
			String expectedValue = "Some parameter";

			// Set a parameter
			container.setParameter("myparam", expectedValue);

			// Upload a file
			URL resource = container.getClass().getResource("/web/test.htm");
			URI uri = new URI(resource.toString());
			container.setParameter("myfile", new File(uri), "text/html");

			// Process page
			Page testPage = container.testPage(TestPage.class);

			// Remove request attribute
			testPage.getContext().setRequestAttribute("id", null);
			assertNull(testPage.getContext().getRequestAttribute("id"));

			// RedirectPage redirects to TestPage.class
			testPage = container.testPage(RedirectPage.class);

			// Check that the page set a ID value as a request attribute
			assertEquals(ForwardPage.ID_VALUE, testPage.getContext().getRequestAttribute("id"));

			// Assert that redirectUrl was set
			assertEquals("/mock/test.htm", container.getRedirect());
			assertEquals(TestPage.class, container.getRedirectPageClass());

			// Alternatively use the container.getDestination() call which returns
			// either the forward or redirect value. For redirect values the
			// context is removed making for easier testing.
			assertEquals("/test.htm", container.getForwardOrRedirectUrl());

			// MockContainer does not process forwarded requests. getHtml should be empty
			assertEquals("", container.getHtml());

		} catch (Exception exception) {
			exception.printStackTrace(System.err);
			fail();
		}
	}

	/**
	 * Test FormPage.
	 */
	public void testFormBinding() {
		try {
			MockContainer container = new MockContainer("web");

			container.start();

			// Set a parameter
			String fieldValue = "one";
			String fieldName = "myfield";

			// Set the form name to ensure a Form submission occurs
			container.setParameter(Form.FORM_NAME, "form");

			// Set the field parameter
			container.setParameter(fieldName, fieldValue);

			// Process page
			FormPage formPage = container.testPage(FormPage.class);

			// Assert that form with id="form" was rendered
			assertTrue(container.getHtml().indexOf("id=\"form\"") > 0);

			// Assert that form field "myfield" was bound to request parameter "myfield"
			assertEquals(fieldValue, formPage.getForm().getFieldValue(fieldName));

		} catch (Exception exception) {
			exception.printStackTrace(System.err);
			fail();
		}
	}
}