/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.click.jquery.examples.page.ajax;

import net.sf.click.jquery.JQEvent;
import net.sf.click.jquery.behavior.JQBehavior;
import net.sf.click.jquery.examples.control.JQActionButton;
import net.sf.click.jquery.examples.control.html.Div;
import org.apache.click.Control;
import org.apache.click.util.HtmlStringBuffer;
import net.sf.click.jquery.examples.page.BorderPage;
import net.sf.click.jquery.taconite.JQTaconite;
import org.apache.click.ActionResult;
import org.apache.click.control.ActionLink;
import org.apache.commons.lang.math.RandomUtils;

/**
 *
 */

public class ActionDemo extends BorderPage {

    private static final long serialVersionUID = 1L;

    @Override
    public void onInit() {
        // Example 1
        // The target div will have its content replaced through Ajax
        final Div target1 = new Div("target1");
        addControl(target1);

        // Create a Ajaxified link that will update a specified target with an
        // ActionResult
        JQActionButton button = new JQActionButton("button", "Click here to make Ajax request");

        button.addBehavior(new JQBehavior() {

            @Override
            public ActionResult onAction(Control source, JQEvent event) {
                // Create a response that will be placed inside the target div
                JQTaconite actionResult = new JQTaconite();
                actionResult.replaceContent(target1, createResponse());
                return actionResult;
            }
        });
        addControl(button);



        // Example 2
        // The target div will have its content replaced through Ajax
        final Div target2 = new Div("target2");
        addControl(target2);

        // Another target div will have its content replaced through Ajax
        final Div target3 = new Div("target3");
        addControl(target3);

        ActionLink link = new ActionLink("link", "Click here to make Ajax request");

        // Provide an alternative message when an Ajax call is made
        /*
        behavior.setIndicatorMessage("\"<h1><img src=\""
            + getContext().getRequest().getContextPath()
            + "/assets/images/indicator.gif\" /> Just a moment...</h1>\"");
         */

        link.addBehavior(new JQBehavior(JQEvent.CLICK) {

            @Override
            public ActionResult onAction(Control source, JQEvent event) {
                // This actionResult will randomly update one target and clear the other

                JQTaconite response = new JQTaconite();
                // Randomly update a different target
                if (RandomUtils.nextBoolean()) {
                    response.empty(target3);
                    response.replaceContent(target2, createResponse());
                } else {
                    //activeTargetId = target3.getId();
                    //inactiveTargetId = target2.getId();
                    response.empty(target2);
                    response.replaceContent(target3, createResponse());
                }

                return response;

                // Return normal Javascript that will be executed by JQuery
                //return new ActionResult("jQuery('#" + activeTargetId + "').html(\"" + createResponse() + "\");" +
                //   "jQuery('#" + inactiveTargetId + "').html(\"\");");
            }
        });
        addControl(link);
    }

    private String createResponse() {
        HtmlStringBuffer buffer = new HtmlStringBuffer();
        buffer.append(
            "<div style='background-color: #EEF1F7; border: 1px solid #6C90CC'>");
        buffer.append("<h2>Ajax Response</h2>");
        buffer.append(
            "<p>This snippet was requested via Ajax. The current time is: ");
        buffer.append(format.currentDate("HH:mm:ss:S"));
        buffer.append("</p>");
        buffer.append("</div>");
        return buffer.toString();
    }
}
