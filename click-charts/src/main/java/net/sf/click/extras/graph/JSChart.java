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
package net.sf.click.extras.graph;

import org.apache.click.control.AbstractControl;
import org.apache.click.element.JsScript;
import org.apache.click.util.HtmlStringBuffer;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a abstract JavaScript Chart control.
 */
public abstract class JSChart extends AbstractControl {

    // Instance Variables -----------------------------------------------------

    /**
     * Height of the DIV element that encloses this chart, default height 350 px.
     */
    protected int chartHeight = 350;

    /**
     * Width of the DIV element that encloses this chart, default width 380 px.
     */
    protected int chartWidth = 380;

    /** The chart display label. */
    protected String label = "Chart";

    /** The list of X-Axis labels. */
    protected List xLabels = new ArrayList();

    /** The list of Y-Axis values. */
    protected List yValues = new ArrayList();

    // Public Methods ---------------------------------------------------------

    /**
     * Adds a "point" to the grapic/chart at the end of the list.
     *
     * @param pointLabel the displayed label of the "point"
     * @param pointValue the value of the "point".
     */
    public void addPoint(String pointLabel, Integer pointValue) {
        xLabels.add(pointLabel);
        yValues.add(pointValue);
    }

    /**
     * Adds a "point" to the grapic/chart at a specified position in the list.
     *
     * @param index index at which the specified point is to be inserted
     * @param pointLabel the displayed label of the "point"
     * @param pointValue the value of the "point".
     */
    public void addPoint(int index, String pointLabel, Integer pointValue) {
        xLabels.add(index, pointLabel);
        yValues.add(index, pointValue);
    }

    /**
     * Return the width of the chart (the enclosing DIV element).
     *
     * @return the width of the chart
     */
    public int getChartWidth() {
        return chartWidth;
    }

    /**
     * Set the width of the chart (of the enclosing DIV element), as a
     * pixel value.
     *
     * @param chartWidth the chart width in pixels.
     */
    public void setChartWidth(int chartWidth) {
        this.chartWidth = chartWidth;
    }

    /**
     * Return the height of the chart (the enclosing DIV element).
     *
     * @return the height of the chart
     */
    public int getChartHeight() {
        return chartHeight;
    }

    /**
     * Set the height of the chart (of the enclosing DIV element), as a
     * pixel value.
     *
     * @param chartHeight the chart height in pixels.
     */
    public void setChartHeight(int chartHeight) {
        this.chartHeight = chartHeight;
    }

    /**
     * Return the label of the chart.
     *
     * @return the label of the chart
     */
    public String getLabel() {
        return label;
    }

    /**
     * Set the chart display caption.
     *
     * @param label the display label of the chart
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Return the HTML HEAD elements for the javascript files used by this
     * control.
     *
     * @see org.apache.click.Control#getHeadElements()
     *
     * @return the HTML HEAD elements for the javascript files used by this
     * control.
     */
    @Override
    public final List getHeadElements() {
        if (headElements == null) {
            headElements = super.getHeadElements();
            headElements.addAll(getChartHeadElements());
        }

        JsScript script = new JsScript();
        script.setId(getId() + "_js_setup");

        if (!headElements.contains(script)) {
            script.setExecuteOnDomReady(true);

            HtmlStringBuffer buffer = new HtmlStringBuffer(512);
            String var = "g_" + getId();
            buffer.append("var ");
            buffer.append(var);
            buffer.append(" = new ");
            buffer.append(getJSChartType());
            buffer.append("(); ");

            for (int i = 0; i < xLabels.size(); i++) {
                String pointLabel = (String) xLabels.get(i);
                Integer pointValue = (Integer) yValues.get(i);
                buffer.append(var);
                buffer.append(".add('");
                buffer.append(pointLabel);
                buffer.append("',");
                buffer.append(pointValue);
                buffer.append("); ");
            }

            buffer.append(var);
            buffer.append(".render('");
            buffer.append(getId());
            buffer.append("','");
            buffer.append(getLabel());
            buffer.append("');\n");

            script.setContent(buffer.toString());
            headElements.add(script);
        }

        return headElements;
    }

    /**
     * This method does nothing.
     *
     * @see org.apache.click.Control#setListener(Object, String)
     *
     * @param listener the listener object with the named method to invoke
     * @param method the name of the method to invoke
     */
    @Override
    public void setListener(Object listener, String method) {
    }

    /**
     * Render the HTML representation of the chart.
     *
     * @see #toString()
     *
     * @param buffer the specified buffer to render the control's output to
     */
    @Override
    public void render(HtmlStringBuffer buffer) {
        buffer.elementStart("div");
        buffer.appendAttribute("id", getId());
        buffer.appendAttribute("style", "overflow:auto; position:relative; height:" + getChartHeight() + "px; width:" + getChartWidth() + "px;");
        buffer.elementEnd();
    }

    /**
     * Return the HTML rendered chart.
     *
     * @return the HTML rendered chart string
     */
    @Override
    public String toString() {
        HtmlStringBuffer buffer = new HtmlStringBuffer(getControlSizeEst());
        render(buffer);
        return buffer.toString();
    }

    // Event Handlers ---------------------------------------------------------

    /**
     * Returns true, as javascript charts perform no server side logic.
     *
     * @return true
     */
    @Override
    public boolean onProcess() {
        return true;
    }

    // Abstract Methods -------------------------------------------------------

    /**
     * Return the HTML HEAD elements.
     *
     * @return the HTML HEAD elements
     */
    protected abstract List getChartHeadElements();

    /**
     * Return the JavaScript Chart type.
     *
     * @return the JavaScript Chart type
     */
    protected abstract String getJSChartType();

}