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
package net.sf.clickclick.examples.jquery.page.ajax.form;

import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.Taconite;
import net.sf.clickclick.jquery.control.ajax.JQActionButton;
import net.sf.clickclick.jquery.control.ajax.JQRadioGroup;
import net.sf.clickclick.util.AjaxAdapter;
import net.sf.clickclick.util.Partial;
import org.apache.click.ActionListener;
import org.apache.click.Control;
import org.apache.click.control.Checkbox;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Radio;
import org.apache.click.control.RadioGroup;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;

/**
 * Provides a dynamically built Form using Ajax.
 *
 * Couple of things to note:
 *  - No Ajax specific controls are used, hence the onInit event needs to register
 *    each Ajax enabled control individually
 *  - We override getHeadElements to return the necessary JQuery libraries through
 *    the JQHelper class
 *  - JQHelper is setup so respond with an Ajax call for any control with the
 *    class 'callback'. Thus any control with the class 'callback' will be make
 *    an Ajax request if it is clicked on
 */
public class DynamicFormAjax extends BorderPage {

    private Form form = new Form("form");

    private JQActionButton start = new JQActionButton("start");

    private JQRadioGroup type = new JQRadioGroup("type");

    private FieldSet desktopFS = new FieldSet("desktop");

    private FieldSet laptopFS = new FieldSet("laptop");

    private Submit save = new Submit("save");

    public DynamicFormAjax() {
        setStateful(true);

        start.setActionListener(new AjaxAdapter() {

            @Override
            public Partial onAjaxAction(Control source) {
                Taconite taconite = new Taconite();
                if (!form.contains(type)) {
                    form.add(type);
                    buildAjaxForm(type);
                    taconite.replace(form);
                }
                return taconite;
            }
        });

        form.add(start);
        addControl(form);
    }

    private void buildAjaxForm(final RadioGroup type) {
        form.add(save);

        save.setActionListener(new ActionListener() {

            public boolean onAction(Control source) {
                System.out.println("SAVED");
                return true;
            }
        });

        final Radio laptop = new Radio("laptop");

        laptop.setActionListener(new AjaxAdapter() {

            @Override
            public Partial onAjaxAction(Control source) {
                Taconite taconite = new Taconite();
                if (form.contains(laptopFS)) {
                    return taconite;
                }

                type.setValue(laptop.getValue());
                form.remove(desktopFS);
                form.add(laptopFS);
                initLaptop(laptopFS);
                taconite.replace(form);
                return taconite;
            }
        });

        final Radio desktop = new Radio("desktop");

        desktop.setActionListener(new AjaxAdapter() {

            @Override
            public Partial onAjaxAction(Control source) {
                Taconite taconite = new Taconite();
                if (form.contains(desktopFS)) {
                    return taconite;
                }

                type.setValue(desktop.getValue());
                form.remove(laptopFS);
                form.add(desktopFS);
                initDesktop(desktopFS);
                taconite.replace(form);
                return taconite;
            }
        });

        type.add(desktop);
        type.add(laptop);
    }

    private void initDesktop(FieldSet fieldSet) {
        if (!fieldSet.getControls().isEmpty()) {
            return;
        }
        fieldSet.add(new TextField("quantity"));
    }

    private void initLaptop(FieldSet fieldSet) {
        if (!fieldSet.getControls().isEmpty()) {
            return;
        }
        fieldSet.add(new TextField("quantity"));
        fieldSet.add(new Checkbox("adapter", "Include adapter?"));
    }
}
