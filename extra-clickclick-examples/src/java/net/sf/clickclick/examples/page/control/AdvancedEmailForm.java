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
package net.sf.clickclick.examples.page.control;

import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.click.extras.control.EmailField;
import org.apache.click.extras.control.PageSubmit;
import net.sf.clickclick.examples.control.RichTextArea;
import net.sf.clickclick.examples.page.BorderPage;
import net.sf.clickclick.examples.page.HomePage;

/**
 * Provides an example page using the custom RichTextArea control.
 *
 * @see RichTextArea
 */
public class AdvancedEmailForm extends BorderPage {

    public Form form = new Form();

    public AdvancedEmailForm() {
        form.setLabelsPosition(Form.POSITION_TOP);
        form.setErrorsPosition(Form.POSITION_TOP);

        EmailField addressField = new EmailField("address", "To:");
        addressField.setRequired(true);
        addressField.setSize(60);
        form.add(addressField);

        TextField subjectField = new TextField("subject", "Subject:");
        subjectField.setRequired(true);
        subjectField.setSize(60);
        form.add(subjectField);

        RichTextArea messageTextArea = new RichTextArea("message");
        messageTextArea.setTheme(RichTextArea.THEME_ADVANCED);
        messageTextArea.setStyle("visibility", "hidden");

        messageTextArea.setLabel("Message:");
        form.add(messageTextArea);

        form.add(new Submit("send", "  Send "));
        form.add(new PageSubmit("cancel", HomePage.class));
    }

}
