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

import net.sf.clickclick.control.html.table.Cell;

/**
 *
 */
public class DataCell extends Cell {

    // -------------------------------------------------------------- Variables

    protected DataControl dataControl;

    // ------------------------------------------------------------ Constructor

    public DataCell(Object dataSource, String expr, String format) {
        setDataControl(new DataControl(dataSource, expr, format));
    }

    public DataCell(String name, Object dataSource, String expr, String format) {
        super(name);
        setDataControl(new DataControl(dataSource, expr, format));
    }

    public DataCell(Object dataSource, String expr) {
        setDataControl(new DataControl(dataSource, expr));
    }

    public DataCell(String name, Object dataSource, String expr) {
        super(name);
        setDataControl(new DataControl(dataSource, expr));
    }

    public DataCell(String name, DataControl dataControl) {
        super(name);
        setDataControl(dataControl);
    }

    public DataCell(DataControl dataControl) {
        setDataControl(dataControl);
    }

    public DataCell(String name) {
        super(name);
    }

    public DataCell() {
    }

    /**
     * @return the dataControl
     */
    public DataControl getDataControl() {
        return dataControl;
    }

    /**
     * @param dataControl the dataControl to set
     */
    public void setDataControl(DataControl dataControl) {
        this.dataControl = dataControl;
        if (!contains(dataControl)) {
            add(dataControl);
        }
    }

    public void setDecorator(DataDecorator decorator) {
        getDataControl().setDecorator(decorator);
    }
}
