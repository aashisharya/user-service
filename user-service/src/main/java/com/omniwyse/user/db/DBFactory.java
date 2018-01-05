package com.omniwyse.user.db;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dieselpoint.norm.Database;
import com.omniwyse.user.bo.Tenants;

@Service
public class DBFactory {

    private final Map<String, Database> dbByTenants = new HashMap<>();

    private final DBConnectionProperties dbProperties;
    private final Database smsDB;

    @Autowired
    public DBFactory(DBConnectionProperties dbProperties) {
        this.dbProperties = dbProperties;
        System.setProperty("norm.jdbcUrl", dbUrl("sms"));
        System.setProperty("norm.user", dbProperties.user());
        System.setProperty("norm.password", dbProperties.password());
        smsDB = new Database();
    }

    public Database db(Tenants tenant) {
        if (!dbByTenants.containsKey(tenant.getDbname())) {
            System.setProperty("norm.jdbcUrl", dbUrl(tenant.getDbname()));
            dbByTenants.put(tenant.getDbname(), new Database());
        }
        return dbByTenants.get(tenant.getDbname());
    }

    public Database getSchoolDb() {
        return smsDB;
    }

    private String dbUrl(String schema) {
        return "jdbc:mysql://" + dbProperties.host() + ":" + dbProperties.port() + "/" + schema + "?useSSL=false";
    }


}