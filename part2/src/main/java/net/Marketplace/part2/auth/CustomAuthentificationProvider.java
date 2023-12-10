package net.Marketplace.part2.auth;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
/**
 * Custom Authentication Provider to handle user authentication.
 */
@Component
public class CustomAuthentificationProvider implements AuthenticationProvider {
    /**
     * Authenticates the user based on the provided authentication object.
     *
     * @param authentication The authentication object containing user credentials.
     * @return Authenticated token if successful, null otherwise.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        if(email != null&& password != null){
            return new UsernamePasswordAuthenticationToken(email, password);
        }

        return null;
    }
    /**
     * Specifies the supported authentication class for this provider.
     *
     * @param authentication The authentication class.
     * @return True if the authentication is supported, false otherwise.
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
