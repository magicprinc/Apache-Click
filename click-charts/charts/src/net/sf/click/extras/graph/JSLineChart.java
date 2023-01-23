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

import java.util.ArrayList;
import java.util.List;
import org.apache.click.Context;
import org.apache.click.element.JsImport;
import org.apache.click.util.ClickUtils;


/**
 * Provides a Line Chart control based on JavaScript only.
 *
 * <table class='htmlHeader' cellspacing='10'>
 * <tr>
 * <td>
 * <img align='middle' hspace='2' src='line-chart.png' title='Line Chart'/>
 * </td>
 * </tr>
 * </table>
 *
 * This control uses the <a href="http://www.walterzorn.com/jsgraphics">JSGraphics</a>
 * library.
 */
public class JSLineChart extends JSChart {

    private static final long serialVersionUID = 1L;

    // ----------------------------------------------------------- Constructors

    /**
     * Create a line chart with no name defined.
     * <p/>
     * <b>Please note</b> the control's name must be defined before it is valid.
     */
    public JSLineChart() {
    }

    /**
     * Create a line chart with the given name.
     *
     * @param name the button name
     */
    public JSLineChart(String name) {
        super.setName(name);
    }

    /**
     * Create a line chart with the given name and label.
     *
     * @param name the name of the chart control
     * @param label the label of the chart that will be displayed
     */
    public JSLineChart(String name, String label) {
        super.setName(name);
        setLabel(label);
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Return the HTML HEAD elements.
     *
     * @see JSChart#getChartHeadElements()
     *
     * @return the HTML HEAD elements
     */
    @Override
    protected List getChartHeadElements() {
        List chartHeadElements = new ArrayList(3);
        Context context = getContext();
        String versionIndicator = ClickUtils.getResourceVersionIndicator(context);

        chartHeadElements.add(new JsImport("/click/control.js", versionIndicator));
        chartHeadElements.add(new JsImport("/click/graph/jsgraph/wz_jsgraphics.js", versionIndicator));
        chartHeadElements.add(new JsImport("/click/graph/jsgraph/line.js", versionIndicator));

        return chartHeadElements;
    }

    /**
     * Return the JavaScript Chart type.
     *
     * @see JSChart#getJSChartType()
     *
     * @return the JavaScript Chart type
     */
    protected String getJSChartType() {
        return "line_graph";
    }

}
