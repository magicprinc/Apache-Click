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
package net.sf.click.ajax4click.examples.page.basic;

import java.util.Date;
import net.sf.click.ajax4click.MultiActionResult;
import net.sf.click.ajax4click.examples.page.BorderPage;
import net.sf.click.ajax4click.jquery.JQBehavior;
import net.sf.click.ajax4click.jquery.JQEvent;
import org.apache.click.ActionResult;
import org.apache.click.Control;
import org.apache.click.control.ActionLink;

/**
 *
 */
public class BasicAjaxWithEffects extends BorderPage {

    @Override
    public void onInit() {
        super.onInit();
        addModel("title", "Basic Ajax with Effects");

        ActionLink link = new ActionLink("link", "What's the Time?");
        link.setId("linkId");
        addControl(link);

        link.addBehavior(new JQBehavior() {

            @Override
            public ActionResult onAction(Control source, JQEvent event) {
                MultiActionResult result = new MultiActionResult();
                String time = getFormat().date(new Date(), "HH:mm:ss");
                // Add the time under the name 'time'
                result.add("time", "The time is now: " + time);
                return result;
            }
        });

        addControl(link);
    }
}
