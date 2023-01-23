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
package net.sf.click.jquery.examples.page.charts.line;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.click.jquery.behavior.JQBehavior;
import net.sf.click.jquery.examples.page.BorderPage;
import org.apache.click.element.CssImport;
import org.apache.click.element.JsImport;
import org.apache.click.element.JsScript;
import org.apache.click.util.HtmlStringBuffer;

/**
 *
 */
public class InteractiveChart extends BorderPage {

    private static final long serialVersionUID = 1L;

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
            headElements.add(new JsImport("/click-jquery/example/jqplot/plugins/jqplot.dragable.min.js"));
            headElements.add(new JsImport("/click-jquery/example/jqplot/plugins/jqplot.highlighter.min.js"));
            headElements.add(new JsImport("/click-jquery/example/jqplot/plugins/jqplot.trendline.min.js"));

            // Render InteractiveChart JavaScript template
            Map jsModel = new HashMap();
            jsModel.put("data", getChartData());
            jsModel.put("label", "Interactive demo");
            headElements.add(new JsScript("/charts/line/interactive-chart.js", jsModel));

            headElements.add(new CssImport("/click-jquery/example/jqplot/jquery.jqplot.min.css"));

        }
        return headElements;
    }

    private String getChartData() {
        HtmlStringBuffer buffer = new HtmlStringBuffer(20);
        buffer.append("['2009-01-1', 400000],");
        buffer.append("['2009-02-1', 420000],");
        buffer.append("['2009-03-1', 410000],");
        buffer.append("['2009-03-31', 430000]");
        return buffer.toString();
    }
}
