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

import net.sf.click.ajax4click.MultiActionResult;
import net.sf.click.ajax4click.examples.page.BorderPage;
import net.sf.click.ajax4click.jquery.JQBehavior;
import net.sf.click.ajax4click.jquery.JQEvent;
import org.apache.click.ActionResult;
import org.apache.click.Control;
import org.apache.click.control.ActionLink;

/**
 * This example demonstrates how to return multiple results to the browser.
 */
public class MultiResultDemo extends BorderPage {

    @Override
    public void onInit() {
        super.onInit();
        addModel("title", "Multi Result");
        ActionLink link = new ActionLink("link", "Load Multiple Results");
        link.setId("linkId");
        addControl(link);

        link.addBehavior(new JQBehavior() {

            @Override
            public ActionResult onAction(Control source, JQEvent event) {
                MultiActionResult result = new MultiActionResult();
                result.add("result1", "<h3>Thank you for shopping at MyCorp!</h3>");
                result.add("result2", "Your invoice number is:");
                result.add("result3", "isIC123");
                return result;
            }
        });

        addControl(link);
    }
}
