package com.omniwyse.user;

import java.sql.SQLException;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omniwyse.user.bo.Tenants;
import com.omniwyse.user.db.DBConnectionProperties;
import com.omniwyse.user.db.DBFactory;



public class FlywayRunner {


    private final String jdbcUrl, user, password;
    private DBFactory dbFactory;

    private static final Logger LOGGER = LoggerFactory.getLogger(FlywayRunner.class);

    @Autowired
    public FlywayRunner(DBConnectionProperties db, DBFactory dbFactory) {
        this.jdbcUrl = "jdbc:mysql://" + db.host() + ":" + db.port() + "/%s?createDatabaseIfNotExist=true";
        this.user = db.user();
        this.password = db.password();
        this.dbFactory = dbFactory;
        migrate("ilibrary");
    }

   /* public void migrate() {
        migrate("sms");
        dbFactory.getSchoolDb().results(Tenants.class).forEach(this::migrate);
    }

    private void migrate(Tenants tenant) {
        LOGGER.info("Migrating schema {} for tenant {}", tenant.getDbname(), tenant.getCode());
        Flyway flyway = new Flyway();
        flyway.setLocations("db/migration/tenants/common", "db/migration/tenants/" + tenant.getCode().toLowerCase());
        flyway.setDataSource(String.format(jdbcUrl, tenant.getDbname()), user, password, "USE " + tenant.getDbname());
        flyway.migrate();
    }*/

    private void migrate(String schema) {
        LOGGER.info("Migrating schema {}", schema);
        Flyway flyway = new Flyway();
        flyway.setLocations("db/migration/ilibrary");
        flyway.setDataSource(String.format(jdbcUrl, schema), user, password, "USE " + schema);
        flyway.migrate();
        try {
            flyway.getDataSource().getConnection().close();
        } catch (SQLException e) {
            LOGGER.warn("Failed to close flyway datasource connection", e);
        }
    }

	public String getJdbcUrl() {
		return jdbcUrl;
	}
    
    
}
