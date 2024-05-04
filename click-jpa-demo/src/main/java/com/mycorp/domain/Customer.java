package com.mycorp.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter @Setter
@ToString
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
}