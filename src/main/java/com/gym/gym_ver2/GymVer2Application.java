package com.gym.gym_ver2;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EntityScan(basePackages = "com.gym.gym_ver2.domain.model.entity")
@SpringBootApplication
@EnableTransactionManagement
public class GymVer2Application {

	public static void main(String[] args) {
		// Carga .env y lo inyecta en el entorno
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		dotenv.entries().forEach(entry ->
				System.setProperty(entry.getKey(), entry.getValue())
		);

		SpringApplication.run(GymVer2Application.class, args);
	}

}
