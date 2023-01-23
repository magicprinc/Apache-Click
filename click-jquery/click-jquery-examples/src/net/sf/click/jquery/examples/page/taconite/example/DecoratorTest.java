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
import org.apache.click.Control;
import org.apache.click.control.ActionLink;
import net.sf.click.jquery.examples.page.BorderPage;
import net.sf.click.jquery.taconite.JQCommand;
import net.sf.click.jquery.taconite.JQTaconite;
import org.apache.click.Context;
import org.apache.click.ActionResult;
import org.apache.commons.lang.math.NumberUtils;

/**
 *
 */
public class DecoratorTest extends BorderPage {

    private static final long serialVersionUID = 1L;

    final String cssSelector = "a.test";

    public ActionLink link = new ActionLink("button", "Counter: 0");

    @Override
    public void onInit() {
        link.setAttribute("class", "test");

        JQBehavior behavior = new JQBehavior(JQEvent.CLICK) {

            @Override
            public ActionResult onAction(Control source, JQEvent eventType) {
                JQTaconite actionResult = new JQTaconite();
                Context context = getContext();
                int count = NumberUtils.toInt(context.getRequestParameter("count"));
                count++;
                link.setParameter("count", count);
                link.setLabel("Counter: " + count);
                JQCommand command = new JQCommand(JQTaconite.REPLACE, cssSelector, link).characterData(true);

                actionResult.add(command);
                return actionResult;
            }
        };
        behavior.setCssSelector(cssSelector);
        link.addBehavior(behavior);
    }
}
