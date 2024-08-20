package cl.bch.cloud.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "swagger.info")
public record OpenApiProperties(
    String version,
    String name,
    String description,
    OpenApiContactProperties contact) {
}
