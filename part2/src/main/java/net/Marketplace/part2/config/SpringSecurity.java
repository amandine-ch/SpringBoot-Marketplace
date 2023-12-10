package net.Marketplace.part2.config;

import net.Marketplace.part2.auth.CustomAuthentificationProvider;
import net.Marketplace.part2.auth.CustomUserDetailsService;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;


/**
 * Configuration class for Spring Security.
 */
@Configuration
@EnableWebSecurity
public class SpringSecurity {
    @Autowired
    private final CustomUserDetailsService userDetailsService;

    /**
     * Constructor injecting the CustomUserDetailsService dependency.
     *
     * @param userDetailsService CustomUserDetailsService implementation
     */
    public SpringSecurity(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    /**
     * Defines a SecurityFilterChain.
     *
     * @param http HttpSecurity object
     * @return SecurityFilterChain instance
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception  {
        http
                .authorizeHttpRequests(r ->
                        r.requestMatchers("/admin/*")
                                .hasRole("ADMIN")
                                .requestMatchers("/order/**")
                                .authenticated()
                                .requestMatchers("/**")
                                .permitAll()
                )
                .logout(l ->
                        l.logoutUrl("/logout")
                                .logoutSuccessUrl("/")
                )
                .securityContext((ctx) ->
                            ctx.securityContextRepository(
                                new DelegatingSecurityContextRepository(
                                        new RequestAttributeSecurityContextRepository(),
                                        new HttpSessionSecurityContextRepository()
                                )
                            ).requireExplicitSave(false)
                        );
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return roleHierarchy;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }

}
