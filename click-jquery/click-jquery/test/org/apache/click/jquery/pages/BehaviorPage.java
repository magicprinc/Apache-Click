package org.apache.click.jquery.pages;

import org.apache.click.Control;
import org.apache.click.Page;
import org.apache.click.ActionResult;
import org.apache.click.control.TextField;
import net.sf.click.jquery.JQEvent;
import net.sf.click.jquery.behavior.JQBehavior;

public class BehaviorPage extends Page {

    @Override
    public void onInit() {
        JQBehavior behavior = new JQBehavior(JQEvent.CLICK) {

            @Override
            public ActionResult onAction(Control source, JQEvent eventType) {
                System.out.println("OK");
                return null;
            }
        };

        TextField field = new TextField("field");
        field.addBehavior(behavior);
        addControl(field);
    }
}
