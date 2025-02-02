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
package net.sf.click.jquery.examples.page.ajax.form;

import net.sf.click.jquery.JQEvent;
import net.sf.click.jquery.behavior.JQBehavior;
import net.sf.click.jquery.control.ajax.JQForm;
import net.sf.click.jquery.examples.page.BorderPage;
import net.sf.click.jquery.taconite.JQTaconite;
import org.apache.click.ActionResult;
import org.apache.click.Control;
import org.apache.click.ajax.DefaultAjaxBehavior;
import org.apache.click.control.Checkbox;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Radio;
import org.apache.click.control.RadioGroup;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.click.util.ClickUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Demonstrates a form that is dynamically built using AJAX, The form is submitted
 * through AJAX.
 */
public class DynamicFormAjax extends BorderPage {

	  private static final long serialVersionUID = 1L;

    private Form form = new JQForm("form");

    private RadioGroup productType = new RadioGroup("productType", true);

    private FieldSet desktopFS = new FieldSet("desktop");

    private FieldSet laptopFS = new FieldSet("laptop");

    private Submit start = new Submit("start");

    private Submit save = new Submit("save");

    public DynamicFormAjax() {
        addControl(form);
        form.add(start);

        // Use explicit binding to determine if the user picked a product type,
        // or submitted the form
        ClickUtils.bind(productType);
        ClickUtils.bind(save);
        if (StringUtils.isNotBlank(productType.getValue()) || save.isClicked()) {
            // If a product type was chosen or the form is submitted, ensure
            // product type group is present in form
            buildProductTypesForm();
        }

        start.addBehavior(new JQBehavior(JQEvent.CLICK) {

            @Override
            public ActionResult onAction(Control source, JQEvent event) {
                // Add product types  to form
                buildProductTypesForm();

                JQTaconite taconite = new JQTaconite();
                // Replace the form in the browser with the dynamically built form
                    taconite.replace(form);
                    // Remove the messagebox element
                taconite.remove("#messagebox");
                return taconite;
            }
        });
    }

    private void buildProductTypesForm() {
        form.add(productType);
        form.add(save);

        // Set Behavior on Submit which will be invoked when form is submitted
        save.addBehavior(new DefaultAjaxBehavior() {

            @Override
            public ActionResult onAction(Control source) {
                JQTaconite taconite = new JQTaconite();
                // Remove the messagebox element
                taconite.remove("#messagebox");
                if (form.isValid()) {
                    // Add the messagebox element above the form
                    taconite.before(form, "<div id='messagebox' class='infoMsg'>Form saved!</div>");
                }
                // Replace the form in the browser with the dynamically built form
                    taconite.replace(form);
                return taconite;
            }
        });

        final Radio laptop = new Radio("laptop");

        laptop.addBehavior(new JQBehavior(JQEvent.CLICK) {

            @Override
            public ActionResult onAction(Control source, JQEvent eventType) {
                JQTaconite taconite = new JQTaconite();

                // Replace the form in the browser with the dynamically built form
                taconite.replace(form);
                    // Remove the messagebox element
                taconite.remove("#messagebox");
                return taconite;
            }
        });

        final Radio desktop = new Radio("desktop");

        desktop.addBehavior(new JQBehavior(JQEvent.CLICK) {

            @Override
            public ActionResult onAction(Control source, JQEvent eventType) {
                JQTaconite taconite = new JQTaconite();
                // Replace the form in the browser with the dynamically built form
                taconite.replace(form);
                    // Remove the messagebox element
                taconite.remove("#messagebox");
                return taconite;
            }
        });

        productType.add(desktop);
        // Use explicit binding to determine if the user selected a desktop
        ClickUtils.bind(desktop);
        if (desktop.isChecked()) {
            // If desktop was selected, build the desktop form
                buildDesktopForm();
            }

        productType.add(laptop);
        // Use explicit binding to determine if the user selected a laptop
        ClickUtils.bind(laptop);
        if (laptop.isChecked()) {
            // If laptop was selected, build the laptop form
                buildLaptopForm();
            }
    }

    private void buildDesktopForm() {
        form.add(desktopFS);

        desktopFS.add(new TextField("quantity", true));
    }

    private void buildLaptopForm() {
        form.add(laptopFS);

        laptopFS.add(new TextField("quantity", true));
        laptopFS.add(new Checkbox("adapter", "Include adapter?"));
    }
}