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
package net.sf.click.jquery.behavior;

import net.sf.click.jquery.JQEvent;
import net.sf.click.jquery.util.JSONWriter;
import org.apache.click.Context;
import org.apache.click.Control;
import org.apache.click.Page;
import org.apache.click.element.Element;
import org.apache.click.element.JsImport;
import org.apache.click.element.JsScript;
import org.apache.click.util.HtmlStringBuffer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provide a specialized JQuery helper that triggers Ajax request at specified
 * intervals.
 * <p/>
 * This helper has an associated JavaScript template that can be modified
 * according to your needs. Click <a href="../../../../../js/template/jquery.refresh.template.js.txt">here</a>
 * to view the template.
 * <p/>
 * JQRefreshHelper can either be embedded inside Click controls or used to decorate
 * the control.
 *
 * <h3>Embedded example</h3>
 *
 * Below is an example of a custom control with an embedded JQRefreshHelper that
 * enables Ajax behavior:
 *
 * <pre class="prettyprint">
 * public class Heartbeat extends Div {
 *
 *     // The embedded JQuery helper object.
 *     private JQHelper jqHelper;
 *
 *     // Constructor
 *     public Heartbeat(String name, int interval) {
 *         super(name);
 *         getJQueryHelper().setInterval(interval);
 *
 *         // The div name will be used as the refreshId
 *         getJQueryHelper().setRefreshId(name);
 *     }
 *
 *     // Initialize the Ajax functionality
 *     &#64;Override
 *     public void onInit() {
 *         super.onInit();
 *         AjaxControlRegistry.registerAjaxControl(this);
 *     }
 *
 *     &#64;Override
 *     public List getHeadElements() {
 *         if (headElements == null) {
 *             headElements = super.getHeadElements();
 *             getJQueryHelper().addHeadElements(headElements);
 *         }
 *         return headElements;
 *     }
 *
 *     public JQHelper getJQueryHelper() {
 *         if (jqHelper == null) {
 *             jqHelper = new JQRefreshHelper(this);
 *         }
 *         return jqHelper;
 *     }
 * } </pre>
 *
 * <h3>Decorate example</h3>
 *
 * Below is an example how to decorate a Div control (clock) to updated its timer
 * at specific intervals:
 *
 * <pre class="prettyprint">
 * public class RefreshDemo extends BorderPage {
 *
 *     private Div clock = new Div("clock");
 *     private Text value = new Text();
 *     private JQActionLink stop = new JQActionLink("stop", "Stop refresh");
 *
 *     // RefreshID is used by the JQRefreshHelper to interact with JavaScript
 *     // functions added by the helper JavaScript template.
 *     private String refreshId = "id";
 *
 *     private SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
 *
 *     public RefreshDemo() {
 *         addControl(clock);
 *         addControl(stop);
 *
 *         value.setText(getTime());
 *         clock.add(value);
 *
 *         clock.addBehavior(new AjaxBehavior() {
 *
 *              public ActionResult onAction(Control source) {
 *                  Taconite actionResult = new Taconite();
 *
 *                  // Replace the clock with the updated clock
 *                  actionResult.replaceContent(clock, value);
 *                  return actionResult;
 *              }
 *         });
 *
 *         stop.addBehavior(new AjaxBehavior(){
 *
 *            &#64;Override
 *            public ActionResult onAjaxAction(Control source) {
 *                Taconite actionResult = new Taconite();
 *
 *                 // Note the eval statement below. This executes raw JavaScript
 *                 // on the browser, in this case a function from this helper's
 *                 // template: Click.refresh.stop('id');
 *                 actionResult.eval("Click.refresh.stop('" + refreshId + "');");
 *
 *                return actionResult;
 *            }
 *        });
 *
 *         JQRefreshHelper helper = new JQRefreshHelper(clock, refreshId, 2000);
 *         helper.ajaxify();
 *     }
 *
 *     private String getTime() {
 *         return format.format(new Date());
 *     }
 * } </pre>
 *
 * <h3>JavaScript</h3>
 * The JavaScript template exposes three public methods that can be interacted
 * with from your Java code. They are:
 * <ul>
 * <li>Click.refresh.start(refreshId, interval); - start the Ajax requests</li>
 * <li>Click.refresh.stop(refreshId); - stop the Ajax requests</li>
 * <li>Click.refresh.update(refreshId, interval); - updates to the new interval</li>
 * </ul>
 *
 * In order to invoke these methods use the {@link net.sf.click.jquery.taconite.JQTaconite#eval(java.lang.String)}
 * command to execute raw JavaScript in the browser. For example:
 *
 * <pre class="prettyprint">
 * String refreshId = "id";
 *
 * JQActionLink start = new JQActionLink("start", "Start refresh (5 second intervals)");
 *
 * start.addBehavior(new AjaxBehavior() {
 *
 *     &#64;Override
 *     public ActionResult onAjaxAction(Control source) {
 *         Taconite actionResult = new Taconite();
 *
 *         // Execute raw JavaScript using eval
 *         actionResult.eval("Click.refresh.start('" + refreshId + "', 5000);");
 *          return actionResult;
 *     }
 * });
 *
 * JQActionLink update = new JQActionLink("update", "Update refresh rate (2 second intervals)");
 *
 * update.addBehavior(new AjaxBehavior() {
 *
 *     &#64;Override
 *     public ActionResult onAction(Control source) {
 *         Taconite actionResult = new Taconite();
 *
 *         // Execute raw JavaScript using eval
 *         actionResult.eval("Click.refresh.update('" + refreshId + "', 2000);");
 *         return actionResult;
 *     }
 * }); </pre>
 */
public class JQPollBehavior extends JQBehavior {

    public static String jqueryPollPath = "/click-jquery/poll/jquery.poll.js";

    private static final int DEFAULT_WAIT_TIME = 5000;

    private static final int DEFAULT_MAX_WAIT_TIME = 30000;

    private static final int DEFAULT_WAIT_MULTIPLIER = 2;

    private static final int DEFAULT_MAX_POLLS = 0;

    private static final int DEFAULT_STOP_AFTER_FAILURES = 0;

    private static final boolean DEFAULT_AUTO_START = true;

    // -------------------------------------------------------------- Variables

    private static final long serialVersionUID = 1L;

    /**
     * The unique poll id.
     */
    protected String pollId;

    /**
     * The wait time between polls in ms, defaults to {@value #DEFAULT_WAIT_TIME}.
     */
    protected int waitTime = DEFAULT_WAIT_TIME;

    /**
     * The maximum wait time between polls in ms, defaults to {@value #MAX_WAIT_TIME}.
     */
    protected int maxWaitTime = DEFAULT_MAX_WAIT_TIME;

    /**
     * Amount to multiply the wait time by in case of a server error, timeout,
     * notmodified, or parser error, defaults to {@value #DEFAULT_WAIT_MULTIPLIER}.
     */
    protected int waitMultiplier = DEFAULT_WAIT_MULTIPLIER;

    /**
     * The maximum number of polls after which the poller stops, defaults to
     * {@value #DEFAULT_MAX_POLLS}. A value of 0 or less means infinite number of polls.
     */
    protected int maxPolls = DEFAULT_MAX_POLLS;

    /**
     * The maximum number of failed polls after which poll is stopped,
     * defaults to {@value #DEFAULT_STOP_AFTER_FAILURES}. A value of 0 or less
     * means infinite number of failed polls.
     */
    protected int stopAfterFailures = DEFAULT_STOP_AFTER_FAILURES;

    /**
     * Flag indicating whether polling should start automatically or not, true
     * by default.
     */
    protected boolean autoStart = DEFAULT_AUTO_START;

    // ----------------------------------------------------------- Constructors

    /**
     * Create a JQPollBehavior for the given target control.
     *
     */
    public JQPollBehavior() {
        super(JQEvent.DOMREADY);
    }

    /**
     * Create a JQPollBehavior for the given target control and refreshId.
     *
     * @param pollId the poll unique id
     */
    public JQPollBehavior(String pollId) {
        super(JQEvent.DOMREADY);
        this.pollId = pollId;
    }

    /**
     * Create a JQRefreshHelper for the given target control, refreshIdnd interval.
     *
     * @param pollId the poll unique ID
     * @param waitTime the wait time between polls
     */
    public JQPollBehavior(String pollId, int waitTime) {
        super(JQEvent.DOMREADY);
        this.pollId = pollId;
        this.waitTime = waitTime;
    }

    // Public Properties ------------------------------------------------------

    /**
     * Return the unique refresh id.
     *
     * @return the unique refresh id
     */
    public String getPollId() {
        return pollId;
    }

    /**
     * Set the unique refresh id.
     *
     * @param pollId the unique refresh id
     */
    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    /**
     * Return the wait time (in milliseconds) between polls.
     *
     * @return the wait time (in milliseconds) between polls
     */
    public int getWaitTime() {
        return waitTime;
    }

    /**
     * Set the wait time (in milliseconds) between polls.
     *
     * @param waitTime the wait time (in milliseconds) between polls
     */
    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    /**
     * @return the maximumWaitTime
     */
    public int getMaxWaitTime() {
        return maxWaitTime;
    }

    /**
     * @param maxWaitTime the maxWaitTime to set
     */
    public void setMaxWaitTime(int maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }

    /**
     * @return the waitMultiplier
     */
    public int getWaitMultiplier() {
        return waitMultiplier;
    }

    /**
     * @param waitMultiplier the waitMultiplier to set
     */
    public void setWaitMultiplier(int waitMultiplier) {
        this.waitMultiplier = waitMultiplier;
    }

    /**
     * @return the maxPolls
     */
    public int getMaxPolls() {
        return maxPolls;
    }

    /**
     * @param maxPolls the maxPolls to set
     */
    public void setMaxPolls(int maxPolls) {
        this.maxPolls = maxPolls;
    }

    /**
     * @return the stopAfterFailures
     */
    public int getStopAfterFailures() {
        return stopAfterFailures;
    }

    /**
     * @param stopAfterFailures the stopAfterFailures to set
     */
    public void setStopAfterFailures(int stopAfterFailures) {
        this.stopAfterFailures = stopAfterFailures;
    }

    /**
     * @return the autoStart
     */
    public boolean isAutoStart() {
        return autoStart;
    }

    /**
     * @param autoStart the autoStart to set
     */
    public void setAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
    }

    @Override
    public void setDelay(int delay) {
        throw new UnsupportedOperationException("Delay not supported by JQPollBehavior");
    }

    // Protected Methods ------------------------------------------------------

    /**
     * Create the poll data model for the Ajax {@link #template}.
     *
     * @return the poll data model for the Ajax template
     */
    protected Map<String, Object> createPollModel(Page page, Control source,
        Context context) {

        Map<String, Object> pollModel = new HashMap<String, Object>();

        if (getPollId() != null) {
            addModel(pollModel, "name", getPollId(), page, context);
        }

        if (getWaitTime() != DEFAULT_WAIT_TIME) {
            addModel(pollModel, "min_wait", getWaitTime(), page, context);
        }

        if (getMaxWaitTime() != DEFAULT_MAX_WAIT_TIME) {
            addModel(pollModel, "max_wait", getMaxWaitTime(), page, context);
        }

        if (getMaxPolls() != DEFAULT_MAX_POLLS) {
            addModel(pollModel, "max_polls", getMaxPolls(), page, context);
        }

        if (getStopAfterFailures() != DEFAULT_STOP_AFTER_FAILURES) {
            addModel(pollModel, "stop_after_failures", getStopAfterFailures(), page, context);
        }

        if (!isAutoStart()) {
            addModel(pollModel, "auto_start", false, page, context);
        }

        return pollModel;
    }

    @Override
    protected void addHeadElementsOnce(Control source) {
        super.addHeadElementsOnce(source);

        List<Element> headElements = source.getHeadElements();

        JsImport jsImport = new JsImport(jqueryPollPath);
        if (!headElements.contains(jsImport)) {
            headElements.add(jsImport);
        }
    }

    @Override
    protected void setupScript(JsScript script, Control source) {
        Map templateModel = createTemplateModel(page, source, getContext());
        templateModel.remove("context");
        String templateJson = new JSONWriter().write(templateModel);

        Map pollModel = createPollModel(page, source, getContext());
        String pollJson = new JSONWriter().write(pollModel);

        HtmlStringBuffer buffer = new HtmlStringBuffer();
        buffer.append("jQuery(document).ready(function(){");
        buffer.append("Click.jq.pollTemplate(");
        buffer.append(templateJson);
        buffer.append(",");
        buffer.append(pollJson);
        buffer.append(");");
        buffer.append("});");

        script.setContent(buffer.toString());
    }

    @Override
    public boolean isAjaxTarget(Context context) {
        return true;
    }
}