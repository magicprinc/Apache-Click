package com.mycorp.service;

import java.util.ArrayList;
import java.util.List;

import com.mycorp.domain.Customer;

public class CustomerService extends ServiceTemplate {

    @SuppressWarnings("unchecked")
    public List<Customer> getCustomers() {
    	return performQuery ("SELECT FROM com.mycorp.domain.Customer c order by c.name");
    }
/*
    @SuppressWarnings("unchecked")
    public int getNumberOfCustomers() {
        CountQuery query = new CountQuery(Customer.class);
        List result = performQuery(query);
        Map row = (Map) result.get(0);
        Integer count = (Integer) row.get("C");
        return count.intValue();
    }

    @SuppressWarnings("unchecked")
    public List<Customer> getCustomersSortedBy(String property, boolean ascending) {
        SelectQuery query = new SelectQuery(Customer.class);
        if (property != null) {
            query.addOrdering(property, ascending);
        }
        return performQuery(query);
    }

    @SuppressWarnings("unchecked")
    public List<Customer> getCustomers(String name, Date startDate) {
        SelectQuery query = new SelectQuery(Customer.class);

        if (StringUtils.isNotBlank(name)) {
            query.andQualifier(ExpressionFactory.likeIgnoreCaseExp(Customer.NAME_PROPERTY, "%" + name + "%"));
        }
        if (startDate != null) {
            query.andQualifier(ExpressionFactory.greaterOrEqualExp(Customer.DATE_JOINED_PROPERTY, startDate));
        }

        query.addOrdering(Customer.NAME_PROPERTY, true);
        query.addOrdering(Customer.DATE_JOINED_PROPERTY, true);

        return performQuery(query);
    }

    @SuppressWarnings("unchecked")
    public List<Customer> getCustomerNamesLike(String name) {
        SelectQuery query = new SelectQuery(Customer.class);

        query.andQualifier(ExpressionFactory.likeIgnoreCaseExp(Customer.NAME_PROPERTY, "%" + name + "%"));

        query.addOrdering(Customer.NAME_PROPERTY, true);

        query.setFetchLimit(10);

        List list = performQuery(query);

        for (int i = 0; i < list.size(); i++) {
            list.set(i, ((Customer)list.get(i)).getName());
        }

        return list;
    }

    @SuppressWarnings("unchecked")
    public List<Customer> getCustomers(Date from, Date to) {
        Expression qual = ExpressionFactory.noMatchExp(Customer.DATE_JOINED_PROPERTY, null);

        if (from != null) {
            qual = qual.andExp(ExpressionFactory.greaterOrEqualExp(Customer.DATE_JOINED_PROPERTY, from));
        }
        if (to != null) {
            qual = qual.andExp(ExpressionFactory.lessOrEqualExp(Customer.DATE_JOINED_PROPERTY, to));
        }

        SelectQuery query = new SelectQuery(Customer.class, qual);
        query.addOrdering(Customer.DATE_JOINED_PROPERTY, true);

        return performQuery(query);
    }

    @SuppressWarnings("unchecked")
    public List<Customer> getCustomersSortedByName(int rows) {
        SelectQuery query = new SelectQuery(Customer.class);
        query.addOrdering(Customer.NAME_PROPERTY, true);
        query.setFetchLimit(rows);
        return performQuery(query);
    }

    @SuppressWarnings("unchecked")
    public List<Customer> getCustomersSortedByDateJoined(int rows) {
        SelectQuery query = new SelectQuery(Customer.class);
        query.addOrdering(Customer.DATE_JOINED_PROPERTY, true);
        query.setFetchLimit(rows);
        return performQuery(query);
    }
*/

    public void saveCustomer(Customer customer) {
        saveObject(customer);
    }

    public Customer getCustomerForID(Object id) {
        return (Customer) getObjectForPK(Customer.class, id);
    }

    public void deleteCustomer(Integer id) {
        Customer customer = getCustomerForID(id);
        deleteCustomer(customer);
    }

    public void deleteCustomer(Customer customer){
    	if (customer != null) {
    		deleteObject(customer);
    		getEntityManager().getTransaction().commit();
    	}
    }

    public Customer findCustomerByID(Object id) {
        if (id != null && id.toString().length() > 0) {
            return (Customer) getCustomerForID(id);
        } else {
            return null;
        }
    }

    /*
    @SuppressWarnings("unchecked")
    public Customer findCustomerByName(String name) {
        SelectQuery query = new SelectQuery(Customer.class);
        query.andQualifier(ExpressionFactory.matchExp(Customer.NAME_PROPERTY,name));

        List list = performQuery(query);

        if (!list.isEmpty()) {
            return (Customer) list.get(0);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Customer> getCustomersForName(String value) {
        Expression template = Expression.fromString("name likeIgnoreCase $name");
        Expression e = template.expWithParameters(toMap(Customer.NAME_PROPERTY, "%" + value + "%"));
        return  performQuery(new SelectQuery(Customer.class, e));
    }

    @SuppressWarnings("unchecked")
    public List<Customer> getCustomersForAge(String value) {
        int age = NumberUtils.toInt(value);
        return performQuery(Customer.class, Customer.AGE_PROPERTY, new Integer(age));
    }

    @SuppressWarnings("unchecked")
    public List<Customer> getCustomersForPage(int offset, int pageSize) {
        List list = getCustomers();

        List pageList = new ArrayList(pageSize);
        for (int i = 0; i < pageSize; i++) {
            // Increment row index with the offset
            int rowIndex = offset + i;

            // Guard against rowIndex that moves past the end of the list
            if (rowIndex >= list.size()) {
                break;
            }
            pageList.add(list.get(rowIndex));
        }

        return pageList;
    }

    @SuppressWarnings("unchecked")
    public List<Customer> getTopCustomersForPage(int offset, int pageSize) {
        List list = getCustomersSortedBy(Customer.HOLDINGS_PROPERTY, false);

        List pageList = new ArrayList(pageSize);
        for (int i = 0; i < pageSize; i++) {
            // Increment row index with the offset
            int rowIndex = offset + i;

            // Guard against rowIndex that moves past the end of the list
            if (rowIndex >= list.size()) {
                break;
            }
            pageList.add(list.get(rowIndex));
        }

        return pageList;
    }
*/
    @SuppressWarnings("unchecked")
    public List<Customer> getInvestmentCatetories() {
        List categories = new ArrayList();

        categories.add("Bonds");
        categories.add("Commerical Property");
        categories.add("Options");
        categories.add("Residential Property");
        categories.add("Stocks");

        return categories;
    }
}
