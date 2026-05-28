package cat.copernic.easytraza.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author orjon
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    /**
     * Executa l'operació addResourceHandlers.
     * @param registry
     */

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/usuaris/**")
                .addResourceLocations("file:uploads/usuaris/");
    }
}
