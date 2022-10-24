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
import net.sf.clickclick.control.html.HtmlLabel;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.control.ajax.JQActionLink;
import net.sf.clickclick.jquery.Taconite;
import net.sf.clickclick.util.AjaxAdapter;
import net.sf.clickclick.util.Partial;
import org.apache.click.Control;

public class TextDemo extends BorderPage {

    private JQActionLink link = new JQActionLink("update", "Request server time", "updateId");

    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private HtmlLabel label = new HtmlLabel("label", "Server time : " + dateFormat.format(new Date()));

    public TextDemo() {
        addControl(link);
        addControl(label);

        link.setActionListener(new AjaxAdapter() {

            public Partial onAjaxAction(Control source) {
                Taconite partial = new Taconite();

                // Using a CSS selector to replace the Label content with the latest
                // Date
                label.setLabel("Current time : " + dateFormat.format(new Date()));
                partial.replace(label);
                return partial;
            }
        });
    }
}
