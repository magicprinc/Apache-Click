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
package net.sf.clickclick.mootools.controls;

import net.sf.clickclick.control.ajax.AjaxForm;
import org.apache.click.Context;
import org.apache.click.element.Element;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.PageImports;
import org.apache.click.element.JsImport;

import java.util.List;

/**
 *
 */
public class MTAjaxForm extends AjaxForm {

    public MTAjaxForm() {
        
    }

    public MTAjaxForm(String name) {
        super(name);
    }

    @Override
    public List<Element> getHeadElements() {
        if(headElements == null) {
            Context context = getContext();
            String versionIndicator = ClickUtils.getResourceVersionIndicator(context);
            headElements = super.getHeadElements();
            headElements.add(new JsImport("/clickclick/mootools/mootools-1.2.js"));
        }
        return headElements;
    }
}
