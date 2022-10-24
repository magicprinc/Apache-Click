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

import net.sf.clickclick.util.AjaxAdapter;
import net.sf.clickclick.util.Partial;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.helper.JQHelper;
import net.sf.clickclick.jquery.Taconite;
import org.apache.click.Control;
import org.apache.click.control.Submit;
import org.apache.commons.lang.math.NumberUtils;

/**
 *
 */
public class ButtonTest extends BorderPage {

    public Submit button = new Submit("button", "Counter: 0");

    public Submit button2 = new Submit("button2", "Counter: 0");

    public void onInit() {
        super.onInit();
        final JQHelper jquery = new JQHelper(button);
        jquery.ajaxify();

        JQHelper jquery2 = new JQHelper(button2);
        jquery2.ajaxify();

        button.addStyleClass("test");
        button.setActionListener(new AjaxAdapter() {
            public Partial onAjaxAction(Control source) {
                Taconite partial = new Taconite();
                int count = NumberUtils.toInt(button.getValue().substring(9));

                button.setLabel("Counter: " + Integer.toString(++count));

                partial.replace(button);
                partial.remove(button2);
                partial.after('#' + button.getId(), button2);
                return partial;
            }
        });

        button2.setActionListener(new AjaxAdapter() {
            public Partial onAjaxAction(Control source) {
                Taconite partial = new Taconite();
                int count = NumberUtils.toInt(button2.getValue().substring(9));
                ++count;

                button2.setLabel("Counter: " + Integer.toString(count));

                partial.replace(button2);
                return partial;
            }
        });
    }
}
