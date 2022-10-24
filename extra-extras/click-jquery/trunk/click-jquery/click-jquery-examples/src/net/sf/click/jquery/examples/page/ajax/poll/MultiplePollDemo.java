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
public class MultiplePollDemo extends BorderPage {

	private static final long serialVersionUID = 1L;

    private Div clock1 = new Div("clock");
    private Text value1 = new Text();
    private ActionLink start1 = new ActionLink("start", "Start poll 1 (5 second intervals)");
    private ActionLink stop1 = new ActionLink("stop", "Stop poll 1");
    private String pollId1 = "mypoll1";

    private Div clock2 = new Div("clock2");
    private Text value2 = new Text();
    private ActionLink start2 = new ActionLink("start2", "Start poll 2 (5 second intervals)");
    private ActionLink stop2 = new ActionLink("stop2", "Stop poll 2");
    private String pollId2 = "mypoll2";

    public MultiplePollDemo() {
        setupPoll(pollId1, clock1, start1, stop1, value1);
        setupPoll(pollId2, clock2, start2, stop2, value2);
    }

    private void setupPoll(final String pollId, final Div clock, ActionLink start, ActionLink stop, final Text value) {
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

                // Replace the clock with the updated clock
                actionResult.replaceContent(clock, value);

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
