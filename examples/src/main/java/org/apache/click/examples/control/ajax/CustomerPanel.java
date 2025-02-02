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
package org.apache.click.examples.control.ajax;

import org.apache.click.Context;
import org.apache.click.Page;
import org.apache.click.control.Panel;
import org.apache.click.examples.domain.Customer;
import org.apache.click.util.HtmlStringBuffer;

import java.util.Map;

public class CustomerPanel extends Panel {

    private static final long serialVersionUID = 1L;

    private Customer customer;

    public CustomerPanel(Page page, Customer customer) {
        this.customer = customer;
        setParent(page);
    }

    @Override
    public void render(HtmlStringBuffer buffer) {
        Context context = Context.getThreadLocalContext();
        Map templateModel = createTemplateModel();
        templateModel.put("customer", customer);
        buffer.append(context.renderTemplate("/control/ajax/ajax-customer.htm", templateModel));
    }
}