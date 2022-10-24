package net.sf.click.examples.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Customer domain class.
 */
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean active;

    private int age;

    private Date dateJoined;

    private String email;

    private double holdings;

    private String investments;

    private String name;

    private Object id;

    public Customer() {
    }

    public Customer(Object id, String name, int age, String email, Date dateJoined,
        String investments, double holdings) {
        setId(id);
        setName(name);
        setEmail(email);
        setAge(age);
        setDateJoined(dateJoined);
        setInvestments(investments);
        setHoldings(holdings);
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setDateJoined(java.util.Date dateJoined) {
        this.dateJoined = dateJoined;
    }

    public java.util.Date getDateJoined() {
        return dateJoined;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setHoldings(double holdings) {
        this.holdings = holdings;
    }

    public double getHoldings() {
        return holdings;
    }

    public void setInvestments(String investments) {
        this.investments = investments;
    }

    public String getInvestments() {
        return investments;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
