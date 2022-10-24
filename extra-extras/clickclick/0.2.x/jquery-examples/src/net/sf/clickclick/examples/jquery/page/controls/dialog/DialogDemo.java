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
package net.sf.clickclick.examples.jquery.page.controls.dialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.click.control.Button;
import org.apache.click.control.Checkbox;
import org.apache.click.control.Label;

import net.sf.clickclick.control.Text;
import net.sf.clickclick.control.grid.Grid;
import net.sf.clickclick.control.html.table.Cell;
import net.sf.clickclick.control.html.table.HeaderCell;
import net.sf.clickclick.control.html.table.HtmlTable;
import net.sf.clickclick.control.html.table.Row;
import net.sf.clickclick.examples.jquery.control.ui.UIDialog;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import org.apache.click.element.CssStyle;
import org.apache.click.element.JsScript;

public class DialogDemo extends BorderPage {

    public String title = "JQuery UI Dialog Demo";

    public void onInit() {
        addControl(buildTable());
        addControl(buildDialog());
    }

    public List getHeadElements() {
        if (headElements == null) {
            headElements = super.getHeadElements();

            Map model = new HashMap();
            JsScript jsInclude = new JsScript("controls/dialog/dialog-demo.js", model);
            headElements.add(jsInclude);

            CssStyle cssStyle = new CssStyle("controls/dialog/dialog-demo.css", model);
            headElements.add(cssStyle);
        }
        return headElements;
    }

    // -------------------------------------------------------- Private Methods

    private HtmlTable buildTable() {
        HtmlTable table = new HtmlTable("table");
        table.addStyleClass("complex");
        buildHeaders(table);
        buildBody(table);
        return table;
    }

    private void buildHeaders(HtmlTable table) {
        Row row = new Row();
        table.add(row);

        HeaderCell header = new HeaderCell();
        header.add(new Text("Firstname"));
        row.add(header);

        header = new HeaderCell();
        header.add(new Text("Lastname"));
        row.add(header);

        header = new HeaderCell();
        header.add(new Text("Age"));
        row.add(header);

        header = new HeaderCell();
        header.add(new Text("Street"));
        row.add(header);
    }

    private void buildBody(HtmlTable table) {
        Row row = new Row();
        row.addStyleClass("odd");
        table.add(row);

        Cell cell = new Cell();
        cell.add(new Text("Steve"));
        row.add(cell);

        cell = new Cell();
        cell.add(new Text("Jones"));
        row.add(cell);


        cell = new Cell();
        cell.add(new Text("21"));
        row.add(cell);


        cell = new Cell();
        cell.add(new Text("15 Short street"));
        row.add(cell);
    }

    private UIDialog buildDialog() {
        UIDialog dialog = new UIDialog("dialog");

        Grid grid = new Grid("grid");
        grid.insert(new Label("Hide Firstname"), 1, 1);
        Checkbox chk = new Checkbox("chk_firstname");
        grid.insert(chk, 1, 2);
        grid.insert(new Label("Hide Lastname"), 2, 1);
        grid.insert(new Checkbox("chk_lastname"), 2, 2);
        grid.insert(new Label("Hide Age"), 3, 1);
        grid.insert(new Checkbox("chk_age"), 3, 2);
        grid.insert(new Label("Hide Street"), 4, 1);
        grid.insert(new Checkbox("chk_street"), 4, 2);
        dialog.add(grid);

        Button button = new Button("close");
        button.setAttribute("onclick", "jQuery('#dialog').dialog('close');");
        dialog.add(button);

        return dialog;
    }
}
