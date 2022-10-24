package org.apache.click.jquery;

import org.apache.click.MockContainer;
import org.apache.click.jquery.pages.BehaviorPage;
import org.junit.Test;

public class BehaviorTest {

    /**
     * Test that redirecting to a htm works.
     */
    @Test
    public void testRedirect() {
        MockContainer container = new MockContainer("web");
        container.start();

        container.getRequest().setMethod("GET");

        BehaviorPage page = (BehaviorPage) container.testPage(BehaviorPage.class);

        System.out.println(container.getHtml());
        container.stop();
    }
}
