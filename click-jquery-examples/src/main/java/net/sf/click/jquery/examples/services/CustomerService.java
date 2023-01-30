package net.sf.click.jquery.examples.services;

import lombok.val;
import net.sf.click.jquery.examples.domain.Customer;
import net.sf.click.jquery.examples.util.StartupListener;
import org.apache.click.service.PropertyService;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 *
 */
public class CustomerService {

  public List<Customer> getCustomers() {
    return StartupListener.CUSTOMERS;
  }

  @SuppressWarnings("unchecked")
  public List<Customer> getCustomersForPage (int offset, int pageSize, String sortColumn, boolean ascending){

    // Ok this implementation is a cheat since all customers are in memory
    List<Customer> customers = StartupListener.CUSTOMERS;

    if (StringUtils.isNotBlank(sortColumn)){
      val propertyService = PropertyService.getPropertyService();

      customers.sort((o1, o2)->{
        int ascendingSort = ascending ? 1 : -1;
        Comparable v1 = (Comparable) propertyService.getValue(o1, sortColumn);
        Comparable v2 = (Comparable) propertyService.getValue(o2, sortColumn);

        if (v1 == null){
          return -1 * ascendingSort;
        } else if (v2 == null){
          return ascendingSort;
        }
        return v1.compareTo(v2) * ascendingSort;
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