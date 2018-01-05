package com.omniwyse.user.config;

import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.omniwyse.user.db.DBConnectionProperties;
import com.omniwyse.user.db.DBFactory;

@EnableConfigurationProperties
@Configuration
public class DBConfig {

	private final String jdbcUrl, user, password;
	@SuppressWarnings("unused")
	private DBFactory dbFactory = null;
	Flyway flyway = null;

	@Autowired
	public DBConfig(DBConnectionProperties db, DBFactory dbFactory) {
		this.jdbcUrl = "jdbc:mysql://" + db.host() + ":" + db.port() + "/%s?createDatabaseIfNotExist=true&verifyServerCertificate=false&useSSL=true";
		this.user = db.user();
		this.password = db.password();
		this.dbFactory = dbFactory;
		migrate("ilibrary");
	}

	private void migrate(String schema) {
		flyway = new Flyway();
		flyway.setLocations("db/migration/ilibrary");
		flyway.setDataSource(String.format(jdbcUrl, schema), user, password, "USE " + schema);
		flyway.setBaselineOnMigrate(true);
		flyway.migrate();
		try {
			flyway.getDataSource().getConnection().close();
		} catch (SQLException e) {
		}
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(flyway.getDataSource());
		em.setPackagesToScan(new String[] { "com.omniwyse.user" });
		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaProperties(additionalProperties());
		return em;
	}

	@Bean
	public DataSource dataSource() {
		return flyway.getDataSource();
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return transactionManager;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	Properties additionalProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.hbm2ddl.auto", "validate");
		properties.setProperty("hibernate.show_sql", "true");
		properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		return properties;
	}

}
