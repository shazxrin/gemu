package me.shazxrin.gemu.configuration.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class WebMvcConfiguration implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Add resource handler that handles routes not handled by REST controllers.
        // The resource handler handles routes used for frontend SPA.
        registry
            .addResourceHandler("/**")
            .addResourceLocations("classpath:/public/**")
            .resourceChain(true)
            .addResolver(new SpaPathResourceResolver());
    }
}
