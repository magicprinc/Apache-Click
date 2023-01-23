package org.apache.click.jquery.behavior;

import net.sf.click.jquery.behavior.JQBehavior;
import java.util.ArrayList;
import java.util.List;
import org.apache.click.Control;
import org.apache.click.ActionResult;
import net.sf.click.jquery.JQEvent;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class JQBehaviorTest {

    public JQBehaviorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testBiorehavior() {
        
        JQBehavior behavior = new JQBehavior() {

            @Override
            public ActionResult onAction(Control source, JQEvent eventType) {
                System.out.println("OK");
                return null;
            }
        };
        List list = new ArrayList();
        
    }
}