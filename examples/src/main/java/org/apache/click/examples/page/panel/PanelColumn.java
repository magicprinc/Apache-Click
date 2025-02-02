/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.click.examples.page.panel;

import org.apache.click.Context;
import org.apache.click.control.Column;
import org.apache.click.control.Panel;
import org.apache.click.util.HtmlStringBuffer;



/**
 * Provides a custom Column that wraps a Panel for display in a Table.
 */
public class PanelColumn extends Column {

    private static final long serialVersionUID = 1L;

    private final Panel panel;

    // Constructor ------------------------------------------------------------

    public PanelColumn(String name, Panel panel) {
        super(name);
        this.panel = panel;
    }

    // Event Handlers ---------------------------------------------------------

    /**
     * @see Column#renderTableDataContent(Object, HtmlStringBuffer, Context, int)
     */
    @Override
    protected void renderTableDataContent(Object row, HtmlStringBuffer buffer,
        Context context, int rowIndex) {

        // We use the name of the column in the model. So if the name is
        // "customer" we can access $customer.name in the "panel.htm" template
        panel.getModel().remove(name);
        panel.addModel(name, row);
        panel.render(buffer);
    }
}