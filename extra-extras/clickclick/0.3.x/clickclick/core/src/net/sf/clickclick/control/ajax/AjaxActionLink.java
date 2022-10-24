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
import net.sf.clickclick.AjaxListener;
import org.apache.click.control.ActionLink;

/**
 * Provides a server-side Ajax enabled ActionLink.
 * <p/>
 * <b>Please note:</b> AjaxActionLink does not work out-of-the-box since no
 * client-side Ajax support is provided.
 */
public class AjaxActionLink extends ActionLink {

    // ----------------------------------------------------------- Constructors

    /**
     * Create an AjaxActionLink with the given name.
     *
     * @param name the link name
     */
    public AjaxActionLink(String name) {
        super(name);
    }

    /**
     * Create a AjaxActionLink with the given name and label.
     *
     * @param name the link name
     * @param label the link display label
     */
    public AjaxActionLink(String name, String label) {
        super(name, label);
    }

    /**
     * Create a AjaxActionLink with the given name, label and id.
     *
     * @param name the link name
     * @param label the link display label
     * @param id the link id
     */
    public AjaxActionLink(String name, String label, String id) {
        super(name, label);
        setId(id);
    }

    /**
     * Create an AjaxActionLink for the given listener object.
     */
    public AjaxActionLink(AjaxListener ajaxListener) {
        setActionListener(ajaxListener);
    }

    /**
     * Create a AjaxActionLink with the given name and listener object.
     *
     * @param name the link name
     * @param ajaxListener the Ajax Listener target object
     */
    public AjaxActionLink(String name, AjaxListener ajaxListener) {
        super(name);
        setActionListener(ajaxListener);
    }

    /**
     * Create a AjaxActionLink with the given name, label and listener object.
     *
     * @param name the link name
     * @param label the link display label
     * @param ajaxListener the Ajax Listener target object
     */
    public AjaxActionLink(String name, String label, AjaxListener ajaxListener) {
        super(name, label);
        setActionListener(ajaxListener);
    }

    /**
     * Create an AjaxActionLink with no name defined.
     * <p/>
     * <b>Please note</b> the control's name must be defined before it is valid.
     */
    public AjaxActionLink() {
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Register the link with the {@link net.sf.clickclick.AjaxControlRegistry}.
     */
    public void onInit() {
        super.onInit();
        AjaxControlRegistry.registerAjaxControl(this);
    }
}
