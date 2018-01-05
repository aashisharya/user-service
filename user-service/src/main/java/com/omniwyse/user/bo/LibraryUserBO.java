package com.omniwyse.user.bo;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "LIBRARY_USER")
@Entity
public class LibraryUserBO {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private long libraryId;
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private UserBO user;

	public LibraryUserBO(long libraryId, UserBO user) {
		super();
		this.libraryId = libraryId;
		this.user = user;
	}
	public LibraryUserBO(UserBO user) {
		super();
		this.user = user;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getLibraryId() {
		return libraryId;
	}

	public void setLibraryId(long libraryId) {
		this.libraryId = libraryId;
	}

	public UserBO getUser() {
		return user;
	}

	public void setUser(UserBO user) {
		this.user = user;
	}

}
