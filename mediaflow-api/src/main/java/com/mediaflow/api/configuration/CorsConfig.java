package com.mediaflow.api.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer{
//     @Override
//   public void addCorsMappings(CorsRegistry registry) {
//     registry.addMapping("/api/**")
//         .allowedOrigins("https://tu-frontend.com") // evitar "*" en producción
//         .allowedMethods("GET","POST","PUT","DELETE","PATCH","OPTIONS")
//         .allowedHeaders("*");
//         //.allowCredentials(true);   // si vas a enviar cookies/autenticación, pero entonces el test retornará el localhost
//   }
}
