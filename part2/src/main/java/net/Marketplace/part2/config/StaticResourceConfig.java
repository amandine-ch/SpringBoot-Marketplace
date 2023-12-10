package net.Marketplace.part2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 * Configuration class to handle static resources.
 */
@Configuration
@EnableWebMvc
public class StaticResourceConfig  implements  WebMvcConfigurer {
    /**
     * Adds resource handlers for static resources.
     * @param registry ResourceHandlerRegistry object to register resource handlers.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Add a resource handler for "/static/**" URL pattern
        registry.addResourceHandler("/static/**")
                // Define the location(s) of static resources
                .addResourceLocations("classpath:/static/")
                // Enable caching with a resource chain
                .resourceChain(true);

    }
}
