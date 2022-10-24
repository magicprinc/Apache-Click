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

import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.Taconite;
import net.sf.clickclick.jquery.control.ajax.JQActionButton;
import net.sf.clickclick.jquery.helper.JQHelper;
import net.sf.clickclick.jquery.util.JQAjaxAdapter;
import net.sf.clickclick.jquery.util.JQEvent;
import net.sf.clickclick.util.AjaxAdapter;
import net.sf.clickclick.util.Partial;
import org.apache.click.Control;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;

public class JQHelperDemo extends BorderPage {

    private Form form = new Form("form");

    private JQActionButton start = new JQActionButton("start");

    private Submit end = new Submit("end");

    private JQHelper helper = new JQHelper("helper-id");

    public JQHelperDemo() {
        // Bind any element with the callback class to the click event
        helper.bind(".callback", JQEvent.CLICK);

        // Disable further bindings
        helper.setBindingDisabled(true);

        // Replace JQActionButton helper with a custom helper
        start.setJQueryHelper(helper);

        start.setActionListener(new AjaxAdapter() {

            @Override
            public Partial onAjaxAction(Control source) {
                Taconite taconite = new Taconite();
                taconite.after(form, "<div>Start clicked!</div>");
                return taconite;
            }
        });
        start.setAttribute("class", "callback");

        end.setActionListener(new JQAjaxAdapter() {

            @Override
            public Partial onAjaxAction(Control source, JQEvent event) {
                Taconite taconite = new Taconite();
                taconite.after(form, "<div>End clicked!</div>");
                return taconite;
            }
        });
        end.setAttribute("class", "callback");
        // End is not an Ajax enabled Control, so we register it explicitly
        helper.registerAjaxControl(end);

        form.add(start);
        form.add(end);
        addControl(form);
    }

    /*
    public List getHeadElements() {
        if (headElements == null) {
            headElements = super.getHeadElements();
            helper.addHeadElements(headElements);
        }
        return headElements;
    }*/
}
