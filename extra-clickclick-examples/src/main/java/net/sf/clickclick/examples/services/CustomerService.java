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
package net.sf.clickclick.examples.services;

import net.sf.clickclick.examples.domain.Customer;
import net.sf.clickclick.examples.util.StartupListener;

import java.util.List;

/**
 *
 */
public class CustomerService {

    public List<Customer> getCustomers() {
        return StartupListener.CUSTOMERS;
    }

    public Customer findCustomer(Object id) {
        for (Customer customer : getCustomers()) {
            if (customer.getId().toString().equals(id)) {
                return customer;
            }
        }
        return null;
    }

    public Customer createCustomer() {
        Customer customer = new Customer();
        List<Customer> customers = getCustomers();
        Long prevId = customers.get(customers.size() - 1).getId();
        customer.setId(prevId + 1);
        return customer;
    }
}