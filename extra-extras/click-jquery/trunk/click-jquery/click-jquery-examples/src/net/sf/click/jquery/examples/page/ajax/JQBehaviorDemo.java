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
package net.sf.click.jquery.examples.page.ajax;

import net.sf.click.jquery.JQEvent;
import net.sf.click.jquery.behavior.JQBehavior;
import net.sf.click.jquery.examples.page.BorderPage;
import net.sf.click.jquery.taconite.JQTaconite;
import org.apache.click.Control;
import org.apache.click.ActionResult;
import org.apache.click.control.Button;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;

public class JQBehaviorDemo extends BorderPage {

	private static final long serialVersionUID = 1L;

    private Form form = new Form("form");

    private Button start = new Button("start");

    private Submit end = new Submit("end");

    public JQBehaviorDemo() {

        JQBehavior behavior = new JQBehavior() {

            @Override
            public ActionResult onAction(Control source, JQEvent event) {
                JQTaconite taconite = new JQTaconite();
                taconite.after(form, "<div>" + source.getName() + " clicked!</div>");
                return taconite;
            }
        };

        start.addBehavior(behavior);
        end.addBehavior(behavior);

        form.add(start);
        form.add(end);
        addControl(form);
    }
}
