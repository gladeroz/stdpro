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
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		basePackages = "app.repository.gims",
		entityManagerFactoryRef = "gimsEntityManagerFactory",
		transactionManagerRef = "gimsTransactionManager"
		)
public class GimsDataSourceConfig {

	private Map<String, Object> hibernateProperties() {
		Map<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", "update");
		properties.putAll(gimsJpaProps().getProperties());
		return properties;
	}

	@Bean
	@ConfigurationProperties("gims")
	public DataSourceProperties gimsDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean
	@ConfigurationProperties("gims.jpa")
	public JpaProperties gimsJpaProps() {
		return new JpaProperties();
	}

	@Bean
	@ConfigurationProperties("gims.hikari")
	public HikariDataSource gimsDataSource() {
		return gimsDataSourceProperties()
				.initializeDataSourceBuilder()
				.type(HikariDataSource.class)
				.build();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean gimsEntityManagerFactory(
			EntityManagerFactoryBuilder builder,
			@Qualifier("gimsDataSource") DataSource dataSource
			) {
		return builder
				.dataSource(dataSource)
				.packages("app.entity.gims")
				.properties(hibernateProperties())
				.persistenceUnit("gims")
				.build();
	}

	@Bean
	public PlatformTransactionManager gimsTransactionManager(
			@Qualifier("gimsEntityManagerFactory") LocalContainerEntityManagerFactoryBean emf) {
		return new JpaTransactionManager(Objects.requireNonNull(emf.getObject()));
	}
}
