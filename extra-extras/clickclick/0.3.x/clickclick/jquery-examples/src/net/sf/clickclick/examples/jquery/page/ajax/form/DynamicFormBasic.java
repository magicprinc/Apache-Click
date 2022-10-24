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

import java.util.List;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.Taconite;
import net.sf.clickclick.jquery.helper.JQHelper;
import net.sf.clickclick.util.AjaxAdapter;
import net.sf.clickclick.jquery.util.JQEvent;
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
public class DynamicFormBasic extends BorderPage {

    private Form form = new Form("form");

    private RadioGroup type = new RadioGroup("type");

    private FieldSet desktopFS = new FieldSet("desktop");

    private FieldSet laptopFS = new FieldSet("laptop");

    private Submit start = new Submit("start");

    private Submit save = new Submit("save");

    private JQHelper helper = new JQHelper(start, ".callback");

    public DynamicFormBasic() {
        // Note: Page is set to stateful
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
        start.setAttribute("class", "callback");

        form.add(start);
        addControl(form);
    }

    @Override
    public void onInit() {
        // 1. Here we ajaxify the start button
        // (ajaxify will register and bind the start button)
        helper.ajaxify();

        // 2.1 Bind will render JQuery code such as:
        // jQuery(':radio').live('click', someFunction );
        // Since the radio buttons are rendered dynamically they may or may
        // not be visible at this stage. So instead of ajaxifying the radios
        // we only create a binding for them so that when they are drawn
        // the necessary JavaScript code is already present and will start
        // triggering the events
        helper.bind(":radio" , JQEvent.CLICK);

        List<Radio> types = type.getRadioList();
        for (Radio type : types) {
            // 2.2 Once the radio buttons are rendered, the following line will
            // be executed. Since the JavaScript code (binding) is already present,
            // we only have to register the radio to receive Ajax callbacks
            helper.registerAjaxControl(type);
        }
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

                initLaptop(laptopFS);
                form.add(laptopFS);

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

                initDesktop(desktopFS);
                form.add(desktopFS);

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
