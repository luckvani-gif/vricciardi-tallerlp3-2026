package py.edu.uc.lp3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import py.edu.uc.lp3.config.CompanerosProperties;

@SpringBootApplication
@ComponentScan(basePackages = { "py.edu.uc.lp3" })
@EnableJpaRepositories(basePackages = { "py.edu.uc.lp3" })
@EntityScan(basePackages = { "py.edu.uc.lp3" })
@EnableConfigurationProperties(CompanerosProperties.class)
@OpenAPIDefinition(info = @Info(
        title = "CS2 A1 — APIs cruzadas POO-09",
        version = "1.0.0",
        description = "CRUD y reglas de munición de A1, más el escenario ataque-coordinado que consulta a A2 y A3."))
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}