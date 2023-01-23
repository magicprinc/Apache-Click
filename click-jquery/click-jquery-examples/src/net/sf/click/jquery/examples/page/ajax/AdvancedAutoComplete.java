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

import java.util.List;
import net.sf.click.jquery.behavior.JQAutoCompleteBehavior;
import net.sf.click.jquery.control.ajax.JQAutoCompleteTextField;
import net.sf.click.jquery.examples.page.BorderPage;
import net.sf.click.jquery.util.Options;
import org.apache.click.ActionListener;
import org.apache.click.Control;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;

/**
 *
 */
public class AdvancedAutoComplete extends BorderPage {

	private static final long serialVersionUID = 1L;

    private Form form = new Form("form");

    public AdvancedAutoComplete() {
        addControl(form);

        final JQAutoCompleteTextField suburbField = new JQAutoCompleteTextField("suburb", "Select your suburb") {

            public List getAutoCompleteList(String criteria) {
                List suggestions = getPostCodeService().getPostCodeLocations(criteria);
                return suggestions;
            }
        };
        suburbField.setWidth("200px");

        // Set formatResult option to set the field value to the suburb
        String jsFormat =
            "function(data) {"
            + " var values = data[0].split(' ');"
            + " var result = '';"
            + " for(var i = 0; i < values.length - 2; i++){"
            + "   result+=values[i]+' '"

            // Trim whitespaces and left over ','
            + " } return result.replace(/^\\s*/, '').replace(/,\\s*$/, '');"
            + "}";

        Options options = new Options().putFunction("formatResult", jsFormat);

        suburbField.getJQBehavior().setAutocCompleteOptions(options);

        final TextField postCodeField = new TextField("postalCode", "Select your postal code");
        JQAutoCompleteBehavior behavior = new JQAutoCompleteBehavior() {

            @Override
            protected List getAutoCompleteList(String criteria) {
                List suggestions = getPostCodeService().getPostCodeLocations(criteria);
                return suggestions;
            }
        };
        postCodeField.addBehavior(behavior);
        postCodeField.setWidth("200px");

        // Set formatResult option to set the field value to the post code
        options = new Options().putFunction("formatResult", "function(data) {var values = data[0].split(' '); return values[values.length - 1];}");
        behavior.setAutocCompleteOptions(options);

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
        form.add(suburbField);
        form.add(postCodeField);
    }
}
