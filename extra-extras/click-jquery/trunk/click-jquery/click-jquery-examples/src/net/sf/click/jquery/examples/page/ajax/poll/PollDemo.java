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
package net.sf.click.jquery.examples.page.ajax.poll;

import java.text.SimpleDateFormat;
import java.util.Date;
import net.sf.click.jquery.JQEvent;
import net.sf.click.jquery.behavior.JQBehavior;
import net.sf.click.jquery.behavior.JQPollBehavior;
import net.sf.click.jquery.examples.control.html.Div;
import net.sf.click.jquery.examples.control.html.Text;
import net.sf.click.jquery.examples.page.BorderPage;
import net.sf.click.jquery.taconite.JQTaconite;
import org.apache.click.Control;
import org.apache.click.ActionResult;
import org.apache.click.control.ActionLink;

/**
 *
 */
public class PollDemo extends BorderPage {

	private static final long serialVersionUID = 1L;

    private Div clock = new Div("clock");
    private Text value = new Text();
    private ActionLink start = new ActionLink("start", "Start poll (5 second intervals)");
    private ActionLink stop = new ActionLink("stop", "Stop poll");

    private String pollId = "mypoll";

    public PollDemo() {
        addControl(clock);
        addControl(start);
        addControl(stop);

        value.setText(getTime());
        clock.add(value);

        start.addBehavior(new JQBehavior(){
            @Override
            public ActionResult onAction(Control source, JQEvent event) {
                JQTaconite actionResult = new JQTaconite();

                actionResult.eval("Click.jq.polls.start('" + pollId + "', 5000)");

                return actionResult;
            }
        });

        stop.addBehavior(new JQBehavior(){
            @Override
            public ActionResult onAction(Control source, JQEvent event) {
                JQTaconite actionResult = new JQTaconite();

                actionResult.eval("Click.jq.polls.stop('" + pollId + "')");

                return actionResult;
            }
        });

        JQPollBehavior behavior = new JQPollBehavior(pollId) {
            @Override
            public ActionResult onAction(Control source, JQEvent event) {
                JQTaconite actionResult = new JQTaconite();

                actionResult.replaceContent(clock, getTime());

                // If the content hasn't changed and you want to decrease the
                // poll frequency, set the response status to '304 Not Modified'
                // as shown in the commented code below.
                //getContext().getResponse().setStatus(HttpServletResponse.SC_NOT_MODIFIED);

                return actionResult;
            }
        };

        clock.addBehavior(behavior);
    }

    private String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:MM:ss");
        return format.format(new Date());
    }
}
