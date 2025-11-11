package com.unibague.inventario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración global de CORS para la API REST.
 *
 * <p>
 * Permite que clientes web alojados en dominios distintos (incluyendo el
 * protocolo file:// cuando se abre un archivo HTML localmente) puedan
 * consumir los microservicios del inventario. Se habilitan todos los
 * métodos y cabeceras comunes. Ajuste las reglas según los requisitos de
 * despliegue en producción.
 */
@Configuration
public class CorsConfig {

    /**
     * Registra la configuración de CORS para todas las rutas de la API.
     *
     * @return configurador de MVC con CORS habilitado
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}