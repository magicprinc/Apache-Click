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

import net.sf.click.jquery.control.ajax.JQForm;
import net.sf.click.jquery.examples.page.BorderPage;
import net.sf.click.jquery.taconite.JQTaconite;
import org.apache.click.Control;
import org.apache.click.ActionResult;
import org.apache.click.ajax.DefaultAjaxBehavior;
import org.apache.click.control.FileField;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.commons.fileupload.FileItem;

public class FileUploadDemo extends BorderPage {

	private static final long serialVersionUID = 1L;

    private Form form;
    private FileField fileField = new FileField("field", true);

    private JQForm ajaxForm;
    private FileField ajaxFileField = new FileField("field", true);

    @Override
    public void onInit() {
        form = new Form("form");
        form.add(fileField);

        Submit submit = new Submit("upload", this, "onUploadClick");
        form.add(submit);

        addControl(form);

        ajaxForm = new JQForm("ajaxForm");
        ajaxForm.setJavaScriptValidation(true);
        ajaxForm.add(ajaxFileField);
        ajaxForm.getJQBehavior().setResetForm(true);

        submit = new Submit("upload");
        submit.addBehavior(new OnSubmitHandler());
        ajaxForm.add(submit);

        submit = new Submit("save");
        submit.addBehavior(new DefaultAjaxBehavior() {

            @Override
            public ActionResult onAction(Control source) {
                System.out.println("Save Clicked - Ajax");
                JQTaconite actionResult = new JQTaconite();

                // Might want to return a success message
                return actionResult;
            }
        });
        ajaxForm.add(submit);

        addControl(ajaxForm);
    }

    public boolean onUploadClick() {
        System.out.println("onUpload Clicked - Non-Ajax");
        if (form.isValid()) {
            addModel("fileItem", fileField.getFileItem());
        }

        return true;
    }

    // NOTE: use an AjaxBehavior as the behavior action handler. Do not use JQBehavior
    // as that is used for binding JS events to controls and here the JQForm does
    // all the binding already
    class OnSubmitHandler extends DefaultAjaxBehavior {

        @Override
        public ActionResult onAction(Control source) {
            System.out.println("Upload Clicked - Ajax");
            JQTaconite actionResult = new JQTaconite();

            if (ajaxForm.isValid()) {

                if (ajaxFileField.getFileItem() == null) {
                    return actionResult;
                }

                FileItem fileItem = ajaxFileField.getFileItem();

                // Update FileItem data
                actionResult.replaceContent("#ajax-fileitem-name", fileItem.getName());
                actionResult.replaceContent("#ajax-fileitem-size", Long.toString(
                    fileItem.getSize()));
                actionResult.replaceContent("#ajax-fileitem-content-type",
                    fileItem.getContentType());
            }
            return actionResult;
        }
    }
}
