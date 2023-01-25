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
package net.sf.click.jquery.examples.page.controls.dialog;

import net.sf.click.jquery.examples.control.ui.UIDialog;
import net.sf.click.jquery.examples.page.BorderPage;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Button;
import org.apache.click.control.Form;
import org.apache.click.control.TextField;
import org.apache.click.element.CssStyle;
import org.apache.click.element.JsScript;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogDemo extends BorderPage {

    private static final long serialVersionUID = 1L;

    @Override
    public void onInit() {
        ActionLink link = new ActionLink("login", "Click here to login");
        link.setId("login-link");

        addControl(link);

        addControl(buildDialog());
    }

    @Override
    public List getHeadElements() {
        if (headElements == null) {
            headElements = super.getHeadElements();

            Map<String, Object> model = new HashMap<String, Object>();
            JsScript jsInclude = new JsScript("controls/dialog/dialog-demo.js", model);
            headElements.add(jsInclude);

            CssStyle cssStyle = new CssStyle("controls/dialog/dialog-demo.css", model);
            headElements.add(cssStyle);
        }
        return headElements;
    }

    // -------------------------------------------------------- Private Methods

    private UIDialog buildDialog() {
        UIDialog dialog = new UIDialog("dialog");
        Form form = new Form("form");
        TextField loginNameField = new TextField("loginName");
        form.add(loginNameField);

        TextField passwordField = new TextField("password");
        form.add(passwordField);

        Button submit = new Button("login");
        Button close = new Button("close");

        form.add(submit);
        form.add(close);

        dialog.add(form);

        return dialog;
    }
}