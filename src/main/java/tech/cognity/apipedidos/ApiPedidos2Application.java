package tech.cognity.apipedidos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ApiPedidos2Application {

	public static void main(String[] args) {
		SpringApplication.run(ApiPedidos2Application.class, args);
	}

}
