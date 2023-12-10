package net.Marketplace.part2.auth;

import net.Marketplace.part2.entity.Users;
import net.Marketplace.part2.repository.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
/**
 * Custom implementation of Spring Security's UserDetailsService.
 * Loads user-specific data for authentication.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UsersRepository usersRepository;
    /**
     * Constructor for CustomUserDetailsService.
     *
     * @param usersRepository Repository for accessing user-related data.
     */
    public CustomUserDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    /**
     * Loads the user's data by email for authentication.
     *
     * @param email The email address of the user.
     * @return UserDetails object containing user-specific details.
     * @throws UsernameNotFoundException if the user is not found in the database.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users users =  usersRepository.findByEmail(email)
                .orElseThrow(
                () -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + email)
        );

        return users;
    }
}
