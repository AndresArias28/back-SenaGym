package com.gym.gym_ver2;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.core.env.Environment;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

@EntityScan(basePackages = "com.gym.gym_ver2.domain.model.entity")
@SpringBootApplication
@EnableTransactionManagement
public class GymVer2Application {

	public static void main(String[] args) {
		// 1) Cargar .env (no falla si no existe)
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

		// 2) Activar perfil desde .env (opcional)
		String profile = dotenv.get("SPRING_PROFILES_ACTIVE");
		if (profile != null && !profile.isBlank()) {
			System.setProperty("spring.profiles.active", profile);
		}
		System.out.println("Active Profile (.env): " + profile);

		// 3) Inyectar variables de .env SOLO si no existen ya
		Properties sys = System.getProperties();
		dotenv.entries().forEach(e -> sys.putIfAbsent(e.getKey(), e.getValue()));

		// 4) Arrancar Spring
		SpringApplication.run(GymVer2Application.class, args);
	}

	@Bean
	ApplicationRunner dbg(Environment env) {
		return args -> {
			System.out.println(">> spring.datasource.url = " + env.getProperty("spring.datasource.url"));
			System.out.println(">> spring.datasource.username = " + env.getProperty("spring.datasource.username"));

		};
	}

}
