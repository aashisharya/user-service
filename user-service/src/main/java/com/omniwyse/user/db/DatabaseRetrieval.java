package com.omniwyse.user.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dieselpoint.norm.Database;
import com.omniwyse.user.bo.Tenants;


@Service
public class DatabaseRetrieval {
	
	@Autowired
	private DBFactory database;
	private Database db;
	private Tenants tenants;
	private String dbname;

	public Database getDatabase(long id) {
		db = database.getSchoolDb();
		tenants = new Tenants();
		dbname = db.where("id=?", id).results(Tenants.class).get(0).getDbname();
		tenants.setDbname(dbname);
		return database.db(tenants);
	}

	public Database tenantDatabase(String dbname){
		tenants = new Tenants();
		tenants.setDbname(dbname);
		return database.db(tenants);
	}
	
}
