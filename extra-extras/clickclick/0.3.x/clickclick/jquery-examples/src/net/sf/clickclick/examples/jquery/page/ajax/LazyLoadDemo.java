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
package net.sf.clickclick.examples.jquery.page.ajax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.clickclick.examples.jquery.control.Window;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.helper.JQHelper;
import net.sf.clickclick.jquery.Taconite;
import net.sf.clickclick.jquery.util.JQEvent;
import net.sf.clickclick.util.AjaxAdapter;
import net.sf.clickclick.util.Partial;
import org.apache.click.Control;
import org.apache.click.control.Column;
import org.apache.click.control.Table;
import org.apache.click.util.HtmlStringBuffer;

/**
 * Demonstrates how to lazily load table rows rendered inside a Window.
 */
public class LazyLoadDemo extends BorderPage {

    private Table table1;

    private Table table2;

    public LazyLoadDemo() {
         table1 = new Table("table1");
         table1.setWidth("100%");
         table1.setClass(Table.CLASS_BLUE1);
         table1.setNullifyRowListOnDestroy(false);
         table1.addColumn(new Column("firstname"));
         table1.addColumn(new Column("lastname"));
         table1.addColumn(new Column("age"));

         table2 = new Table("table2");
         table2.setWidth("100%");
         table2.setClass(Table.CLASS_BLUE1);
         table2.setNullifyRowListOnDestroy(false);
         table2.addColumn(new Column("firstname"));
         table2.addColumn(new Column("lastname"));
         table2.addColumn(new Column("age"));
    }

    public void onInit() {
        Window window1 = new Window("panel1");
        window1.setActionListener(new AjaxAdapter(){

            public Partial onAjaxAction(Control source) {
                Taconite taconite = new Taconite();
                table1.setRowList(createData());
                taconite.replace(table1);
                return taconite;
            }
        });

        JQHelper helper = new JQHelper(window1);
        setupHelper(helper, window1);
        window1.add(table1);
        addControl(window1);

        Window window2 = new Window("panel2");
        window2.setActionListener(new AjaxAdapter(){

            public Partial onAjaxAction(Control source) {
                Taconite taconite = new Taconite();
                table2.setRowList(createData());
                taconite.replace(table2);
                return taconite;
            }
        });

        helper = new JQHelper(window2);
        setupHelper(helper, window2);

        window2.add(table2);
        addControl(window2);
    }

    // -------------------------------------------------------- Private Methods

    private void setupHelper(JQHelper helper, Control indicatorTarget) {
        // Set the event to DOMREADY, meaning the Ajax request is invoked
        // as soon as the browser DOM is available
        helper.setEvent(JQEvent.DOMREADY);

        // Set the target of the Ajax indicator (busy indicator)
        helper.setIndicatorTarget(indicatorTarget);

        HtmlStringBuffer buffer = new HtmlStringBuffer();
        String contextPath = getContext().getRequest().getContextPath();

        // Set a custom indicator message that displays an animated gif
        helper.setIndicatorMessage("<b>Loading...&nbsp;</b> <img src=\""
            + contextPath + "/assets/images/ajax-loader.gif\"/>");

        // Set indicator options to align the message box in the upper right
        // hand corner
        buffer.append("showOverlay: true,");
        buffer.append("centerX: false,");
        buffer.append("centerY: false,");
        buffer.append("css: {");
        buffer.append("  textAlign: 'right',");
        buffer.append("  padding: 5,");
        buffer.append("  width: '',");
        buffer.append("  top:  '5px',");
        buffer.append("  left: '',");
        buffer.append("  right: '5px'");
        buffer.append("}");

        helper.setIndicatorOptions(buffer.toString());

        helper.ajaxify();
    }

    private List createData() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // ignore
        }

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        list.add(createPerson());
        list.add(createPerson());
        return list;
    }

    private Map<String, String> createPerson() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("firstname", "John");
        map.put("lastname", "Smith");
        map.put("age", "30");
        return map;
    }
}
