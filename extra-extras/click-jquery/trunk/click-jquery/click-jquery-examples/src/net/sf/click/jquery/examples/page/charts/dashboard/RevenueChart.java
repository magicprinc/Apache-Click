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
package net.sf.click.jquery.examples.page.charts.dashboard;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.click.jquery.behavior.JQBehavior;
import org.apache.click.control.AbstractControl;
import org.apache.click.element.CssImport;
import org.apache.click.element.JsImport;
import org.apache.click.element.JsScript;
import org.apache.click.util.HtmlStringBuffer;

/**
 *
 */
public class RevenueChart extends AbstractControl {

    private static final long serialVersionUID = 1L;

    public RevenueChart(String name) {
        super(name);
        setAttribute("style", "float:left; margin:5px 30px; width:200px; height:200px;");
    }

    @Override
    public String getTag() {
        return "div";
    }

    @Override
    public List getHeadElements() {
        if (headElements == null) {
            headElements = super.getHeadElements();

            // Import excanvas.js for Internet Explorer which doesn't support the canvas element
            JsImport jsImport = new JsImport("/click-jquery/example/jqplot/excanvas.min.js");
            jsImport.setConditionalComment(JsImport.IF_IE);
            headElements.add(jsImport);

            // Import jquery.js
            headElements.add(new JsImport(JQBehavior.jqueryPath));

            // Import JQPlot libraries
            headElements.add(new JsImport("/click-jquery/example/jqplot/jquery.jqplot.min.js"));
            headElements.add(new JsImport("/click-jquery/example/jqplot/plugins/jqplot.canvasAxisTickRenderer.min.js"));
            headElements.add(new JsImport("/click-jquery/example/jqplot/plugins/jqplot.canvasTextRenderer.min.js"));
            headElements.add(new JsImport("/click-jquery/example/jqplot/plugins/jqplot.dateAxisRenderer.min.js"));

            // Render Chart Page JavaScript template
            Map jsModel = new HashMap();
            jsModel.put("data", getChartData());
            jsModel.put("selector", getId());
            jsModel.put("year", getYear());
            jsModel.put("label", "Revenue for first quarter : " + getYear());
            headElements.add(new JsScript("/charts/dashboard/revenue-chart.js", jsModel));

            headElements.add(new CssImport("/click-jquery/example/jqplot/jquery.jqplot.min.css"));
        }

        return headElements;
    }

    private String getChartData() {
        HtmlStringBuffer buffer = new HtmlStringBuffer(20);
        int year = getYear();
        buffer.append("['").append(year).append("-01-1', 400000],");
        buffer.append("['").append(year).append("-02-1', 450000],");
        buffer.append("['").append(year).append("-03-1', 410000],");
        buffer.append("['").append(year).append("-03-31',510000]");
        return buffer.toString();
    }

    private int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }
}
