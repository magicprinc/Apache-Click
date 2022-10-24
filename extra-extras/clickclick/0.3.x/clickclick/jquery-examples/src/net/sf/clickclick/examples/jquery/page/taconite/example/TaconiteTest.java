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

import java.util.ArrayList;
import java.util.List;
import org.apache.click.Control;
import org.apache.click.Page;
import org.apache.click.control.Form;
import org.apache.click.control.Option;
import org.apache.click.extras.control.DateField;
import net.sf.clickclick.util.AjaxAdapter;
import net.sf.clickclick.util.Partial;
import net.sf.clickclick.control.html.Div;
import net.sf.clickclick.jquery.control.ajax.JQActionLink;
import net.sf.clickclick.jquery.Taconite;

/**
 *
 */
public class TaconiteTest extends Page {

    private JQActionLink link = new JQActionLink("link");

    private static final List<Option> SORTABLE_OPTIONS = new ArrayList<Option>();

    static {
        for (int i = 1; i <= 6; i++) {
            SORTABLE_OPTIONS.add(new Option(Integer.toString(i),
                "Drag to sort me " + i));
        }
    }

    public void onInit() {
        Form form = new Form("form");
        Div wrapper = new Div();
        final String wrapperId = "date_wrapper";
        wrapper.setId(wrapperId);
        wrapper.add(new DateField("date"));
        form.add(wrapper);
        addControl(form);

        link.setActionListener(new AjaxAdapter() {

            public Partial onAjaxAction(Control source) {
                Taconite partial = new Taconite();
                partial.replaceContent('#' + wrapperId, new DateField("date"));
                return partial;
            }
        });

        addControl(link);
    }
}
