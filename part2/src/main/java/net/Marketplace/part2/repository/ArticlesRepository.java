package net.Marketplace.part2.repository;

import net.Marketplace.part2.entity.Articles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
/**
 * Repository interface for performing CRUD operations on Articles entity.
 */
public interface ArticlesRepository extends JpaRepository <Articles, UUID> {
    /**
     * Finds an article by its name.
     *
     * @param name The name of the article to find.
     * @return An Optional containing the article with the specified name if found.
     */
    Optional<Articles> findByName(String name);
    /**
     * Gets an article by its ID.
     *
     * @param ID The ID of the article to get.
     * @return The article with the specified ID.
     */
    Articles getById(UUID ID);
}
