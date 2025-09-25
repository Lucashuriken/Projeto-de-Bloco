package org.bugsolver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configurar recursos estáticos
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

        registry.addResourceHandler("/pages/**")
                .addResourceLocations("classpath:/static/pages/");

        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/", "classpath:/static/pages/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Redirecionar página raiz para login
        registry.addViewController("/").setViewName("forward:/pages/login.html");
        registry.addViewController("/index.html").setViewName("forward:/pages/login.html");
    }
}