package com.luxus.book_network_api;

import com.luxus.book_network_api.role.Role;
import com.luxus.book_network_api.role.RoleRepository;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
/*
 * Toujours faire ceci si on utilise @EntityListeners(AuditingEntityListener.class) dans une classe 
 * dans l'application
 * Cette annotation permet de gérer les dates de création et de modification et les modifieurs des entités
 */
@EnableJpaAuditing(auditorAwareRef = "auditorAware")

/*
 * Permettre à l'application de pourvoir excuter des fonctions asynchrons comme dans l'envoie de mail
 * EmailServie
 * package com.luxus.book_network_api.email
 */
@EnableAsync
public class BookNetworkApiApplication {

	@Autowired
	private HikariDataSource dataSource;  // Injection de HikariDataSource

	public static void main(String[] args) {
		SpringApplication.run(BookNetworkApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(RoleRepository roleRepository) {
		return args -> {
			if (roleRepository.findByName("USER").isEmpty()) {
				roleRepository.save(Role.builder().name("USER").build());
			}
		};
	}

	@PreDestroy  // Annotation pour indiquer que cette méthode doit être exécutée à l'arrêt de l'application
	public void cleanup() {
		if (dataSource != null) {
			dataSource.close();  // Fermez le pool de connexions HikariCP
		}
	}

}
