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
package net.sf.clickclick.control.html.table;

import org.apache.click.control.AbstractContainer;

/**
 *
 */
public class TableBody extends AbstractContainer {

    public final String getTag() {
        return "tbody";
    }

    /**
     * Add the given text array to the table. A new {@link Row} is created and
     * for each text object a new {@link Cell} is created and added to the row.
     * <p/>
     * This method delegates to {@link #insert(java.lang.Object, int)}.
     *
     * @param text the text array to add to the row
     * @return the row that was created
     */
    public Row addRow(Object... textArray) {
        Row row = new Row();
        add(row);
        for (Object text : textArray) {
            row.add(text);
        }
        return row;
    }
}
