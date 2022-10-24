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
package net.sf.clickclick.control.ajax;

import net.sf.clickclick.AjaxControlRegistry;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.util.HtmlStringBuffer;

/**
 * Provides a server-side Ajax enabled Form.
 * <p/>
 * <b>Please note:</b> AjaxForm does not work out-of-the-box since no
 * client-side Ajax support is provided.
 */
public class AjaxForm extends Form {

    // ----------------------------------------------------------- Constructors

    /**
     * Create a default AjaxForm.
     */
    public AjaxForm() {
    }

    /**
     * Create an AjaxForm for the given name.
     *
     * @param name the name of the form
     */
    public AjaxForm(String name) {
        super(name);
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Register the link with the {@link net.sf.clickclick.AjaxControlRegistry}.
     */
    public void onInit() {
        super.onInit();
        AjaxControlRegistry.registerAjaxControl(this);
    }

    /**
     * Render the link to the given buffer.
     * <p/>
     * This method delegates to {@link #addIdField()} to add a hidden field
     * containing the Form's ID which ensures Ajax requests from this form are
     * identified.
     *
     * @param buffer the buffer to render to
     */
    public void render(HtmlStringBuffer buffer) {
        addIdField();
        super.render(buffer);
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Adds a hidden Field containing the Form's ID which ensures Ajax requests
     * from this form can be identified.
     */
    protected void addIdField() {
        // Add the Form ID as a HiddeField to trigger Ajax callback
        String id = getId();
        if (getField(id) == null) {
            add(new HiddenField(id, "1"));
        }
    }
}
