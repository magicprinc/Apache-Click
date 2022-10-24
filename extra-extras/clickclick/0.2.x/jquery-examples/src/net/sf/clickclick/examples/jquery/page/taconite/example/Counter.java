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
package net.sf.clickclick.examples.jquery.page.taconite.example;

import org.apache.click.Control;
import net.sf.clickclick.util.AjaxAdapter;
import net.sf.clickclick.util.Partial;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.control.ajax.JQActionLink;
import net.sf.clickclick.jquery.Command;
import net.sf.clickclick.jquery.Taconite;
import org.apache.commons.lang.math.NumberUtils;

/**
 *
 */
public class Counter extends BorderPage {

    public JQActionLink link = new JQActionLink("link", "Counter: 0");

    public void onInit() {
        super.onInit();
        link.setActionListener(new AjaxAdapter() {
            public Partial onAjaxAction(Control source) {
                Taconite partial = new Taconite();
                int count = NumberUtils.toInt(link.getParameter("count"));
                ++count;
                link.setParameter("count", Integer.toString(count));
                link.setLabel("Counter: " + Integer.toString(count));
                Command command = new Command("replace", link).characterData(true);

                // link normally contian '&' which breaks XML parsing. TODO update
                // Click Link's to use &amp; instead

                partial.add(command);
                return partial;
            }
        });
    }
}
