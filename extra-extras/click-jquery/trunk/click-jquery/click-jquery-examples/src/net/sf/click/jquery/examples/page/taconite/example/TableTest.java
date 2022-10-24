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
package net.sf.click.jquery.examples.page.taconite.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.click.jquery.JQEvent;
import net.sf.click.jquery.behavior.JQBehavior;
import org.apache.click.Control;
import org.apache.click.control.Column;
import org.apache.click.control.Table;
import net.sf.click.jquery.examples.page.BorderPage;
import net.sf.click.jquery.taconite.JQCommand;
import net.sf.click.jquery.taconite.JQTaconite;
import org.apache.click.ActionResult;
import org.apache.click.control.ActionLink;
import org.apache.commons.lang.math.NumberUtils;

/**
 *
 */
public class TableTest extends BorderPage {

	private static final long serialVersionUID = 1L;

    public ActionLink link = new ActionLink("link", "Counter: 0");
    public ActionLink link2 = new ActionLink("link2", "Add table row");

    @Override
    public void onInit() {
        link.addBehavior(new JQBehavior() {

            @Override
            public ActionResult onAction(Control source, JQEvent eventType) {
                JQTaconite actionResult = new JQTaconite();
                int count = NumberUtils.toInt(getContext().getRequestParameter("count"));
                ++count;

                // After the link is added, we can change the href parameters.
                link.setParameter("count", count);
                link.setLabel("Counter: " + count);
                actionResult.replace(link);
                actionResult.remove(link2);
                actionResult.after(link, link2);
                Table table = createTable();
                table.setClass(Table.CLASS_COMPLEX);
                //command.add(table);
                actionResult.remove(table);
                actionResult.after(link2, table);
                return actionResult;
            }
        });

        JQBehavior behavior = new JQBehavior() {

            @Override
            public ActionResult onAction(Control source, JQEvent eventType) {
                JQTaconite actionResult = new JQTaconite();
                JQCommand command = new JQCommand(JQTaconite.AFTER, "tr.even", "<tr><td>WHOO HOOO!!!</td></tr>");
                actionResult.add(command);
                return actionResult;
            }
        };

        link2.addBehavior(behavior);
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
