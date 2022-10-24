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
package net.sf.clickclick.examples.jquery.page.ajax;

import java.text.SimpleDateFormat;
import java.util.Date;
import net.sf.clickclick.control.Text;
import net.sf.clickclick.control.html.Div;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.control.ajax.JQActionLink;
import net.sf.clickclick.jquery.helper.JQRefreshHelper;
import net.sf.clickclick.jquery.Taconite;
import net.sf.clickclick.util.AjaxAdapter;
import net.sf.clickclick.util.Partial;
import org.apache.click.Control;

/**
 *
 */
public class RefreshDemo extends BorderPage {

    private Div clock = new Div("clock");
    private Text value = new Text();
    private JQActionLink start = new JQActionLink("start", "Start refresh (5 second intervals)");
    private JQActionLink stop = new JQActionLink("stop", "Stop refresh");

    private String refreshId = "id";

    private SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

    public RefreshDemo() {
        addControl(clock);
        addControl(start);
        addControl(stop);

        value.setText(getTime());
        clock.add(value);

        clock.setActionListener(new AjaxAdapter() {

            public Partial onAjaxAction(Control source) {
                Taconite partial = new Taconite();

                // Replace the clock with the updated clock
                partial.replaceContent(clock, value);

                return partial;
            }
        });

        start.setActionListener(new AjaxAdapter(){
            public Partial onAjaxAction(Control source) {
                Taconite partial = new Taconite();

                partial.eval("Click.refresh.start('" + refreshId + "', 5000);");

                return partial;
            }
        });

        stop.setActionListener(new AjaxAdapter(){
            public Partial onAjaxAction(Control source) {
                Taconite partial = new Taconite();

                partial.eval("Click.refresh.stop('" + refreshId + "');");

                return partial;
            }
        });

        JQRefreshHelper helper = new JQRefreshHelper(clock, refreshId, 2000);
        
        helper.ajaxify();
    }

    private String getTime() {
        return format.format(new Date());
    }
}
