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
package net.sf.click.jquery.examples.page.taconite.example;

import net.sf.click.jquery.JQEvent;
import net.sf.click.jquery.behavior.JQBehavior;
import net.sf.click.jquery.examples.page.BorderPage;
import net.sf.click.jquery.taconite.JQCommand;
import net.sf.click.jquery.taconite.JQTaconite;
import org.apache.click.Control;
import org.apache.click.ActionResult;
import org.apache.click.control.ActionLink;
import org.apache.commons.lang.math.NumberUtils;

/**
 *
 */
public class Counter extends BorderPage {

	private static final long serialVersionUID = 1L;

    public ActionLink link = new ActionLink("link", "Counter: 0");

    @Override
    public void onInit() {
        link.addBehavior(new JQBehavior(JQEvent.CLICK) {

            @Override
            protected void addHeadElementsOnce(Control source) {
                super.addHeadElementsOnce(source);
            }

            @Override
            public ActionResult onAction(Control source, JQEvent eventType) {
                JQTaconite actionResult = new JQTaconite();
                int count = NumberUtils.toInt(getContext().getRequestParameter("count"));
                ++count;
                link.setParameter("count", count);
                link.setLabel("Counter: " + count);
                JQCommand command = new JQCommand("replace", link).characterData(true);

                actionResult.add(command);
                return actionResult;
            }
        });
    }
}
