package com.mycorp.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Customer {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	private String name;

	private String email;
	
	private Integer age;

	private String investments;
	
	private Double holdings;

	private Boolean active;
	
	private Date dateJoined;

	public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getInvestments() {
		return investments;
	}
	public void setInvestments(String investments) {
		this.investments = investments;
	}
	public Double getHoldings() {
		return holdings;
	}
	public void setHoldings(Double holdings) {
		this.holdings = holdings;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public Date getDateJoined() {
		return dateJoined;
	}
	public void setDateJoined(Date dateJoined) {
		this.dateJoined = dateJoined;
	}
}
