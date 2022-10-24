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
package net.sf.clickclick.examples.jquery.page.taconite;

import org.apache.click.Control;
import net.sf.clickclick.util.AjaxAdapter;
import net.sf.clickclick.util.Partial;
import net.sf.clickclick.control.html.Div;
import net.sf.clickclick.jquery.control.ajax.JQActionLink;
import net.sf.clickclick.jquery.Taconite;
import org.apache.click.Page;
import org.apache.click.extras.control.DateField;

/**
 *
 */
public class TaconitePage extends Page {

    private JQActionLink link = new JQActionLink("link");

    public void onInit() {
        super.onInit();

        Div wrapper = new Div("dateWrapper");
        final String wrapperId = "wrapper_id";
        wrapper.setId(wrapperId);
        wrapper.add(new DateField("date"));

        link.setActionListener(new AjaxAdapter() {

            public Partial onAjaxAction(Control source) {
                Taconite partial = new Taconite();
                partial.replaceContent('#' + wrapperId, new DateField("date"));
                return partial;
            }
        });

        addControl(wrapper);
        addControl(link);
    }
}
