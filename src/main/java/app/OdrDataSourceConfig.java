package app;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		basePackages = "app.repository.odr",
		entityManagerFactoryRef = "odrEntityManagerFactory",
		transactionManagerRef = "odrTransactionManager"
		)
public class OdrDataSourceConfig {
	
	private Map<String, Object> hibernateProperties() {
		Map<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", "update");
		properties.putAll(odrJpaProps().getProperties());
		return properties;
	}

	@Primary
	@Bean
	@ConfigurationProperties("odr")
	public DataSourceProperties odrDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Primary
	@Bean
	@ConfigurationProperties("odr.jpa")
	public JpaProperties odrJpaProps() {
		return new JpaProperties();
	}

	@Primary
	@Bean
	@ConfigurationProperties("odr.hikari")
	public HikariDataSource odrDataSource() {
		return odrDataSourceProperties()
				.initializeDataSourceBuilder()
				.type(HikariDataSource.class)
				.build();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean odrEntityManagerFactory(
			EntityManagerFactoryBuilder builder,
			@Qualifier("odrDataSource") DataSource dataSource
			) {
		return builder
				.dataSource(dataSource)
				.packages("app.entity.odr")
				.properties(hibernateProperties())
				.persistenceUnit("odr")
				.build();
	}

	@Bean
	public PlatformTransactionManager odrTransactionManager(
			@Qualifier("odrEntityManagerFactory") LocalContainerEntityManagerFactoryBean emf) {
		return new JpaTransactionManager(Objects.requireNonNull(emf.getObject()));
	}
}
