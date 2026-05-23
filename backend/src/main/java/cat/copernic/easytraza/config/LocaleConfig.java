package cat.copernic.easytraza.config;

import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * Configura la internacionalització de la web.
 *
 * Permet canviar l'idioma amb el paràmetre {@code ?lang=ca} o {@code ?lang=es}
 * i guarda la selecció en una cookie perquè es mantingui entre pàgines.
 */
@Configuration
public class LocaleConfig implements WebMvcConfigurer {
    /**
     * Executa l'operació localeResolver.
     */

    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver("easytraza_lang");
        resolver.setDefaultLocale(Locale.forLanguageTag("ca"));
        return resolver;
    }
    /**
     * Executa l'operació localeChangeInterceptor.
     */

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }
    /**
     * Executa l'operació addInterceptors.
     */

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
