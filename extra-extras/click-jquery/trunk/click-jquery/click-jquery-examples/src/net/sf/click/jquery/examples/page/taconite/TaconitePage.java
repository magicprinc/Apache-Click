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
package net.sf.click.jquery.examples.page.taconite;

import net.sf.click.jquery.JQEvent;
import net.sf.click.jquery.behavior.JQBehavior;
import net.sf.click.jquery.examples.control.html.Div;
import net.sf.click.jquery.taconite.JQTaconite;
import org.apache.click.Control;
import org.apache.click.Page;
import org.apache.click.ActionResult;
import org.apache.click.control.ActionLink;
import org.apache.click.extras.control.DateField;

/**
 *
 */
public class TaconitePage extends Page {

	private static final long serialVersionUID = 1L;

    private ActionLink link = new ActionLink("link");

    @Override
    public void onInit() {
        final Div wrapper = new Div("dateWrapper");
        wrapper.setId("wrapper_id");

        wrapper.add(new DateField("date"));

        link.addBehavior(new JQBehavior("click") {

            @Override
            public ActionResult onAction(Control source, JQEvent event) {
                JQTaconite actionResult = new JQTaconite();
                actionResult.replaceContent(wrapper, new DateField("date"));
                return actionResult;
            }
        });

        addControl(wrapper);
        addControl(link);
    }
}
