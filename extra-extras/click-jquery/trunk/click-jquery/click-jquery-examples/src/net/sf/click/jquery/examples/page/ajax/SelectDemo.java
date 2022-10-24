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
import org.apache.click.Control;
import org.apache.click.control.Form;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import net.sf.click.jquery.examples.page.BorderPage;
import net.sf.click.jquery.taconite.JQTaconite;
import org.apache.click.ActionResult;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.click.util.ClickUtils;

public class SelectDemo extends BorderPage {

	private static final long serialVersionUID = 1L;

    // Create a JQuery Select control.
    private Select provinceSelect = new Select("provinceSelect", "Select a Province:", true);

    private Select citySelect = new Select("citySelect", "Select a City:", true);

    private Form form = new Form("form");

    public SelectDemo() {
        addControl(form);

        provinceSelect.addBehavior(new JQBehavior(JQEvent.CHANGE) {
            @Override
            public ActionResult onAction(Control source, JQEvent event) {
                return updateCities();
            }
        });

        form.add(new TextField("name", true));
        form.add(provinceSelect);
        form.add(citySelect);
        form.add(new Submit("submit", this, "onSubmit"));

        //When page is initialized, load the provinces and cities.
        populateProvinces();
        populateCities();
    }

    // -------------------------------------------------------- Private Methods

    public boolean onSubmit() {
        if (form.isValid()) {
            // Save form
        }
        return true;
    }

    private ActionResult updateCities() {
        JQTaconite actionResult = new JQTaconite();

        // Update citySelect
        actionResult.replace(citySelect);
        return actionResult;
    }

    private void populateProvinces() {
        provinceSelect.add(Option.EMPTY_OPTION);
        provinceSelect.add(new Option("GAU", "Gauteng"));
        provinceSelect.add(new Option("WC", "Western Cape"));
        provinceSelect.add(new Option("N", "KwaZulu Natal"));
    }

    private void populateCities() {
        citySelect.add(Option.EMPTY_OPTION);

        // explicitly bind the province select value so we can determine which
        // cities to add
        ClickUtils.bind(provinceSelect);

        // Retrieve the selected province
        String provinceCode = provinceSelect.getValue();
        if ("GAU".equals(provinceCode)) {
            citySelect.add(new Option("PTA", "Pretoria"));
            citySelect.add(new Option("JHB", "Johannesburg"));
            citySelect.add(new Option("CEN", "Centurion"));
        } else if ("WC".equals(provinceCode)) {
            citySelect.add(new Option("CT", "Cape Town"));
            citySelect.add(new Option("G", "George"));
        } else if ("N".equals(provinceCode)) {
            citySelect.add(new Option("DBN", "Durban"));
        }
    }
}
