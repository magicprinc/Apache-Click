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
public class TableHeader extends AbstractContainer {

    public final String getTag() {
        return "thead";
    }

    /**
     * Add the given headers to the table. A new {@link HeaderRow} is created
     * and for each header a new {@link HeaderCell} is created and added to the
     * row.
     * <p/>
     * This method delegates to {@link #insert(java.lang.Object, int)}.
     *
     * @param text the text array to add to the row
     * @return the header row that was created
     */
    public HeaderRow setColumns(Object... textArray) {
        HeaderRow headerRow = new HeaderRow();
        add(headerRow);
        for (Object text : textArray) {
            headerRow.add(text);
        }
        return headerRow;
    }
}
