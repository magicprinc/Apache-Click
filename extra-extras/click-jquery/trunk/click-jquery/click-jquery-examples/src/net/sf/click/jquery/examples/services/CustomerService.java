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
package net.sf.click.jquery.examples.services;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.sf.click.jquery.examples.domain.Customer;
import net.sf.click.jquery.examples.util.StartupListener;
import org.apache.click.util.PropertyUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 */
public class CustomerService {

    public List<Customer> getCustomers() {
        return StartupListener.CUSTOMERS;
    }

    @SuppressWarnings("unchecked")
    public List<Customer> getCustomersForPage(int offset, int pageSize, final String sortColumn, final boolean ascending) {

        // Ok this implementation is a cheat since all customers are in memory
        List<Customer> customers = StartupListener.CUSTOMERS;

        if (StringUtils.isNotBlank(sortColumn)) {
            Collections.sort(customers, new Comparator() {

                public int compare(Object o1, Object o2) {
                    int ascendingSort = ascending ? 1 : -1;
                    Comparable v1 = (Comparable) PropertyUtils.getValue(o1, sortColumn);
                    Comparable v2 = (Comparable) PropertyUtils.getValue(o2, sortColumn);
                    if (v1 == null) {
                        return -1 * ascendingSort;
                    } else if (v2 == null) {
                        return 1 * ascendingSort;
                    }
                    return v1.compareTo(v2) * ascendingSort;
                }
            });
        }

        return customers.subList(offset, Math.min(offset + pageSize, customers.size()));
    }

    public int getNumberOfCustomers() {
        return StartupListener.CUSTOMERS.size();
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
