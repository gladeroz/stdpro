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
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
    basePackages = "app.repository.gims",
    entityManagerFactoryRef = "gimsEntityManagerFactory",
    transactionManagerRef = "gimsTransactionManager"
)
public class GimsDataSourceConfig {

    @Bean
    @ConfigurationProperties("gims.datasource")
    public DataSourceProperties gimsDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource gimsDataSource() {
        return gimsDataSourceProperties().initializeDataSourceBuilder().build();
    }

    private Map<String, String> HibernateProperties() {
    	HashMap<String, String> properties = new HashMap<>();
        properties.put("hibernate.show_sql", "false");
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        return properties;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean gimsEntityManagerFactory(
        EntityManagerFactoryBuilder builder, // Injecté automatiquement
        @Qualifier("gimsDataSource") DataSource dataSource
    ) {
        return builder
            .dataSource(dataSource)
            .packages("app.entity.gims") // Package des entités
            .properties(HibernateProperties())
            .persistenceUnit("gims")
            .build();
    }

    @Bean
    public PlatformTransactionManager gimsTransactionManager(
        @Qualifier("gimsEntityManagerFactory") LocalContainerEntityManagerFactoryBean emf
    ) {
        return new JpaTransactionManager(emf.getObject());
    }
}
