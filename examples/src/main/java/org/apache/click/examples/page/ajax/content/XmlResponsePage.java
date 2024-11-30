package org.apache.click.examples.page.ajax.content;

import org.apache.click.ActionResult;
import org.apache.click.Control;
import org.apache.click.ajax.DefaultAjaxBehavior;
import org.apache.click.control.ActionLink;
import org.apache.click.examples.page.BorderPage;

/**
 * Demonstrates how an ActionResult can return an XML response.
 */
public class XmlResponsePage extends BorderPage {
	private static final long serialVersionUID = 1L;

	final ActionLink link = new ActionLink("link", "here");

	public XmlResponsePage() {
		link.setId("link-id");

		addControl(link);

		// Add an Ajax behavior to the link. The behavior will be invoked when the
		// link is clicked. See the basic-ajax-demo.htm template for the client-side Ajax code
		link.addBehavior(new DefaultAjaxBehavior(){
			@Override public ActionResult onAction (Control source) {
				// Formatted date instance that will be added to the
				String now = format.currentDate("MMM, yyyy dd HH:mm:ss");

				String msg = "<payload><msg>XML returned at: </msg>"
					+ "<date>" + now + "</date></payload>";
				// Return an action result containing the message
				return new ActionResult(msg, ActionResult.XML);
			}
		});
	}
}