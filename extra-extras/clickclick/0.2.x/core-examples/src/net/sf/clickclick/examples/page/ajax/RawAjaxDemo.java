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
package net.sf.clickclick.examples.page.ajax;

import java.text.SimpleDateFormat;
import java.util.Date;
import net.sf.clickclick.AjaxControlRegistry;
import net.sf.clickclick.util.AjaxAdapter;
import org.apache.click.Control;
import org.apache.click.control.ActionLink;
import org.apache.click.util.HtmlStringBuffer;
import net.sf.clickclick.util.Partial;
import net.sf.clickclick.examples.page.BorderPage;

public class RawAjaxDemo extends BorderPage {

    public void onInit() {
        super.onInit();
        ActionLink link = new ActionLink("link", "Click here make Ajax request");

        // Register the link as an Ajax control. This allows Click to process
        // only this link when an Ajax request is made that targets the link.
        // When Click realizes that an Ajax request targets a specific Control,
        // it won't process any other control.
        AjaxControlRegistry.registerAjaxControl(link);

        // How does Click know which Control is the target of the Ajax request?
        // Ajax requests can target a specific Control by setting the Controls
        // unique HTML id attribute as a request parameter.
        // Click will check if any registered ajax control HTML id matches a request
        // parameter. If it does Click will invoke that Controls #onProcess method
        // and fire its registered listener.

        // Here we set the unique HTML id attribute so Click can check if an
        // Ajax request targets this link or not
        link.setId("link_id");

        // Create the request parameters to send to server -> link_id=1&actionLink=link.
        // There are two key/value pairs send to the server.
        // #1. The link_id=1 key/value pair informs Click which Control is the target of the Ajax call, in this case the ActionLink.
        //     The value of the id does not really matter. As long as the id parameter is present Click will
        //     know the target Control and invoke its #onProcess method as described above.
        // #2. The actionLink=link key/value pair is the normal parameters sent when clicking on an ActionLink.
        //     The actionLink parameter informs Click of the name of the Link the was clicked and will fire the Control Listener.
        HtmlStringBuffer params = new HtmlStringBuffer();
        params.append(link.getId()).append("=1&");
        params.append(link.ACTION_LINK).append("=").append(link.getName());

        // Create the url to call -> /examples/ajax/raw-ajax-demo.htm
        String url = getContext().getRequest().getContextPath() + getContext().getResourcePath();

        // Set the onclick attribute to invoke the ajax() function with the specified
        // url, parameters and callback function. The callback function in this example is
        // updateLog
        link.setAttribute("onclick", "ajax('" + url + "','" + params + "',updateLog); return false;");

        // Set an AjaxListener 
        link.setActionListener(new AjaxAdapter() {
            public Partial onAjaxAction(Control source) {
                // Partial represents a partial response from the server.
                // Partial can return any String or byte array, including:
                // JSON, xml and html.
                HtmlStringBuffer buffer = new HtmlStringBuffer();
                buffer.append("<div style='background-color: #EEF1F7; border: 1px solid #6C90CC'>");
                buffer.append("<h2>Ajax Response</h2>");
                buffer.append("<p>This snippet was request by Ajax. The current time is: ");
                buffer.append(getDate());
                buffer.append("</p>");
                buffer.append("</div>");
                return new Partial(buffer.toString());
            }
        });

        addControl(link);
    }

    /**
     * Utility function which formats the current time
     */
    private String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("H:m:s:S");
        return format.format(new Date());
    }
}
