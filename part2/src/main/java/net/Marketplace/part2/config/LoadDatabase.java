package net.Marketplace.part2.config;

import net.Marketplace.part2.repository.ArticlesRepository;
import net.Marketplace.part2.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for initializing the database.
 */
@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
    /**
     * Initializes the database with sample data if needed.
     *
     * @param userRepository    UsersRepository instance for managing user data
     * @param articlesRepository ArticlesRepository instance for managing articles data
     * @return CommandLineRunner instance
     */
    @Bean
    CommandLineRunner initDatabase(UsersRepository userRepository, ArticlesRepository articlesRepository) {
        return  args -> {
        };
    }
}
