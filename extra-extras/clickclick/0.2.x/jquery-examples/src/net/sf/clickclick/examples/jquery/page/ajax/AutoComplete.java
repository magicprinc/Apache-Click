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

import java.util.List;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.control.ajax.JQAutoCompleteTextField;
import org.apache.click.ActionListener;
import org.apache.click.Control;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;

/**
 *
 */
public class AutoComplete extends BorderPage {

    private Form form = new Form("form");

    public AutoComplete() {
        addControl(form);

        final JQAutoCompleteTextField autoField = new JQAutoCompleteTextField("autoField") {

            public List getAutoCompleteList(String criteria) {
                List suggestions = getPostCodeService().getPostCodeLocations(criteria);
                return suggestions;
            }
        };
        autoField.setWidth("200px");

        Submit submit = new Submit("submit");
        submit.setActionListener(new ActionListener() {
            public boolean onAction(Control source) {
                if (form.isValid()) {
                    // save form data
                }
                return true;
            }
        });

        form.add(submit);
        form.add(autoField);
    }
}
