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
import org.apache.click.control.ActionLink;
import net.sf.clickclick.util.AjaxAdapter;
import net.sf.clickclick.util.Partial;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.helper.JQHelper;
import net.sf.clickclick.jquery.Command;
import net.sf.clickclick.jquery.Taconite;
import org.apache.commons.lang.math.NumberUtils;

/**
 *
 */
public class DecoratorTest extends BorderPage {

    public ActionLink button = new ActionLink("button", "Counter: 0");
    //public ActionLink button2 = new ActionLink("button2", "Counter: 0");

    public void onInit() {
        super.onInit();
        
        button.setAttribute("class", "test");
        //button2.setAttribute("class", "test");
        new JQHelper(button, "a.test").ajaxify();
        
        button.setActionListener(new AjaxAdapter() {
            public Partial onAjaxAction(Control source) {
                Taconite partial = new Taconite();
                int count = NumberUtils.toInt(button.getParameter("count"));
                ++count;
                button.setParameter("count", Integer.toString(count));
                button.setLabel("Counter: " + Integer.toString(count));
                Command command = new Command(Taconite.REPLACE, "a.test",button).characterData(true);

                // link normally contains '&' which breaks XML parsing. TODO update
                // Click Link's to use &amp; instead

                //command.add(button2);
                partial.add(command);
                return partial;
            }
        });
    }
}
