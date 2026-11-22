package py.edu.uc.lp3.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * URLs de los compañeros y timeout de los clientes HTTP de POO-09.
 * Se enlazan desde application.properties y se pueden sobrescribir con
 * A2_BASE_URL, A3_BASE_URL y COMPANEROS_TIMEOUT.
 */
@ConfigurationProperties(prefix = "companeros")
public record CompanerosProperties(String a2BaseUrl, String a3BaseUrl, Duration timeout) {
}
