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
package net.sf.clickclick.control.data;

import net.sf.clickclick.control.html.table.Row;

/**
 *
 */
public class DataRow extends Row {

    // ----------------------------------------------------------- Constructors

    /**
     * Create a default table row.
     */
    public DataRow() {
    }

    /**
     * Create a table row with the given name.
     *
     * @param name the name of the row
     */
    public DataRow(String name) {
        super(name);
    }

    // --------------------------------------------------------- Public methods

    public DataCell add(Object dataSource, String expr) {
        return add(dataSource, expr, (String) null);
    }


    public DataCell add(Object dataSource, String expr, String format) {
        if (dataSource == null) {
            throw new IllegalArgumentException("dataSource cannot be null");
        }
        if (expr == null) {
            throw new IllegalArgumentException("expr cannot be null");
        }

        DataCell dc = new DataCell(dataSource, expr, format);
        add(dc);
        return dc;
    }

    public DataCell add(Object dataSource, String expr, DataDecorator decorator) {
        if (dataSource == null) {
            throw new IllegalArgumentException("dataSource cannot be null");
        }
        if (expr == null) {
            throw new IllegalArgumentException("expr cannot be null");
        }

        DataCell dc = new DataCell(dataSource, expr);
        dc.setDecorator(decorator);
        add(dc);
        return dc;
    }
}
