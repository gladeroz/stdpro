package app;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
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
@EnableJpaRepositories(
    basePackages = "app.repository.odr",
    entityManagerFactoryRef = "odrEntityManagerFactory",
    transactionManagerRef = "odrTransactionManager"
)
@EnableTransactionManagement
public class OdrDataSourceConfig {

    @Primary
    @Bean
    @ConfigurationProperties("odr.datasource")
    public DataSourceProperties odrDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean
    @ConfigurationProperties("odr.datasource.configuration")
    public DataSource odrDataSource() {
        return odrDataSourceProperties()
            .initializeDataSourceBuilder()
            .type(HikariDataSource.class)
            .build();
    }

    private Map<String, String> HibernateProperties() {
    	HashMap<String, String> properties = new HashMap<>();
        properties.put("hibernate.show_sql", "false");
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        return properties;
    }

    @Primary
    @Bean(name = "odrEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean odrEntityManagerFactory(
        EntityManagerFactoryBuilder builder,
        @Qualifier("odrDataSource") DataSource dataSource
    ) {
        return builder
            .dataSource(dataSource)
            .packages("app.entity.odr")
            .properties(HibernateProperties())
            .persistenceUnit("odr")
            .build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager odrTransactionManager(
        @Qualifier("odrEntityManagerFactory") LocalContainerEntityManagerFactoryBean odrEntityManagerFactory
    ) {
        return new JpaTransactionManager(odrEntityManagerFactory.getObject());
    }
}