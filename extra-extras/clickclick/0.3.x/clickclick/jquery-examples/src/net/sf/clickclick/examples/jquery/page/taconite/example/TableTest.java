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
package net.sf.clickclick.examples.jquery.page.taconite.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.click.Control;
import org.apache.click.control.Column;
import org.apache.click.control.Table;
import net.sf.clickclick.util.AjaxAdapter;
import net.sf.clickclick.util.Partial;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.control.ajax.JQActionLink;
import net.sf.clickclick.jquery.Command;
import net.sf.clickclick.jquery.Taconite;
import org.apache.commons.lang.math.NumberUtils;

/**
 *
 */
public class TableTest extends BorderPage {

    public JQActionLink link = new JQActionLink("link", "Counter: 0");
    public JQActionLink link2 = new JQActionLink("link2", "Add table row");

    public void onInit() {
        super.onInit();
        link.setActionListener(new AjaxAdapter() {
            public Partial onAjaxAction(Control source) {
                Taconite partial = new Taconite();
                int count = NumberUtils.toInt(link.getParameter("count"));
                ++count;

                // After the link is added, we can change the href parameters.
                link.setParameter("count", Integer.toString(count));
                link.setLabel("Counter: " + Integer.toString(count));
                partial.replace(link);
                partial.remove(link2);
                partial.after(link, link2);
                Table table = createTable();
                table.setClass(Table.CLASS_COMPLEX);
                //command.add(table);
                partial.remove(table);
                partial.after(link2, table);
                return partial;
            }
        });
        
        link2.setActionListener(new AjaxAdapter() {
            public Partial onAjaxAction(Control source) {
                Taconite partial = new Taconite();
                Command command = new Command(Taconite.AFTER, "tr.even", "<tr><td>WHOO HOOO!!!</td></tr>");
                partial.add(command);
                return partial;
            }
        });
    }

    private static Table createTable() {
        Table table = new Table("table");
        table.setSortable(true);
        table.addColumn(new Column("name"));
        table.setRowList(getUsers());
        return table;
    }

    private static List getUsers() {
        List list = new ArrayList();
        Map map = new HashMap();
        list.add(map);
        map.put("name", "John");
        map = new HashMap();
        list.add(map);
        map.put("name", "Sue");
        return list;
    }
}
