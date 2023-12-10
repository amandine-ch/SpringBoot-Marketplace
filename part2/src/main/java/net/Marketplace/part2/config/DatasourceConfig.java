package net.Marketplace.part2.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
/**
 * Configuration class for setting up the data source.
 */
@Configuration
public class DatasourceConfig {
    /**
     * Creates and configures a DataSource bean for the application.
     *
     * @return DataSource configured with the required driver class, URL, username, and password
     */
    @Bean
    public DataSource getDataSource(){
        return  DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url("jdbc:postgresql://db.nmfhqkcmkvsgjmxnbbla.supabase.co:5432/postgres")
                .username("postgres")
                .password("Cytech*2025")
                .build();
    }
}
