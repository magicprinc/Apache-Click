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

import net.sf.clickclick.util.AjaxAdapter;
import org.apache.click.Control;
import org.apache.click.control.Form;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import net.sf.clickclick.util.Partial;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.control.ajax.JQSelect;
import net.sf.clickclick.jquery.Taconite;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;

public class SelectDemo extends BorderPage {

	private static final long serialVersionUID = 1L;

	public String title = "Select Demo";

    // Create a JQuery Select control.
    private JQSelect provinceSelect = new JQSelect("provinceSelect", "Select a Province:", true);

    private Select citySelect = new Select("citySelect", "Select a City:", true);

    private Form form = new Form("form");

    public SelectDemo() {
        // Create a stateful page
        setStateful(true);

        addControl(form);

        provinceSelect.setActionListener(new AjaxAdapter() {
            public Partial onAjaxAction(Control source) {
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

    private Partial updateCities() {
        Taconite partial = new Taconite();

        // Clear the city options
        citySelect.getOptionList().clear();

        // Populate the cities
        populateCities();

        // Update the citySelect
        partial.replace(citySelect);
        return partial;
    }

    private void populateProvinces() {
        provinceSelect.add(Option.EMPTY_OPTION);
        provinceSelect.add(new Option("GAU", "Gauteng"));
        provinceSelect.add(new Option("WC", "Western Cape"));
        provinceSelect.add(new Option("N", "KwaZulu Natal"));
    }

    private void populateCities() {
        citySelect.add(Option.EMPTY_OPTION);

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
