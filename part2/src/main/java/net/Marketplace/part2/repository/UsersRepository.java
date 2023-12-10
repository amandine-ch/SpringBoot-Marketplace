package net.Marketplace.part2.repository;

import net.Marketplace.part2.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for performing CRUD operations on Users entity.
 */
@Transactional
public interface UsersRepository extends JpaRepository<Users, UUID> {
    /**
     * Finds users by their name.
     *
     * @param name The name of the user(s) to find.
     * @return A list of users with the specified name.
     */
    List<Users> findByName(String name);
    /**
     * Finds a user by their email address.
     *
     * @param email The email address of the user to find.
     * @return An Optional containing the user with the specified email if found.
     */
    Optional<Users> findByEmail(String email);
    /**
     * Updates the loyalty points of a user.
     *
     * @param loyalityPoints The loyalty points to update for the user.
     * @param id            The ID of the user to update.
     * @return The number of affected rows after updating the loyalty points.
     */
    @Modifying
    @Query(value = "update users set loyalty_points = :loyalityPoints where id = :id", nativeQuery = true)
    int updateLoyalityPoints(@Param("loyalityPoints") Integer loyalityPoints,@Param("id") UUID id);
}
