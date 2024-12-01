package org.apache.click.jquery;

import lombok.val;
import org.apache.click.MockContainer;
import org.apache.click.jquery.pages.BehaviorPage;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class BehaviorTest {
  /** Test that redirecting to a htm works. */
  @Test
	public void testRedirect() {
    val container = new MockContainer("web").pages("org.apache.click.jquery.pages");
    container.start();

    container.getRequest().setMethod("GET");

    BehaviorPage page = container.testPage(BehaviorPage.class);
    assertNotNull(page);

    System.out.println(container.getHtml());
    container.stop();
  }
}