package com.omniwyse.user.bo;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "users_credential")
//@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class UsersCredential {
	@Id
	@GeneratedValue(strategy =  GenerationType.AUTO)
	private long id;
	@Column(nullable = false,length = 50,unique =  true)
	private String email;
	@Column(name="password",nullable = false)
	private String password;
	@OneToOne(cascade=CascadeType.ALL,optional = false)
	private UserBO user;
	@ManyToOne(cascade=CascadeType.ALL,optional = false)
	private UserStatusBO status;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserBO getUser() {
		return user;
	}

	public void setUser(UserBO user) {
		this.user = user;
	}

	public UserStatusBO getStatus() {
		return status;
	}

	public void setStatus(UserStatusBO status) {
		this.status = status;
	}

}
