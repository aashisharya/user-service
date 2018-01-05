package com.omniwyse.user.bo;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name = "REGISTERED_USER")
public class RegisteredUserBO {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(name = "registered_date")
	private Date registeredDate;
	@Column(name = "amount_paid")
	private double paidAmount;
	@Column(name = "amount_due")
	private double dueAmount;
	@ManyToOne(cascade = CascadeType.ALL,optional = false)
	private UserBO user;

	public RegisteredUserBO() {
		super();
	}

	public RegisteredUserBO(double paidAmount, double dueAmount, UserBO user) {
		super();
		this.paidAmount = paidAmount;
		this.dueAmount = dueAmount;
		this.user = user;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getRegisteredDate() {
		return registeredDate;
	}

	public void setRegisteredDate(Date registeredDate) {
		this.registeredDate = registeredDate;
	}

	@PrePersist
	protected void onCreate() {
		this.registeredDate = new Date();
	}

	public double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public double getDueAmount() {
		return dueAmount;
	}

	public void setDueAmount(double dueAmount) {
		this.dueAmount = dueAmount;
	}

	public UserBO getUser() {
		return user;
	}

	public void setUser(UserBO user) {
		this.user = user;
	}

}
